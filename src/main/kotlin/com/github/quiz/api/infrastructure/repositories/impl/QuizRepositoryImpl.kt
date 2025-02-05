package com.github.quiz.api.infrastructure.repositories.impl

import com.github.quiz.api.domain.models.User
import com.github.quiz.api.domain.models.quiz.Option
import com.github.quiz.api.domain.models.quiz.Question
import com.github.quiz.api.domain.models.quiz.Quiz
import com.github.quiz.api.infrastructure.entities.UserEntity
import com.github.quiz.api.infrastructure.entities.quiz.OptionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuestionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuizEntity
import com.github.quiz.api.infrastructure.repositories.QuizRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class QuizRepositoryImpl(
    @PersistenceContext private val entityManager: EntityManager
) : QuizRepository {

    override fun createQuiz(user: User, name: String): Quiz {
        val managedUserEntity = entityManager.merge(UserEntity.fromDomain(user))
        val quiz = QuizEntity(name = name, createdBy = managedUserEntity)
        entityManager.persist(quiz)
        return quiz.toDomain()
    }

    override fun createQuestion(quiz: Quiz, text: String): Question {
        val question = QuestionEntity(
            text = text,
            options = mutableListOf(),
            correctAnswer = null,
            quizId = quiz.quizId
        )
        entityManager.persist(question)
        return question.toDomain()
    }

    override fun createOption(question: Question, text: String): Option {
        val option = OptionEntity(text = text, questionId = question.questionId)
        entityManager.persist(option)
        return option.toDomain()
    }

    override fun findQuizById(id: Long): Quiz? {
        val quiz = entityManager.find(QuizEntity::class.java, id)
        return quiz?.toDomain()
    }

    override fun findQuizzesCreatedByUser(userId: Long): List<Quiz> {
        val userEntity = entityManager.getReference(UserEntity::class.java, userId)
        val query = entityManager.createQuery(
            "SELECT q FROM QuizEntity q WHERE q.createdBy = :user",
            QuizEntity::class.java
        )
        query.setParameter("user", userEntity)
        return query.resultList.map { it.toDomain() }
    }

    override fun findQuestionsAssociatedWithQuiz(quizId: Long): List<Question> {
        TODO("Implement questions of questions associated with quiz")
    }

    override fun updateQuiz(quizId: Long, name: String): Quiz {
        val quiz = entityManager.find(QuizEntity::class.java, quizId)
            ?: throw IllegalArgumentException("Quiz not found with id: $quizId")
        quiz.name = name
        return quiz.toDomain()
    }

    override fun updateQuestion(questionId: Long, text: String, correctAnswerId: Long?): Question {
        TODO("Implement questions of questions associated with quiz")
    }

    override fun updateOption(optionId: Long, text: String): Option {
        val option = entityManager.find(OptionEntity::class.java, optionId)
            ?: throw IllegalArgumentException("Option not found with id: $optionId")
        option.text = text
        return option.toDomain()
    }

    override fun deleteQuiz(quizId: Long) {
        val quiz = entityManager.find(QuizEntity::class.java, quizId) ?: return
        entityManager.remove(quiz)
    }

    override fun deleteOption(optionId: Long) {
        val option = entityManager.find(OptionEntity::class.java, optionId) ?: return

        val query = entityManager.createQuery(
            "SELECT q FROM QuestionEntity q WHERE q.correctAnswer = :option",
            QuestionEntity::class.java
        )
        query.setParameter("option", option)
        query.resultList.forEach { it.correctAnswer = null }

        entityManager.remove(option)
    }

    override fun deleteQuestion(questionId: Long) {
        val question = entityManager.find(QuestionEntity::class.java, questionId) ?: return
        entityManager.remove(question)
    }
}