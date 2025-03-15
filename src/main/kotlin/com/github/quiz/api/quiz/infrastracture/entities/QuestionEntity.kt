package com.github.quiz.api.quiz.infrastracture.entities

import jakarta.persistence.*

@Entity
@Table(name = "questions")
data class QuestionEntity(
    @Id
    @Column(name = "question_id")
    val questionId: String,

    @Column(name = "text", nullable = false)
    var text: String,

    @Column(name = "next_question_id")
    var nextOptionId: Long = 1,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    val options: MutableList<OptionEntity> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "correct_option_id", referencedColumnName = "option_id", nullable = true)
    var correctAnswer: OptionEntity? = null,

    @Column(name = "quiz_id", nullable = false, columnDefinition = "UUID")
    val quizId: Long? = null,
)