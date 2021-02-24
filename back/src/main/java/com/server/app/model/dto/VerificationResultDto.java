package com.server.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class VerificationResultDto {
    private String output;
    private String errorMessage;
    private int lineNumber;

    // test usage only
    @JsonIgnore
    private Exception exception;

    private VerificationResultDto() {
    }

    public static VerificationResultDto valid(String output) {
        return new Builder()
                .withOutput(output)
                .build();
    }

    public static VerificationResultDto invalidTask(String output) {
        return new Builder()
                .withErrorMessage("Problem z danymi wejściowymi - skontaktuj się z administratorem")
                .withOutput(output)
                .build();
    }

    public static VerificationResultDto invalidInput(String message, int lineNumber) {
        return new Builder()
                .withErrorMessage(message)
                .withLineNumber(lineNumber)
                .build();
    }

    public static VerificationResultDto invalid(Exception exception) {
        return invalid(exception, null);
    }

    public static VerificationResultDto invalid(Exception exception, String output) {
        return new Builder()
                .withErrorMessage(exception.getMessage())
                .withException(exception)
                .withOutput(output)
                .build();
    }

    public String getOutput() {
        return output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getLineNumber() {
        return lineNumber;
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

    private static class Builder {
        private String output;
        private String errorMessage;
        private int lineNumber;
        private Exception exception;

        private Builder withOutput(String output) {
            this.output = output;
            return this;
        }

        private Builder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        private Builder withLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        private Builder withException(Exception exception) {
            this.exception = exception;
            return this;
        }

        private VerificationResultDto build() {
            VerificationResultDto resultDto = new VerificationResultDto();
            resultDto.output = output;
            resultDto.errorMessage = errorMessage;
            resultDto.lineNumber = lineNumber;
            resultDto.exception = exception;
            return resultDto;
        }
    }
}