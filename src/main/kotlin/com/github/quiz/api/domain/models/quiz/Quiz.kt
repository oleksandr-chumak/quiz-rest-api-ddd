package com.github.quiz.api.domain.models.quiz

import com.github.quiz.api.domain.models.User

/**
 * Represents a quiz with a collection of questions.
 *
 * @property quizId The unique identifier of the quiz.
 * @property name The name of the quiz.
 */
data class Quiz(
    val quizId: Long,
    var name: String,
    val createdBy: User,
    val questions: List<Question> = mutableListOf()
) {

    /**
     * Adds a new question to the quiz.
     *
     * @param questionText The unique identifier of the question inside quiz.
     * @return The newly created question.
     */
    fun addQuestion(questionText: String): Question {
        throw NotImplementedError()
    }

    /**
     * Retrieves all questions in the quiz.
     *
     * @return A list of all questions.
     */
    fun getAllQuestions(): List<Question> = throw NotImplementedError()


    fun updateQuestion(questionId: Short, questionText: String): Question {
        throw NotImplementedError();
    }

    /**
     * Removes a question from the quiz.
     *
     * @param question The question to remove.
     */
    fun removeQuestion(questionId: Short) {
        throw NotImplementedError()
    }


    fun addOptionToQuestion(questionId: Short, optionText: String): Option {
        throw NotImplementedError()
    }

    fun updateOption(questionId: Short, optionId: Short, optionText: String): Option {
        throw NotImplementedError()
    }

    fun removeOptionFromQuestion(questionId: Short, optionId: Short) {
        throw NotImplementedError()
    }
}