package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.BooleanConstant;

public interface EqualityCheckable {
    BooleanConstant equalsV(Value v2);
}