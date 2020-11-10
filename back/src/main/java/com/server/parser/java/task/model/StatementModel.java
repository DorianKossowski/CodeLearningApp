package com.server.parser.java.task.model;

import java.util.Objects;
import java.util.Optional;

public class StatementModel {
    private String inMethod;
    private String text;
    private String resolved;
    private String ifCond;
    private String elseIfCond;
    private Boolean isInElse;
    private String logInfo;

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

    public Optional<String> getIfCond() {
        return Optional.ofNullable(ifCond);
    }

    public Optional<String> getElseIfCond() {
        return Optional.ofNullable(elseIfCond);
    }

    public Optional<Boolean> isInElse() {
        return Optional.ofNullable(isInElse);
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
        StatementModel that = (StatementModel) o;
        return Objects.equals(inMethod, that.inMethod) &&
                Objects.equals(text, that.text) &&
                Objects.equals(resolved, that.resolved) &&
                Objects.equals(ifCond, that.ifCond) &&
                Objects.equals(elseIfCond, that.elseIfCond) &&
                Objects.equals(isInElse, that.isInElse) &&
                Objects.equals(logInfo, that.logInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inMethod, text, resolved, ifCond, elseIfCond, isInElse, logInfo);
    }

    public static class Builder {
        private String inMethod;
        private String text;
        private String resolved;
        private String ifCond;
        private String elseIfCond;
        private Boolean isInElse;
        private String logInfo;

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

        public Builder withIf(String ifCond) {
            this.ifCond = ifCond;
            return this;
        }

        public Builder withElseIf(String elseIfCond) {
            this.elseIfCond = elseIfCond;
            return this;
        }

        public Builder isInElse(boolean isInElse) {
            this.isInElse = isInElse;
            return this;
        }

        public Builder withLogInfo(String errorMessage) {
            this.logInfo = errorMessage;
            return this;
        }

        public StatementModel build() {
            StatementModel statementModel = new StatementModel();
            statementModel.inMethod = this.inMethod;
            statementModel.text = this.text;
            statementModel.resolved = this.resolved;
            statementModel.ifCond = this.ifCond;
            statementModel.elseIfCond = this.elseIfCond;
            statementModel.isInElse = this.isInElse;
            statementModel.logInfo = this.logInfo;

            return statementModel;
        }
    }
}
