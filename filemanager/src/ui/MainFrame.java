package ui;

import model.FileStat;
import service.FileService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    private final FileService fileService = new FileService();
    private File copiedFile = null;

    private JTextField dirField;
    private JTextField nameField;
    private JTextArea resultArea;
    private JTextArea previewArea;

    public MainFrame() {
        setTitle("简易文件资源管理器");
        setSize(850, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top panel
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("目录:"));
        dirField = new JTextField(20);
        searchPanel.add(dirField);
        searchPanel.add(new JLabel("文件名:"));
        nameField = new JTextField(10);
        searchPanel.add(nameField);
        JButton searchBtn = new JButton("查找文件");
        searchPanel.add(searchBtn);

        // Center area
        resultArea = new JTextArea(14, 60);
        resultArea.setEditable(false);
        JScrollPane resultScroll = new JScrollPane(resultArea);

        previewArea = new JTextArea(10, 60);
        previewArea.setEditable(false);
        JScrollPane previewScroll = new JScrollPane(previewArea);

        JPanel opPanel = new JPanel();
        JButton copyBtn = new JButton("拷贝");
        JButton pasteBtn = new JButton("粘贴");
        JButton previewBtn = new JButton("预览文本");
        JButton renameBtn = new JButton("重命名");
        JButton statBtn = new JButton("统计文件夹");
        opPanel.add(copyBtn);
        opPanel.add(pasteBtn);
        opPanel.add(previewBtn);
        opPanel.add(renameBtn);
        opPanel.add(statBtn);

        // Layout
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(resultScroll, BorderLayout.CENTER);
        add(previewScroll, BorderLayout.SOUTH);
        add(opPanel, BorderLayout.PAGE_END);

        // Actions
        searchBtn.addActionListener(e -> searchAction());
        copyBtn.addActionListener(e -> copyAction());
        pasteBtn.addActionListener(e -> pasteAction());
        previewBtn.addActionListener(e -> previewAction());
        renameBtn.addActionListener(e -> renameAction());
        statBtn.addActionListener(e -> statAction());
    }

    private void searchAction() {
        String dir = dirField.getText();
        String name = nameField.getText();
        if (dir.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入目录和文件名！");
            return;
        }
        File baseDir = new File(dir);
        List<File> results = fileService.searchFiles(baseDir, name);
        resultArea.setText("查找结果：\n");
        if (results.isEmpty()) {
            resultArea.append("未找到文件。\n");
        } else {
            results.forEach(f -> resultArea.append(f.getAbsolutePath() + "\n"));
        }
    }

    private void copyAction() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            copiedFile = fc.getSelectedFile();
            resultArea.append("已拷贝：" + copiedFile.getAbsolutePath() + "\n");
        }
    }

    private void pasteAction() {
        if (copiedFile == null) {
            JOptionPane.showMessageDialog(this, "请先拷贝一个文件！");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File targetDir = fc.getSelectedFile();
            try {
                fileService.copyFile(copiedFile, targetDir);
                resultArea.append("已粘贴到：" + targetDir.getAbsolutePath() + "\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "粘贴失败：" + e.getMessage());
            }
        }
    }

    private void previewAction() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                List<String> lines = fileService.previewTextFile(file, 100);
                previewArea.setText("");
                for (String l : lines) previewArea.append(l + "\n");
                if (lines.isEmpty()) previewArea.setText("不是支持的文本类文件或文件为空。");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "预览失败：" + e.getMessage());
            }
        }
    }

    private void renameAction() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String newName = JOptionPane.showInputDialog(this, "输入新文件名：", file.getName());
            if (newName != null && !newName.trim().isEmpty()) {
                boolean ok = fileService.renameFile(file, newName.trim());
                resultArea.append((ok ? "重命名成功：" : "重命名失败：") + file.getParent() + File.separator + newName + "\n");
            }
        }
    }

    private void statAction() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File dir = fc.getSelectedFile();
            FileStat stat = fileService.statFolder(dir);
            resultArea.append("文件夹 " + dir.getAbsolutePath() + " 内有文件数：" + stat.getFileCount()
                    + "，文件夹数：" + stat.getFolderCount() + "\n");
        }
    }
}