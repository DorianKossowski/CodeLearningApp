package com.server.parser.java;

import com.server.parser.ParserTestBase;
import org.junit.jupiter.api.Test;

public class JavaParserTest extends ParserTestBase<JavaParser> {

    public JavaParserTest() {
        super(JavaLexer::new, JavaParser::new);
    }

    @Test
    public void shouldParseClassDec() {
        String input = "class Hello { body }";

        shouldParseToEof(input, JavaParser::classDec);
    }
}