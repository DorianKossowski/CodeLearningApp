package com.server.parser.java.visitor;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.MethodContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JavaVisitorTestBase {
    static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    protected ClassContext context = new ClassContext();

    protected MethodContext createMethodContext() {
        return createMethodContext("");
    }

    protected MethodContext createMethodContext(String methodName) {
        MethodHeader methodHeader = mock(MethodHeader.class);
        when(methodHeader.getName()).thenReturn(methodName);
        MethodContext methodContext = context.createEmptyMethodContext();
        methodContext.save(methodHeader);
        return methodContext;
    }

    protected void assertVariableDec(VariableDef variableDef, String type, String name) {
        assertThat(variableDef).extracting(VariableDef::getType, VariableDef::getName)
                .containsExactly(type, name);
    }
}