package model;

public class FileStat {
    private int fileCount = 0;
    private int folderCount = 0;

    public FileStat() {}

    public void addFile() { fileCount++; }
    public void addFolder() { folderCount++; }

    public int getFileCount() { return fileCount; }
    public int getFolderCount() { return folderCount; }
}
