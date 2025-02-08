package com.github.quiz.api.infrastructure.repositories

import com.github.quiz.api.domain.models.quiz.Option
import com.github.quiz.api.domain.models.quiz.Question
import com.github.quiz.api.domain.models.quiz.Quiz
import java.util.*

interface QuizRepository {

    // create operations

    fun createQuiz(quiz: Quiz): Quiz
    fun createQuestion(quiz: Quiz, question: Question): Question
    fun createOption(question: Question, option: Option): Option

    // read operations

    fun findQuizById(id: UUID): Quiz?
    fun findQuizzesCreatedByUser(userId: UUID): List<Quiz>
    fun findQuestionsAssociatedWithQuiz(quizId: UUID): List<Question>

    // update operations

    fun updateQuiz(quiz: Quiz): Quiz
    fun updateQuestion(question: Question): Question
    fun updateOption(option: Option): Option

    // delete operations

    fun deleteQuiz(quizId: UUID): Boolean
    fun deleteOption(optionId: UUID): Boolean
    fun deleteQuestion(questionId: UUID): Boolean
}