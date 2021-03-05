package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.FieldVarInitExpressionFunction;
import com.server.parser.java.ast.MethodVar;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.IntConstant;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.value.ObjectValue;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.ast.value.PrimitiveValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ObjectRefExpressionVisitorTest extends JavaVisitorTestBase {
    private final ObjectRefExpressionVisitor visitor = new ObjectRefExpressionVisitor();

    @Test
    void shouldVisitObjectRefExpression() {
        MethodContext methodContext = createMethodContext();
        methodContext.addVariable(createStringVariable("x"));
        String input = "x";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        ObjectRefExpression expression = visitor.visit(c, methodContext);

        assertThat(expression.getText()).isEqualTo("x");
        assertThat(expression.getConstant().c).isEqualTo("value");
    }

    private Variable createStringVariable(String name) {
        StringConstant stringConstant = new StringConstant("value");
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(stringConstant));
        return new MethodVar("String", name, value);
    }

    @Test
    void shouldThrowWhenWrongObjectRefExpression() {
        MethodContext methodContext = createMethodContext();
        String input = "x";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        assertThatThrownBy(() -> visitor.visit(c, methodContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt x nie istnieje");
    }

    @Test
    void shouldVisitObjectRefWithAttributeExpression() {
        MethodContext methodContext = createMethodContext();
        PrimitiveValue attributeValue = new PrimitiveValue(new Literal(new IntConstant()));
        methodContext.addVariable(createObjectWithIntAttribute("a", "b", attributeValue));
        String input = "a.b";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        ObjectRefExpression expression = visitor.visit(c, methodContext);

        assertThat(expression.getValue()).isSameAs(attributeValue);
    }

    private Variable createObjectWithIntAttribute(String objectName, String attributeName, Value attributeValue) {
        FieldVar fieldVar = new FieldVar("int", attributeName, mock(FieldVarInitExpressionFunction.class), attributeValue);
        Instance instance = new Instance("MyClass", Collections.singletonMap(attributeName, fieldVar));
        return new MethodVar("String", objectName, new ObjectValue(instance));
    }

    @Test
    void shouldVisitThis() {
        MethodContext methodContext = createMethodContext();
        PrimitiveValue attributeValue = new PrimitiveValue(new Literal(new IntConstant()));
        Variable variable = createObjectWithIntAttribute("obj", "a", attributeValue);
        methodContext.setThisValue((ObjectValue) variable.getValue());
        String input = "this.a";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        ObjectRefExpression expression = visitor.visit(c, methodContext);

        assertThat(expression.getValue()).isSameAs(attributeValue);
    }

    @Test
    void shouldThrowWhenThisInStaticContext() {
        MethodContext methodContext = createMethodContext();
        String input = "this.a";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        assertThatThrownBy(() -> visitor.visit(c, methodContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można użyć słowa kluczowego this ze statycznego kontekstu");
    }
}