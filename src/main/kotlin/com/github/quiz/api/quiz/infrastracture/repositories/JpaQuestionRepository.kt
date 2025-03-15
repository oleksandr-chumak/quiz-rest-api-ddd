package com.github.quiz.api.quiz.infrastracture.repositories

import com.github.quiz.api.quiz.infrastracture.entities.QuestionEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaQuestionRepository : JpaRepository<QuestionEntity, String> {
}