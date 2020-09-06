package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassHeader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassHeaderVisitorTest extends JavaVisitorTestBase {
    private final ClassHeaderVisitor visitor = new ClassHeaderVisitor();

    @Test
    void shouldVisitClassHeader() {
        String input = "public class c";
        JavaParser.ClassHeaderContext c = HELPER.shouldParseToEof(input, JavaParser::classHeader);

        ClassHeader header = visitor.visit(c);

        assertThat(header.getName()).isEqualTo("c");
    }
}