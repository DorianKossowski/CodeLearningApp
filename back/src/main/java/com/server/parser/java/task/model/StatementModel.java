package com.server.parser.java.task.model;

import java.util.Objects;
import java.util.Optional;

public class StatementModel {
    private String inMethod;

    private StatementModel() {
    }

    public Optional<String> getInMethod() {
        return Optional.of(inMethod);
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
        return Objects.equals(inMethod, that.inMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inMethod);
    }

    public static class Builder {
        private String inMethod;

        public Builder withMethod(String method) {
            this.inMethod = method;
            return this;
        }

        public StatementModel build() {
            StatementModel statementModel = new StatementModel();
            statementModel.inMethod = this.inMethod;

            return statementModel;
        }
    }
}
