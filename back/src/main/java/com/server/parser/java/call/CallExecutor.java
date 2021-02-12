package com.server.parser.java.call;

import com.server.parser.java.ast.statement.Call;
import com.server.parser.util.exception.ResolvingException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

public class CallExecutor implements Serializable {

    public Call call() {
        throw new NotImplementedException();
    }

    public CallStatement callPrintMethod(CallInvocation invocation) {
        int argumentsSize = invocation.getArgs().size();
        checkPrintMethodArguments(invocation.getName(), argumentsSize);
        return new CallStatement(invocation, Collections.emptyList());
    }

    private void checkPrintMethodArguments(String methodName, int argumentsSize) {
        if (argumentsSize != 1) {
            throw new ResolvingException(String.format("Metoda %s musi przyjmować tylko jeden argument (wywołano z %d)",
                    methodName, argumentsSize));
        }
    }
}