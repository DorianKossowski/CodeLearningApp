package com.server.parser.java.task;

import java.util.Objects;
import java.util.Optional;

public class MethodModel {
    private String name;

    private MethodModel() {
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
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
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class Builder {
        private String name;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public MethodModel build() {
            MethodModel model = new MethodModel();
            model.name = this.name;
            return model;
        }
    }
}