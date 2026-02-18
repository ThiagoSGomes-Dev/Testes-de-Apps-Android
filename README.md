# Testes de Apps Android

## Frameworks de Teste Android

| Nome | Descrição | Quando Utilizar? | Exemplo de Código | Link |
|------|-----------|-----------------|-------------------|------|
| **JUnit** | Framework base para testes unitários. | Para testar lógica de negócio sem dependência do Android SDK. | [code](#junit)  | [junit.org](https://junit.org/) |
| **Mockito** | Biblioteca para criar e verificar mocks em Java/Kotlin. | Para simular dependências como APIs ou bancos de dados em testes unitários. | [code](#mockito) | [site.mockito.org](https://site.mockito.org/) |
| **MockK** | Biblioteca de mocking específica para Kotlin. | Para testar classes em Kotlin com suporte a corrotinas, funções de extensão e objetos. | [code](#mockk) | [mockk.io](https://mockk.io/) |
| **Espresso** | Ferramenta para testes de UI nativos no Android. | Para validar interações de interface como cliques, textos e navegação em telas. | [code](#espresso) | [developer.android.com/espresso](https://developer.android.com/training/testing/espresso?hl=pt-br) |
| **Robolectric** | Framework para testar componentes Android na JVM. | Para testar Activities, Views e lógica com o Android SDK sem emulador. | [code](#robolectric) | [robolectric.org](https://robolectric.org/) |
| **Turbine** | Biblioteca para testar Flow e corrotinas. | Para testar valores emitidos por Flow, StateFlow ou SharedFlow. | [code](#turbine) | [github.com/cashapp/turbine](https://cashapp.github.io/turbine/docs/1.x/) |
| **Maestro** | Ferramenta de testes E2E com arquivos YAML. | Para escrever testes de navegação e fluxo do app de forma simples e rápida, ideal para CI/CD. | [code](#maestro) | [maestro.mobile.dev](https://docs.maestro.dev/) |

### JUnit

`@Test fun sum() { assertEquals(4, 2 + 2) }`

### Mockito

`val list = mock(List::class.java)` `list.add("Item") verify(list).add("Item")`

### MockK

`val repo = mockk<Repo>() every { repo.getData() } returns "OK" assertEquals("OK", repo.getData())`

### Espresso

`onView(withId(R.id.button)).perform(click())` `onView(withText("Hello")).check(matches(isDisplayed()))`

### Robolectric

`val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get() assertEquals("Title", activity.title)`

### Turbine

`flowOf(1, 2).test { assertEquals(1, awaitItem()) assertEquals(2, awaitItem()) awaitComplete() }`

### Maestro

`- launchApp - tapOn: "Login" - inputText: "user@example.com" - assertVisible: "Bem-vindo"`

***Dummy, Fake, Stub (e Mock/Spy) -> são tipos de objetos de teste (test doubles)***

```kotlin
// Fake: implementação de teste funcional de uma interface, porém simplificada.
//       Possui comportamento real, mas reduzido, sem todas as regras de produção.

// Dummy: objeto usado apenas para satisfazer parâmetros obrigatórios.
//        Não é utilizado na lógica do teste.

// Stub: objeto que retorna valores pré-definidos.
//       Não valida chamadas nem interações.
```
***Arrange–Act–Assert (AAA) → é estrutura do teste***

```kotlin
// Arrange: prepara cenário.

// Act: executa a ação.

// Assert: verifica o resultado.
```

OBS: ```AAA``` organiza o fluxo do teste. ```Dummy, Fake, Stub (test doubles)``` define como você substitui dependências.
