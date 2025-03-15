package com.github.quiz.api.quiz.infrastracture.entities

import jakarta.persistence.*

@Entity
@Table(name = "options")
data class OptionEntity(
    @Id
    @Column(name = "option_id")
    val optionId: String,

    @Column(name = "text", nullable = false)
    var text: String,

    @Column(name = "question_id", nullable = false)
    var questionId: String? = null
)
