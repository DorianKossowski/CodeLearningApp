package com.server.app.integration;

import com.server.app.model.dto.VerificationInputDto;
import com.server.app.model.dto.VerificationResultDto;
import com.server.parser.ParserAcceptanceTestCasesProvider;
import com.server.parser.util.AcceptanceTestCaseFetcher;
import com.server.parser.util.AcceptanceTestCaseModel;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TaskVerificationIntegrationTest extends IntegrationTestBase {

    @Test
    void shouldReturnValidVerificationResult() throws Exception {
        VerificationInputDto inputDto = createTestInputDto("app/integration/valid-parse-input");
        VerificationResultDto resultDto = VerificationResultDto.valid("Hello World");

        performValidate(inputDto, resultDto);
    }

    private VerificationInputDto createTestInputDto(String casePath) {
        String testCase = ParserAcceptanceTestCasesProvider.getSingleTestCase(casePath);
        AcceptanceTestCaseModel acceptanceTestCaseModel = AcceptanceTestCaseFetcher.fetchModel(testCase);
        return new VerificationInputDto(acceptanceTestCaseModel.getTask(), acceptanceTestCaseModel.getInput());
    }

    private void performValidate(VerificationInputDto inputDto, VerificationResultDto resultDto) throws Exception {
        mvc.perform(post("/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorMessage").value(resultDto.getErrorMessage()))
                .andExpect(jsonPath("$.lineNumber").value(resultDto.getLineNumber()))
                .andExpect(jsonPath("$.output").value(resultDto.getOutput()));
    }

    @Test
    void shouldReturnInvalidVerificationResultWhenNotSatisfy() throws Exception {
        VerificationInputDto inputDto = createTestInputDto("app/integration/not-satisfy-parse-input");
        Exception exception = new Exception("Oczekiwana instrukcja \"Wywołanie metody z literału\" nie istnieje");
        VerificationResultDto resultDto = VerificationResultDto.invalid(exception, "Bye World");

        performValidate(inputDto, resultDto);
    }

    @Test
    void shouldReturnInvalidVerificationResultWhenWrongTask() throws Exception {
        VerificationInputDto inputDto = createTestInputDto("app/integration/wrong-task-parse-input");
        VerificationResultDto resultDto = VerificationResultDto.invalidTask("Hello World");

        performValidate(inputDto, resultDto);
    }

    @Test
    void shouldReturnInvalidVerificationResultWhenWrongInput() throws Exception {
        VerificationInputDto inputDto = createTestInputDto("app/integration/wrong-input-parse-input");
        VerificationResultDto resultDto = VerificationResultDto.invalidInput("Problem podczas parsowania: } [4:0]", 4);

        performValidate(inputDto, resultDto);
    }

    @Test
    void shouldReturnInvalidVerificationResultWhenObjectNotExists() throws Exception {
        VerificationInputDto inputDto = createTestInputDto("app/integration/invalid-object-not-exists");
        Exception exception = new Exception("Problem podczas rozwiązywania: Obiekt obj nie istnieje");
        VerificationResultDto resultDto = VerificationResultDto.invalid(exception);

        performValidate(inputDto, resultDto);
    }

    @Test
    void shouldReturnInvalidVerificationResultWhenOWrongLiteralType() throws Exception {
        VerificationInputDto inputDto = createTestInputDto("app/integration/invalid-wrong-literal-type");
        Exception exception = new Exception("Problem podczas rozwiązywania: Wyrażenie 'S' nie jest typu String");
        VerificationResultDto resultDto = VerificationResultDto.invalid(exception);

        performValidate(inputDto, resultDto);
    }
}