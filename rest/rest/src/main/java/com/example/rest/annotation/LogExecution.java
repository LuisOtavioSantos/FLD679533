package com.example.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * COMO CRIAR UMA ANOTAÇÃO CUSTOMIZADA:
 * - @interface: declara que esta é uma anotação (não uma interface normal)
 * - @Target: define ONDE a anotação pode ser usada (METHOD = apenas em métodos)
 * - @Retention: define QUANDO a anotação está disponível (RUNTIME = em tempo de execução)
 *
 * Exemplo de uso:
 *   @LogExecution
 *   public List<ProductDTO> findAll() { ... }
 */
@Target(ElementType.METHOD)         // meta-annotation: indica ao compilador que só pode ser usada em métodos
@Retention(RetentionPolicy.RUNTIME) // Disponível em tempo de execução (necessário para AOP) SOURCE/CLASS/RUNTIME
public @interface LogExecution {
    // Anotação simples sem parâmetros
    // Poderia ter parâmetros, ex: String value() default ""; para mensagem customizada
}
