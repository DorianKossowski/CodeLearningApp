package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.constant.StringConstant;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.ObjectWrapperValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.FieldVarInitExpressionFunction;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.java.variable.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ObjectRefExpressionVisitorTest extends JavaVisitorTestBase {
    private ObjectRefExpressionVisitor visitor;

    @Test
    void shouldVisitObjectRefExpression() {
        MethodContext methodContext = createMethodContext();
        methodContext.addVariable(createStringVariable("x"));
        String input = "x";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        ObjectRefExpression expression = new ObjectRefExpressionVisitor(methodContext).visit(c);

        assertThat(expression.getText()).isEqualTo("x");
        assertThat(expression.getLiteral().getConstant().c).isEqualTo("value");
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

        assertThatThrownBy(() -> new ObjectRefExpressionVisitor(methodContext).visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt x nie istnieje");
    }

    @Test
    void shouldVisitObjectRefWithAttributeExpression() {
        MethodContext methodContext = createMethodContext();
        Value attributeValue = mock(Value.class, RETURNS_DEEP_STUBS);
        methodContext.addVariable(createObjectWithIntAttribute("a", "b", attributeValue));
        String input = "a.b";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        ObjectRefExpression expression = new ObjectRefExpressionVisitor(methodContext).visit(c);

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
        Value attributeValue = mock(Value.class, RETURNS_DEEP_STUBS);
        Variable variable = createObjectWithIntAttribute("obj", "a", attributeValue);
        methodContext.setThisValue((ObjectValue) variable.getValue());
        String input = "this.a";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        ObjectRefExpression expression = new ObjectRefExpressionVisitor(methodContext).visit(c);

        assertThat(expression.getValue()).isSameAs(attributeValue);
    }

    @Test
    void shouldVisitReferenceToCallResult() {
        MethodContext methodContext = mock(MethodContext.class, RETURNS_DEEP_STUBS);
        when(methodContext.getParameters().getMethodName()).thenReturn("");
        Value attributeValue = mock(Value.class, RETURNS_DEEP_STUBS);
        mockContextWithCall(methodContext, attributeValue);
        String input = "fun().a";
        JavaParser.ExpressionContext c = HELPER.shouldParseToEof(input, JavaParser::expression);

        ObjectRefExpression expression = new ObjectRefExpressionVisitor(methodContext).visit(c);

        assertThat(expression.getValue()).isSameAs(attributeValue);
    }

    private void mockContextWithCall(MethodContext methodContext, Value attributeValue) {
        CallStatement callStatement = mock(CallStatement.class);
        FieldVar fieldVar = new FieldVar("int", "a", mock(FieldVarInitExpressionFunction.class), attributeValue);
        Instance instance = new Instance("MyClass", Collections.singletonMap("a", fieldVar));
        when(callStatement.getResult()).thenReturn(instance);

        when(methodContext.getParameters().getCallResolver().resolve(anyBoolean(), any())).thenReturn(callStatement);
    }
}