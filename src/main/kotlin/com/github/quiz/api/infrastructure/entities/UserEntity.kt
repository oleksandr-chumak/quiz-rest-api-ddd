package com.github.quiz.api.infrastructure.entities

import com.github.quiz.api.domain.models.User
import com.github.quiz.api.infrastructure.entities.quiz.QuizEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var userId: Long = 0,

    @OneToMany(
        mappedBy = "createdBy",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var createdQuizzes: MutableList<QuizEntity> = mutableListOf()
) {
    fun toDomain(): User {
        return User(
            userId = userId,
        )
    }

    companion object {
        fun fromDomain(user: User): UserEntity {
            return UserEntity(
                userId = user.userId,
            )
        }
    }
}