package com.server.parser.java;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.JavaContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JavaProgramRunner {
    private final JavaContext context;
    private final MainRunner mainRunner;

    public JavaProgramRunner(JavaContext context) {
        this(context, new MainRunner());
    }

    public JavaProgramRunner(JavaContext context, MainRunner mainRunner) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
        this.mainRunner = Objects.requireNonNull(mainRunner, "mainRunner cannot be null");
    }

    public Optional<List<Statement>> run(List<Method> methods) {
        context.getStaticFields().values().forEach(fieldVar -> fieldVar.initialize(context));
        return mainRunner.run(methods);
    }

    static class MainRunner {
        public Optional<List<Statement>> run(List<Method> methods) {
            return getMainMethod(methods)
                    .map(mainMethod -> mainMethod.getMethodContext().resolveStatements(mainMethod.getBodyContext()));
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