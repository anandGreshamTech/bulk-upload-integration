package com.gresham.bulk.upload.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.awaitility.Awaitility.await;

@Service
public class ResourceReader {


    public static boolean isFileCreated(String commsOut, String rejectFile) {
        String[] testFilePresent = {
                "/usr/local/bin/kubectl", "exec", "-n", "coil",
                commsOut, "--", "sh", "-c", "test -f ~/output/" + rejectFile + " && echo File exists"
        };
        try {
            await().atMost(300, TimeUnit.SECONDS)
                    .pollDelay(10, TimeUnit.SECONDS)
                    .until(() ->
                            new Loader().run(testFilePresent, true).get(0).equalsIgnoreCase("File exists")

                    );
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public void cleanUp(List<Path> files, String pattern) {
        files.stream().forEach(file -> {
            if (file.getFileName().toString().contains(pattern)) {
                try {
                    Files.delete(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public Path createTestFile(Path scenarioFile) {
        Path target = Path.of(scenarioFile.getParent().toString().concat("/" + createFileName("SMOKESORA", "AccountOpen")));
        try {
            Files.copy(scenarioFile, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    public Path createTestFile(Path scenarioFile, String authlink,String type) {
        String fileName = scenarioFile.getFileName().toString();
        String hyphen = "-";
        if(fileName.contains(hyphen)){
            //I am going to pass authlink for negative test case concatenated with '_'
            authlink=fileName.substring(fileName.indexOf(hyphen)+1,fileName.length()-fileName.indexOf(hyphen));
        }
        Path target = Path.of(scenarioFile.getParent().toString().concat("/" + createFileName(authlink, type)));
        try {
            Files.copy(scenarioFile, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }
    @NotNull
    public Path getInputFile(List<Path> files, String patter) {
        return files.stream().filter(file -> file.getFileName().toString().contains(patter)).collect(toList()).get(0);
    }


    
    public String getResourcePath(String resourceFile) {
        URL file = this.getClass().getResource(resourceFile);
        return file.getPath();
    }

    public List<Path> getFiles(Path currentPath) {
        List<Path> files = null;
        try {
            files = Files.list(currentPath).filter(Files::isRegularFile).collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    public String getCurrentDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        return dateTimeFormatter.format(now);
    }

    public String createFileName(String customer, String type) {
        return String.format("%s_%s_%s.csv",  type,customer, getCurrentDate());
    }
    public List<Path> getDirs(String basePath) {
        List<Path> dirs = new LinkedList<>();
        try (Stream<Path> paths = Files.list(Paths.get(basePath))) {
            dirs= paths.filter(Files::isDirectory)
                    .collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dirs;
    }

}
