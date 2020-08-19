package com.server.parser.java;

import com.server.parser.ParserBuilder;

public class JavaParserBuilder {

    public static JavaParser build(String input) {
        return ParserBuilder.build(input, JavaLexer::new, JavaParser::new);
    }
}
