package com.server.app.model.dto;

public class VerificationInputDto {
    private String task;
    private String input;

    public VerificationInputDto() {
    }

    public VerificationInputDto(String task, String input) {
        this.task = task;
        this.input = input;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}