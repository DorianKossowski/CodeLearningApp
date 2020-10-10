package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodBody;
import com.server.parser.java.ast.Statement;
import com.server.parser.java.context.MethodContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MethodBodyVisitorTest extends JavaVisitorTestBase {
    private final MethodBodyVisitor visitor = new MethodBodyVisitor(new MethodContext(""));

    @Test
    void shouldVisitStatementsInCorrectOrder() {
        String input = "String a = \"s\"; System.out.println(\"sss\"); String b = \"s2\";";
        JavaParser.MethodBodyContext c = HELPER.shouldParseToEof(input, JavaParser::methodBody);

        MethodBody methodBody = visitor.visit(c);

        List<Statement> statements = methodBody.getStatements();
        assertThat(statements).hasSize(3);
        assertThat(statements.get(0).getText()).isEqualTo("String a = \"s\"");
        assertThat(statements.get(1).getText()).isEqualTo("System.out.println(\"sss\")");
        assertThat(statements.get(2).getText()).isEqualTo("String b = \"s2\"");
    }
}