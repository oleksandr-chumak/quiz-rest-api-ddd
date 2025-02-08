package com.github.quiz.api.infrastructure.repositories.impl.integration

import com.github.quiz.api.domain.models.quiz.Option
import com.github.quiz.api.domain.models.quiz.Question
import com.github.quiz.api.domain.models.quiz.Quiz
import com.github.quiz.api.infrastructure.entities.UserEntity
import com.github.quiz.api.infrastructure.entities.quiz.OptionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuestionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuizEntity
import com.github.quiz.api.infrastructure.repositories.impl.QuizRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import java.util.*

@DataJpaTest
@Import(QuizRepositoryImpl::class)
@Transactional
class QuizRepositoryImplIntegrationTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var quizRepository: QuizRepositoryImpl

    private fun createTestUser(): UserEntity {
        val user = UserEntity(userId = UUID.randomUUID())
        entityManager.persist(user)
        entityManager.flush()

        return user
    }

    private fun createTestQuiz(user: UserEntity = createTestUser()): QuizEntity {
        val quiz = QuizEntity(
            quizId = UUID.randomUUID(),
            name = "Test Name",
            questions = mutableListOf(),
            createdBy = user
        )
        entityManager.persist(quiz)
        entityManager.flush()

        return quiz
    }

    private fun createTestQuestion(quiz: QuizEntity = createTestQuiz()): QuestionEntity {
        val question = QuestionEntity(
            questionId = UUID.randomUUID(),
            text = "Who is the president of the United States?",
            options = mutableListOf(),
            correctAnswer = null,
            quizId = quiz.quizId
        )
        entityManager.persist(question)
        entityManager.flush()

        return question
    }

    private fun createTestOption(question: QuestionEntity = createTestQuestion()): OptionEntity {
        val option = OptionEntity(
            optionId = UUID.randomUUID(),
            text = "Test Option",
            questionId = question.questionId
        )
        entityManager.persist(option)
        entityManager.flush()

        return option
    }

    @BeforeEach
    fun setUp() {
        entityManager.clear()
    }

    @Test
    fun `should create a new quiz`() {
        val user = createTestUser()

        val testQuiz = Quiz(
            quizId = UUID.randomUUID(),
            name = "Test Name",
            createdBy = user.toDomain(),
        )

        val createdQuiz = quizRepository.createQuiz(testQuiz)
        assertThat(createdQuiz).isNotNull
        assertThat(createdQuiz.createdBy.userId).isEqualTo(testQuiz.createdBy.userId)
        assertThat(createdQuiz.name).isEqualTo(testQuiz.name)
    }

    @Test
    fun `should create a new question`() {
        val quiz = createTestQuiz()

        val testQuestion = Question(
            questionId = UUID.randomUUID(),
            text = "Who is the president of the United States?",
        )

        val createdQuestion = quizRepository.createQuestion(quiz.toDomain(), testQuestion)

        assertThat(createdQuestion).isNotNull
        assertThat(createdQuestion.text).isEqualTo("Who is the president of the United States?")
    }

    @Test
    fun `should create a new option`() {
        val question = createTestQuestion()

        val testOption = Option(
            optionId = UUID.randomUUID(),
            text = "Trump",
        )

        val option = quizRepository.createOption(question.toDomain(), testOption)

        assertThat(option).isNotNull
        assertThat(option.text).isEqualTo("Trump")
    }

    @Test
    fun `should find quiz by id`(){
        val quiz = createTestQuiz()

        val foundQuiz = quizRepository.findQuizById(quiz.quizId)

        assertThat(foundQuiz).isNotNull
        assertThat(foundQuiz?.quizId).isEqualTo(quiz.quizId)
    }

    @Test
    fun `should find quizzes created by a user`(){
        val user = createTestUser()

        val quizzes = listOf(
            createTestQuiz(user),
            createTestQuiz(user),
            createTestQuiz(user)
        )

        val result = quizRepository.findQuizzesCreatedByUser(user.userId)

        assertThat(result)
            .isNotNull
            .hasSize(3)
            .extracting("quizId")
            .containsExactlyInAnyOrder(*quizzes.map { it.quizId }.toTypedArray())
    }

    @Test
    fun `should find questions associated with quizzes`(){
        val quiz = createTestQuiz()
        val questions = listOf(
            createTestQuestion(quiz),
            createTestQuestion(quiz),
            createTestQuestion(quiz)
        )

        val foundQuestions = quizRepository.findQuestionsAssociatedWithQuiz(quiz.quizId)

        assertThat(foundQuestions)
            .isNotNull
            .hasSize(3)
            .extracting("questionId")
            .containsExactlyInAnyOrder(*questions.map { it.questionId }.toTypedArray())
    }

    @Test
    fun `should update quiz`(){
        val quiz = createTestQuiz()

        val testQuiz = Quiz(
            quizId = quiz.quizId,
            name = "Updated Quiz",
            createdBy = quiz.createdBy.toDomain()
        )

        val updatedQuiz = quizRepository.updateQuiz(testQuiz)
        val foundQuiz = entityManager.find(QuizEntity::class.java, quiz.quizId)

        assertThat(foundQuiz).isNotNull
        assertThat(foundQuiz.quizId).isEqualTo(testQuiz.quizId)
        assertThat(foundQuiz.name).isEqualTo(testQuiz.name)

        assertThat(updatedQuiz).isNotNull
        assertThat(updatedQuiz.quizId).isEqualTo(testQuiz.quizId)
        assertThat(updatedQuiz.name).isEqualTo(testQuiz.name)
    }

    @Test
    fun `should update question`(){
        val question = createTestQuestion()

        val testQuestion = Question(
            questionId = question.questionId,
            text = "Updated Question",
            correctAnswer = null
        )

        val updatedQuestion = quizRepository.updateQuestion(testQuestion)
        val foundQuestion = entityManager.find(QuestionEntity::class.java, question.questionId)

        assertThat(foundQuestion).isNotNull
        assertThat(foundQuestion.questionId).isEqualTo(testQuestion.questionId)
        assertThat(foundQuestion.text).isEqualTo(testQuestion.text)
        assertThat(foundQuestion.correctAnswer).isEqualTo(testQuestion.correctAnswer)

        assertThat(updatedQuestion).isNotNull
        assertThat(updatedQuestion.questionId).isEqualTo(testQuestion.questionId)
        assertThat(updatedQuestion.text).isEqualTo(testQuestion.text)
        assertThat(updatedQuestion.correctAnswer).isEqualTo(testQuestion.correctAnswer)
    }

    @Test
    fun `should update option`(){
        val option = createTestOption()

        val testOption = Option(
            optionId = option.optionId,
            text = "Updated Option",
        )


        val updatedOption = quizRepository.updateOption(testOption)
        val foundOption = entityManager.find(OptionEntity::class.java, option.optionId)

        assertThat(foundOption).isNotNull
        assertThat(foundOption.text).isEqualTo(testOption.text)

        assertThat(updatedOption).isNotNull
        assertThat(updatedOption.text).isEqualTo(testOption.text)
    }

    @Test
    fun `should delete quiz`(){
        val quiz = createTestQuiz()
        val res = quizRepository.deleteQuiz(quiz.quizId)
        val foundQuiz = entityManager.find(QuizEntity::class.java, quiz.quizId)

        assertThat(foundQuiz).isNull()
        assertThat(res).isTrue()
    }

    @Test
    fun `should delete question`(){
        val question = createTestQuestion()
        val res = quizRepository.deleteQuestion(question.questionId)
        val foundQuestion = entityManager.find(QuestionEntity::class.java, question.questionId)

        assertThat(foundQuestion).isNull()
        assertThat(res).isTrue()
    }

    @Test
    fun `should delete option`(){
        val option = createTestOption()
        val res = quizRepository.deleteOption(option.optionId)
        val foundOption = entityManager.find(OptionEntity::class.java, option.optionId)

        assertThat(foundOption).isNull()
        assertThat(res).isTrue()
    }
}