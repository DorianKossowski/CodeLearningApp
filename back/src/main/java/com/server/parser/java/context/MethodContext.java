package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.call.CallExecutor;
import com.server.parser.util.ValuePreparer;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// TODO refactor contexts (maybe inheritance)
public class MethodContext implements JavaContext {
    private final ClassContext classContext;
    private MethodHeader methodHeader;
    private final Map<String, Variable> nameToField;
    private final Map<String, Variable> nameToVariable = new HashMap<>();

    MethodContext(ClassContext classContext) {
        this.classContext = Objects.requireNonNull(classContext, "classContext cannot be null");
        this.nameToField = classContext.getFields();
    }

    @Override
    public JavaContext createLocalContext() {
        return new LocalContext(classContext.getCallExecutor(), nameToField, nameToVariable, getMethodName(),
                methodHeader.isStatic());
    }

    public void save(MethodHeader methodHeader) {
        this.methodHeader = Objects.requireNonNull(methodHeader, "methodHeader cannot be null");
        classContext.getCallExecutor().getCallableKeeper().keepCallable(this, methodHeader);
    }

    public String getClassName() {
        return classContext.getName();
    }

    public Map<String, Variable> getNameToVariable() {
        return ImmutableMap.copyOf(nameToVariable);
    }

    @Override
    public String getMethodName() {
        return methodHeader.getName();
    }

    @Override
    public void addVariable(Variable var) {
        String varName = var.getName();
        nameToVariable.computeIfPresent(varName, (key, $) -> {
            throw new ResolvingException("Obiekt " + key + " już istnieje");
        });
        nameToVariable.put(varName, var);
    }

    @Override
    public void updateVariable(String var, Expression expression) {
        Variable variable = getVariable(var);
        Value newValue = ValuePreparer.prepare(variable.getType(), expression);
        variable.setValue(newValue);
    }

    @Override
    public Variable getVariable(String var) {
        if (nameToVariable.containsKey(var)) {
            return nameToVariable.get(var);
        }
        if (nameToField.containsKey(var)) {
            Variable variable = nameToField.get(var);
            if (methodHeader.isStatic() && !variable.isStatic()) {
                throw new ResolvingException("Nie można użyć " + var + " ze statycznego kontekstu");
            }
            return variable;
        }
        throw new ResolvingException("Obiekt " + var + " nie istnieje");
    }

    @Override
    public CallExecutor getCallExecutor() {
        return classContext.getCallExecutor();
    }
}