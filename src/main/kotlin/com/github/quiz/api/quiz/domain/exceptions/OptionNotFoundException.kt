package com.github.quiz.api.quiz.domain.exceptions

import com.github.quiz.api.common.domain.exceptions.DomainException
import java.util.*

class OptionNotFoundException(quizId: UUID, optionId: UUID): DomainException("Option with id: $optionId does not exist in quiz: $quizId")