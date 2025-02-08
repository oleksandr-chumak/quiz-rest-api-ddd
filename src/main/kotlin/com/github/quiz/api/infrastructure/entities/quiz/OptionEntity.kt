package com.github.quiz.api.infrastructure.entities.quiz

import com.github.quiz.api.domain.models.quiz.Option
import jakarta.persistence.*
import java.util.UUID


@Entity
@Table(name = "options")
data class OptionEntity(
    @Id
    @Column(name = "option_id", columnDefinition = "UUID")
    val optionId: UUID,

    @Column(name = "text", nullable = false)
    var text: String,

    @Column(name = "question_id", nullable = false, columnDefinition = "UUID")
    var questionId: UUID? = null
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
