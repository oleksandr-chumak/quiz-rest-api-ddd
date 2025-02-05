package com.github.quiz.api.domain.services;

import com.github.quiz.api.domain.models.quiz.Quiz
import com.github.quiz.api.domain.models.quizattempt.QuizSnapshot

interface QuizService {
    fun findQuizById(quizId: Long): Quiz?
    fun createQuiz(quizText: String): Quiz
    fun deleteQuiz(quiz: Quiz)
    fun createQuizSnapshot(quiz: Quiz): QuizSnapshot
}
