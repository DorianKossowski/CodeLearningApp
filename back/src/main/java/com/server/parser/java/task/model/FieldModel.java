package com.server.parser.java.task.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FieldModel {
    private List<String> modifiers;
    private String type;
    private String name;
    private String value;
    private String logInfo;

    private FieldModel() {
    }

    public Optional<List<String>> getModifiers() {
        return Optional.ofNullable(modifiers);
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getValue() {
        return Optional.ofNullable(value);
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
        FieldModel that = (FieldModel) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(logInfo, that.logInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, logInfo);
    }

    public static class Builder {
        private List<String> modifiers;
        private String type;
        private String name;
        private String value;
        private String logInfo;

        public Builder withModifiers(List<String> modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Builder withLogInfo(String errorMessage) {
            this.logInfo = errorMessage;
            return this;
        }

        public FieldModel build() {
            FieldModel model = new FieldModel();
            model.modifiers = this.modifiers;
            model.type = this.type;
            model.name = this.name;
            model.value = this.value;
            model.logInfo = this.logInfo;
            return model;
        }
    }
}