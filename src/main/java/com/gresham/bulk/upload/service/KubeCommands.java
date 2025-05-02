package com.gresham.bulk.upload.service;

import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@Service
public class KubeCommands {
    String[] commsOutPodName = {
            "sh", "-c", "/usr/local/bin/kubectl get pods -n coil -o custom-columns=NAME:.metadata.name | grep comms-out"
    };
    String kubectlDir = "/usr/local/bin/kubectl";

    public String[] getCommsOutPodCommand(){
        return commsOutPodName;
    }
    
    public String[] getCopyTestFile(Path file){
        return new String[]{
                kubectlDir, "cp", "-n", "coil", file.toString(),
                "comms-in-0:data/fileactive/bulkupload/waiting/".concat(file.getFileName().toString()), "-c", "comm-anz-in"};
        
    }
    
    public String[] readFileFromConsole(String podName,String fileName){
       return new String[] {
               kubectlDir, "exec", "-n", "coil", "-i", "-t",
                podName,
                "-c", "comm-anz-out",
                "--", "sh", "-c", "more ~/output/".concat(fileName)
        };
    }


    public boolean isFileCreated(String commsOut, String file) {
        String[] testFilePresent = {
                kubectlDir, "exec", "-n", "coil",
                commsOut, "--", "sh", "-c", "test -f ~/output/" + file + " && echo File exists"
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

}
