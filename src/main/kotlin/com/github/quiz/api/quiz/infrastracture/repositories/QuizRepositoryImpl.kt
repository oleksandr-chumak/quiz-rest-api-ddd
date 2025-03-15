package com.github.quiz.api.quiz.infrastracture.repositories

import com.github.quiz.api.quiz.domain.models.Quiz
import com.github.quiz.api.quiz.domain.repository.QuizRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class QuizRepositoryImpl : QuizRepository {

    @Autowired
    lateinit var jpaQuizRepository: JpaQuizRepository

    override fun save(quiz: Quiz): Quiz {
        TODO("Not yet implemented")
    }

    override fun findQuizById(quizId: UUID): Quiz? {
        TODO("Not yet implemented")
    }

    override fun deleteQuiz(quizId: UUID) {
        TODO("Not yet implemented")
    }
}