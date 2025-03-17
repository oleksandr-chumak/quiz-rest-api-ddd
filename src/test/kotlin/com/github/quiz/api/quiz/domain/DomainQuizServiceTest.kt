package com.github.quiz.api.quiz.domain

import com.github.quiz.api.quiz.domain.exceptions.QuizNotFoundException
import com.github.quiz.api.quiz.domain.exceptions.UnauthorizedQuizModificationException
import com.github.quiz.api.quiz.domain.factories.QuizFactory
import com.github.quiz.api.quiz.domain.models.Question
import com.github.quiz.api.quiz.domain.models.Quiz
import com.github.quiz.api.quiz.domain.repository.QuizRepository
import com.github.quiz.api.quiz.domain.services.DomainQuizService
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class DomainQuizServiceTest {

    @Mock
    private lateinit var repository: QuizRepository

    @Mock
    private lateinit var quizFactory: QuizFactory

    @InjectMocks
    private lateinit var service: DomainQuizService

    // TODO: check this test

    @Test
    fun `createQuiz should create and save quiz via factory`() {
        val name = "Test Quiz"
        val performedBy = UUID.randomUUID()
        val mockQuiz = mock<Quiz>()
        whenever(quizFactory.create(name, performedBy)).thenReturn(mockQuiz)

        val result = service.createQuiz(name, performedBy)

        assertEquals(mockQuiz, result)
        verify(repository).save(mockQuiz)
    }

    @Test
    fun `deleteQuiz should delete when quiz exists and user is owner`() {
        val quizId = UUID.randomUUID()
        val performedBy = UUID.randomUUID()
        val mockQuiz = Quiz(quizId, "Sample Quiz", performedBy)

        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)
        assertDoesNotThrow { service.deleteQuiz(quizId, performedBy) }
        verify(repository).deleteQuiz(quizId)
    }

    @Test
    fun `deleteQuiz should throw QuizNotFoundException when quiz not found`() {
        val quizId = UUID.randomUUID()
        whenever(repository.findQuizById(quizId)).thenReturn(null)

        assertThrows<QuizNotFoundException> {
            service.deleteQuiz(quizId, UUID.randomUUID())
        }
    }

    @Test
    fun `deleteQuiz should throw when user is not owner`() {
        val quizId = UUID.randomUUID()
        val performedBy = UUID.randomUUID()
        val mockQuiz = Quiz(quizId, "Sample Quiz", UUID.randomUUID())

        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)

        assertThrows<UnauthorizedQuizModificationException> {
            service.deleteQuiz(quizId, performedBy)
        }
    }

    // TODO: check this test

    @Test
    fun `addQuestion should add question and save quiz`() {
        val quizId = UUID.randomUUID()
        val title = "New Question"
        val performedBy = UUID.randomUUID()
        val mockQuiz = mock<Quiz>()
        val mockQuestion = mock<Question>()
        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)
        whenever(mockQuiz.addQuestion(title, performedBy)).thenReturn(mockQuestion)

        val result = service.addQuestion(quizId, title, performedBy)

        assertEquals(mockQuestion, result)
        verify(repository).save(mockQuiz)
    }

    // TODO: check this test

    @Test
    fun `removeQuestion should remove question and save quiz`() {
        val quizId = UUID.randomUUID()
        val questionId = UUID.randomUUID()
        val performedBy = UUID.randomUUID()
        val mockQuiz = mock<Quiz>()
        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)

        service.removeQuestion(quizId, questionId, performedBy)

        verify(mockQuiz).removeQuestion(questionId, performedBy)
        verify(repository).save(mockQuiz)
    }

    // TODO: check this test

    @Test
    fun `updateQuestionTitle should update title and save quiz`() {
        val quizId = UUID.randomUUID()
        val questionId = UUID.randomUUID()
        val newTitle = "Updated Title"
        val performedBy = UUID.randomUUID()
        val mockQuiz = mock<Quiz>()
        val mockQuestion = mock<Question>()
        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)
        whenever(mockQuiz.changeQuestionTitle(questionId, newTitle, performedBy)).thenReturn(mockQuestion)

        val result = service.updateQuestionTitle(quizId, questionId, newTitle, performedBy)

        assertEquals(mockQuestion, result)
        verify(repository).save(mockQuiz)
    }

    // TODO: check this test

    @Test
    fun `addQuestion should throw QuizNotFoundException when quiz not found`() {
        val quizId = UUID.randomUUID()
        whenever(repository.findQuizById(quizId)).thenReturn(null)

        assertThrows<QuizNotFoundException> {
            service.addQuestion(quizId, "Title", UUID.randomUUID())
        }
    }

    // TODO: check this test

    @Test
    fun `addOption should add option and save quiz`() {
        val quizId = UUID.randomUUID()
        val questionId = UUID.randomUUID()
        val optionText = "New Option"
        val performedBy = UUID.randomUUID()
        val mockQuiz = mock<Quiz>()
        val mockOption = mock<com.github.quiz.api.quiz.domain.models.Option>()
        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)
        whenever(mockQuiz.addOption(questionId, optionText, performedBy)).thenReturn(mockOption)

        val result = service.addOption(quizId, questionId, optionText, performedBy)

        assertEquals(mockOption, result)
        verify(repository).save(mockQuiz)
    }

    // TODO: check this test

    @Test
    fun `removeOption should remove option and save quiz`() {
        val quizId = UUID.randomUUID()
        val questionId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val performedBy = UUID.randomUUID()
        val mockQuiz = mock<Quiz>()
        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)

        service.removeOption(quizId, questionId, optionId, performedBy)

        verify(mockQuiz).removeOption(questionId, optionId, performedBy)
        verify(repository).save(mockQuiz)
    }

    // TODO: check this test

    @Test
    fun `updateOptionTitle should update title and save quiz`() {
        val quizId = UUID.randomUUID()
        val questionId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val newTitle = "Updated Option"
        val performedBy = UUID.randomUUID()
        val mockQuiz = mock<Quiz>()
        val mockOption = mock<com.github.quiz.api.quiz.domain.models.Option>()
        whenever(repository.findQuizById(quizId)).thenReturn(mockQuiz)
        whenever(mockQuiz.changeOptionTitle(questionId, optionId, newTitle, performedBy)).thenReturn(mockOption)

        val result = service.updateOptionTitle(quizId, questionId, optionId, newTitle, performedBy)

        assertEquals(mockOption, result)
        verify(repository).save(mockQuiz)
    }
}