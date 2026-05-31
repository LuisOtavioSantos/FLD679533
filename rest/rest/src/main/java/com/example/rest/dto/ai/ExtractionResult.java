package com.example.rest.dto.ai;

public record ExtractionResult(
    String nomeCliente,
    String servicoDesejado,
    String dataHoraFormatada,
    String sentimentoDoCliente
) {}
