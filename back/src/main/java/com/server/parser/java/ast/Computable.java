package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;

public interface Computable {
    Constant<?> getConstant();

    Computable compute(Computable v2, String operation);
}