package com.server.parser.java.visitor;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaVisitorTestBase {
    static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    protected void assertVariableDec(Variable variable, String type, String name) {
        assertThat(variable).extracting(Variable::getType, Variable::getName)
                .containsExactly(type, name);
    }
}