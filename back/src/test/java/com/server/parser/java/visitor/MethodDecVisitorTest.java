package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodCall;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Statement;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodDecVisitorTest extends JavaVisitorTestBase {
    private final MethodDecVisitor visitor = new MethodDecVisitor("");

    @Test
    void shouldVisitMethodDec() {
        String input = "void m(String[] a) { println(\"HELLO\"); }";
        JavaParser.MethodDecContext c = HELPER.shouldParseToEof(input, JavaParser::methodDec);

        Method method = visitor.visit(c);

        MethodHeader header = method.getHeader();
        Statement statement = Iterables.getOnlyElement(method.getBody().getStatements());
        assertThat(header).extracting(MethodHeader::getResult, MethodHeader::getName)
                .containsExactly("void", "m");
        assertVariableDec(Iterables.getOnlyElement(header.getArguments()), "String[]", "a");
        assertThat(((MethodCall) statement)).extracting(MethodCall::getName,
                call -> Iterables.getOnlyElement(call.getArgs()).getText())
                .containsExactly("println", "HELLO");
    }
}