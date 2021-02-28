package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.call.CallResolver;
import com.server.parser.util.ValuePreparer;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalContext implements JavaContext {
    private final CallResolver callResolver;
    private final Map<String, FieldVar> nameToField;
    private final Map<String, Variable> nameToVariable;
    private final String className;
    private final String methodName;
    private final String methodResultType;
    private final boolean isStaticContext;
    private final Map<String, Variable> localNameToVariable = new HashMap<>();

    LocalContext(CallResolver callResolver, Map<String, FieldVar> nameToField, Map<String, Variable> nameToVariable,
                 String className, String methodName, String methodResultType, boolean isStaticContext) {
        this.callResolver = Objects.requireNonNull(callResolver, "callResolver cannot be null");
        this.nameToField = Objects.requireNonNull(nameToField, "nameToField cannot be null");
        this.nameToVariable = Objects.requireNonNull(nameToVariable, "nameToVariable cannot be null");
        this.className = Objects.requireNonNull(className, "className cannot be null");
        this.methodName = Objects.requireNonNull(methodName, "methodName cannot be null");
        this.methodResultType = Objects.requireNonNull(methodResultType, "methodResultType cannot be null");
        this.isStaticContext = isStaticContext;
    }

    Map<String, Variable> getNameToVariable() {
        return ImmutableMap.copyOf(getConcatenatedNameToVariables());
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public boolean isStaticContext() {
        return isStaticContext;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public String getMethodResultType() {
        return methodResultType;
    }

    @Override
    public JavaContext createLocalContext() {
        return new LocalContext(callResolver, nameToField, getConcatenatedNameToVariables(), className, methodName,
                methodResultType, isStaticContext);
    }

    private Map<String, Variable> getConcatenatedNameToVariables() {
        return Stream.concat(nameToVariable.entrySet().stream(), localNameToVariable.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void addVariable(Variable var) {
        String varName = var.getName();
        validateVariableExistence(varName, nameToVariable);
        validateVariableExistence(varName, localNameToVariable);
        localNameToVariable.put(varName, var);
    }

    private void validateVariableExistence(String varName, Map<String, Variable> nameToVariable) {
        if (nameToVariable.containsKey(varName)) {
            throw new ResolvingException("Obiekt " + varName + " już istnieje");
        }
    }

    @Override
    public void updateVariable(String name, Expression expression) {
        Variable variable = getVariable(name);
        Value newValue = ValuePreparer.prepare(variable.getType(), expression);
        variable.setValue(newValue);
    }

    @Override
    public Variable getVariable(String var) {
        if (localNameToVariable.containsKey(var)) {
            return localNameToVariable.get(var);
        }
        if (nameToVariable.containsKey(var)) {
            return nameToVariable.get(var);
        }
        if (nameToField.containsKey(var)) {
            FieldVar variable = nameToField.get(var);
            if (isStaticContext && !variable.isStatic()) {
                throw new ResolvingException("Nie można użyć " + var + " ze statycznego kontekstu");
            }
            return variable;
        }
        throw new ResolvingException("Obiekt " + var + " nie istnieje");
    }

    @Override
    public CallResolver getCallResolver() {
        return callResolver;
    }

    @Override
    public Map<String, FieldVar> getStaticFields() {
        return nameToField.entrySet().stream()
                .filter(entry -> entry.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void setFields(Map<String, FieldVar> nameToField) {
        this.nameToField.putAll(nameToField);
    }
}