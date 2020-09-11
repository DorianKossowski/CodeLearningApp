package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassBody;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Variable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClassBodyVisitor extends JavaVisitor<ClassBody> {
    private final String className;

    public ClassBodyVisitor(String className) {
        this.className = Objects.requireNonNull(className, "className cannot be null");
    }

    @Override
    public ClassBody visitClassBody(JavaParser.ClassBodyContext ctx) {
        FieldDecVisitor fieldDecVisitor = new FieldDecVisitor();
        List<Variable> fields = ctx.fieldDec().stream()
                .map(fieldDecVisitor::visit)
                .collect(Collectors.toList());
        MethodDecVisitor methodDecVisitor = new MethodDecVisitor(className);
        List<Method> methods = ctx.methodDec().stream()
                .map(methodDecVisitor::visit)
                .collect(Collectors.toList());

        return new ClassBody(fields, methods);
    }
}