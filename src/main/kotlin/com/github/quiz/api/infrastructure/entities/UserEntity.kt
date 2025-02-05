package com.github.quiz.api.infrastructure.entities

import com.github.quiz.api.domain.models.User
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var userId: Long,
) {
    fun toDomain(): User {
        return User(
            userId = userId,
        )
    }
}