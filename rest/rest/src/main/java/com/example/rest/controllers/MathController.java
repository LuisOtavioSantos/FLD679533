package com.example.rest.controllers;

import com.example.rest.exception.UnsupportedMathOperationException;
import com.example.rest.math.SimpleMath;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.rest.services.converters.NumberConverter.convertToDouble;
import static com.example.rest.services.converters.NumberConverter.isNotNumeric;

@RestController
@RequestMapping("/math")
@Profile("dev")
public class MathController {

    private final SimpleMath math = new SimpleMath();

    @GetMapping("/sum/{a}/{b}")
    public Double sum(
            @PathVariable("a") String a,
            @PathVariable("b") String b
    ) {
        if(isNotNumeric(a) || isNotNumeric(b)) {
            throw new UnsupportedMathOperationException("Por favor, insira um valor numérico válido."); // Aciona o manipulador global (Erro 400)
        }
        return math.sum(convertToDouble(a), convertToDouble(b));
    }

    @GetMapping("/subtraction/{a}/{b}")
    public Double subtraction(
            @PathVariable("a") String a,
            @PathVariable("b") String b
    ) {
        if(isNotNumeric(a) || isNotNumeric(b)) {
            throw new UnsupportedMathOperationException("Por favor, insira um valor numérico válido."); // Aciona o manipulador global (Erro 400)
        }
        return math.subtraction(convertToDouble(a), convertToDouble(b));
    }

    @GetMapping("/multiplication/{a}/{b}")
    public Double multiplication(
            @PathVariable("a") String a,
            @PathVariable("b") String b
    ) {
        if(isNotNumeric(a) || isNotNumeric(b)) {
            throw new UnsupportedMathOperationException("Por favor, insira um valor numérico válido."); // Aciona o manipulador global (Erro 400)
        }
        return math.multiplication(convertToDouble(a), convertToDouble(b));
    }

    @GetMapping("/division/{a}/{b}")
    public Double division(
            @PathVariable("a") String a,
            @PathVariable("b") String b
    ) {
        if(isNotNumeric(a) || isNotNumeric(b)) {
            throw new UnsupportedMathOperationException("Por favor, insira um valor numérico válido."); // Aciona o manipulador global (Erro 400)
        }

        Double numB = convertToDouble(b);
        if(numB == 0D) {
            throw new UnsupportedMathOperationException("Divisão por zero não é permitida."); // Evita falha crítica
        }

        return math.division(convertToDouble(a), numB);
    }

}