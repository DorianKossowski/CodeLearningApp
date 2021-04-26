package com.server.parser.java.value;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.value.util.ValuePreparer;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.util.exception.ResolvingException;
import com.server.parser.util.exception.ResolvingUninitializedException;
import com.server.parser.util.exception.ResolvingVoidException;

import java.util.Collections;
import java.util.Map;

public class ObjectValue extends Value {
    private final Map<String, FieldVar> fields;

    public ObjectValue(Instance instance) {
        super(instance);
        this.fields = instance.getFields();
    }

    ObjectValue(Literal literal) {
        super(literal);
        this.fields = Collections.emptyMap();
    }

    public Map<String, FieldVar> getFields() {
        return fields;
    }

    @Override
    public Value getAttribute(String name) {
        FieldVar fieldVar = fields.get(name);
        if (fieldVar == null) {
            throw new ResolvingException("Nie można znaleźć pola " + name);
        }
        return fieldVar.getValue();
    }

    @Override
    public void updateAttribute(String name, Expression newExpression) {
        if (!fields.containsKey(name)) {
            throw new ResolvingException("Nie można znaleźć pola " + name);
        }
        FieldVar fieldVar = fields.get(name);
        Value newValue = ValuePreparer.prepare(fieldVar.getType(), newExpression);
        fieldVar.setValue(newValue);
    }

    @Override
    public String toString() {
        return expression.getResolvedText();
    }

    @Override
    public boolean equalsOperator(Value v2) {
        if (v2 instanceof NullValue) {
            return false;
        }
        if (v2 instanceof UninitializedValue) {
            throw new ResolvingUninitializedException(v2.expression.getText());
        }
        if (v2 instanceof VoidValue) {
            throw new ResolvingVoidException();
        }
        if (v2 instanceof PrimitiveValue || v2 instanceof ObjectWrapperValue) {
            throw new ResolvingException("Nie można porównać typu obiektowego z " + v2.getExpression().getResolvedText());
        }
        if (v2 instanceof ObjectValue) {
            return expression == v2.expression;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equalsMethod(Value v2) {
        if (v2 instanceof NullValue) {
            return false;
        }
        if (v2 instanceof UninitializedValue) {
            throw new ResolvingUninitializedException(v2.expression.getText());
        }
        if (v2 instanceof VoidValue) {
            throw new ResolvingVoidException();
        }
        return expression == v2.expression;
    }

    @Override
    public boolean and(Value v2) {
        throw new ResolvingException("Nie można wykonać operacji &&");
    }

    @Override
    public boolean or(Value v2) {
        throw new ResolvingException("Nie można wykonać operacji ||");
    }
}