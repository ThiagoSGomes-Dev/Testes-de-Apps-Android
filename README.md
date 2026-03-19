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
 
```kotlin
@Test fun sum() { assertEquals(4, 2 + 2) }
```
 
---
 
### Mockito
 
```kotlin
val list = mock(List::class.java)
list.add("Item")
verify(list).add("Item")
```
 
---
 
### MockK
 
```kotlin
val repo = mockk<Repo>()
every { repo.getData() } returns "OK"
assertEquals("OK", repo.getData())
```
 
---
 
### Espresso
 
```kotlin
onView(withId(R.id.button)).perform(click())
onView(withText("Hello")).check(matches(isDisplayed()))
```
 
---
 
### Robolectric
 
```kotlin
val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
assertEquals("Title", activity.title)
```
 
---
 
### Turbine
 
```kotlin
flowOf(1, 2).test {
    assertEquals(1, awaitItem())
    assertEquals(2, awaitItem())
    awaitComplete()
}
```
 
---
 
### Maestro
 
```yaml
- launchApp
- tapOn: "Login"
- inputText: "user@example.com"
- assertVisible: "Bem-vindo"
```
 
---
 
## Conceitos Fundamentais
 
### Test Doubles
 
`Dummy`, `Fake`, `Stub` (e `Mock`/`Spy`) são tipos de objetos de teste conhecidos como **test doubles**.
 
```kotlin
// Fake: implementação de teste funcional de uma interface, porém simplificada.
//       Possui comportamento real, mas reduzido, sem todas as regras de produção.
 
// Dummy: objeto usado apenas para satisfazer parâmetros obrigatórios.
//        Não é utilizado na lógica do teste.
 
// Stub: objeto que retorna valores pré-definidos.
//       Não valida chamadas nem interações.
```
 
---

### Estruturas de Teste
 
#### Arrange-Act-Assert (AAA)
 
Organiza o **fluxo** do teste.

#### Given-When-Then (GWT)
 
Mais indicado quando o foco é **comportamento**, não implementação.
 
```kotlin
// Given: dado o contexto.
// When: quando algo acontece.
// Then: então espero um resultado.
```
 
```kotlin
// Arrange: prepara cenário.
// Act: executa a ação.
// Assert: verifica o resultado.
```

> `AAA` organiza o fluxo do teste. `Dummy`, `Fake`, `Stub` (test doubles) define como você substitui dependências.
 
---

## Testes de API com MockWebServer
 
### Anotações essenciais

### Nomeando testes (BDD)
 
```kotlin
fun `when fetching data then request correct endpoint`()
```
 
| Parte  | Significado              |
|--------|--------------------------|
| `when` | Acao: buscar dados       |
| `then` | Resultado: chamar endpoint correto |
 
O nome funciona como **documentacao viva** do comportamento esperado.
 
---
 
**`@Rule`** é um mecanismo flexível para adicionar, alterar ou interceptar comportamentos de testes. Age como um hook executado antes e depois de cada método de teste.
 
**`@Test`** indica ao JUnit que o método deve ser executado como teste. Sem essa anotação, o método é ignorado.
 
---
 
### Nomeando testes (BDD)
 
```kotlin
fun `when fetching data then request correct endpoint`()
```
 
| Parte  | Significado              |
|--------|--------------------------|
| `when` | Acao: buscar dados       |
| `then` | Resultado: chamar endpoint correto |
 
O nome funciona como **documentacao viva** do comportamento esperado.
 
---

### Anatomia de um teste de API
 
#### `runCatching { ... }`

#### `.blockingGet()`
 
Vem do RxJava. Executa a chamada de forma **sincrona**, garantindo que a requisicao seja concluida antes de prosseguir com as verificacoes.
 
```kotlin
runCatching { apiRule.service.getAlgumaCoisa().blockingGet() }
```

#### `takeRequest()`
 
```kotlin
val request = apiRule.server.takeRequest()
```
 
O `MockWebServer` registra todas as requisicoes recebidas. Esse metodo retorna a proxima requisicao da fila, com acesso a:
 
- metodo HTTP (`GET`, `POST`, ...)
- path
- headers
- body

#### Assertions
 
```kotlin
assertEquals("GET", request.method)
assertEquals("/minha/rota/aqui/", request.path)
```
 
---

### Fluxo completo
 
```
Teste inicia
    -> Retrofit chama o MockWebServer
    -> Servidor fake registra a requisicao
    -> takeRequest() captura os dados enviados
    -> Assertions validam metodo e rota
```
 
---
 
### O que esse teste garante (e o que nao garante)
 
| Garante | Nao garante |
|---------|-------------|
| Anotacao `@GET("...")` correta | Conteudo da resposta |
| Retrofit configurado corretamente | Parsing do JSON |
| Chamada HTTP feita como esperado | Regras de negocio |
| Contrato da API | Navegacao entre telas |
 
Se alguem alterar `@GET("rota_errada")`, o teste falha imediatamente.
 
---
 
### Evolucoes naturais
 
```kotlin
// Validar headers
assertEquals("application/json", request.getHeader("Content-Type"))
 
// Validar query params
assertTrue(request.path!!.contains("id=123"))
 
// Validar body (POST)
assertEquals("{...}", request.body.readUtf8())
```
 
---
 
### Tabela resumo
 
| Parte            | Funcao                                      |
|------------------|---------------------------------------------|
| `runCatching`    | Dispara a requisicao sem quebrar o teste    |
| `service`        | Retrofit apontando para o servidor fake     |
| `takeRequest()`  | Captura o que foi enviado ao MockWebServer  |
 
---
 
## Robolectric em Profundidade
 
### O que e o Robolectric?
 
Robolectric e um framework que permite executar testes de codigo Android diretamente na **JVM**, sem necessidade de um emulador ou dispositivo fisico. Ele faz isso implementando **"shadows"** (sombras) das classes do Android SDK, simulando seu comportamento em tempo de execucao.
 
Sem o Robolectric, qualquer classe do Android (`Activity`, `Context`, `View`, etc.) lancaria `RuntimeException` ao ser instanciada fora de um ambiente Android real.
 
---
 
### Como funciona internamente
 
Quando voce chama:
 
```kotlin
val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
```
 
O Robolectric executa internamente uma sequencia de etapas:
 
1. Cria uma instancia da `MainActivity` sem precisar de APK ou emulador.
2. Simula o ciclo de vida do Android (`onCreate`, `onStart`, `onResume`) via `.setup()`.
3. Retorna a instancia pronta para ser inspecionada via `.get()`.
 
---
 
### O ciclo de vida controlado
 
O `ActivityController` do Robolectric permite controlar cada etapa do ciclo de vida manualmente:
 
```kotlin
val controller = Robolectric.buildActivity(MainActivity::class.java)
 
controller.create()   // dispara onCreate()
controller.start()    // dispara onStart()
controller.resume()   // dispara onResume()
controller.pause()    // dispara onPause()
controller.stop()     // dispara onStop()
controller.destroy()  // dispara onDestroy()
 
// Ou tudo de uma vez:
controller.setup()    // create + start + resume
```
 
Isso e util para testar comportamentos especificos de cada fase do ciclo de vida.
 
---
 
### Shadows
 
Os *shadows* sao implementacoes paralelas das classes do Android SDK, escritas pelo time do Robolectric. Quando seu codigo chama `activity.getSystemService(...)`, por exemplo, o Robolectric intercepta essa chamada e delega ao shadow correspondente.
 
Voce tambem pode criar shadows customizados para classes especificas que precisam de comportamento diferente nos testes.
 
---
 
### Quando usar Robolectric vs Espresso
 
| Criterio                | Robolectric                         | Espresso                            |
|-------------------------|-------------------------------------|-------------------------------------|
| Ambiente de execucao    | JVM (sem emulador)                  | Emulador ou dispositivo fisico      |
| Velocidade              | Rapido                              | Lento (inicializa o app real)       |
| Fidelidade              | Simulada (shadows)                  | Alta (comportamento real do SO)     |
| Ideal para              | Logica de UI, ciclo de vida, estado | Fluxos de navegacao, interacao real |
 
---
 
### Exemplo pratico: testando estado apos rotacao
 
```kotlin
@Test
fun `deve manter o titulo apos rotacao de tela`() {
    val controller = Robolectric.buildActivity(MainActivity::class.java).setup()
    val activity = controller.get()
 
    // Simula rotacao de tela (recria a Activity)
    controller.configurationChange(Configuration().apply {
        orientation = Configuration.ORIENTATION_LANDSCAPE
    })
 
    val rotatedActivity = controller.get()
    assertEquals("Titulo esperado", rotatedActivity.title)
}
```
 
---
 
### Limitacoes do Robolectric
 
- Nem todos os comportamentos do Android sao perfeitamente replicados pelos shadows. Casos muito especificos de hardware ou de versoes muito recentes do SDK podem se comportar de forma diferente.
- Testes de animacoes e transicoes visuais nao sao confiaveis no Robolectric.
- Para validar fluxos reais de navegacao com `Intent` entre `Activities`, o Espresso tende a ser mais adequado.
 
Dispara a requisicao HTTP sem quebrar o teste em caso de erro (ex: JSON vazio). Aqui o objetivo e apenas **disparar a chamada**, nao validar a resposta.
