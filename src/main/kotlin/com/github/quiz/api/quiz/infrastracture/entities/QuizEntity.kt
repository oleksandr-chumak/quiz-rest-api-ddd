package com.github.quiz.api.quiz.infrastracture.entities

import com.github.quiz.api.infrastructure.entities.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "quizzes")
data class QuizEntity(
    @Id
    @Column(name = "quiz_id")
    val quizId: Long,

    @Column(name = "name")
    var name: String,

    @Column(name = "next_question_id")
    var nextQuestionId: Long = 1,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    val questions: MutableList<QuestionEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val createdBy: UserEntity,
)