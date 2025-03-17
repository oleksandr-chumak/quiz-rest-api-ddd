package com.github.quiz.api.quiz.domain.exceptions

import com.github.quiz.api.common.domain.exceptions.DomainException
import java.util.UUID

class QuestionNotFoundException(quizId: UUID, questionId: UUID) : DomainException("Question with id: $questionId does not exist in quiz: $quizId")