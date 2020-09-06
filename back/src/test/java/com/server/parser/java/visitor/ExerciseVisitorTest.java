package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Exercise;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExerciseVisitorTest extends JavaVisitorTestBase {
    private final ExerciseVisitor visitor = new ExerciseVisitor();

    @Test
    void shouldVisitExercise() {
        String input = "public class c {}";
        JavaParser.ExerciseContext c = HELPER.shouldParseToEof(input, JavaParser::exercise);

        Exercise exercise = visitor.visit(c);

        assertThat(exercise.getClassAst().getHeader().getName()).isEqualTo("c");
    }
}