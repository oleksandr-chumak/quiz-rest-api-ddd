package com.github.quiz.api.domain.models.quiz

import java.util.UUID

/**
 * Represents an option for a question.
 *
 * @property optionId The unique identifier that is only unique within the question context.
 * @property text The text/content of the option.
 */
data class Option(val optionId: UUID, val text: String) {}