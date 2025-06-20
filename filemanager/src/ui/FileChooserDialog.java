package ui;

import javax.swing.*;
import java.io.File;

/**
 * 可复用的文件/文件夹选择对话框。
 */
public class FileChooserDialog {
    /**
     * 选择文件
     */
    public static File chooseFile(JFrame parent, String title) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(title);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = fc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }

    /**
     * 选择文件夹
     */
    public static File chooseDirectory(JFrame parent, String title) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(title);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fc.showOpenDialog(parent);
        if (ret == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }
}