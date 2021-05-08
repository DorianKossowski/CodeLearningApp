package com.server.parser.java.value;

import com.server.parser.java.constant.Constant;

public interface ConstantProvider {
    Constant<?> getConstant();
}