package com.example.rest.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspecto que intercepta métodos anotados com @LogExecution
 *
 * AOP (Aspect-Oriented Programming) permite adicionar comportamento a métodos
 * SEM modificar o código deles. O Spring AOP funciona assim:
 *
 * 1. @Aspect: marca esta classe como um "aspecto" (interceptador)
 * 2. @Around: o método executa ANTES e DEPOIS do método interceptado
 * 3. ProceedingJoinPoint: representa o método original sendo chamado
 *
 * Quando um método anotado com @LogExecution for executado, este aspecto:
 * - Registra o nome do método e seus parâmetros
 * - Mede o tempo de execução
 * - Loga tudo no console via SLF4J
 */
@Aspect // Informa ao spring que esta classe vai ser um aspecto com lógica transversal
@Component // Informa ao spring que vai ser um Bean (IoC)
public class LogExecutionAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogExecutionAspect.class);

    // @Around(ANTES e DEPOIS), @Before, @After, @AfterReturning (Apenas quando executa com sucesso), @AfterThrowing (Apenas quando o lança uma exceção)
    @Around("@annotation(LogExecution)")
    public Object logExecutionTime(@NonNull ProceedingJoinPoint joinPoint) throws Throwable {

        // Nome completo do método (ex: "ProductServices.findAll")
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String fullName = className + "." + methodName;

        String params = Arrays.toString(joinPoint.getArgs()); // É necessário passar os Parâmetros

        logger.info("[LogExecution] Iniciando: {}({})", fullName, params);
        // Marca o tempo antes de executar
        long startTime = System.currentTimeMillis();

        // Executa o método original (proceed = "prossiga com a execução")
        Object result = joinPoint.proceed();

        // Calcula o tempo total
        long duration = System.currentTimeMillis() - startTime;

        logger.info("[LogExecution] Finalizado: {} em {}ms", fullName, duration);

        // Retorna o resultado original do método
        return result;
    }
}
