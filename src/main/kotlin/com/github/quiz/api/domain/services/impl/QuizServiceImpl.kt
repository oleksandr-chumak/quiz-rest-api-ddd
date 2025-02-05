package com.github.quiz.api.domain.services.impl;

import com.github.quiz.api.domain.models.quiz.Quiz
import com.github.quiz.api.domain.models.quizattempt.QuizSnapshot
import com.github.quiz.api.domain.services.QuizService

public class QuizServiceImpl : QuizService  {
    override fun findQuizById(quizId: Long): Quiz? {
        TODO("Not yet implemented")
    }

    override fun createQuiz(quizText: String): Quiz {
        TODO("Not yet implemented")
    }

    override fun deleteQuiz(quiz: Quiz) {
        TODO("Not yet implemented")
    }

    override fun createQuizSnapshot(quiz: Quiz): QuizSnapshot {
        TODO("Not yet implemented")
    }
}
