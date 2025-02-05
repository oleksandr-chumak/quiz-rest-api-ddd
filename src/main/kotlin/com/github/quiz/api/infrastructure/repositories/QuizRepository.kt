package com.github.quiz.api.infrastructure.repositories

import com.github.quiz.api.domain.models.User
import com.github.quiz.api.domain.models.quiz.Option
import com.github.quiz.api.domain.models.quiz.Question
import com.github.quiz.api.domain.models.quiz.Quiz

interface QuizRepository {

    // create operations

    fun createQuiz(userId: Long, name: String ): Quiz
    fun createQuestion(quizId: Long, text: String): Question
    fun createOption(questionId: Long, text: String): Option

    // read operations

    fun findQuizById(id: Long): Quiz?
    fun findQuizzesCreateByUser(user: User): List<Quiz>
    fun findQuestionsByQuizId(quizId: Long): List<Question>

    // update operations

    fun updateQuiz(quizId: Long, name: String): Quiz
    fun updateQuestion(questionId: Long, text: String, correctAnswerId: Long? ): Question
    fun updateOption(optionId: Long, text: String): Option

    // delete operations

    fun deleteQuiz(quizId: Long)
    fun deleteOption(optionId: Long)
    fun deleteQuestion(questionId: Long)
}