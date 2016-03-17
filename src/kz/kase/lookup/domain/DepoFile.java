package kz.kase.lookup.domain;

import java.util.Date;

/**
 * Created by MM on 17.03.2016.
 */
public class DepoFile {
    //    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String CONVERTED = "Converted";

    private final String filename;
    private final String date;
    private final String time;
    private final Date datetime = null;

    private String dealId = "";
    private String refNum = "";
    private String codeTo = "";
    private boolean isConverted = false;
    private boolean isSentAccordingToEtrDir=false;
    private String sentTimeAccordingToEtrDir="";
    private String responseFile = "";
    private String responseTime = "";

    public DepoFile(String filename, String date, String time) {
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

    public boolean isSentAccordingToEtrDir() {
        return isSentAccordingToEtrDir;
    }

    public String getResponseFile() {
        return responseFile;
    }

    public String getDealId() {
        return dealId;
    }

    public String getRefNum() {
        return refNum;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public String getCodeTo() {
        return codeTo;
    }

    public String getSentTimeAccordingToEtrDir() {
        return sentTimeAccordingToEtrDir;
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
        this.isSentAccordingToEtrDir = isInEtrDir;
    }

    public void setResponseFile(String responseFile) {
        this.responseFile = responseFile;
    }

    public void setSentTimeAccordingToEtrDir(String sentTimeAccordingToEtrDir) {
        this.sentTimeAccordingToEtrDir = sentTimeAccordingToEtrDir;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Filename:  ").append(filename).append("\n");
        sb.append("Created:   ").append(date).append(" ").append(time).append("\n");
        sb.append("Deal ID:   ").append(dealId).append("\n");
        sb.append("RefNum:    ").append(refNum).append("\n");
        sb.append("Code:      ").append(codeTo).append("\n");
        sb.append("Converted: ").append(isConverted).append("\n");
        sb.append("Found in Out Dir:   ").append(isSentAccordingToEtrDir).append("\n");
        sb.append("Created in Out Dir: ").append(sentTimeAccordingToEtrDir).append("\n");
        sb.append("Response file: ").append(responseFile).append("\n");
        sb.append("Response Time: ").append(responseTime).append("\n");
        return sb.toString();
    }
}
