package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstructorHeader;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MethodVisitorTest extends JavaVisitorTestBase {
    private final MethodVisitor visitor = new MethodVisitor();
    private final MethodVisitor.MethodVisitorInternal visitorInternal =
            new MethodVisitor.MethodVisitorInternal(createMethodContext());

    @Test
    void shouldVisitMethodArgs() {
        String input = "(Integer[] a, double b, int c)";
        JavaParser.MethodArgsContext c = HELPER.shouldParseToEof(input, JavaParser::methodArgs);

        List<VariableDef> arguments = visitorInternal.visit(c);

        assertThat(arguments).hasSize(3);
        assertVariableDec(arguments.get(0), "Integer[]", "a");
        assertVariableDec(arguments.get(1), "double", "b");
        assertVariableDec(arguments.get(2), "int", "c");
    }

    @Test
    void shouldVisitMethodArgsWhenEmpty() {
        String input = "()";
        JavaParser.MethodArgsContext c = HELPER.shouldParseToEof(input, JavaParser::methodArgs);

        List<VariableDef> arguments = visitorInternal.visit(c);

        assertThat(arguments).isEmpty();
    }

    @Test
    void shouldVisitSingleMethodArg() {
        String input = "public synchronized void m(String[] a)";
        JavaParser.MethodHeaderContext c = HELPER.shouldParseToEof(input, JavaParser::methodHeader);

        MethodHeader header = visitorInternal.visit(c);

        assertThat(header.getModifiers()).containsExactly("public", "synchronized");
        assertVariableDec(Iterables.getOnlyElement(header.getArguments()), "String[]", "a");
        assertThat(header.getResult()).isEqualTo("void");
        assertThat(header.getName()).isEqualTo("m");
    }

    @Test
    void shouldVisitMethodDec() {
        context.setName("MyClass");
        String input = "void m(String[] a) { println(\"HELLO\"); }";
        JavaParser.MethodDecContext c = HELPER.shouldParseToEof(input, JavaParser::methodDec);

        Method method = visitor.visit(c, createMethodContext());

        assertThat(method.getClassName()).isEqualTo("MyClass");
        MethodHeader header = method.getHeader();
        assertThat(header).extracting(MethodHeader::getResult, MethodHeader::getName)
                .containsExactly("void", "m");
        assertVariableDec(Iterables.getOnlyElement(header.getArguments()), "String[]", "a");
        assertThat(Iterables.getOnlyElement(method.getBodyContext().statementList().statement()).getText())
                .isEqualTo("println(\"HELLO\");");
    }

    @Test
    void shouldVisitConstructorDec() {
        context.setName("MyClass");
        String input = "MyClass(String[] a) { println(\"HELLO\"); }";
        JavaParser.ConstructorDecContext c = HELPER.shouldParseToEof(input, JavaParser::constructorDec);

        Method method = visitor.visit(c, createMethodContext());

        assertThat(method.getClassName()).isEqualTo("MyClass");
        MethodHeader header = method.getHeader();
        assertThat(header).isExactlyInstanceOf(ConstructorHeader.class);
        assertThat(header).extracting(MethodHeader::getResult, MethodHeader::getName)
                .containsExactly(null, "MyClass");
        assertVariableDec(Iterables.getOnlyElement(header.getArguments()), "String[]", "a");
        assertThat(Iterables.getOnlyElement(method.getBodyContext().statementList().statement()).getText())
                .isEqualTo("println(\"HELLO\");");
    }

    @Test
    void shouldThrowWhenWrongConstructorName() {
        context.setName("MyClass");
        String input = "X() {}";
        JavaParser.ConstructorDecContext c = HELPER.shouldParseToEof(input, JavaParser::constructorDec);

        assertThatThrownBy(() -> visitor.visit(c, createMethodContext()))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Konstruktor X różny od nazwy klasy MyClass");
    }
}