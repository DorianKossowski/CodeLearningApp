package com.server.parser.java.task.model;

import java.util.Objects;
import java.util.Optional;

public class StatementModel {
    private String inMethod;
    private String text;
    private String resolved;
    private String errorMessage;

    private StatementModel() {
    }

    public Optional<String> getInMethod() {
        return Optional.ofNullable(inMethod);
    }

    public Optional<String> getText() {
        return Optional.ofNullable(text);
    }

    public Optional<String> getResolved() {
        return Optional.ofNullable(resolved);
    }

    public Optional<String> getErrorMessage() {
        return Optional.ofNullable(errorMessage);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatementModel that = (StatementModel) o;
        return Objects.equals(inMethod, that.inMethod) &&
                Objects.equals(text, that.text) &&
                Objects.equals(resolved, that.resolved) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inMethod, text, resolved, errorMessage);
    }

    public static class Builder {
        private String inMethod;
        private String text;
        private String resolved;
        private String errorMessage;

        public Builder withMethod(String method) {
            this.inMethod = method;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withResolved(String resolved) {
            this.resolved = resolved;
            return this;
        }

        public Builder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public StatementModel build() {
            StatementModel statementModel = new StatementModel();
            statementModel.inMethod = this.inMethod;
            statementModel.text = this.text;
            statementModel.resolved = this.resolved;
            statementModel.errorMessage = this.errorMessage;

            return statementModel;
        }
    }
}
