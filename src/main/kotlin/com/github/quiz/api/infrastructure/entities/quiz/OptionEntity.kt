package com.github.quiz.api.infrastructure.entities.quiz

import com.github.quiz.api.domain.models.quiz.Option
import jakarta.persistence.*


@Entity
@Table(name = "options")
data class OptionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    val optionId: Long = 0,

    @Column(name = "text", nullable = false)
    var text: String,

    @Column(name = "question_id", nullable = false)
    var questionId: Long? = null
) {
    fun toDomain(): Option {
        return Option(
            optionId = this.optionId,
            text = this.text
        )
    }

    companion object {
        fun fromDomain(option: Option): OptionEntity {
            return OptionEntity(
                option.optionId,
                option.text,
            )
        }
    }
}
