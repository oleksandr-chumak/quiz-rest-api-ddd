package com.github.quiz.api.quiz.infrastracture.configuration

import com.github.quiz.api.quiz.domain.factories.DomainQuizFactory
import com.github.quiz.api.quiz.domain.factories.QuizFactory
import com.github.quiz.api.quiz.domain.repository.QuizRepository
import com.github.quiz.api.quiz.domain.services.DomainQuizService
import com.github.quiz.api.quiz.domain.services.QuizService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration {

    @Bean
    fun quizFactory(quizRepository: QuizRepository): QuizFactory {
        return DomainQuizFactory(quizRepository)
    }

    @Bean
    fun quizService(quizRepository: QuizRepository, quizFactory: QuizFactory): QuizService {
        return DomainQuizService(quizRepository, quizFactory)
    }
}