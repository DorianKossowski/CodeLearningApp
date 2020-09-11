package com.server.app.service.impl.json;

import com.server.app.service.json.ExerciseToJsonService;
import com.server.app.service.json.JsonComparingService;
import com.server.parser.java.ast.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExerciseToJsonServiceImplTest {
    private final JsonComparingService comparingService = new JsonComparingServiceImpl();
    private final ExerciseToJsonService service = new ExerciseToJsonServiceImpl();


    @Test
    void shouldMapToJson() {
        String expected = "{\"classAst\" : {\"header\" : {\"name\" : \"MyClass\"},\"body\" : {" +
                "\"fields\" : [ {\"type\" : \"String\",\"name\" : \"str\",\"value\" : null} ]," +
                "\"methods\" : [ {\"className\" : \"MyClass\", \"header\" : {\"result\" : \"int\",\"name\" : \"myMethod\"," +
                " \"arguments\" : [ {\"type\" : \"Integer\",\"name\" : \"arg\",\"value\" : null} ] },\"body\" : {" +
                "  \"statements\" : [ {\"name\" : \"methodToCall\",\"args\" : [ {\"text\" : \"\\\"expr1 expr2\\\"\"} ]} ]}} ]}}}";
        Exercise exercise = createSampleExercise();

        String json = service.mapToJson(exercise);

        assertThat(comparingService.areEqual(json, expected)).isTrue();
    }

    private Exercise createSampleExercise() {
        ClassHeader classHeader = new ClassHeader("MyClass");

        Variable variable = new Variable("String", "str");
        Method method = createSampleMethod();
        ClassBody classBody = new ClassBody(Collections.singletonList(variable), Collections.singletonList(method));

        return new Exercise(new ClassAst(classHeader, classBody));
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