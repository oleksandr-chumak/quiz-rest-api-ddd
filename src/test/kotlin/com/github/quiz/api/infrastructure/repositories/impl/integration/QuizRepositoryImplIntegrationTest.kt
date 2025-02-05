package com.github.quiz.api.infrastructure.repositories.impl.integration

import com.github.quiz.api.infrastructure.entities.UserEntity
import com.github.quiz.api.infrastructure.entities.quiz.OptionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuestionEntity
import com.github.quiz.api.infrastructure.entities.quiz.QuizEntity
import com.github.quiz.api.infrastructure.repositories.impl.QuizRepositoryImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(QuizRepositoryImpl::class)
class QuizRepositoryImplIntegrationTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var quizRepository: QuizRepositoryImpl

    @Test
    fun `createQuiz should persist a new quiz`() {
        val user = UserEntity(userId = 0)
        entityManager.persist(user)
        entityManager.flush()

        val quiz = quizRepository.createQuiz(user.userId, "Math Quiz")
        assertThat(quiz.quizId).isNotNull()
        assertThat(quiz.name).isEqualTo("Math Quiz")
        assertThat(quiz.createdBy.userId).isEqualTo(user.userId)
    }


    @Test
    fun `findQuizById should retrieve the quiz with questions and options`() {
        val user = UserEntity(userId = 0)
        entityManager.persist(user)
        val quiz = QuizEntity(name = "Science Quiz", createdBy = user)
        entityManager.persist(quiz)
        val question = QuestionEntity(text = "What is gravity?", quiz = quiz)
        entityManager.persist(question)
        val option = OptionEntity(text = "A force", question = question)
        entityManager.persist(option)
        question.options.add(option)
        entityManager.flush()

        val foundQuiz = quizRepository.findQuizById(quiz.quizId)
        assertThat(foundQuiz).isNotNull
    }


    // Additional tests for other methods can be added here
}