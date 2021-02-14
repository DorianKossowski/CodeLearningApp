package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskVisitorTest extends JavaVisitorTestBase {
    private final TaskVisitor visitor = new TaskVisitor();

    @Test
    void shouldVisitTask() {
        String input = "public class c {}";
        JavaParser.TaskContext c = HELPER.shouldParseToEof(input, JavaParser::task);

        Task task = visitor.visit(c);

        assertThat(task.getClassAst().getHeader().getName()).isEqualTo("c");
    }

    @Test
    void shouldGetMainMethod() {
        String input = "public class c { public static void main(String[] args){} }";
        JavaParser.TaskContext c = HELPER.shouldParseToEof(input, JavaParser::task);
        Task task = visitor.visit(c);
        List<Method> methods = task.getClassAst().getBody().getMethods();

        assertThat(new TaskVisitor.MainRunner().getMainMethod(methods)).isPresent();
    }
}