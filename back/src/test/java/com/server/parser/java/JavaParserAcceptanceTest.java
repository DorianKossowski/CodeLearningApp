package com.server.parser.java;

import com.server.parser.ParserAcceptanceTestBase;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.function.Function;

public class JavaParserAcceptanceTest extends ParserAcceptanceTestBase<JavaParser> {
    private static final String PATH = "parser/java";

    public JavaParserAcceptanceTest() {
        super(JavaLexer::new, JavaParser::new);
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected Function<JavaParser, ParserRuleContext> getParsingRule() {
        return JavaParser::taskEOF;
    }
}
