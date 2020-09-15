package com.server.parser.util;

public class AcceptanceTestCaseModel {
    private final String task;
    private final String input;

    public AcceptanceTestCaseModel(String task, String input) {
        this.task = task;
        this.input = input;
    }

    public String getTask() {
        return task;
    }

    public String getInput() {
        return input;
    }
}