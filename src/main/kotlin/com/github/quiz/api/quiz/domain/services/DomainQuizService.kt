package com.github.quiz.api.quiz.domain.services

import com.github.quiz.api.quiz.domain.exceptions.QuizNotFoundException
import com.github.quiz.api.quiz.domain.factories.QuizFactory
import com.github.quiz.api.quiz.domain.models.Option
import com.github.quiz.api.quiz.domain.models.Question
import com.github.quiz.api.quiz.domain.models.Quiz
import com.github.quiz.api.quiz.domain.repository.QuizRepository
import java.util.*

class DomainQuizService(
    private val repository: QuizRepository,
    private val quizFactory: QuizFactory
) : QuizService {

    // region Quiz Operation

    override fun createQuiz(name: String, performedBy: UUID): Quiz {
        val quiz = quizFactory.create(name, performedBy)
        repository.save(quiz)
        return quiz
    }

    override fun deleteQuiz(quizId: UUID, performedBy: UUID) {
        val quiz = getQuiz(quizId)
        quiz.assertOwnership(performedBy)
        repository.deleteQuiz(quizId)
    }

    // endregion

    // region Question Operation

    override fun addQuestion(quizId: UUID, title: String, performedBy: UUID): Question {
        val quiz = getQuiz(quizId)
        val question = quiz.addQuestion(title, performedBy)
        repository.save(quiz)
        return question
    }

    override fun removeQuestion(quizId: UUID, questionId: UUID, performedBy: UUID) {
        val quiz = getQuiz(quizId)
        quiz.removeQuestion(questionId, performedBy)
        repository.save(quiz)
    }

    override fun updateQuestionTitle(
        quizId: UUID,
        questionId: UUID,
        title: String,
        performedBy: UUID
    ): Question {
        val quiz = getQuiz(quizId)
        val question = quiz.changeQuestionTitle(questionId, title, performedBy)
        repository.save(quiz)
        return question
    }

    // endregion

    // region Option Operations

    override fun addOption(quizId: UUID, questionId: UUID, optionText: String, performedBy: UUID): Option {
        val quiz = getQuiz(quizId)
        val option = quiz.addOption(questionId, optionText, performedBy)
        repository.save(quiz)
        return option
    }

    override fun removeOption(quizId: UUID, questionId: UUID, optionId: UUID, performedBy: UUID) {
        val quiz = getQuiz(quizId)
        quiz.removeOption(questionId, optionId, performedBy)
        repository.save(quiz)
    }

    override fun updateOptionTitle(
        quizId: UUID,
        questionId: UUID,
        optionId: UUID,
        title: String,
        performedBy: UUID
    ): Option {
        val quiz = getQuiz(quizId)
        val option = quiz.changeOptionTitle(questionId, optionId, title, performedBy)
        repository.save(quiz)
        return option
    }

    // endregion

    // region Private Helpers

    private fun getQuiz(quizId: UUID): Quiz {
        return repository.findQuizById(quizId) ?: throw QuizNotFoundException(quizId)
    }

    // endregion
}