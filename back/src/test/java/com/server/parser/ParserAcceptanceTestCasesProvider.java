package com.server.parser;

import com.server.parser.java.JavaParserAcceptanceTest;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserAcceptanceTestCasesProvider {

    public static Map<String, String> getTestCases(String casesDirectoryPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(getCasesDirectoryUri(casesDirectoryPath)))) {
            return paths.filter(Files::isRegularFile)
                    .collect(Collectors.toMap(
                            path -> path.getFileName().toString(),
                            ParserAcceptanceTestCasesProvider::getTestInput,
                            (name1, name2) -> name1, TreeMap::new
                    ));
        } catch (IOException e) {
            throw new RuntimeException("Exception during getting test cases", e);
        }
    }

    public static String getSingleTestCase(String casePath) {
        return getTestInternal(new File(getCasesDirectoryUri(casePath)));
    }

    private static String getTestInternal(File testCase) {
        try {
            return FileUtils.readFileToString(testCase, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Exception during reading test input from: " + testCase.getAbsolutePath(), e);
        }
    }

    private static String getTestInput(Path path) {
        return getTestInternal(path.toFile());
    }

    private static URI getCasesDirectoryUri(String path) {
        try {
            return Objects.requireNonNull(JavaParserAcceptanceTest.class.getClassLoader().getResource(path)).toURI();
        } catch (Exception e) {
            throw new RuntimeException("Exception during reading test cases from: " + path, e);
        }
    }
}
