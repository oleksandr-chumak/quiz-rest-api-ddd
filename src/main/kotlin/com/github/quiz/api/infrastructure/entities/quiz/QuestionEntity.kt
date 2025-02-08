package com.github.quiz.api.infrastructure.entities.quiz

import com.github.quiz.api.domain.models.quiz.Question
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "questions")
data class QuestionEntity(
    @Id
    @Column(name = "question_id", columnDefinition = "UUID")
    val questionId: UUID,

    @Column(name = "text", nullable = false)
    var text: String,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    val options: MutableList<OptionEntity> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "correct_option_id", referencedColumnName = "option_id", nullable = true)
    var correctAnswer: OptionEntity? = null,

    @Column(name = "quiz_id", nullable = false, columnDefinition = "UUID")
    val quizId: UUID? = null,
) {
    fun toDomain(): Question {
        return Question(
            questionId = this.questionId,
            text = this.text,
            options = this.options.map { it.toDomain() },
            correctAnswer = this.correctAnswer?.toDomain()
        )
    }

    companion object {
        fun fromDomain(question: Question): QuestionEntity {
            return QuestionEntity(
                question.questionId,
                question.text,
                options = question.options.map { OptionEntity.fromDomain(it) }.toMutableList(),
                correctAnswer =  question.correctAnswer?.let{ OptionEntity.fromDomain(it) }
            )
        }
    }
}