package com.server.parser.java;

import com.server.parser.ParserBuilder;
import com.server.parser.java.ast.Exercise;
import com.server.parser.java.visitor.ExerciseVisitor;

public class JavaParserAdapter {

    public static Exercise parseExercise(String input) {
        JavaParser javaParser = ParserBuilder.build(input, JavaLexer::new, JavaParser::new);
        JavaParser.ExerciseEOFContext context = javaParser.exerciseEOF();
        return new ExerciseVisitor().visitExerciseEOF(context);
    }
}