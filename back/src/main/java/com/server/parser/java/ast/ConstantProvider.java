package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;

public interface ConstantProvider {
    Constant<?> getConstant();
}