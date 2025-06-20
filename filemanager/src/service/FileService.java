package service;

import model.FileStat;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    // 1. 递归查找指定目录中的文件
    public List<File> searchFiles(File dir, String filename) {
        List<File> results = new ArrayList<>();
        if (dir == null || !dir.isDirectory()) return results;
        File[] files = dir.listFiles();
        if (files == null) return results;
        for (File file : files) {
            if (file.getName().equalsIgnoreCase(filename)) {
                results.add(file);
            }
            if (file.isDirectory()) {
                results.addAll(searchFiles(file, filename));
            }
        }
        return results;
    }

    // 2. 文件复制
    public boolean copyFile(File source, File targetDir) throws IOException {
        if (source == null || targetDir == null || !targetDir.isDirectory()) return false;
        File dest = new File(targetDir, source.getName());
        Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return true;
    }

    // 3. 文本预览（前100行）
    public List<String> previewTextFile(File file, int maxLines) throws IOException {
        List<String> lines = new ArrayList<>();
        if (file == null || !file.isFile()) return lines;
        String name = file.getName().toLowerCase();
        if (!(name.endsWith(".txt") || name.endsWith(".java") || name.endsWith(".ini") || name.endsWith(".bat"))) {
            return lines;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int cnt = 0;
            while ((line = br.readLine()) != null && cnt < maxLines) {
                lines.add(line);
                cnt++;
            }
        }
        return lines;
    }

    // 4. 重命名文件
    public boolean renameFile(File file, String newName) {
        if (file == null || newName == null || newName.trim().isEmpty()) return false;
        File newFile = new File(file.getParent(), newName);
        return file.renameTo(newFile);
    }

    // 5. 统计文件夹（文件&文件夹数量）
    public FileStat statFolder(File dir) {
        FileStat stat = new FileStat();
        countFilesRecursive(dir, stat);
        return stat;
    }

    private void countFilesRecursive(File dir, FileStat stat) {
        if (dir == null || !dir.isDirectory()) return;
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isFile()) stat.addFile();
            else if (file.isDirectory()) {
                stat.addFolder();
                countFilesRecursive(file, stat);
            }
        }
    }
}