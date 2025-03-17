package com.github.quiz.api.quiz.domain.models

import java.util.UUID

/**
 * Represents an option for a question.
 *
 * @property optionId The unique identifier that is only unique within the question context.
 * @property title The text/content of the option.
 */


data class Option(val optionId: UUID, var title: Title) {
    data class Title(val value: String) {
        companion object {
            private const val TITLE_MAX_LENGTH = 30
        }

        init {
            assert(value.isNotBlank()) { "Title cannot be empty" }
            assert(value.length < TITLE_MAX_LENGTH) { "Title cannot be longer than $TITLE_MAX_LENGTH" }
        }
    }
}