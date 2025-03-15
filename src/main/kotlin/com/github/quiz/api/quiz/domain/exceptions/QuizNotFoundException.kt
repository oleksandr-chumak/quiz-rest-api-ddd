package com.github.quiz.api.quiz.domain.exceptions

import com.github.quiz.api.common.domain.exceptions.DomainException
import java.util.UUID

class QuizNotFoundException(quizId: UUID) : DomainException("Quiz with id: $quizId does not exist.")