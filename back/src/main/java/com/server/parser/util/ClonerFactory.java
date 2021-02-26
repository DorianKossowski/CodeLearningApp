package com.server.parser.util;

import com.rits.cloning.Cloner;
import com.server.parser.java.ast.FieldVarInitExpressionSupplier;

public class ClonerFactory {
    private static final Class<?>[] DONT_CLONE = {FieldVarInitExpressionSupplier.class};

    public static Cloner createCloner(Class<?>... dontClone) {
        Cloner cloner = new Cloner();
        cloner.dontClone(DONT_CLONE);
        cloner.dontClone(dontClone);
        return cloner;
    }
}