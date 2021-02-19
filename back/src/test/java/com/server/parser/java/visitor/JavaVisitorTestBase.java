package com.server.parser.java.visitor;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.MethodContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JavaVisitorTestBase {
    static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    protected ClassContext context = new ClassContext();

    protected MethodContext createMethodContext() {
        return createMethodContext("", "void");
    }

    protected MethodContext createMethodContext(String methodName, String resultTypeName) {
        MethodHeader methodHeader = mock(MethodHeader.class);
        when(methodHeader.getName()).thenReturn(methodName);
        when(methodHeader.getResult()).thenReturn(resultTypeName);
        MethodContext methodContext = context.createEmptyMethodContext();
        methodContext.save(methodHeader, mock(JavaParser.MethodBodyContext.class));
        return methodContext;
    }

    protected void assertVariableDec(VariableDef variableDef, String type, String name) {
        assertVariableDec(variableDef, Collections.emptyList(), type, name);
    }

    protected void assertVariableDec(VariableDef variableDef, List<String> modifiers, String type, String name) {
        assertThat(variableDef.getModifiers()).isEqualTo(modifiers);
        assertThat(variableDef).extracting(VariableDef::getType, VariableDef::getName)
                .containsExactly(type, name);
    }
}