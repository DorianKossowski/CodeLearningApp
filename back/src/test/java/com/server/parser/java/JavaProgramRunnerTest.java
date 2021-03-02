package com.server.parser.java;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.Method;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.TaskVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JavaProgramRunnerTest {
    static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock
    private JavaContext context;
    @Mock
    private JavaProgramRunner.MainRunner mainRunner;

    private JavaProgramRunner runner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        runner = new JavaProgramRunner(context, mainRunner);
    }

    @Test
    void shouldInitializeStaticFields() {
        FieldVar fieldVar = mock(FieldVar.class);
        when(context.getStaticFields()).thenReturn(Collections.singletonMap("", fieldVar));

        runner.run(null);

        verify(fieldVar).initialize();
    }

    @Test
    void shouldGetMainMethod() {
        String input = "public class c { public static void main(String[] args){} }";
        JavaParser.TaskContext c = HELPER.shouldParseToEof(input, JavaParser::task);
        List<Method> methods = new TaskVisitor().visitTask(c).getClassAst().getBody().getMethods();

        assertThat(new JavaProgramRunner.MainRunner().getMainMethod(methods)).isPresent();
    }
}