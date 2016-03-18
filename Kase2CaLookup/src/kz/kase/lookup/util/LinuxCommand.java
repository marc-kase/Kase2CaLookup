package kz.kase.lookup.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class LinuxCommand {
    public static String run(String command, String lineDelimiter) throws IOException {
        StringBuilder result = new StringBuilder();
        String[] cmd = {"/bin/bash", "-c", command};
        Process pb = Runtime.getRuntime().exec(cmd);

        String line;
        BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
        while ((line = input.readLine()) != null) {
            result.append(line).append(lineDelimiter);
        }
        input.close();
        return result.toString();
    }
}
