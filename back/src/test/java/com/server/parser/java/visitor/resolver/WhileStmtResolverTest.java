package com.server.parser.java.visitor.resolver;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;

class WhileStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);
    
}