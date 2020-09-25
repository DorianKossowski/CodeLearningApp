package com.server.app.integration;

import com.server.app.model.dto.TestInputDto;
import com.server.app.model.dto.VerificationResultDto;
import com.server.parser.ParserAcceptanceTestCasesProvider;
import com.server.parser.util.AcceptanceTestCaseFetcher;
import com.server.parser.util.AcceptanceTestCaseModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TestParseIntegrationTest extends IntegrationTestBase {

    @Test
    void shouldReturnValidVerificationResult() throws Exception {
        String testCase = ParserAcceptanceTestCasesProvider.getSingleTestCase("app/integration/valid-parse-input");
        AcceptanceTestCaseModel acceptanceTestCaseModel = AcceptanceTestCaseFetcher.fetchModel(testCase);
        TestInputDto inputDto = new TestInputDto(acceptanceTestCaseModel.getTask(), acceptanceTestCaseModel.getInput());
        VerificationResultDto resultDto = VerificationResultDto.valid();

        mvc.perform(post("/parse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorMessage").value(resultDto.getErrorMessage()))
                .andExpect(jsonPath("$.lineNumber").value(resultDto.getLineNumber()));
    }
}