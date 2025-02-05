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

    override fun createQuiz(userId: Long, name: String): Quiz {
        val user = entityManager.find(UserEntity::class.java, userId)
            ?: throw IllegalArgumentException("User not found with id: $userId")
        val quiz = QuizEntity(name = name, createdBy = user)
        entityManager.persist(quiz)
        return quiz.toDomain()
    }

    override fun createQuestion(quizId: Long, text: String): Question {
        val quiz = entityManager.find(QuizEntity::class.java, quizId)
            ?: throw IllegalArgumentException("Quiz not found with id: $quizId")
        val question = QuestionEntity(
            text = text,
            quiz = quiz,
            options = mutableListOf(),
            correctAnswer = null
        )
        quiz.questions.add(question)
        entityManager.persist(question)
        return question.toDomain()
    }

    override fun createOption(questionId: Long, text: String): Option {
        val question = entityManager.find(QuestionEntity::class.java, questionId)
            ?: throw IllegalArgumentException("Question not found with id: $questionId")
        val option = OptionEntity(text = text, question = question)
        question.options.add(option)
        entityManager.persist(option)
        return option.toDomain()
    }

    override fun findQuizById(id: Long): Quiz? {
        val quiz = entityManager.find(QuizEntity::class.java, id)
        return quiz?.toDomain()
    }

    override fun findQuizzesCreateByUser(user: User): List<Quiz> {
        val userEntity = entityManager.getReference(UserEntity::class.java, user.userId)
        val query = entityManager.createQuery(
            "SELECT q FROM QuizEntity q WHERE q.createdBy = :user",
            QuizEntity::class.java
        )
        query.setParameter("user", userEntity)
        return query.resultList.map { it.toDomain() }
    }

    override fun findQuestionsByQuizId(quizId: Long): List<Question> {
        val query = entityManager.createQuery(
            "SELECT DISTINCT q FROM QuestionEntity q LEFT JOIN FETCH q.options LEFT JOIN FETCH q.correctAnswer WHERE q.quiz.quizId = :quizId",
            QuestionEntity::class.java
        )
        query.setParameter("quizId", quizId)
        return query.resultList.map { it.toDomain() }
    }

    override fun updateQuiz(quizId: Long, name: String): Quiz {
        val quiz = entityManager.find(QuizEntity::class.java, quizId)
            ?: throw IllegalArgumentException("Quiz not found with id: $quizId")
        quiz.name = name
        return quiz.toDomain()
    }

    override fun updateQuestion(questionId: Long, text: String, correctAnswerId: Long?): Question {
        val question = entityManager.find(QuestionEntity::class.java, questionId)
            ?: throw IllegalArgumentException("Question not found with id: $questionId")
        question.text = text

        question.correctAnswer = if (correctAnswerId != null) {
            val option = entityManager.find(OptionEntity::class.java, correctAnswerId)
                ?: throw IllegalArgumentException("Option not found with id: $correctAnswerId")
            if (option.question.questionId != questionId) {
                throw IllegalArgumentException("Option does not belong to the question")
            }
            option
        } else {
            null
        }

        return question.toDomain()
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