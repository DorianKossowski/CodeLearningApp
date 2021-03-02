package com.server.parser.java.task.verifier;

import java.util.List;

public abstract class CommonVerifier {

    protected static boolean hasSameModifiers(List<String> actualModifiers, List<String> expectedModifiers) {
        if (expectedModifiers.size() == 0) {
            return actualModifiers.isEmpty();
        }
        return actualModifiers.containsAll(expectedModifiers);
    }
}