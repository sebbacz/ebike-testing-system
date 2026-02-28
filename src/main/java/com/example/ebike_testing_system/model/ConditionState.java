package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConditionState {
    VERY_BAD("--"), BAD("-"), NEUTRAL(""), GOOD("+"),
    VERY_GOOD("++"), NOT_APPLICABLE("n.v.t");
    private final String symbol;

    ConditionState(String symbol) {
        this.symbol = symbol;
    }

    @JsonValue
    public String getSymbol() {
        return symbol;
    }

    @JsonCreator
    public static ConditionState fromSymbol(String symbol) {
        for (ConditionState state : ConditionState.values()) {
            if (state.getSymbol().equalsIgnoreCase(symbol)) {
                return state;
            }
        }
        // Handle cases where the symbol is not found (e.g., throw an IllegalArgumentException)
        // Or return a default value if appropriate for your application
        throw new IllegalArgumentException("Unknown ConditionState symbol: " + symbol);
    }
}
