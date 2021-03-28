package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.value.UninitializedValue;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatementListVisitorTest extends JavaVisitorTestBase {
    private StatementListVisitor visitor;

    @Test
    void shouldVisitStatementsInCorrectOrder() {
        String input = "String a = \"s\"; System.out.println(\"sss\"); String b = \"s2\";";
        JavaParser.StatementListContext c = HELPER.shouldParseToEof(input, JavaParser::statementList);

        List<Statement> statements = new StatementListVisitor(createMethodContext()).visit(c);

        assertThat(statements).hasSize(3);
        assertThat(statements.get(0).getText()).isEqualTo("String a = \"s\"");
        assertThat(statements.get(1).getText()).isEqualTo("CALL Statement");
        assertThat(statements.get(2).getText()).isEqualTo("String b = \"s2\"");
    }

    @Test
    void shouldValidateRemainingStatements() {
        String input = "return; a = 1;";
        JavaParser.StatementListContext c = HELPER.shouldParseToEof(input, JavaParser::statementList);

        assertThatThrownBy(() -> new StatementListVisitor(createMethodContext()).visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt a nie istnieje");
    }

    @Test
    void shouldValidateRemainingStatementsInSeparateContext() {
        String input = "return; a = 1;";
        MethodContext methodContext = createMethodContext();
        methodContext.addVariable(new MethodVar("int", "a", new UninitializedValue(new UninitializedExpression(""))));
        JavaParser.StatementListContext c = HELPER.shouldParseToEof(input, JavaParser::statementList);

        new StatementListVisitor(methodContext).visit(c);

        assertThat(methodContext.getVariable("a").getValue()).isExactlyInstanceOf(UninitializedValue.class);
    }
}