package com.server.parser.java;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class JavaParserBuilder {

    public static JavaParser build(String input) {
        CodePointCharStream codePointCharStream = CharStreams.fromString(input);
        JavaLexer lexer = new JavaLexer(codePointCharStream);

        CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);

        JavaParser parser = new JavaParser(commonTokenStream);
        parser.setErrorHandler(new BailErrorStrategy());
        return parser;
    }
}
