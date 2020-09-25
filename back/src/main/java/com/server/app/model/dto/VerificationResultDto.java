package com.server.app.model.dto;

import java.util.Objects;

public class VerificationResultDto {
    private String errorMessage;
    private int lineNumber;

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

    public static VerificationResultDto valid() {
        return new VerificationResultDto();
    }

    public static VerificationResultDto invalid(String message) {
        return new VerificationResultDto(message);
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
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorMessage, lineNumber);
    }
}