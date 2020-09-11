package com.server.parser.java.task;

import com.server.parser.ParserBuilder;
import com.server.parser.java.JavaTaskLexer;
import com.server.parser.java.JavaTaskParser;

public class JavaTaskParserBuilder {

    public static JavaTaskParser build(String input) {
        return ParserBuilder.build(input, JavaTaskLexer::new, JavaTaskParser::new);
    }
}