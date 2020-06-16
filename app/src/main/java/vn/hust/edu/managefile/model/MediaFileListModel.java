package vn.hust.edu.managefile.model;

public class MediaFileListModel {
    private String fileName, filePath,fileSize,fileCreatedTime;

    public MediaFileListModel(String fileName, String filePath, String fileSize, String fileCreatedTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileCreatedTime = fileCreatedTime;
    }

    public MediaFileListModel(){

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileCreatedTime() {
        return fileCreatedTime;
    }

    public void setFileCreatedTime(String fileCreatedTime) {
        this.fileCreatedTime = fileCreatedTime;
    }
}
