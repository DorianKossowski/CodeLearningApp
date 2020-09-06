package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Exercise;

public class ExerciseVisitor extends JavaBaseVisitor<Exercise> {

    @Override
    public Exercise visitExerciseEOF(JavaParser.ExerciseEOFContext ctx) {
        return visitExercise(ctx.exercise());
    }

    @Override
    public Exercise visitExercise(JavaParser.ExerciseContext ctx) {
        ClassAst classAst = new ClassDecVisitor().visit(ctx.classDec());
        return new Exercise(classAst);
    }
}