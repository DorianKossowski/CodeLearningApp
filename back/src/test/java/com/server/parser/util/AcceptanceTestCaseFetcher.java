package com.server.parser.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcceptanceTestCaseFetcher {
    private static final Pattern TEST_CASE_PATTERN = Pattern.compile("### TASK ###(.*)### INPUT ###(.*)",
            Pattern.DOTALL);

    public static AcceptanceTestCaseModel fetchModel(String testCase) {
        Matcher matcher = TEST_CASE_PATTERN.matcher(testCase);
        matcher.find();
        return new AcceptanceTestCaseModel(matcher.group(1), matcher.group(2));
    }
}