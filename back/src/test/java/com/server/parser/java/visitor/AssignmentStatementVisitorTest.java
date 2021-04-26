package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.ast.statement.expression_statement.MethodVarDef;
import com.server.parser.java.constant.IntConstant;
import com.server.parser.java.constant.StringConstant;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.value.PrimitiveValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.FieldVarInitExpressionFunction;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.java.variable.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class AssignmentStatementVisitorTest extends JavaVisitorTestBase {
    private MethodContext methodContext;

    private AssignmentStatementVisitor visitor;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        methodContext = createMethodContext("METHOD_NAME", "void");
        visitor = new AssignmentStatementVisitor(methodContext);
    }

    @Test
    void shouldVisitAssignment() {
        methodContext.addVariable(createStringVariable());
        String input = "a = \"str\"";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        Assignment assignment = visitor.visit(c);

        assertThat(assignment.getText()).isEqualTo(input);
        assertThat(assignment.getId()).isEqualTo("a");
        assertThat(assignment.getValue().getText()).isEqualTo("\"str\"");
    }

    private Variable createStringVariable() {
        StringConstant stringConstant = new StringConstant("value");
        PrimitiveValue value = new PrimitiveValue(new Literal(stringConstant));
        return new MethodVar("String", "a", value);
    }

    @Test
    void shouldThrowWhenInvalidAssignment() {
        methodContext.addVariable(createStringVariable());
        String input = "a = 5";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        assertThatThrownBy(() -> visitor.visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie 5 nie jest typu String");
    }

    @Test
    void shouldVisitAttributeAssignment() {
        Value initValue = new PrimitiveValue(new Literal(new IntConstant()));
        FieldVar fieldVar = new FieldVar("int", "b", mock(FieldVarInitExpressionFunction.class), initValue);
        Instance instance = new Instance("MyType", Collections.singletonMap("b", fieldVar));
        MethodVarDef varDef = new MethodVarDef("", "MyType", "a", instance, false);
        methodContext.addVariable(new MethodVar(varDef));
        String input = "a.b = 1";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        Assignment assignment = visitor.visit(c);

        assertThat(assignment.getText()).isEqualTo(input);
        assertThat(assignment.getId()).isEqualTo("a.b");
        assertThat(assignment.getValue().getText()).isEqualTo("1");
        Variable updatedVar = methodContext.getVariable("a");
        assertThat((updatedVar.getValue()).getAttribute("b").getExpression().getResolvedText()).isEqualTo("1");
    }
}