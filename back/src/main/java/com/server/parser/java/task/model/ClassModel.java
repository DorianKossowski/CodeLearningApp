package com.server.parser.java.task.model;

import java.util.Objects;
import java.util.Optional;

public class ClassModel {
    private String name;
    private String logInfo;

    private ClassModel() {
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
        return Objects.equals(name, that.name) &&
                Objects.equals(logInfo, that.logInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, logInfo);
    }

    public static class Builder {
        private String name;
        private String logInfo;

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
            model.name = this.name;
            model.logInfo = this.logInfo;
            return model;
        }
    }
}