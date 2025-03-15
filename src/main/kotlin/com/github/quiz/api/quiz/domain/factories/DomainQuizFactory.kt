package com.github.quiz.api.quiz.domain.factories

import com.github.quiz.api.quiz.domain.models.Quiz
import com.github.quiz.api.quiz.domain.repository.QuizRepository
import java.util.*

class DomainQuizFactory(
    private val repository: QuizRepository
) : QuizFactory {

    override fun create(name: String, userId: UUID): Quiz {
        return Quiz(
            UUID.randomUUID(),
            name,
            userId,
        )
    }

}