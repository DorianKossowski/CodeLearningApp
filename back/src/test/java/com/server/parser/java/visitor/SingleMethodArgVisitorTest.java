package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import org.junit.jupiter.api.Test;

class SingleMethodArgVisitorTest extends JavaVisitorTestBase {
    private final SingleMethodArgVisitor visitor = new SingleMethodArgVisitor();

    @Test
    void shouldVisitSingleMethodArg() {
        String input = "Integer[] a";
        JavaParser.SingleMethodArgContext c = HELPER.shouldParseToEof(input, JavaParser::singleMethodArg);

        Variable variable = visitor.visit(c);

        assertVariableDec(variable, "Integer[]", "a");
    }
}