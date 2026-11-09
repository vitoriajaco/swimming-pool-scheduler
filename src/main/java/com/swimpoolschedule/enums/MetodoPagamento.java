package com.swimpoolschedule.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MetodoPagamento {
    DINHEIRO,
    CREDITO,
    DEBITO,
    PIX,
    BOLSISTA;

    // Provide a JSON-friendly mapping that accepts multiple common input formats
    @JsonCreator
    public static MetodoPagamento fromString(String value) {
        if (value == null) return null;
        String normalized = value.trim().toUpperCase().replaceAll("[\"' ]+", "_");
        switch (normalized) {
            case "DINHEIRO":
                return DINHEIRO;
            case "CARTAO_CREDITO":
            case "CARTAO-CREDITO":
            case "CARTAOCREDITO":
            case "CREDIT_CARD":
            case "CREDIT":
            case "CREDITO":
                return CREDITO;
            case "CARTAO_DEBITO":
            case "CARTAO-DEBITO":
            case "CARTAODEBITO":
            case "DEBIT_CARD":
            case "DEBITO":
                return DEBITO;
            case "PIX":
                return PIX;
            case "BOLSISTA":
            case "SCHOLAR":
            case "SCHOLARSHIP":
                return BOLSISTA;
            default:
                // Try direct enum match as fallback
                try {
                    return MetodoPagamento.valueOf(normalized);
                } catch (Exception e) {
                    return null;
                }
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

