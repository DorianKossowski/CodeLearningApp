package com.server.app.service.json;

import com.server.parser.java.ast.TaskAst;

public interface TaskToJsonService {
    String mapToJson(TaskAst taskAst);
}