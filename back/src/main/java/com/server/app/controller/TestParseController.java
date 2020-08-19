package com.server.app.controller;

import com.server.app.model.dto.TestInputDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class TestParseController {

    @PostMapping("/parse")
    public String parseInput(@RequestBody TestInputDto input) {
        return "Parsing: " + input.getInput();
    }
}