package com.rocketseat.rocketia

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rocketseat.rocketia.ui.activity.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.runners.Suite

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var appContext: Context

    // Executa antes de cada teste.
    // Obtém o contexto real do app rodando no dispositivo/emulador,
    // usado para acessar recursos, preferências e sistema Android.
    @Before
    fun setUp() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        // Verifica se o contexto obtido pertence ao pacote esperado do app.
        // Garante que o teste está rodando apontando para a aplicação correta.
        assertEquals("com.rocketseat.rocketia", appContext)
    }

    @Test
    fun readAppNameFromResource() {
        // Lê o valor do recurso string app_name definido em res/values/strings.xml.
        // Valida se o nome retornado corresponde ao esperado.
        // Testa acesso a recursos Android via contexto.
        val appName = appContext.getString(R.string.app_name)

        assertEquals("RocketIA", appName)
    }

    @Test
    fun launchMainActivity() {
        // Inicia a MainActivity em ambiente de teste instrumentado.
        // Verifica se a Activity pode ser criada sem falhas.
        // Confirma que o ciclo básico de inicialização funciona.
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            assertNotNull(scenario)
        }
    }

    @Test
    fun writeAndReadSharedPreferences() {
        // Cria (ou abre) um arquivo de SharedPreferences de teste.
        // Escreve um valor e depois lê o mesmo valor.
        // Valida se a persistência local está funcionando corretamente.
        val pref = appContext.getSharedPreferences("test_prefis", Context.MODE_PRIVATE)
        pref.edit().putString("key", "hello").apply()
        val value = pref.getString("key", null)

        assertEquals("hello", value)
    }

}
