package com.github.quiz.api.infrastructure.repositories

import com.github.quiz.api.domain.models.User
import com.github.quiz.api.domain.models.quiz.Option
import com.github.quiz.api.domain.models.quiz.Question
import com.github.quiz.api.domain.models.quiz.Quiz

interface QuizRepository {

    // create operations

    fun createQuiz(user: User, name: String ): Quiz
    fun createQuestion(quiz: Quiz, text: String): Question
    fun createOption(question: Question, text: String): Option

    // read operations

    fun findQuizById(id: Long): Quiz?
    fun findQuizzesCreatedByUser(userId: Long): List<Quiz>
    fun findQuestionsAssociatedWithQuiz(quizId: Long): List<Question>

    // update operations

    fun updateQuiz(quizId: Long, name: String): Quiz?
    fun updateQuestion(questionId: Long, text: String, correctAnswerId: Long? ): Question?
    fun updateOption(optionId: Long, text: String): Option?

    // delete operations

    fun deleteQuiz(quizId: Long): Boolean
    fun deleteOption(optionId: Long): Boolean
    fun deleteQuestion(questionId: Long): Boolean
}