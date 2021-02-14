package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.JavaContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TaskVisitor extends JavaBaseVisitor<Task> {
    private final JavaContext context = new ClassContext();
    private final MainRunner mainRunner = new MainRunner();

    @Override
    public Task visitTaskEOF(JavaParser.TaskEOFContext ctx) {
        return visitTask(ctx.task());
    }

    @Override
    public Task visitTask(JavaParser.TaskContext ctx) {
        ClassAst classAst = context.getVisitor(ClassAst.class).visit(ctx.classDec(), context);

        List<Statement> calledStatements = mainRunner.run(classAst.getBody().getMethods()).orElse(Collections.emptyList());
        return new Task(classAst, calledStatements);
    }

    static class MainRunner {
        private final StatementListVisitor statementListVisitor = new StatementListVisitor();

        public Optional<List<Statement>> run(List<Method> methods) {
            return getMainMethod(methods)
                    .map(mainMethod -> statementListVisitor.visit(mainMethod.getBodyContext(), mainMethod.getMethodContext()));
        }

        Optional<Method> getMainMethod(List<Method> methods) {
            return methods.stream()
                    .filter(MainRunner::hasMainModifiers)
                    .filter(MainRunner::hasMainName)
                    .filter(MainRunner::hasMainResult)
                    .filter(MainRunner::hasMainArgs)
                    .findFirst();
        }

        private static boolean hasMainModifiers(Method method) {
            return method.getHeader().getModifiers().containsAll(Arrays.asList("public", "static"));
        }

        private static boolean hasMainName(Method method) {
            return method.getHeader().getName().equals("main");
        }

        private static boolean hasMainResult(Method method) {
            return method.getHeader().getResult().equals("void");
        }

        private static boolean hasMainArgs(Method method) {
            List<VariableDef> arguments = method.getHeader().getArguments();
            return arguments.size() == 1 && arguments.get(0).getType().equals("String[]");
        }
    }
}