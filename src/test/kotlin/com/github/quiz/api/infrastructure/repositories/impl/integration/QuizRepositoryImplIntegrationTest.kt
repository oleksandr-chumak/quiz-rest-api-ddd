package com.github.quiz.api.infrastructure.repositories.impl.integration

import com.github.quiz.api.infrastructure.entities.UserEntity
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

@DataJpaTest
@Import(QuizRepositoryImpl::class)
@Transactional
class QuizRepositoryImplIntegrationTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var quizRepository: QuizRepositoryImpl

    private fun createTestUser(): UserEntity {
        val user = UserEntity()
        entityManager.persist(user)
        entityManager.flush()

        return user
    }

    private fun createTestQuiz(user: UserEntity = createTestUser()): QuizEntity {
        val quiz = QuizEntity(
            name = "Test Name",
            questions = mutableListOf(),
            createdBy = user
        )
        entityManager.persist(quiz)
        entityManager.flush()

        return quiz;
    }

    private fun createTestQuestion(quiz: QuizEntity = createTestQuiz()): QuestionEntity {
        val question = QuestionEntity(
            text = "Who is the president of the United States?",
            options = mutableListOf(),
            correctAnswer = null,
            quizId = quiz.quizId
        )
        entityManager.persist(question)
        entityManager.flush()

        return question
    }

    @BeforeEach
    fun setUp() {
        entityManager.clear()
    }

    @Test
    fun `should create a new quiz`() {
        val user = createTestUser()

        val quiz = quizRepository.createQuiz(user.toDomain(), "New Quiz")
        assertThat(quiz).isNotNull
        assertThat(quiz.createdBy.userId).isEqualTo(user.userId)
        assertThat(quiz.name).isEqualTo("New Quiz")
    }

    @Test
    fun `should create a new question`() {
        val quiz = createTestQuiz()

        val question = quizRepository.createQuestion(quiz.toDomain(), "Who is the president of the United States?")

        assertThat(question).isNotNull
        assertThat(question.text).isEqualTo("Who is the president of the United States?")
    }

    @Test
    fun `should create a new option`() {
        val question = createTestQuestion()

        val option = quizRepository.createOption(question.toDomain(), "Trump")

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

        quizRepository.updateQuiz(quiz.quizId, "Updated Quiz")

        val updatedQuiz = entityManager.find(QuizEntity::class.java, quiz.quizId)

        assertThat(updatedQuiz).isNotNull
        assertThat(updatedQuiz?.quizId).isEqualTo(quiz.quizId)
        assertThat(updatedQuiz?.name).isEqualTo("Updated Quiz")
    }

    @Test
    fun `should update question`(){
        val question = createTestQuestion()

        quizRepository.updateQuestion(question.questionId, "Updated Question", null)

        val updatedQuestion = entityManager.find(QuestionEntity::class.java, question.questionId)

        assertThat(updatedQuestion).isNotNull
        assertThat(updatedQuestion?.questionId).isEqualTo(question.questionId)
        assertThat(updatedQuestion?.text).isEqualTo("Updated Question")
        assertThat(updatedQuestion?.correctAnswer).isEqualTo(null)
    }

}