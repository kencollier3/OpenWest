package com.appsolutelypositive.openwest;


import android.support.annotation.NonNull;

import java8.util.function.BiFunction;

/**
 * Created by toadzky on 7/10/17.
 */
public enum Operation {

    ADDITION("+", (a,b) -> a + b),
    SUBTRACTION("-", (a,b) -> a - b),
    MULTIPLICATION("*", (a,b) -> a * b),
    DIVISION("÷", (a,b) -> a / b);

    private final String symbol;
    private final BiFunction<Double,Double,Double> implementation;

    public static Operation fromSymbol(String symbol) {
        switch (symbol) {
            case "+":
                return ADDITION;
            case "-":
                return SUBTRACTION;
            case "*":
                return MULTIPLICATION;
            case "÷":
                return DIVISION;
            default:
                return null;
        }
    }

    Operation(@NonNull String symbol, @NonNull BiFunction<Double, Double, Double> implementation) {
        this.symbol = symbol;
        this.implementation = implementation;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double apply(Double left, Double right) {
        return implementation.apply(left, right);
    }

}
