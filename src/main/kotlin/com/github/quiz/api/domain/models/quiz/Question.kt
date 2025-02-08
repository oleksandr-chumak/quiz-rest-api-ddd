package com.github.quiz.api.domain.models.quiz

import java.util.*

/**
 * Represents a question with a collection of options.
 *
 * @property questionId The unique identifier that is only unique within the quiz context.
 * @property text The text/content of the question.
 * @property correctAnswer The correct option for the question (nullable).
 * @property options The list of options for the question.
 */
data class Question(
    val questionId: UUID,
    val text: String,
    val correctAnswer: Option? = null,
    val options: List<Option> = mutableListOf()
)
