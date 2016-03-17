package kz.kase.lookup;


import kz.kase.lookup.domain.DepoFile;
import kz.kase.lookup.util.LinuxCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kase2CA {
    public List<DepoFile> getDepoFiles(String directory) throws IOException {
        String filesList = LinuxCommand
                .run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                        + "| grep -i '' | cut -d ' ' -f8-10", "\n");
        String[] fs = filesList.split("\n");
        List<String> fls = Arrays.asList(new String[fs.length]);

        List<DepoFile> depoFiles = new ArrayList<>();
        String[] fline;
        for (String f : fls) {
            fline = f.split(" ");
            depoFiles.add(new DepoFile(fline[2], fline[0], fline[1]));
        }
        return depoFiles;
    }

    public String getFileModificationTime(String directory, String filename) throws IOException {
        return LinuxCommand
                .run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                        + "| grep -i '" + filename + "' | cut -d ' ' -f8,9", "");
    }

    public String getFileContent(String file, String pattern) throws IOException {
        return LinuxCommand.run("grep -i '" + pattern + "' " + file, "");
    }

    public String getFilenameByPattern(String file, String pattern) throws IOException {
        return LinuxCommand.run("grep -i '" + pattern + "' " + file, "");
    }

    public void checkEtransferOutDir(String directory, DepoFile depoFile) throws IOException {
        String f = LinuxCommand.run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                + "| grep -i '" + depoFile + "' | cut -d ' ' -f8-10", "");
        if (!f.isEmpty()) depoFile.setIsSentAccordingToEtrDir(true);
        else depoFile.setIsSentAccordingToEtrDir(false);
    }

    public boolean checkConverterData(String converterFile, DepoFile depoFile) {
        try {
            String res = LinuxCommand.run("grep -i -A3 '" + depoFile.getFilename() + "' " +
                    converterFile + " | grep -i 'status'", "");
            if (res.contains(DepoFile.CONVERTED)) return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        final String depoDir = "";
        final String converterFile = "";
        final String etrOutDir = "";
        final String etrInDir = "";

        Kase2CA app = new Kase2CA();

        List<DepoFile> depoFileList = app.getDepoFiles("");

        for (DepoFile file : depoFileList) {
            file.setCodeTo(app.getFileContent(depoDir + "/" + file.getFilename(), ":20:"));
            file.setDealId(app.getFileContent(depoDir + "/" + file.getFilename(), ":DL_:"));
            file.setRefNum(app.getFileContent(depoDir + "/" + file.getFilename(), ":REF:"));
            file.setIsConverted(app.checkConverterData(converterFile, file));
            String sentTimeFilesystem = app.getFileModificationTime(etrOutDir, file.getFilename());
            if (sentTimeFilesystem.isEmpty()) {
                file.setIsSentAccordingToEtrDir(false);
            } else {
                file.setIsSentAccordingToEtrDir(true);
                file.setSentTimeAccordingToEtrDir(sentTimeFilesystem);
            }
            file.setResponseFile(app.getFilenameByPattern(etrInDir + "/" + file.getFilename(), ":21:" + file.getCodeTo()));
            file.setResponseTime(app.getFileModificationTime(etrInDir, file.getFilename()));

            System.out.println(file.toString());
            System.out.println("---------------------------------------------------------");
        }

    }
}
