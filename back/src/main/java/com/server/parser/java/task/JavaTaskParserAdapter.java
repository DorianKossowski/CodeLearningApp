package com.server.parser.java.task;

import com.server.parser.ParserBuilder;
import com.server.parser.java.JavaTaskLexer;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.verifier.TaskVerifier;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class JavaTaskParserAdapter {

    public static void verifyTask(String taskInput, Task resolvedTask) {
        JavaTaskParser.RulesEOFContext rulesEOFContext = parseTaskRules(taskInput);
        JavaTaskListener javaTaskListener = new JavaTaskListener(new TaskVerifier(resolvedTask));
        new ParseTreeWalker().walk(javaTaskListener, rulesEOFContext);
    }

    private static JavaTaskParser.RulesEOFContext parseTaskRules(String taskInput) {
        JavaTaskParser parser = ParserBuilder.build(taskInput, JavaTaskLexer::new, JavaTaskParser::new);
        JavaTaskParser.RulesEOFContext rulesEOFContext = parser.rulesEOF();
        return rulesEOFContext;
    }
}