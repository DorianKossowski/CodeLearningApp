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
        TestInputDto inputDto = createTestInputDto("app/integration/valid-parse-input");
        VerificationResultDto resultDto = VerificationResultDto.valid();

        mvc.perform(post("/parse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorMessage").value(resultDto.getErrorMessage()))
                .andExpect(jsonPath("$.lineNumber").value(resultDto.getLineNumber()));
    }

    private TestInputDto createTestInputDto(String casePath) {
        String testCase = ParserAcceptanceTestCasesProvider.getSingleTestCase(casePath);
        AcceptanceTestCaseModel acceptanceTestCaseModel = AcceptanceTestCaseFetcher.fetchModel(testCase);
        return new TestInputDto(acceptanceTestCaseModel.getTask(), acceptanceTestCaseModel.getInput());
    }

    @Test
    void shouldReturnInvalidVerificationResultWhenNotSatisfy() throws Exception {
        TestInputDto inputDto = createTestInputDto("app/integration/not-satisfy-parse-input");
        VerificationResultDto resultDto = VerificationResultDto.invalid(
                "Oczekiwana instrukcja \"Wywołanie metody z literału\" nie istnieje");

        mvc.perform(post("/parse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorMessage").value(resultDto.getErrorMessage()))
                .andExpect(jsonPath("$.lineNumber").value(resultDto.getLineNumber()));
    }
}