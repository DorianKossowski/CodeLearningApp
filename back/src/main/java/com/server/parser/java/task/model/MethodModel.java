package com.server.parser.java.task.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MethodModel {
    private List<String> modifiers;
    private String result;
    private String name;
    private List<MethodArgs> args;

    private MethodModel() {
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getResult() {
        return Optional.ofNullable(result);
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
        return Objects.equals(modifiers, that.modifiers) &&
                Objects.equals(result, that.result) &&
                Objects.equals(name, that.name) &&
                Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifiers, result, name, args);
    }

    public static class Builder {
        private List<String> modifiers = new ArrayList<>();
        private String result;
        private String name;
        private List<MethodArgs> args = new ArrayList<>();

        public Builder withModifiers(List<String> modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public Builder withResult(String result) {
            this.result = result;
            return this;
        }

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
            model.modifiers = this.modifiers;
            model.result = this.result;
            model.name = this.name;
            model.args = this.args;
            return model;
        }
    }
}