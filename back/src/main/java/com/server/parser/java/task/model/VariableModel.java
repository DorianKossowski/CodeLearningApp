package com.server.parser.java.task.model;

import java.util.Objects;
import java.util.Optional;

public class VariableModel {
    private String text;
    private String logInfo;

    private VariableModel() {
    }

    public Optional<String> getText() {
        return Optional.ofNullable(text);
    }

    public Optional<String> getLogInfo() {
        return Optional.ofNullable(logInfo);
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
        VariableModel that = (VariableModel) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(logInfo, that.logInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, logInfo);
    }

    public static class Builder {
        private String text;
        private String logInfo;

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withLogInfo(String errorMessage) {
            this.logInfo = errorMessage;
            return this;
        }

        public VariableModel build() {
            VariableModel statementModel = new VariableModel();
            statementModel.text = this.text;
            statementModel.logInfo = this.logInfo;

            return statementModel;
        }
    }
}