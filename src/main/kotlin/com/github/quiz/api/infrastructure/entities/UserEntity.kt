package com.github.quiz.api.infrastructure.entities

import com.github.quiz.api.domain.aggregates.User
import com.github.quiz.api.quiz.infrastracture.entities.QuizEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @Column(name = "user_id", columnDefinition = "UUID")
    var userId: Long,

    @OneToMany(
        mappedBy = "createdBy",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var createdQuizzes: MutableList<QuizEntity> = mutableListOf()
)