package com.github.quiz.api.quiz.domain

import com.github.quiz.api.quiz.domain.exceptions.MaxOptionsExceededException
import com.github.quiz.api.quiz.domain.exceptions.OptionNotFoundException
import com.github.quiz.api.quiz.domain.exceptions.QuestionNotFoundException
import com.github.quiz.api.quiz.domain.exceptions.UnauthorizedQuizModificationException
import com.github.quiz.api.quiz.domain.models.Question
import com.github.quiz.api.quiz.domain.models.Quiz
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class QuizTest {
    private val ownerId = UUID.randomUUID()
    private val otherUserId = UUID.randomUUID()
    private lateinit var quiz: Quiz

    @BeforeEach
    fun setup() {
        quiz = Quiz(
            quizId = UUID.randomUUID(),
            name = "Test Quiz",
            createdBy = ownerId
        )
    }

    @Nested
    inner class AuthorizationTests {
        @Test
        fun `modification by owner should succeed`() {
            val question = quiz.addQuestion("Q1", ownerId)
            assertThat(quiz.questions).containsExactly(question)
        }

        @Test
        fun `modification by non-owner should throw UnauthorizedQuizModificationException`() {
            assertThrows<UnauthorizedQuizModificationException> {
                quiz.addQuestion("Q1", otherUserId)
            }
        }
    }

    @Nested
    inner class QuestionManagement {
        @Test
        fun `addQuestion should add new question`() {
            val question = quiz.addQuestion("New Question", ownerId)
            assertThat(question.title.value).isEqualTo("New Question")
            assertThat(quiz.questions).contains(question)
        }

        @Test
        fun `changeQuestionTitle should update existing question`() {
            val question = quiz.addQuestion("Original", ownerId)
            val updated = quiz.changeQuestionTitle(question.questionId, "Updated", ownerId)
            assertThat(updated.title.value).isEqualTo("Updated")
        }

        @Test
        fun `changeQuestionTitle with invalid ID should throw QuestionNotFoundException`() {
            assertThrows<QuestionNotFoundException> {
                quiz.changeQuestionTitle(UUID.randomUUID(), "Updated", ownerId)
            }
        }

        @Test
        fun `removeQuestion should delete existing question`() {
            val question = quiz.addQuestion("Q1", ownerId)
            quiz.removeQuestion(question.questionId, ownerId)
            assertThat(quiz.questions).isEmpty()
        }
    }

    @Nested
    inner class OptionManagement {
        private lateinit var question: Question

        @BeforeEach
        fun setupQuestion() {
            question = quiz.addQuestion("Q1", ownerId)
        }

        @Test
        fun `addOption should create new option`() {
            val option = quiz.addOption(question.questionId, "Option 1", ownerId)
            assertThat(option.title.value).isEqualTo("Option 1")
            assertThat(question.options).contains(option)
        }

        @Test
        fun `addOption beyond max limit should throw MaxOptionsExceededException`() {
            repeat(4) { quiz.addOption(question.questionId, "Option ${it + 1}", ownerId) }
            assertThrows<MaxOptionsExceededException> {
                quiz.addOption(question.questionId, "Option 5", ownerId)
            }
        }

        @Test
        fun `changeOptionTitle should update existing option`() {
            val option = quiz.addOption(question.questionId, "Original", ownerId)
            val updated = quiz.changeOptionTitle(question.questionId, option.optionId, "Updated", ownerId)
            assertThat(updated.title.value).isEqualTo("Updated")
        }

        @Test
        fun `changeOptionTitle with invalid ID should throw OptionNotFoundException`() {
            assertThrows<OptionNotFoundException> {
                quiz.changeOptionTitle(question.questionId, UUID.randomUUID(), "Updated", ownerId)
            }
        }

        @Test
        fun `removeOption should delete existing option`() {
            val option = quiz.addOption(question.questionId, "Option", ownerId)
            quiz.removeOption(question.questionId, option.optionId, ownerId)
            assertThat(question.options).isEmpty()
        }
    }

    @Nested
    inner class ExceptionPropagation {
        @Test
        fun `operations on invalid question should throw QuestionNotFoundException`() {
            val invalidQuestionId = UUID.randomUUID()
            assertThrows<QuestionNotFoundException> {
                quiz.addOption(invalidQuestionId, "Option", ownerId)
            }
        }

        @Test
        fun `operations on invalid option should throw OptionNotFoundException`() {
            val question = quiz.addQuestion("Q1", ownerId)
            assertThrows<OptionNotFoundException> {
                quiz.changeOptionTitle(question.questionId, UUID.randomUUID(), "Updated", ownerId)
            }
        }
    }
}