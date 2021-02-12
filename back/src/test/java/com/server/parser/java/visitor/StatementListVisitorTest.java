package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StatementListVisitorTest extends JavaVisitorTestBase {
    private final StatementListVisitor visitor = new StatementListVisitor();

    @Test
    void shouldVisitStatementsInCorrectOrder() {
        String input = "String a = \"s\"; System.out.println(\"sss\"); String b = \"s2\";";
        JavaParser.StatementListContext c = HELPER.shouldParseToEof(input, JavaParser::statementList);

        List<Statement> statements = visitor.visit(c, createMethodContext());

        assertThat(statements).hasSize(3);
        assertThat(statements.get(0).getText()).isEqualTo("String a = \"s\"");
        assertThat(statements.get(1).getText()).isEqualTo("CALL Statement");
        assertThat(statements.get(2).getText()).isEqualTo("String b = \"s2\"");
    }
}