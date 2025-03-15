package com.github.quiz.api.quiz.domain.services

import com.github.quiz.api.quiz.domain.models.Option
import com.github.quiz.api.quiz.domain.models.Question
import com.github.quiz.api.quiz.domain.models.Quiz
import java.util.*

/**
 * Service interface for managing quizzes, questions, and options.
 * Provides operations for CRUD functionality and maintains authorization checks through performedBy parameters.
 */
interface QuizService {

    // region Quiz Operations

    /**
     * Creates a new quiz with the specified name.
     *
     * @param name The display name for the new quiz
     * @param performedBy The UUID of the user creating the quiz
     * @return The newly created Quiz object
     * @throws IllegalArgumentException if name is blank or invalid
     */
    fun createQuiz(name: String, performedBy: UUID): Quiz

    /**
     * Deletes a quiz and its associated questions/options.
     *
     * @param quizId The UUID of the quiz to delete
     * @param performedBy The UUID of the user requesting deletion
     * @throws SecurityException if user doesn't own the quiz
     */
    fun deleteQuiz(quizId: UUID, performedBy: UUID)

    // endregion

    // region Question Operations

    /**
     * Adds a new question to a quiz.
     *
     * @param quizId The UUID of the quiz to modify
     * @param title The text content of the new question
     * @param performedBy The UUID of the user performing the action
     * @return The newly created Question object
     * @throws IllegalArgumentException if quiz doesn't exist
     * @throws SecurityException if user doesn't own the quiz
     */
    fun addQuestion(quizId: UUID, title: String, performedBy: UUID): Question

    /**
     * Removes a question from a quiz.
     *
     * @param quizId The UUID of the containing quiz
     * @param questionId The UUID of the question to remove
     * @param performedBy The UUID of the user performing the action
     * @throws IllegalArgumentException if question doesn't exist in quiz
     * @throws SecurityException if user doesn't own the quiz
     */
    fun removeQuestion(quizId: UUID, questionId: UUID, performedBy: UUID)

    /**
     * Updates the text content of a question.
     *
     * @param quizId The UUID of the containing quiz
     * @param questionId The UUID of the question to update
     * @param title The new text content for the question
     * @param performedBy The UUID of the user performing the action
     * @return The updated Question object
     * @throws IllegalArgumentException if question doesn't exist in quiz
     * @throws SecurityException if user doesn't own the quiz
     */
    fun updateQuestionTitle(
        quizId: UUID,
        questionId: UUID,
        title: String,
        performedBy: UUID
    ): Question

    // endregion

    // region Option Operations

    /**
     * Adds a new option to a question.
     *
     * @param quizId The UUID of the containing quiz
     * @param questionId The UUID of the question to modify
     * @param optionText The text content of the new option
     * @param performedBy The UUID of the user performing the action
     * @return The newly created Option object
     * @throws IllegalArgumentException if question doesn't exist in quiz
     * @throws SecurityException if user doesn't own the quiz
     */
    fun addOption(
        quizId: UUID,
        questionId: UUID,  // Fixed from Short to UUID
        optionText: String,
        performedBy: UUID
    ): Option

    /**
     * Removes an option from a question.
     *
     * @param quizId The UUID of the containing quiz
     * @param questionId The UUID of the containing question
     * @param optionId The UUID of the option to remove
     * @param performedBy The UUID of the user performing the action
     * @throws IllegalArgumentException if option doesn't exist in question
     * @throws SecurityException if user doesn't own the quiz
     */
    fun removeOption(
        quizId: UUID,
        questionId: UUID,
        optionId: UUID,
        performedBy: UUID
    )

    /**
     * Updates the text content of an option.
     *
     * @param quizId The UUID of the containing quiz
     * @param questionId The UUID of the containing question
     * @param optionId The UUID of the option to update
     * @param title The new text content for the option
     * @param performedBy The UUID of the user performing the action
     * @return The updated Option object
     * @throws IllegalArgumentException if option doesn't exist in question
     * @throws SecurityException if user doesn't own the quiz
     */
    fun updateOptionTitle(
        quizId: UUID,
        questionId: UUID,
        optionId: UUID,
        title: String,
        performedBy: UUID
    ): Option

    // endregion
}