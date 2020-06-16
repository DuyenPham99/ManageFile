package vn.hust.edu.managefile.model;

public class InternalStorageFilesModel {
    private String fileName;
    private String filePath;

    public InternalStorageFilesModel(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
    public InternalStorageFilesModel(){

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
}
