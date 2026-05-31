# EXEMPLO JAVA - Spring Boot Backend com Banco de Dados e Integração de IA

Este é o repositório do backend do projeto, desenvolvido com **Spring Boot 3.x/4.x** (Java 21), integrado a um banco de dados **MySQL** e estruturado para suportar múltiplos modelos de Inteligência Artificial usando **LangChain4j**. O projeto possui suporte para IA local e gratuita (**Ollama / Llama 3.2**) e IA comercial de alta performance (**Google Gemini API / gemini-1.5-flash**).

## Repositório Central (Orquestrador)

Este projeto foi projetado para rodar em conjunto com um frontend Next.js e serviços auxiliares de banco de dados e inteligência artificial. 

A orquestração central de todos estes serviços (incluindo o Docker Compose) e a infraestrutura unificada estão concentradas no repositório guarda-chuva principal:
*   [Repositório Central](https://github.com/LuisOtavioSantos/Intro-o-Ao-Desenvolvimento-de-Sistemas-Web.git)

Recomenda-se clonar e executar o projeto a partir do repositório central para obter o ambiente completo de desenvolvimento.

---

## Pré-requisitos
Certifique-se de ter instalado em sua máquina:
* **Docker** e **Docker Compose**
* **Git**
* Um cliente HTTP como **Postman** (coleções inclusas na pasta `requests/`)

---

## 1. Configurando Chaves e Credenciais de API

Antes de rodar a aplicação, você precisa configurar os segredos de integração no seu ambiente. Siga as instruções abaixo para obter cada uma das chaves:

### A. Chave Secreta do JWT (Segurança)
Para criptografar os tokens JWT de autenticação do sistema, gere uma chave de 256 bits codificada em Base64 rodando um dos comandos a seguir no seu terminal:
* **OpenSSL (Recomendado):** `openssl rand -base64 32`
* **Python:** `python -c "import secrets, base64; print(base64.b64encode(secrets.token_bytes(32)).decode())"`
* **Node.js:** `node -e "console.log(require('crypto').randomBytes(32).toString('base64'))"`

### B. Chave de API para Envio de E-mails (Gmail)
Para habilitar o envio automático de e-mails de validação de cadastro:
1. Acesse sua conta do Google e vá em **Segurança**.
2. Certifique-se de que a **Verificação em Duas Etapas** está ATIVADA.
3. Acesse o painel de Senhas de App: [https://myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords)
4. Dê um nome para o app (ex: `Spring Boot App`) e gere uma nova **Senha de App** de 16 caracteres.
5. Copie essa senha (sem os espaços) para configurar na variável `MAIL_PASSWORD`.

### C. Chave de API para a IA do Google Gemini (Premium)
Para utilizar o modelo premium `gemini-1.5-flash` nas requisições da IA paga:
1. Acesse o Google AI Studio: [https://aistudio.google.com/api-keys](https://aistudio.google.com/api-keys)
2. Clique em **Criar Novo Projeto** (Create API key in new project).
3. Clique em **Criar Token / Criar Chave de API**.
4. Copie a chave gerada (ela começa com `AIzaSy...`) para configurar na variável `GEMINI_API_KEY`.

---

## 2. Configurando o Arquivo de Ambiente (`.env`)

Copie o arquivo de template `.env-template` para criar o seu arquivo `.env` definitivo:
```bash
cp .env-template .env
```
*(No Windows PowerShell, use: `cp .env-template .env`)*

Abra o arquivo `.env` recém-criado e preencha com as suas chaves e credenciais:
```properties
# E-mail de Envio (Gmail)
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_de_app_gerada_no_google

# Chave API do Google Gemini
GEMINI_API_KEY=sua_chave_gemini_aqui_comecando_com_AIzaSy
```

> [!IMPORTANT]
> O arquivo `.env` contém dados sensíveis. Ele está configurado no `.gitignore` para nunca ser enviado para o repositório público do Git!

---

## 3. Como Rodar a Aplicação com Docker

Oferecemos três modos de execução customizados de acordo com a sua necessidade no momento do desenvolvimento ou deploy:

### Modo Desenvolvimento (Dev Mode)
Inicia o Spring Boot com o perfil de desenvolvimento ativo, ativando o **Spring DevTools** e recarga automática do contexto em modificações de classes compiladas. A porta externa exposta da API é `8090`.
```bash
docker compose -f docker-compose.yml -f docker-compose-dev.yml up --build -d
```

### Modo Watch (Hot Reloading de Arquivos em Tempo Real)
Este é o **padrão industrial** para alta produtividade! Ele monitora os arquivos do diretório `./rest/rest` na sua máquina local e sincroniza-os em tempo real com o contêiner em execução, reiniciando o servidor instantaneamente a cada salvamento:
```bash
docker compose -f docker-compose.yml -f docker-compose-dev.yml watch
```
*ou use a flag `--watch` diretamente:*
```bash
docker compose -f docker-compose.yml -f docker-compose-dev.yml up --watch
```

### Modo Produção (Prod Mode)
Compila a aplicação Spring Boot de forma otimizada em uma imagem única, estável, estéril e autocontida sem dependências do ambiente local:
```bash
docker compose up --build -d
```

### Parar a Execução
Para parar os contêineres e liberar as portas do seu computador:
```bash
docker compose -f docker-compose.yml -f docker-compose-dev.yml down
```

---

## 4. Testando a Integração de IA

### A. IA Local e Gratuita (Ollama - Llama 3.2)
1. Certifique-se de que o contêiner `ollama` está rodando.
2. Na primeira inicialização, o contêiner `ollama_pull` baixará automaticamente o modelo `llama3.2` (cerca de 2.0GB).
3. Teste o endpoint chamando via Postman: `GET http://localhost:8090/api/ai/chat?prompt=Olá`

### B. IA Premium e Paga (Google Gemini API - Gemini 1.5 Flash)
1. Certifique-se de ter configurado uma chave válida de API em `GEMINI_API_KEY` no `.env`.
2. Faça login para obter um token JWT válido.
3. Teste o endpoint chamando via Postman com o cabeçalho `Authorization: Bearer <SEU_TOKEN_JWT>`:
   `GET http://localhost:8090/api/ai/premium/gemini/chat?prompt=Explique o Spring Boot`

---

## 5.Resumo de Comandos Úteis do Projeto
Confira o arquivo [comandos_uteis.txt](./comandos_uteis.txt) para ver scripts SQL para popular o banco de dados e comandos de administração de banco de dados no Docker.
