package com.github.quiz.api.quiz.domain.repository

import com.github.quiz.api.quiz.domain.models.Quiz
import java.util.UUID

interface QuizRepository {
    fun save(quiz: Quiz): Quiz
    fun findQuizById(quizId: UUID): Quiz?
    fun deleteQuiz(quizId: UUID)
}