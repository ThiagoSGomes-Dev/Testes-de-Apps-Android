# Testes de Apps Android

## Frameworks de Teste Android

| Nome | Descrição | Quando Utilizar? | Exemplo de Código | Link |
|------|-----------|-----------------|-------------------|------|
| **JUnit** | Framework base para testes unitários. | Para testar lógica de negócio sem dependência do Android SDK. | [code](https://github.com/ThiagoSGomes-Dev/Testes-de-Apps-Android/blob/main/README.md#junit)  | junit.org |
| **Mockito** | Biblioteca para criar e verificar mocks em Java/Kotlin. | Para simular dependências como APIs ou bancos de dados em testes unitários. | `val list = mock(List::class.java)` `list.add("Item") verify(list).add("Item")` | site.mockito.org |
| **MockK** | Biblioteca de mocking específica para Kotlin. | Para testar classes em Kotlin com suporte a corrotinas, funções de extensão e objetos. | `val repo = mockk<Repo>() every { repo.getData() } returns "OK" assertEquals("OK", repo.getData())` | mockk.io |
| **Espresso** | Ferramenta para testes de UI nativos no Android. | Para validar interações de interface como cliques, textos e navegação em telas. | `onView(withId(R.id.button)).perform(click())` `onView(withText("Hello")).check(matches(isDisplayed()))` | developer.android.com/espresso |
| **Robolectric** | Framework para testar componentes Android na JVM. | Para testar Activities, Views e lógica com o Android SDK sem emulador. | `val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get() assertEquals("Title", activity.title)` | robolectric.org |
| **Turbine** | Biblioteca para testar Flow e corrotinas. | Para testar valores emitidos por Flow, StateFlow ou SharedFlow. | `flowOf(1, 2).test { assertEquals(1, awaitItem()) assertEquals(2, awaitItem()) awaitComplete() }` | github.com/cashapp/turbine |
| **Maestro** | Ferramenta de testes E2E com arquivos YAML. | Para escrever testes de navegação e fluxo do app de forma simples e rápida, ideal para CI/CD. | `- launchApp - tapOn: "Login" - inputText: "user@example.com" - assertVisible: "Bem-vindo"` | maestro.mobile.dev |

### JUnit

`@Test fun sum() { assertEquals(4, 2 + 2) }`
