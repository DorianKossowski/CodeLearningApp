package com.server.parser.java.task;

import com.server.parser.java.task.ast.MethodArgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MethodModel {
    private String name;
    private List<MethodArgs> args;

    private MethodModel() {
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public List<MethodArgs> getArgs() {
        return args;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return getName().orElse("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodModel that = (MethodModel) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, args);
    }

    public static class Builder {
        private String name;
        private List<MethodArgs> args = new ArrayList<>();

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withArgs(List<MethodArgs> args) {
            this.args = args;
            return this;
        }

        public MethodModel build() {
            MethodModel model = new MethodModel();
            model.name = this.name;
            model.args = this.args;
            return model;
        }
    }
}