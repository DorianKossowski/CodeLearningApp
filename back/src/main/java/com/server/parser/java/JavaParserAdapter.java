package com.server.parser.java;

import com.server.parser.ParserBuilder;
import com.server.parser.java.ast.Task;
import com.server.parser.java.visitor.TaskVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.function.Function;

public class JavaParserAdapter {

    public static Task getResolvedTask(String input) {
        JavaParser.TaskEOFContext context = parseTask(input);
        return new TaskVisitor().visitTaskEOF(context);
    }

    private static JavaParser.TaskEOFContext parseTask(String input) {
        return parse(input, JavaParser::taskEOF);
    }

    public static JavaParser.StatementContext parseStatement(String input) {
        return parse(input, JavaParser::statement);
    }

    public static JavaParser.ExpressionContext parseExpression(String input) {
        return parse(input, JavaParser::expression);
    }

    private static <P extends ParserRuleContext> P parse(String input, Function<JavaParser, P> parseFunction) {
        JavaParser javaParser = ParserBuilder.build(input, JavaLexer::new, JavaParser::new);
        return parseFunction.apply(javaParser);
    }
}