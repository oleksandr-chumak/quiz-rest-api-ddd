package com.github.quiz.api.infrastructure.entities.quiz

import com.github.quiz.api.domain.models.quiz.Question
import jakarta.persistence.*

@Entity
@Table(name = "questions")
data class QuestionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    val questionId: Long = 0,

    @Column(name = "text", nullable = false)
    var text: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    var quiz: QuizEntity,

    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val options: MutableList<OptionEntity> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "option_id", referencedColumnName = "question_id")
    var correctAnswer: OptionEntity? = null
) {
    fun toDomain(): Question {
        return Question(
            questionId = this.questionId,
            text = this.text,
            options = this.options.map { it.toDomain() },
            correctAnswer = this.correctAnswer?.toDomain()
        )
    }
}