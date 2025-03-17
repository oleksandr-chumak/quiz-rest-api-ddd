package com.github.quiz.api.quiz.domain.exceptions

import com.github.quiz.api.common.domain.exceptions.DomainException
import java.util.UUID

class MaxOptionsExceededException(maxCount: Int, questionId: UUID)
    : DomainException("Maximum number of options ($maxCount) reached for question: $questionId")