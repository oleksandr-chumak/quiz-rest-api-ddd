package com.github.quiz.api.quiz.domain.factories

import com.github.quiz.api.quiz.domain.models.Quiz
import java.util.UUID

interface QuizFactory {
    fun create(name: String, userId: UUID): Quiz
}