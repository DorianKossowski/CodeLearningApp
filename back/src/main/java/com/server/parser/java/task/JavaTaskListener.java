package com.server.parser.java.task;

import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaTaskBaseListener;
import com.server.parser.java.JavaTaskParser;

import java.util.Objects;

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
    public void enterMethodRuleSpec(JavaTaskParser.MethodRuleSpecContext ctx) {
        methodBuilder.withName(JavaGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }
}