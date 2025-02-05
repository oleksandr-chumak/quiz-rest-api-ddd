package com.github.quiz.api.infrastructure.entities.quiz

import com.github.quiz.api.domain.models.User
import com.github.quiz.api.domain.models.quiz.Quiz
import com.github.quiz.api.infrastructure.entities.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "quizzes")
data class QuizEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    val quizId: Long = 0,

    @Column(name = "name")
    var name: String,

    @OneToMany(mappedBy = "quiz", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val questions: MutableList<QuestionEntity> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
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
}