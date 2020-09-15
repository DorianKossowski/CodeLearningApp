package com.server.parser.java.task;

import com.server.parser.java.JavaTaskBaseListener;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.task.ast.MethodArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JavaTaskListener extends JavaTaskBaseListener {
    private final TaskVerifier taskVerifier;

    private MethodModel.Builder methodBuilder;

    public JavaTaskListener(TaskVerifier taskVerifier) {
        this.taskVerifier = Objects.requireNonNull(taskVerifier, "taskVerifier cannot be null");
    }

    @Override
    public void enterMethodRule(JavaTaskParser.MethodRuleContext ctx) {
        methodBuilder = MethodModel.builder();
    }

    @Override
    public void exitMethodRule(JavaTaskParser.MethodRuleContext ctx) {
        taskVerifier.verifyMethod(methodBuilder.build());
    }

    @Override
    public void enterMethodNameRuleSpec(JavaTaskParser.MethodNameRuleSpecContext ctx) {
        JavaTaskGrammarHelper.extractValue(ctx.valueOrEmpty()).ifPresent(value -> methodBuilder.withName(value));
    }

    @Override
    public void enterMethodArgsRuleSpec(JavaTaskParser.MethodArgsRuleSpecContext ctx) {
        List<MethodArgs> args = new ArrayList<>();
        for (JavaTaskParser.ArgsElementContext argsContext : ctx.argsElement()) {
            Optional<String> typeOptional = JavaTaskGrammarHelper.extractValue(argsContext.valueOrEmpty().get(0));
            Optional<String> nameOptional = JavaTaskGrammarHelper.extractValue(argsContext.valueOrEmpty().get(1));
            args.add(new MethodArgs(typeOptional.orElse(null), nameOptional.orElse(null)));
        }
        methodBuilder.withArgs(args);
    }
}