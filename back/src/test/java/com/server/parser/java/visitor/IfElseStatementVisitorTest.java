package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.constant.StringConstant;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.value.ObjectWrapperValue;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IfElseStatementVisitorTest extends JavaVisitorTestBase {
    private IfElseStatementVisitor visitor;

    @Test
    void shouldValidateInSeparateContext() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("init")));
        methodContext.addVariable(new MethodVar("String", "str", value));
        visitor = new IfElseStatementVisitor(methodContext);

        JavaParser.IfElseStatementContext c = HELPER.shouldParseToEof("if(true) str = \"true\"; else str = \"false\";",
                JavaParser::ifElseStatement);

        visitor.validateBranchesContent(c);

        assertThat(methodContext.getVariable("str").getValue()).isSameAs(value);
    }

    @Test
    void shouldThrowWhenSingleVariableDefAsContent() {
        visitor = new IfElseStatementVisitor(createMethodContext());
        JavaParser.IfElseStatementContext c = HELPER.shouldParseToEof("if(true) String str = \"true\";",
                JavaParser::ifElseStatement);

        assertThatThrownBy(() -> visitor.validateBranchesContent(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Deklaracja String str = \"true\" nie jest w tym miejscu dozwolona");
    }
}