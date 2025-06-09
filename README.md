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

## 🔐 Autenticação Spotify e Gerenciamento de Sessão

A autenticação é realizada com a biblioteca oficial da Spotify:

```
implementation("com.spotify.android:auth:<versão>")
```

1. O app abre o login do Spotify com `AuthorizationClient.openLoginActivity(...)`.
2. Após a autenticação, é retornado um `authorizationCode`.
3. O código é trocado por um `access_token`, `refresh_token` e tempo de expiração via `AuthRepository`.

---

### 🧠 SessionManager

A classe `SessionManagerImpl` centraliza a lógica de autenticação e sessão segura do usuário, incluindo:

- Troca de `code` por token
- Armazenamento seguro do `access_token`, `refresh_token` e `expires_at`
- Armazenamento das credenciais do app (`clientId` e `clientSecret`)
- Atualização automática do token expirado

```kotlin
val isLoggedIn = sessionManager.isLoggedIn()

val token = sessionManager.getAccessToken()

val refreshed = sessionManager.refreshAccessToken()

sessionManager.clearSession()
```

Autenticação inicial:

```kotlin
sessionManager.loginWithCode(code, clientId, clientSecret)
```

Verificação e renovação automática da sessão:

```kotlin
val isSessionValid = sessionManager.ensureValidSession()
```

---

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

## 🔧 Configuração
- Acesse: [Spotify Developer Dashboard](https://developer.spotify.com/dashboard)
- Crie seu app e configure o redirect URI para: "pedroid://callback"
- Copie o CLIENT_ID e CLIENT_SECRET
- No arquivo keys.properties, insira:
  - CLIENT_ID=xxx
  - CLIENT_SECRET=xxx
 
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
