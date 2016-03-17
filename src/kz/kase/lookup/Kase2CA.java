package kz.kase.lookup;


import kz.kase.lookup.domain.DepolFile;
import kz.kase.lookup.util.LinuxCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kase2CA {
    public List<DepolFile> getDepoFiles(String directory) throws IOException {
        String filesList = LinuxCommand
                .run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                        + "| grep -i '' | cut -d ' ' -f8-10", "\n");
        String[] fs = filesList.split("\n");
        List<String> fls = Arrays.asList(new String[fs.length]);

        List<DepolFile> depolFiles = new ArrayList<>();
        String[] fline = {};
        for (String f : fls) {
            fline = f.split(" ");
            depolFiles.add(new DepolFile(fline[2], fline[0], fline[1]));
        }
        return depolFiles;
    }

    public String getFileContent(String file, String pattern) throws IOException {
        return LinuxCommand.run("grep -i '" + pattern + "' " + file, "");
    }

    public void checkEtransferOutDir(String directory, DepolFile depolFile) throws IOException {
        String f = LinuxCommand.run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                + "| grep -i '" + depolFile + "' | cut -d ' ' -f8-10", "");
        if (!f.isEmpty()) depolFile.setIsSentAccordingToEtrDir(true);
        else depolFile.setIsSentAccordingToEtrDir(false);
    }

    public void checkConverterData(String converterFile, DepolFile depolFile) throws IOException {
        String res = LinuxCommand.run("grep -i -A3 '" + depolFile.getFilename() + "' " +
                converterFile + " | grep -i 'status'", "");
        boolean status = false;
        if (res.contains(DepolFile.CONVERTED)) status = true;

        depolFile.setIsConverted(status);
    }
}
