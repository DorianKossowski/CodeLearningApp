package com.server.parser.java.visitor.resolver;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.MethodContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WhileStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Test
    void shouldValidateInSeparateContext() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("init")));
        methodContext.addVariable(new Variable("String", "str", value));

        JavaParser.WhileStatementContext c = HELPER.shouldParseToEof("while(true) str = \"true\";",
                JavaParser::whileStatement);

        ForStmtResolver.validateContent(methodContext, c.statement());

        assertThat(methodContext.getVariable("str").getValue()).isSameAs(value);
    }
}