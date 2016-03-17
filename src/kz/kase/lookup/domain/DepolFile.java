package kz.kase.lookup.domain;

import java.util.Date;

/**
 * Created by MM on 17.03.2016.
 */
public class DepolFile {
    //    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String CONVERTED = "Converted";

    private final String filename;
    private final String date;
    private final String time;
    private final Date datetime = null;

    private String dealId;
    private String refNum;
    private String codeTo;
    private boolean isConverted;
    private boolean isInEtrDir;
    private String responseFile = null;
    private String sentTimeFile;

    public DepolFile(String filename, String date, String time) {
        this.filename = filename;
        this.date = date;
        this.time = time;
//        datetime = df.parse(date+" "+time);
    }

    public String getFilename() {
        return filename;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Date getDatetime() {
        return datetime;
    }

    public boolean isConverted() {
        return isConverted;
    }

    public boolean isInEtrDir() {
        return isInEtrDir;
    }

    public String getResponseFile() {
        return responseFile;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public void setCodeTo(String codeTo) {
        this.codeTo = codeTo;
    }

    public void setIsConverted(boolean isConverted) {
        this.isConverted = isConverted;
    }

    public void setIsSentAccordingToEtrDir(boolean isInEtrDir) {
        this.isInEtrDir = isInEtrDir;
    }

    public void setResponseFile(String responseFile) {
        this.responseFile = responseFile;
    }
}
