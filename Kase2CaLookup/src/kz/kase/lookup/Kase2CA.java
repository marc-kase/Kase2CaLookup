package kz.kase.lookup;


import kz.kase.lookup.domain.DepoFile;
import kz.kase.lookup.util.LinuxCommand;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Kase2CA {
    public static final String REF_KASE_REQUEST = ":20:";
    public static final String REF_SDEPO_RESP = ":21:";
    public static final String DEAL_NUMBER = ":35E:";

    public static final String PROPERTIES = "from-kase.properties";
    public static final String DEPO_OUT_DIR = "depo.out.dir";
    public static final String CONVERTER_LOG = "converter.log";
    public static final String ETRANS_INBOX = "etrans.inbox";
    public static final String ETRANS_OUTBOX = "etrans.outbox";
    public static final String EXPORT_FILE = "export-to";
    public static final String CA_LOGIN = "ca.login";
    public static final String DATE = "date";

    public List<DepoFile> getDepoFiles(String directory) throws IOException {
        String filesList = LinuxCommand
                .run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                        + "| grep -i '' | rev | cut -d ' ' -f1-3 | rev", "\n");
        String[] fs = filesList.split("\n");
//        List<String> fls = Arrays.asList(fs/*, new String[fs.length]*/);
//        fls.remove(0);

        List<DepoFile> depoFiles = new ArrayList<>();
        String[] fline;
        for (int i = 1; i < fs.length; i++) {
            fline = fs[i].split(" ");
            depoFiles.add(new DepoFile(fline[2], fline[0], fline[1]));
        }
        return depoFiles;
    }

    public String getFileModificationTime(String directory, String filename, String grepRange) throws IOException {
        return LinuxCommand
                .run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                        + "| grep -i '" + filename + "' | sed 's/     / /g' | cut -d ' ' " + grepRange, "");
    }

    public String getFileContent(String file, String pattern) throws IOException {
        return LinuxCommand.run("grep -i '" + pattern + "' " + file, "");
    }

    public String getFilenameByPattern(String file, String pattern, String grepOption) throws IOException {
        return LinuxCommand.run("grep " + grepOption + " '" + pattern + "' " + file + " | cut -d ':' -f1", "");
    }

    public void checkEtransferOutDir(String directory, DepoFile depoFile) throws IOException {
        String f = LinuxCommand.run("ls -l --time-style=+'%Y-%m-%d %H:%M:%S' " + directory
                + "| grep -i '" + depoFile + "' | cut -d ' ' -f8-10", "");
        if (!f.isEmpty()) depoFile.setIsSentAccordingToEtrDir(true);
        else depoFile.setIsSentAccordingToEtrDir(false);
    }

    public boolean checkConverterData(String converterFile, DepoFile depoFile) {
        try {
            String res = LinuxCommand.run("grep -i -A4 '" + depoFile.getFilename() + "' " +
                    converterFile + " | grep -i 'move to'", "");
//            System.out.println("grep -i -A4 '" + depoFile.getFilename() + "' " + converterFile + " | grep -i 'move to'");
            if (res.contains(DepoFile.CONVERTED)) return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        Reader p = new FileReader(PROPERTIES);
        props.load(p);

/*        final String depoDir = "/home/mark/Documents/KACD/data/TS2111/depo/sent";
        final String converterFile = "/home/mark/Documents/KACD/data/converter/logs/converter.log";
        final String etrOutDir = "/home/mark/Documents/KACD/data/eTransfer_kz/SKASE000/sent";
        final String etrInDir = "/home/mark/Documents/KACD/data/eTransfer_kz/SKASE000/in";
        final String exportFile = "/home/mark/Documents/KACD/data/export.txt";
        final String contrAgent = "SDEPO001";
        final String date = "2016-03-17";*/

        final String depoDir = props.getProperty(DEPO_OUT_DIR);
        final String converterFile = props.getProperty(CONVERTER_LOG);
        final String etrOutDir = props.getProperty(ETRANS_OUTBOX);
        final String etrInDir = props.getProperty(ETRANS_INBOX);
        final String exportFile = props.getProperty(EXPORT_FILE);
        final String contrAgent = props.getProperty(CA_LOGIN);
        final String date = props.getProperty(DATE);

        System.out.println("Started");

        String etrOutDirOnDay = etrOutDir + "/" + date + "/" + contrAgent;
        String etrInDirOnDay = etrInDir + "/" + date + "/" + contrAgent;

        Kase2CA app = new Kase2CA();

        List<DepoFile> depoFileList = app.getDepoFiles(depoDir);

        FileWriter writer = new FileWriter(exportFile);

        for (DepoFile file : depoFileList) {
            file.setRefNum(app.getFileContent(depoDir + "/" + file.getFilename(), REF_KASE_REQUEST).replace(REF_KASE_REQUEST, ""));
            file.setDealId(app.getFileContent(depoDir + "/" + file.getFilename(), DEAL_NUMBER).replace(DEAL_NUMBER, ""));
//            file.setRefNum(app.getFileContent(depoDir + "/" + file.getFilename(), ":REF:"));
            file.setIsConverted(app.checkConverterData(converterFile, file));
            String sentTimeFilesystem = app.getFileModificationTime(etrOutDirOnDay, file.getFilename(), "-f8,9");
            if (sentTimeFilesystem.isEmpty()) {
                file.setIsSentAccordingToEtrDir(false);
            } else {
                file.setIsSentAccordingToEtrDir(true);
                file.setSentTimeAccordingToEtrDir(sentTimeFilesystem);
            }

            String respFile = app.getFilenameByPattern(etrInDirOnDay + "/*.*", REF_SDEPO_RESP + file.getRefNum(), "-w");
            if (!respFile.isEmpty()) {
                Path f = Paths.get(respFile);
                file.setResponseFile(f.getFileName().toString());
                file.setResponseTime(app.getFileModificationTime(etrInDirOnDay, f.getFileName().toString(), "-f6,7"));
            }

//            System.out.println(file.toString());
//            System.out.println("---------------------------------------------------------");
            writer.write(file.toString() + "\n---------------------------------------------------\n");
        }

        writer.close();

        System.out.println("Stopped");
    }
}
