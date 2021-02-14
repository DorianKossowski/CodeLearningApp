package com.server.parser.java.task.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClassModel {
    private List<String> modifiers;
    private String name;
    private String logInfo;

    private ClassModel() {
    }

    public Optional<List<String>> getModifiers() {
        return Optional.ofNullable(modifiers);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
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
        ClassModel that = (ClassModel) o;
        return Objects.equals(modifiers, that.modifiers) &&
                Objects.equals(name, that.name) &&
                Objects.equals(logInfo, that.logInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modifiers, name, logInfo);
    }

    public static class Builder {
        private List<String> modifiers;
        private String name;
        private String logInfo;

        public Builder withModifiers(List<String> modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withLogInfo(String errorMessage) {
            this.logInfo = errorMessage;
            return this;
        }

        public ClassModel build() {
            ClassModel model = new ClassModel();
            model.modifiers = this.modifiers;
            model.name = this.name;
            model.logInfo = this.logInfo;
            return model;
        }
    }
}