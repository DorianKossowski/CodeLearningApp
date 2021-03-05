package com.server.parser.java.ast;

import com.server.parser.java.constant.ConstantProvider;

public interface Computable extends ConstantProvider {
    Computable compute(Computable v2, String operation);
}