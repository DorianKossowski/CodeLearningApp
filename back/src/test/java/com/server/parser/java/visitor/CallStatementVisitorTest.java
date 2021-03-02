package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.MethodVar;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.MethodVarDef;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.ast.value.PrimitiveValue;
import com.server.parser.java.context.MethodContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class CallStatementVisitorTest extends JavaVisitorTestBase {
    private final String METHOD_NAME = "methodName";

    private final CallStatementVisitor visitor = new CallStatementVisitor();
    private MethodContext methodContext;

    @BeforeEach
    void setUp() {
        methodContext = createMethodContext(METHOD_NAME, "void");
    }

    @Test
    void shouldVisitMethodCall() {
        String input = "System.out.print(\"Hello World\")";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        CallStatement callStatement = visitor.visit(c, methodContext);

        CallInvocation invocation = callStatement.getCallInvocation();
        assertThat(invocation.getText()).isEqualTo(input);
        assertThat(invocation.printMethodName()).isEqualTo(METHOD_NAME);
        assertThat(invocation.getName()).isEqualTo("System.out.print");
        assertThat(Iterables.getOnlyElement(invocation.getArgs()).getText()).isEqualTo("\"Hello World\"");
    }

    @Test
    void shouldVisitMethodCallWithoutArgs() {
        MethodHeader header = new MethodHeader(Collections.emptyList(), "void", "someMethod", Collections.emptyList());
        context.getCallResolver().getCallableKeeper().keepCallable(new Method(methodContext, header, HELPER.shouldParseToEof("", JavaParser::methodBody)));
        String input = "someMethod()";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        CallStatement call = visitor.visit(c, methodContext);

        CallInvocation invocation = call.getCallInvocation();
        assertThat(invocation.getText()).isEqualTo(input);
        assertThat(invocation.printMethodName()).isEqualTo(METHOD_NAME);
        assertThat(invocation.getName()).isEqualTo("someMethod");
        assertThat(invocation.getArgs()).isEmpty();
    }

    @Test
    void shouldGetCorrectMethodCallValue() {
        VariableDef arg1 = new MethodVarDef("", "String", "a1", NullExpression.INSTANCE, false);
        VariableDef arg2 = new MethodVarDef("", "String", "a2", NullExpression.INSTANCE, false);
        MethodHeader header = new MethodHeader(Collections.emptyList(), "void", "someMethod", Arrays.asList(arg1, arg2));
        context.getCallResolver().getCallableKeeper().keepCallable(new Method(methodContext, header, HELPER.shouldParseToEof("", JavaParser::methodBody)));
        methodContext.addVariable(createStringVariable("a1"));
        methodContext.addVariable(createStringVariable("a2"));
        methodContext.addVariable(createStringVariable("var"));
        String input = "someMethod(\"literal\", var)";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        CallStatement call = visitor.visit(c, methodContext);

        assertThat(call.getCallInvocation().getResolved()).isEqualTo("someMethod(\"literal\", \"value\")");
    }

    private Variable createStringVariable(String name) {
        StringConstant stringConstant = new StringConstant("value");
        PrimitiveValue value = new PrimitiveValue(new Literal(stringConstant));
        return new MethodVar("String", name, value);
    }
}