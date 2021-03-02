package com.server.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class VerificationResultDto {
    private String output;
    private String errorMessage;
    private int lineNumber;
    @JsonIgnore
    private Exception exception;

    public VerificationResultDto() {
    }

    public VerificationResultDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public VerificationResultDto(String errorMessage, int lineNumber) {
        this.errorMessage = errorMessage;
        this.lineNumber = lineNumber;
    }

    public static VerificationResultDto invalidTask() {
        return new VerificationResultDto("Problem z danymi wejściowymi - skontaktuj się z administratorem");
    }

    public static VerificationResultDto invalidInput(String message, int lineNumber) {
        return new VerificationResultDto(message, lineNumber);
    }

    public static VerificationResultDto valid(String output) {
        VerificationResultDto verificationResultDto = new VerificationResultDto();
        verificationResultDto.output = output;
        return verificationResultDto;
    }

    public static VerificationResultDto invalid(Exception exception) {
        VerificationResultDto resultDto = new VerificationResultDto(exception.getMessage());
        resultDto.exception = exception;
        return resultDto;
    }

    public String getOutput() {
        return output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VerificationResultDto that = (VerificationResultDto) o;
        return lineNumber == that.lineNumber &&
                Objects.equals(output, that.output) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, errorMessage, lineNumber);
    }
}