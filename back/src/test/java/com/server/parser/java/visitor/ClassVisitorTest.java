package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.*;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ClassVisitorTest extends JavaVisitorTestBase {
    private final ClassVisitor visitor = new ClassVisitor();

    private ClassVisitor.ClassVisitorInternal visitorInternal;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        visitorInternal = new ClassVisitor.ClassVisitorInternal(context);
    }

    @Test
    void shouldCreateFromClassDec() {
        String input = "public class c { void m() {} }";
        JavaParser.ClassDecContext c = HELPER.shouldParseToEof(input, JavaParser::classDec);

        ClassAst classAst = visitor.visit(c, context);

        assertThat(classAst.getHeader().getName()).isEqualTo("c");
        assertThat(Iterables.getOnlyElement(classAst.getBody().getMethods()).getHeader().getName()).isEqualTo("m");
    }

    @Test
    void shouldVisitClassHeader() {
        String input = "public class c";
        JavaParser.ClassHeaderContext c = HELPER.shouldParseToEof(input, JavaParser::classHeader);

        ClassHeader header = visitorInternal.visit(c);

        assertThat(Iterables.getOnlyElement(header.getModifiers())).isEqualTo("public");
        assertThat(header.getName()).isEqualTo("c");
    }

    @Test
    void shouldVisitClassBody() {
        String input = "int i; void m(){} MyClass(){} void m2(){} private String s;";
        JavaParser.ClassBodyContext c = HELPER.shouldParseToEof(input, JavaParser::classBody);

        ClassBody body = visitorInternal.visit(c);

        assertThat(body.getConstructors()).extracting(constructor -> constructor.getHeader().getName())
                .containsExactly("MyClass");
        assertThat(body.getMethods()).extracting(method -> method.getHeader().getName())
                .containsExactly("m", "m2");
        assertThat(body.getFields()).extracting(VariableDef::getName)
                .containsExactly("i", "s");
    }

    @Test
    void shouldCreateDefaultConstructorForEmptyClassBody() {
        String input = "";
        JavaParser.ClassBodyContext c = HELPER.shouldParseToEof(input, JavaParser::classBody);

        ClassBody body = visitorInternal.visit(c);

        Method constructor = Iterables.getOnlyElement(body.getConstructors());
        assertThat(constructor.getHeader())
                .returns(Collections.emptyList(), MethodHeader::getModifiers)
                .returns("MyClass", MethodHeader::getName)
                .returns(Collections.emptyList(), MethodHeader::getArguments);
        assertThat(body.getMethods()).isEmpty();
        assertThat(body.getFields()).isEmpty();
    }
}