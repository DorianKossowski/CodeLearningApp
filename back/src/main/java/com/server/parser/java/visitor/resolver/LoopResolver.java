package com.server.parser.java.visitor.resolver;

import com.server.parser.util.exception.ResolvingException;

public class LoopResolver {
    static final int MAX_ITER_NUMBER = 1000;

    static void validateMaxIteration(int iterNumber) {
        if (iterNumber >= MAX_ITER_NUMBER) {
            throw new ResolvingException("Ogranicz liczbę iteracji! Maksymalna dostępna liczba iteracji w pętli to 1000");
        }
    }
}