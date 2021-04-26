package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.expression_statement.VariableDef;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodHeader implements AstElement {
    private final List<String> modifiers;
    private final String result;
    private final String name;
    private final List<VariableDef> arguments;

    public MethodHeader(List<String> modifiers, String result, String name, List<VariableDef> arguments) {
        this.modifiers = Objects.requireNonNull(modifiers, "modifiers cannot be null");
        this.result = result;
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.arguments = Objects.requireNonNull(arguments, "arguments cannot be null");
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public String getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

    public List<VariableDef> getArguments() {
        return arguments;
    }

    public boolean isConstructor() {
        return result == null;
    }

    public boolean isStatic() {
        return modifiers.contains("static");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodHeader that = (MethodHeader) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(getVariablesType(arguments), getVariablesType(that.arguments)) &&
                Objects.equals(isConstructor(), that.isConstructor());
    }

    private static List<String> getVariablesType(List<VariableDef> variables) {
        return variables.stream()
                .map(VariableDef::getType)
                .collect(Collectors.toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getVariablesType(arguments), isConstructor());
    }

    @Override
    public String toString() {
        return name;
    }
}
