package com.rocketseat.rocketia.data.mapper

import com.ibm.icu.impl.IllegalIcuArgumentException
import com.rocketseat.rocketia.data.createAIChatTextEntityStub
import com.rocketseat.rocketia.domain.model.AIChatText
import com.rocketseat.rocketia.domain.model.AIChatTextType
import org.junit.Assert.assertThrows
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AIChatHistoryMapperTest {

    // Given / When / Then - É um jeito de estruturar testes baseado em comportamento.
    // Veio do BDD Behavior Driven Development (Desenvolvimento orientado pelo comportamento).
    // A ideia é escrever o teste como se fosse uma mini história
    @Test
    fun `GIVEN AIChatTextEntity from USER QUESTION type WHEN toDomain is called THEN should convert to AIChatText USER QUESTION`() {
        // Given (dado o contexto)
        val userQuestionEntity = createAIChatTextEntityStub(from = AIChatTextType.USER_QUESTION)

        // When (quando algo acontece)
        val result = userQuestionEntity.toDomain()

        // Then (então espero um resultado)
        assertTrue { result is AIChatText.UserQuestion }
        assertEquals(userQuestionEntity.text, (result as AIChatText.UserQuestion).question )
    }

    @Test
    fun `GIVEN AIChatTextEntity from AI ANSWER type WHEN toDomain is called THEN should convert to AIChatText from AI ANSWER`() {
        // Given (dado o contexto)
        val aiAnswerEntity = createAIChatTextEntityStub(from = AIChatTextType.AI_ANSWER)

        // When (quando algo acontece)
        val result = aiAnswerEntity.toDomain()

        // Then (então espero um resultado)
        assertTrue { result is AIChatText.AIAnswer }
        assertEquals(aiAnswerEntity.text, (result as AIChatText.AIAnswer).answer )
    }

    @Test
    fun `GIVEN AIChatTextEntity from AI UNKNOWN type WHEN toDomain is called THEN should trigger an exception`() {
        // Given (dado o contexto)
        val unknownEntity = createAIChatTextEntityStub(from = null)

        // When (quando algo acontece)
        val result = assertThrows(IllegalIcuArgumentException::class.java) {
            unknownEntity.toDomain()
        }

        // Then (então espero um resultado)
        assertEquals("Invalid from value: UNKNOWN", result.message)
    }

}