package com.github.quiz.api.quiz.domain.models

import com.github.quiz.api.quiz.domain.exceptions.UnauthorizedQuizModificationException
import java.util.UUID

class Quiz(
    val quizId: UUID,
    var name: String,
    val createdBy: UUID,
) {
    fun addQuestion(title: String, performedBy: UUID): Question {
        assertOwnership(performedBy)
        TODO("Not yet implemented")
    }

    fun changeQuestionTitle(questionId: UUID, title: String, performedBy: UUID): Question {
        assertOwnership(performedBy)
        TODO("Not yet implemented")
    }

    fun removeQuestion(questionId: UUID, performedBy: UUID) {
        assertOwnership(performedBy)
        TODO("Not yet implemented")
    }

    fun addOption(questionId: UUID, optionTitle: String, performedBy: UUID): Option {
        assertOwnership(performedBy)

        TODO("Not yet implemented")
    }

    fun changeOptionTitle(questionId: UUID, optionId: UUID, optionText: String, performedBy: UUID): Option {
        assertOwnership(performedBy)
        TODO("Not yet implemented")
    }

    fun removeOption(questionId: UUID, optionId: UUID, performedBy: UUID) {
        assertOwnership(performedBy)
        TODO("Not yet implemented")
    }

    fun assertOwnership(performedBy: UUID) {
        if (createdBy != performedBy) {
            throw UnauthorizedQuizModificationException(quizId, performedBy)
        }
    }
}