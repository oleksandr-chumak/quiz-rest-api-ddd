package com.github.quiz.api.quiz.domain.models

import com.github.quiz.api.quiz.domain.exceptions.MaxOptionsExceededException
import com.github.quiz.api.quiz.domain.exceptions.OptionNotFoundException
import com.github.quiz.api.quiz.domain.exceptions.QuestionNotFoundException
import com.github.quiz.api.quiz.domain.exceptions.UnauthorizedQuizModificationException
import java.util.UUID

class Quiz(
    val quizId: UUID,
    var name: String,
    val createdBy: UUID,
    val questions: MutableList<Question> = mutableListOf(),
) {
    companion object {
        private const val MAX_OPTIONS_COUNT = 4
    }

    fun addQuestion(title: String, performedBy: UUID): Question {
        assertOwnership(performedBy)

        val question = Question(UUID.randomUUID(), Question.Title(title))
        questions.add(question)

        return question
    }

    fun changeQuestionTitle(questionId: UUID, title: String, performedBy: UUID): Question {
        assertOwnership(performedBy)

        val question = getQuestion(questionId)
        question.title = Question.Title(title)

        return question
    }

    fun removeQuestion(questionId: UUID, performedBy: UUID) {
        assertOwnership(performedBy)

        val question = getQuestion(questionId)
        questions.remove(question)
    }

    fun addOption(questionId: UUID, optionTitle: String, performedBy: UUID): Option {
        assertOwnership(performedBy)

        val question = getQuestion(questionId)

        if(question.options.size == MAX_OPTIONS_COUNT) {
            throw MaxOptionsExceededException(MAX_OPTIONS_COUNT, question.questionId)
        }

        val option = Option(UUID.randomUUID(), Option.Title(optionTitle))
        question.options.add(option)

        return option
    }

    fun changeOptionTitle(questionId: UUID, optionId: UUID, optionText: String, performedBy: UUID): Option {
        assertOwnership(performedBy)

        val option = getOption(questionId, optionId)
        option.title = Option.Title(optionText)

        return option
    }

    fun removeOption(questionId: UUID, optionId: UUID, performedBy: UUID) {
        assertOwnership(performedBy)

        val question = getQuestion(questionId)
        val option = getOption(question, optionId)
        question.options.remove(option)
    }

    fun assertOwnership(performedBy: UUID) {
        if (createdBy != performedBy) {
            throw UnauthorizedQuizModificationException(quizId, performedBy)
        }
    }

    private fun getQuestion(questionId: UUID): Question {
        return questions.find { it.questionId == questionId } ?: throw QuestionNotFoundException(quizId, questionId)
    }

    private fun getOption(question: Question, optionId: UUID): Option {
        return question.options.find { it.optionId == optionId } ?: throw OptionNotFoundException(quizId, optionId)
    }

    private fun getOption(questionId: UUID, optionId: UUID): Option {
        val question = getQuestion(questionId)
        return getOption(question, optionId)
    }
}
