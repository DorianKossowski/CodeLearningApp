package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.call.CallResolver;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.value.util.ValuePreparer;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO refactor contexts (maybe inheritance)
public class MethodContext implements JavaContext {
    private final ClassContext classContext;
    private MethodHeader methodHeader;
    private final Map<String, FieldVar> nameToField;
    private final Map<String, Variable> nameToVariable = new HashMap<>();
    private ObjectValue thisValue;

    MethodContext(ClassContext classContext) {
        this.classContext = Objects.requireNonNull(classContext, "classContext cannot be null");
        this.nameToField = classContext.getFields();
    }

    @Override
    public JavaContext createLocalContext() {
        return new LocalContext(classContext.getCallResolver(), nameToField, nameToVariable, getClassName(),
                getMethodName(), getMethodResultType(), isStaticContext(), thisValue);
    }

    public Method save(MethodHeader methodHeader, JavaParser.MethodBodyContext methodBody) {
        this.methodHeader = Objects.requireNonNull(methodHeader, "methodHeader cannot be null");
        Method method = new Method(this, methodHeader, methodBody);
        classContext.getCallResolver().getCallableKeeper().keepCallable(method);
        return method;
    }

    @Override
    public String getClassName() {
        return classContext.getClassName();
    }

    @Override
    public boolean isStaticContext() {
        return methodHeader.isStatic();
    }

    @Override
    public ObjectValue getThisValue() {
        return thisValue;
    }

    @Override
    public void setThisValue(ObjectValue thisValue) {
        this.thisValue = thisValue;
        this.nameToField.putAll(thisValue.getFields());
    }

    public Map<String, Variable> getNameToVariable() {
        return ImmutableMap.copyOf(nameToVariable);
    }

    @Override
    public String getMethodName() {
        return methodHeader.getName();
    }

    @Override
    public String getMethodResultType() {
        return methodHeader.getResult();
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
            FieldVar variable = nameToField.get(var);
            if (methodHeader.isStatic() && !variable.isStatic()) {
                throw new ResolvingException("Nie można użyć " + var + " ze statycznego kontekstu");
            }
            return variable;
        }
        throw new ResolvingException("Obiekt " + var + " nie istnieje");
    }

    @Override
    public CallResolver getCallResolver() {
        return classContext.getCallResolver();
    }

    @Override
    public Map<String, FieldVar> getStaticFields() {
        return nameToField.entrySet().stream()
                .filter(entry -> entry.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, FieldVar> getFields() {
        return ImmutableMap.copyOf(nameToField);
    }

    @Override
    public void setFields(Map<String, FieldVar> nameToField) {
        this.nameToField.putAll(nameToField);
    }
}