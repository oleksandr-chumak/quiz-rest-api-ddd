package com.github.quiz.api.infrastructure.repositories.impl

import com.github.quiz.api.domain.models.quiz.Option
import com.github.quiz.api.domain.models.quiz.Question
import com.github.quiz.api.domain.models.quiz.Quiz
import com.github.quiz.api.infrastructure.entities.UserEntity
import com.github.quiz.api.infrastructure.entities.quiz.OptionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuestionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuizEntity
import com.github.quiz.api.infrastructure.repositories.QuizRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class QuizRepositoryImpl (
    @PersistenceContext private val entityManager: EntityManager
) : QuizRepository {

    override fun createQuiz(quiz: Quiz): Quiz {
        val managedUserEntity = entityManager.merge(UserEntity.fromDomain(quiz.createdBy))
        val quizEntity = QuizEntity(quizId = quiz.quizId, quiz.name, createdBy = managedUserEntity)
        entityManager.persist(quizEntity)
        return quizEntity.toDomain()
    }

    override fun createQuestion(quiz: Quiz, question: Question): Question {
        val questionEntity = QuestionEntity(
            questionId = question.questionId,
            text = question.text,
            options = mutableListOf(),
            correctAnswer = null,
            quizId = quiz.quizId
        )
        entityManager.persist(questionEntity)
        return questionEntity.toDomain()
    }

    override fun createOption(question: Question, option: Option): Option {
        val optionEntity = OptionEntity(
            optionId = option.optionId,
            text = option.text,
            questionId = question.questionId)
        entityManager.persist(optionEntity)
        return optionEntity.toDomain()
    }

    override fun findQuizById(id: UUID): Quiz? {
        val quiz = entityManager.find(QuizEntity::class.java, id)
        return quiz?.toDomain()
    }

    override fun findQuizzesCreatedByUser(userId: UUID): List<Quiz> {
        val userEntity = entityManager.getReference(UserEntity::class.java, userId)
        val query = entityManager.createQuery(
            "SELECT q FROM QuizEntity q WHERE q.createdBy = :user",
            QuizEntity::class.java
        )
        query.setParameter("user", userEntity)
        return query.resultList.map { it.toDomain() }
    }

    override fun findQuestionsAssociatedWithQuiz(quizId: UUID): List<Question> {
        val query = entityManager.createQuery(
            "SELECT q FROM QuestionEntity q WHERE q.quizId = :quizId",
            QuestionEntity::class.java
        )
        query.setParameter("quizId", quizId)
        return query.resultList.map { it.toDomain() }
    }

    override fun updateQuiz(quiz: Quiz): Quiz {
        val quizEntity = entityManager.find(QuizEntity::class.java, quiz.quizId)
            ?: throw EntityNotFoundException("QuizEntity ${quiz.quizId} not found")
        quiz.name = quizEntity.name
        return quizEntity.toDomain()
    }

    override fun updateQuestion(question: Question): Question     {
        val questionEntity = entityManager.find(QuestionEntity::class.java, question.questionId)
            ?: throw EntityNotFoundException("QuestionEntity ${question.questionId} not found")
        questionEntity.text = question.text
        questionEntity.correctAnswer = question.correctAnswer?.let { OptionEntity.fromDomain(it) }
        return questionEntity.toDomain()
    }

    override fun updateOption(option: Option): Option {
        val optionEntity = entityManager.find(OptionEntity::class.java, option.optionId)
            ?: throw EntityNotFoundException("OptionEntity ${option.optionId} not found")
        optionEntity.text = option.text
        return optionEntity.toDomain()
    }

    override fun deleteQuiz(quizId: UUID): Boolean {
        val quiz = entityManager.find(QuizEntity::class.java, quizId) ?: return false
        entityManager.remove(quiz)
        return true
    }

    override fun deleteOption(optionId: UUID): Boolean {
        val option = entityManager.find(OptionEntity::class.java, optionId) ?: return false
        entityManager.remove(option)
        return true
    }

    override fun deleteQuestion(questionId: UUID): Boolean {
        val question = entityManager.find(QuestionEntity::class.java, questionId) ?: return false
        entityManager.remove(question)
        return true
    }
}