package com.server.parser.java.task.ast;

import java.util.Objects;
import java.util.Optional;

public class MethodArgs {
    private final String type;
    private final String name;

    public MethodArgs(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodArgs that = (MethodArgs) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}