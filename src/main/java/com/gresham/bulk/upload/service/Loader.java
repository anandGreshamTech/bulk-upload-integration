package com.gresham.bulk.upload.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class Loader {

    public List<String> run(String[] command,boolean firstLine ) {
        List<String> lines = new LinkedList<>();
        Arrays.stream(command).forEach(part->{
            System.out.print(part+" ");
        });
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    lines.add(line);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    public static void runTimeExample(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command); // Example command for Mac

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor(); // Wait for the process to complete
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

