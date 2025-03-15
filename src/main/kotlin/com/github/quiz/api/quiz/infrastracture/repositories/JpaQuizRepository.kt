package com.github.quiz.api.quiz.infrastracture.repositories

import com.github.quiz.api.quiz.infrastracture.entities.QuestionEntity
import com.github.quiz.api.quiz.infrastracture.entities.QuizEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface JpaQuizRepository : JpaRepository<QuizEntity, Long> {

    @Query("SELECT q FROM QuizEntity q WHERE q.createdBy.userId = :userId")
    fun findQuizzesCreatedByUser(@Param("userId") userId: UUID): List<QuizEntity>

    @Query("SELECT q FROM QuestionEntity q WHERE q.quizId = :quizId")
    fun findQuestionsAssociatedWithQuiz(@Param("quizId") quizId: Long): List<QuestionEntity>

    @Query("SELECT q FROM QuizEntity q ORDER BY q.quizId DESC")
    fun findTopByOrderByQuizIdDesc(): QuizEntity?
}