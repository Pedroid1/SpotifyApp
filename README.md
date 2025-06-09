<h1 align="center">🎵 SpotifyApp</h1>
<h3 align="center">Clean Architecture · MVVM · Paging 3 · Room · Spotify Auth</h3>

<p align="center">
  <a href="https://wa.me/+5574999637391">
    <img alt="WhatsApp" src="https://img.shields.io/badge/WhatsApp-25D366?style=for-the-badge&logo=whatsapp&logoColor=white"/>
  </a>
  <a href="https://www.linkedin.com/in/pedro-henrique-de-souza-ar/">
    <img alt="LinkedIn" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"/>
  </a>
  <a href="mailto:pedro.steam2016@hotmail.com">
    <img alt="Gmail" src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white"/>
  </a>
</p>

---

## 📌 Sobre o Projeto

O **SpotifyApp** é um projeto Android desenvolvido em **Kotlin** que replica a experiência do Spotify utilizando autenticação oficial e acesso à API Web. A arquitetura segue os princípios da **Clean Architecture**, com foco em **modularização**, **testabilidade**, **segurança** e **escalabilidade**.

---

## ✅ Requisitos Obrigatórios

- [x] Autenticação via Spotify
- [x] Listar artistas
- [x] Listar álbuns de um artista
- [x] Paginação com scroll infinito (Paging 3)
- [x] Funcionamento offline (cache local com Room)
- [x] Testes unitários
- [x] Segmentação de commits

### 🎁 Bônus

- [ ] Testes instrumentados
- [x] Firebase Crashlytics
- [x] CI/CD com pipeline e deploy
- [x] Responsividade (celular e tablet)

---

## 🧱 Arquitetura

- 🧠 Clean Architecture (UI, Domain, Data)
- 🧩 MVVM (Model-View-ViewModel)
- 🧪 Hilt para Injeção de Dependência
- ⚙️ UiState + DataResource para estados de tela
- 🔄 Paging 3 com RemoteMediator e Room

<p align="center">
 <img src="prints/arch.png" width="80%"/>
</p>

---

## 🗂️ Estrutura Modular

### 🔹 Core Modules
- `analytics`: Firebase Analytics e Crashlytics
- `common`: Extensões e utilitários compartilhados
- `data`: Retrofit, Room, repositórios e mediators
- `domain`: Use Cases e regras de negócio
- `eventbus`: Comunicação desacoplada entre features
- `model`: Modelos de dados compartilhados
- `navigation`: Navegação modular
- `testing`: Utilitários e fakes para testes
- `designsystem`: Componentes visuais reutilizáveis

### 🔸 Feature Modules
- `home`: Tela de artistas
- `albums`: Lista de álbuns
- `playlist`: Visualização e criação de playlists
- `profile`: Perfil do usuário autenticado

### ⚙️ Infra
- `build-logic`: Convention Plugins com DSL Kotlin

---

## 🧩 Comunicação Desacoplada entre Features

Cada feature possui dois módulos:

- `privatemodule`: Implementações internas
- `publicmodule`: Interface exposta para o app e outras features

### 📁 Exemplo – Feature Albums

```kotlin
interface AlbumsFeatureCommunicator {
    fun launchFeature(albumsFeatureArgs: AlbumsFeatureArgs)

    data class AlbumsFeatureArgs(
        val previousRoute: String,
        val artist: Artist
    ) : Serializable
}
```
---

## 🔐 Autenticação Spotify e Gerenciamento de Sessão

A autenticação é realizada com a biblioteca oficial da Spotify:

```
implementation("com.spotify.android:auth:<versão>")
```

1. O app abre o login do Spotify com `AuthorizationClient.openLoginActivity(...)`.
2. Após a autenticação, é retornado um `authorizationCode`.
3. O código é trocado por um `access_token`, `refresh_token` e tempo de expiração via `AuthRepository`.

### 🔒 Armazenamento Seguro

Os tokens são armazenados com **EncryptedSharedPreferences**, utilizando o **Android Keystore** para garantir:

- Criptografia AES-256 na escrita e leitura
- Impossibilidade de acesso direto ao conteúdo salvo, mesmo com acesso root
- Proteção contra ataques físicos e lógicos ao armazenamento local

```kotlin
val prefs = EncryptedSharedPreferences.create(
    context,
    "secure_prefs",
    MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build(),
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

Essa arquitetura garante que os dados sensíveis do usuário estejam protegidos mesmo em dispositivos comprometidos, seguindo as melhores práticas recomendadas pelo Android.

---

## ⚙️ CI/CD com GitHub Actions

O projeto utiliza **GitHub Actions** para garantir a qualidade contínua do código em cada *push* ou *pull request* para a branch `main`.

### 🔍 Etapas da Pipeline

#### 🧪 `check`
- Valida o wrapper do Gradle
- Executa o **Detekt** para garantir que o código siga os padrões de qualidade e estilo definidos
- Timeout configurado: `60 minutos`
- Evita execuções concorrentes com `concurrency.group`

#### ✅ `unit-tests`
- Executa os testes unitários automatizados
- Timeout configurado: `10 minutos`
- Depende da conclusão bem-sucedida do job `check`

### ✅ Benefícios
- Garante que o código enviado para a branch principal esteja **formatado corretamente** e **sem quebras nos testes**
- Automatiza validações manuais e reduz erros humanos
- Melhora a confiança no deploy contínuo (CI)

---

## 🔧 Configuração

### 🎵 Integração com a API do Spotify

1. Acesse o [Spotify Developer Dashboard](https://developer.spotify.com/dashboard).
2. Crie um novo app ou edite um existente.
3. No campo **Redirect URI**, adicione:
   ```
   pedroid://callback
   ```
4. No campo **Android packages**, insira os seguintes packages do app:
   ```
   com.pedroid.spotifyapp.debug
   com.pedroid.spotifyapp
   ```
5. Insira também a chave **SHA1** do certificado de assinatura (keystore).
6. Na seção **Which API/SDKs are you planning to use?**, selecione a opção:
   ```
   ☑ Android
   ```
   > Isso é necessário para ativar os fluxos de autenticação corretos para dispositivos Android.

---

### 🔎 Como obter o SHA1 do projeto

Você pode obter o SHA1 da assinatura `debug` com um dos métodos abaixo:

#### ✅ Opção 1 – Via terminal:

Execute o comando abaixo na raiz do projeto:

```bash
./gradlew signingReport
```

No console, procure a saída semelhante a esta:

```
Variant: debug
Config: debug
Store: ...
Alias: ...
SHA1: A1:B2:C3:D4:E5:...
```

#### ✅ Opção 2 – Via Android Studio:

1. Abra a aba **Gradle** (geralmente no canto direito da IDE).
2. Procure pela opção **Execute Gradle Task** e selecione.
3. Ao abrir o popup, digite o seguinte comando: `signingReport`.
4. A saída será exibida no console inferior. Copie o valor do campo `SHA1`.

> 📎 Documentação oficial: [Android - Signing your app](https://developer.android.com/studio/publish/app-signing#signing-report)

---

### 🗝️ Configuração local

Copie o `CLIENT_ID` e `CLIENT_SECRET` do projeto criado no Dashboard do Spotify.

No arquivo `keys.properties` (já criado na raiz do projeto), insira:

```
CLIENT_ID=seu_client_id
CLIENT_SECRET=seu_client_secret
```
 
## ▶️ Execução
- Pré-requisitos:
  - Java 17
  - Clone o projeto:
    - git clone https://github.com/Pedroid1/SpotifyApp.git
    - Abra no Android Studio e aguarde a sincronização do Gradle. Em seguida, execute o app em um emulador ou dispositivo real.

## Features Screenshots
<p float="left" align="left">
  <img src="prints/artists.jpg" width="25%"/>
  <img src="prints/albums.jpg" width="25%"/>
  <img src="prints/playlists.jpg" width="25%"/>
  <img src="prints/profile.jpg" width="25%"/>
</p>
   
## 👨‍💻 Autor
Pedro Henrique de Souza Araujo | [Linkedin](https://www.linkedin.com/in/pedro-henrique-de-souza-ar/)
