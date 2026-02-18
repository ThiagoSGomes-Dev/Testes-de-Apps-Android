package com.rocketseat.rocketia.data

import android.widget.Toast
import com.rocketseat.rocketia.data.datasource.AIChatLocalDataSource
import com.rocketseat.rocketia.data.datasource.AIChatRemoteDataSource
import com.rocketseat.rocketia.data.datasource.FakeAIChatLocalDataSourceImpl
import com.rocketseat.rocketia.data.datasource.FakeAIChatRemoteDataSourceImpl
import com.rocketseat.rocketia.data.local.database.AIChatTextEntity
import com.rocketseat.rocketia.data.repository.AIChatRepositoryImpl
import com.rocketseat.rocketia.domain.model.AIChatTextType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
// Run não será mais do Junit e sim do Roboletric
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ExampleUnitTest {
    // Para testes unitarios não é necessario uma suite de testes, poóis eles rodam direto na JVM

    // Fake: implementação de teste funcional de uma interface, porém simplificada.
    // Possui comportamento real, mas reduzido, sem todas as regras de produção.

    // Dummy: objeto usado apenas para satisfazer parâmetros obrigatórios.
    // Não é utilizado na lógica do teste.

    // Stub (via MockK): objeto que retorna valores pré-definidos.
    // Não valida chamadas nem interações.

    @Test
    fun `example fake dummy end`() = runTest {
        // arrange -> preparar cenário
        val fakeAIChatRemoteDataSourceImpl =
            FakeAIChatRemoteDataSourceImpl(validStacks = listOf("Kotlin"))
        val dummyAIChatTextEntity = AIChatTextEntity(
            from = AIChatTextType.USER_QUESTION.name,
            stack = "stack",
            datetime = 0L,
            text = "text"
        )

        val dummyIAChatTextEntityList =
            listOf(dummyAIChatTextEntity, dummyAIChatTextEntity, dummyAIChatTextEntity)
        val stubAIChatRemoteDataSourceImpl = mockk<AIChatLocalDataSource>()
        coEvery { stubAIChatRemoteDataSourceImpl.getAIChatByStack(any()) } returns dummyIAChatTextEntityList

        val testRepository = AIChatRepositoryImpl(
            aiChatLocalDataSource = stubAIChatRemoteDataSourceImpl,
            aiChatRemoteDataSource = fakeAIChatRemoteDataSourceImpl
        )

        // act -> executar o que quer testar
        val result = testRepository.getAIChatByStack(stack = "Java")

        // assert -> verificar o resultado
        coVerify(exactly = 1) {
            stubAIChatRemoteDataSourceImpl.getAIChatByStack(stack = "Java")
            assertEquals(3, result.size)
        }

        // mock: objeto configurado para retornar valores fixos, possibilitando verificar interações.
        // spy: wrapper sobre objeto real que registra suas interações.
        @Test
        fun `example mock end spy`() = runTest {
            // arrange
            val fakeAIChatLocalDataSourceImpl = FakeAIChatLocalDataSourceImpl()

            val mockAIChatRemoteDataSourceImpl = mockk<AIChatRemoteDataSource>(relaxed = true)
            val spyAIChatLocalDataSourceImpl = spyk<AIChatLocalDataSource>(fakeAIChatLocalDataSourceImpl)

            val testRepository = AIChatRepositoryImpl(
                aiChatLocalDataSource = spyAIChatLocalDataSourceImpl,
                aiChatRemoteDataSource = mockAIChatRemoteDataSourceImpl
            )
            // act
            testRepository.sendUserQuestion("question")

            // assert
            coVerify(exactly = 1) { mockAIChatRemoteDataSourceImpl.sendPrompt(any(), any()) }
            coVerify(exactly = 1) { spyAIChatLocalDataSourceImpl.insertAIChatConversation(any(), any()) }

        }

    }
}