package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassAst;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassDecVisitorTest extends JavaVisitorTestBase {
    private final ClassDecVisitor visitor = new ClassDecVisitor();

    @Test
    void shouldCreateFromClassDec() {
        String input = "public class c { void m() {} }";
        JavaParser.ClassDecContext c = HELPER.shouldParseToEof(input, JavaParser::classDec);

        ClassAst classAst = visitor.visit(c);

        assertThat(classAst.getHeader().getName()).isEqualTo("c");
        assertThat(Iterables.getOnlyElement(classAst.getBody().getMethods()).getHeader().getName()).isEqualTo("m");
    }
}