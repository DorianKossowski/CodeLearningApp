package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.Variable;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;

public class MethodContext extends DelegatingContext {
    private final Map<String, FieldVar> nameToField;
    private final Map<String, Variable> nameToVariable = new HashMap<>();
    private ObjectValue thisValue;

    MethodContext(ClassContext classContext) {
        super(classContext);
        this.nameToField = new HashMap<>(classContext.getFields());
    }

    public Method save(MethodHeader methodHeader, JavaParser.MethodBodyContext methodBody) {
        setParameters(ContextParameters.createMethodContextParameters(getParameters(), methodHeader.getName(),
                methodHeader.getResult(), methodHeader.isStatic()));
        Method method = new Method(this, methodHeader, methodBody);
        getParameters().getCallResolver().getCallableKeeper().keepCallable(method);
        return method;
    }

    @Override
    public JavaContext createLocalContext() {
        return new LocalContext(this);
    }

    @Override
    public ObjectValue getThisValue() {
        return thisValue;
    }

    public void setThisValue(ObjectValue thisValue) {
        this.thisValue = thisValue;
        this.nameToField.putAll(thisValue.getFields());
    }

    @Override
    Map<String, Variable> getImmutableVariables() {
        return ImmutableMap.copyOf(nameToVariable);
    }

    @Override
    public void addVariable(Variable var) {
        nameToVariable.put(getValidatedVariableName(var), var);
    }

    @Override
    public Variable getVariable(String var) {
        if (nameToVariable.containsKey(var)) {
            return nameToVariable.get(var);
        }
        if (nameToField.containsKey(var)) {
            FieldVar variable = nameToField.get(var);
            if (getParameters().isStaticContext() && !variable.isStatic()) {
                throw new ResolvingException("Nie można użyć " + var + " ze statycznego kontekstu");
            }
            return variable;
        }
        throw new ResolvingException("Obiekt " + var + " nie istnieje");
    }

    @Override
    public Map<String, FieldVar> getFields() {
        return nameToField;
    }
}