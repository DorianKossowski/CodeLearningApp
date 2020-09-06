package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassBody;
import com.server.parser.java.ast.Variable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassBodyVisitorTest extends JavaVisitorTestBase {
    private final ClassBodyVisitor visitor = new ClassBodyVisitor();

    @Test
    void shouldVisitClassBody() {
        String input = "int i; void m(){} void m2(){} private String s;";
        JavaParser.ClassBodyContext c = HELPER.shouldParseToEof(input, JavaParser::classBody);

        ClassBody body = visitor.visit(c);

        assertThat(body.getMethods()).extracting(method -> method.getHeader().getName())
                .containsExactly("m", "m2");
        assertThat(body.getFields()).extracting(Variable::getName)
                .containsExactly("i", "s");
    }
}