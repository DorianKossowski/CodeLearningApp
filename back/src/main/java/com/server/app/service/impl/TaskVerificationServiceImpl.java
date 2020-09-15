package com.server.app.service.impl;

import com.server.app.service.TaskVerificationService;
import com.server.parser.java.JavaParserAdapter;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.ast.Exercise;
import com.server.parser.java.task.JavaTaskListener;
import com.server.parser.java.task.JavaTaskParserBuilder;
import com.server.parser.java.task.verifier.TaskVerifier;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.stereotype.Service;

//TODO make service generic
@Service
public class TaskVerificationServiceImpl implements TaskVerificationService {

    @Override
    public void verify(String input, String task) {
        Exercise exercise = JavaParserAdapter.parseExercise(input);

        JavaTaskListener javaTaskListener = new JavaTaskListener(new TaskVerifier(exercise));
        JavaTaskParser javaTaskParser = JavaTaskParserBuilder.build(task);
        new ParseTreeWalker().walk(javaTaskListener, javaTaskParser.rulesEOF());
    }
}