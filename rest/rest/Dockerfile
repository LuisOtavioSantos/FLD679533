# ESTÁGIO 1: COMPILAÇÃO

# Versão Alpine é mais leve, porém pode dar conflito de dependencias, ideal pra teste e desenvolvimento
FROM maven:3.9-eclipse-temurin-21-alpine AS build
# A versão abaixo é mais completa, porém mais pesada e mais recomendada pra produção
# FROM maven:3.9.16-sapmachine-21 AS build 


WORKDIR /app

# Copia pom.xml para carregar as dependências de cache primeiro
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia arquivos de código fonte
COPY src ./src

# Compila o projeto e pula os testes para agilizar o build do container
RUN mvn clean package -DskipTests

# ESTÁGIO 2: EXECUÇÃO (JRE LEVE) 
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR compilado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Cria a pasta uploads/ para o upload físico de imagens de produto
RUN mkdir -p uploads

# Expõe a porta padrão definida no yml
EXPOSE 8080

# Comando de inicialização forçando o perfil de produção
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
