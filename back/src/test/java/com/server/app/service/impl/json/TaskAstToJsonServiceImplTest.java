package com.server.app.service.impl.json;

import com.server.app.service.json.JsonComparingService;
import com.server.app.service.json.TaskToJsonService;
import com.server.parser.java.ast.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskAstToJsonServiceImplTest {
    private final JsonComparingService comparingService = new JsonComparingServiceImpl();
    private final TaskToJsonService service = new TaskToJsonServiceImpl();


    @Test
    void shouldMapToJson() {
        String expected = "{\"classAst\" : {\"header\" : {\"name\" : \"MyClass\"},\"body\" : {" +
                "\"fields\" : [ {\"type\" : \"String\",\"name\" : \"str\",\"value\" : null} ]," +
                "\"methods\" : [ {\"className\" : \"MyClass\", \"header\" : {\"result\" : \"int\",\"name\" : \"myMethod\"," +
                " \"arguments\" : [ {\"type\" : \"Integer\",\"name\" : \"arg\",\"value\" : null} ] },\"body\" : {" +
                "  \"statements\" : [ {\"name\" : \"methodToCall\",\"args\" : [ {\"text\" : \"\\\"expr1 expr2\\\"\"} ]} ]}} ]}}}";
        TaskAst taskAst = createSampleTask();

        String json = service.mapToJson(taskAst);

        assertThat(comparingService.areEqual(json, expected)).isTrue();
    }

    private TaskAst createSampleTask() {
        ClassHeader classHeader = new ClassHeader("MyClass");

        Variable variable = new Variable("String", "str");
        Method method = createSampleMethod();
        ClassBody classBody = new ClassBody(Collections.singletonList(variable), Collections.singletonList(method));

        return new TaskAst(new ClassAst(classHeader, classBody));
    }

    private Method createSampleMethod() {
        Variable argument = new Variable("Integer", "arg");
        MethodHeader methodHeader = new MethodHeader("int", "myMethod", Collections.singletonList(argument));

        List<Expression> callArgs = Collections.singletonList(new Expression("\"expr1 expr2\""));
        MethodCall methodCall = new MethodCall("methodToCall", callArgs);
        MethodBody methodBody = new MethodBody(Collections.singletonList(methodCall));

        return new Method("MyClass", methodHeader, methodBody);
    }
}