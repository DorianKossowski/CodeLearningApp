package com.server.parser.java;

import com.server.parser.ParserBuilder;
import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.visitor.TaskVisitor;

public class JavaParserAdapter {

    public static TaskAst parseTask(String input) {
        JavaParser javaParser = ParserBuilder.build(input, JavaLexer::new, JavaParser::new);
        JavaParser.TaskEOFContext context = javaParser.taskEOF();
        return new TaskVisitor().visitTaskEOF(context);
    }
}