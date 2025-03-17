package com.github.quiz.api.quiz.domain.models

import java.util.UUID

/**
 * Represents a question with a collection of options.
 *
 * @property questionId The unique identifier that is only unique within the quiz context.
 * @property title The text/content of the question.
 * @property correctAnswer The correct option for the question (nullable).
 * @property options The list of options for the question.
 */
data class Question(
    val questionId: UUID,
    var title: Title,
    var correctAnswer: Option? = null,
    val options: MutableList<Option> = mutableListOf(),
) {
    data class Title(val value: String) {
        companion object {
            private const val TITLE_MAX_LENGTH = 50
        }

        init {
            assert(value.isNotBlank()) { "Title cannot be empty" }
            assert(value.length < TITLE_MAX_LENGTH) { "Title cannot be longer than $TITLE_MAX_LENGTH" }
        }
    }
}
