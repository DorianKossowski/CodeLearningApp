package com.server.parser.util;

public class AcceptanceTestCaseModel {
    private final String task;
    private final String input;
    private final String output;

    public AcceptanceTestCaseModel(String task, String input, String output) {
        this.task = task;
        this.input = input;
        this.output = output;
    }

    public String getTask() {
        return task;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }
}