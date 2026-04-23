package com.gresham.bulk.upload.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class Loader {

    /*public List<String> run(String[] command,boolean ignoreConsoleFirstLine ) {
        List<String> lines = new LinkedList<>();
        System.out.println("COMMAND IS :");
        System.out.println(Arrays.toString(command));
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            
            processBuilder.redirectErrorStream(true);

            processBuilder.environment().put("USE_GKE_GCLOUD_AUTH_PLUGIN", "True");
            Map<String, String> env = processBuilder.environment();
            String currentPath = env.get("PATH");
            env.put("PATH", currentPath + ":/Users/anandnagvanshi/gcloud/google-cloud-sdk/bin");
            processBuilder.redirectErrorStream(true);
            
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                    lines.add(line);
            }
            if (ignoreConsoleFirstLine) {
                lines.remove(0);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
*/

    public List<String> run(String[] command, boolean ignoreConsoleFirstLine) {
        List<String> lines = new LinkedList<>();

        System.out.println("COMMAND IS:");
        System.out.println(Arrays.toString(command));

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            Map<String, String> env = processBuilder.environment();

            // Ensure plugin is used
            env.put("USE_GKE_GCLOUD_AUTH_PLUGIN", "True");

            // Fix PATH (add directory, not binary)
            String currentPath = env.getOrDefault("PATH", "");
            env.put("PATH", currentPath + ":/Users/anandnagvanshi/gcloud/google-cloud-sdk/bin");

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            process.waitFor(); // good practice

            if (ignoreConsoleFirstLine && !lines.isEmpty()) {
                lines.remove(0);
            }

        } catch (Exception e) {
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

