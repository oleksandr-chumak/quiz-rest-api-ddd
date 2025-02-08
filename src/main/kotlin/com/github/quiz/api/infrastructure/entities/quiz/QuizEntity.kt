package com.github.quiz.api.infrastructure.entities.quiz

import com.github.quiz.api.domain.models.User
import com.github.quiz.api.domain.models.quiz.Quiz
import com.github.quiz.api.infrastructure.entities.UserEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "quizzes")
data class QuizEntity(
    @Id
    @Column(name = "quiz_id", columnDefinition = "UUID")
    val quizId: UUID,

    @Column(name = "name")
    var name: String,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    val questions: MutableList<QuestionEntity> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val createdBy: UserEntity,
) {
    fun toDomain(): Quiz {
        return Quiz(
            quizId = this.quizId,
            name = this.name,
            createdBy = this.createdBy.toDomain(),
            questions = this.questions.map { it.toDomain() }
        )
    }

    companion object {
        fun fromDomain(quiz: Quiz): QuizEntity {
            return QuizEntity(
                quizId = quiz.quizId,
                name = quiz.name,
                questions = quiz.questions.map { QuestionEntity.fromDomain(it) }.toMutableList(),
                createdBy = UserEntity.fromDomain(quiz.createdBy)
            )
        }
    }
}