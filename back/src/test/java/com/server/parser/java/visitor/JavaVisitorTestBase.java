package com.server.parser.java.visitor;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.JavaContext;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaVisitorTestBase {
    static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    protected final JavaContext context = new JavaContext();

    protected void assertVariableDec(VariableDef variableDef, String type, String name) {
        assertThat(variableDef).extracting(VariableDef::getType, VariableDef::getName)
                .containsExactly(type, name);
    }
}