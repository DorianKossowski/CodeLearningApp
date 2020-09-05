package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class JavaDefaultTextVisitor extends JavaBaseVisitor<String> {

    @Override
    public String visitTerminal(TerminalNode node) {
        return node.getText();
    }

    @Override
    protected String defaultResult() {
        return "";
    }

    @Override
    protected String aggregateResult(String aggregate, String nextResult) {
        return aggregate + nextResult;
    }
}