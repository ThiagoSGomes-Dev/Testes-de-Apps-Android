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

***Given-When-Then (GWT) → é mais indicado quando o foco do teste é comportamento, não implementação***

```kotlin
// Given: dado o contexto.

// When: quando algo acontece.

// Then: então espero um resultado.
```

OBS: ```AAA``` organiza o fluxo do teste. ```Dummy, Fake, Stub (test doubles)``` define como você substitui dependências.

### Testes em Api

```@Rule``` é um mecanismo flexível para adicionar, alterar ou interceptar comportamentos de testes, agindo como um "gancho" (hook) antes e depois da execução de cada método de teste.

```@Test``` Diz pro JUnit “isso aqui é um teste que deve ser executado”, Sem isso, o método é ignorado.

2. Nome do teste
fun `when fetching data then request correct endpoint`

### Nome descritivo (BDD):

|      |                    |
|------|--------------------|
| When | Ação: buscar dados |
| Then | Resultado: chamar endpoint correto |

É basicamente documentação viva.

🔹 3. runCatching { ... }
runCatching { apiRule.service.getAlgumaCoisa().blockingGet() }
O que acontece aqui:

Você chama sua API (getAlgumaCoisa())

Usa .blockingGet() (provavelmente RxJava) pra executar de forma síncrona

Qualquer erro é capturado silenciosamente

🤔 Por que usar isso?

Porque você não está testando a resposta da API, só quer:

👉 disparar a requisição HTTP

Se der erro (ex: JSON vazio), não importa aqui.

🔹 4. apiRule.service.getAlgumaCoisa()

👉 Esse service é o Retrofit criado dentro da sua ApiTestRule.

Mas ao invés de bater na internet:

👉 Ele chama o MockWebServer

🔹 5. .blockingGet()

👉 Vem do RxJava.

Serve pra:

Executar a chamada de forma síncrona

Esperar a resposta antes de continuar

Sem isso, o teste poderia terminar antes da requisição acontecer.

🔹 6. apiRule.server.takeRequest()
val request = apiRule.server.takeRequest()

🔥 Aqui está o coração do teste.

👉 O MockWebServer guarda todas as requisições feitas.

Esse método:

Pega a próxima requisição feita

Retorna um objeto com:

método (GET, POST…)

path

headers

body

🔹 7. assertEquals("GET", request.method)
assertEquals("GET", request.method)

👉 Verifica:

“A requisição foi do tipo GET?”

Se não for → teste falha ❌

🔹 8. assertEquals("/minha/rota/aqui/", request.path)
assertEquals("/minha/rota/aqui/", request.path)

👉 Verifica:

“A URL chamada foi exatamente essa?”

🧠 Fluxo completo (resumido)

Teste começa

Você chama a API (via Retrofit)

Retrofit chama o MockWebServer

O servidor fake registra a requisição

Você captura essa requisição

Valida método + rota

🎯 O que esse teste garante

Ele valida que:

Seu @GET("minha/rota/aqui/") está correto

Seu Retrofit está configurado certo

A chamada HTTP é feita como esperado

⚠️ O que ele NÃO testa

Conteúdo da resposta

Parsing do JSON

Regra de negócio

Navegação (onboarding etc.)

🧠 Insight importante

Esse teste é sobre:

👉 contrato da API

Se alguém mudar isso:

@GET("rota_errada")

👉 seu teste quebra imediatamente 💥

🚀 Evoluções naturais desse teste

Você pode evoluir pra validar:

Headers
assertEquals("application/json", request.getHeader("Content-Type"))
Query params
assertTrue(request.path!!.contains("id=123"))
Body (POST)
assertEquals("{...}", request.body.readUtf8())
💥 Resumo direto

Cada parte do teste:

Parte	Função
runCatching	dispara a requisição sem quebrar o teste
service	Retrofit chamando servidor fake
takeRequest()	captura o que foi enviado
assertEquals	valida comportamento

