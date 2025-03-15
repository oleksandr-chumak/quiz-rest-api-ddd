package com.github.quiz.api.quiz.domain.exceptions

import com.github.quiz.api.common.domain.exceptions.DomainException
import java.util.UUID

class UnauthorizedQuizModificationException(quizId: UUID, userId: UUID)
    : DomainException("User $userId is not authorized to modify quiz with id: $quizId")