package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 文件上传工具类
 * <p>
 * 用于处理候选人照片的上传
 */
public class FileUploadUtil {

    // 允许上传的文件扩展名
    private static final Set<String> ALLOWED_FILE_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif"
    ));

    // 最大文件大小 (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 保存上传的文件
     *
     * @param fileName    文件名
     * @param fileContent 文件内容
     * @param uploadDir   上传目录
     * @return 保存的文件路径，如果上传失败则返回null
     */
    public static String saveFile(String fileName, byte[] fileContent, String uploadDir) throws IOException {
        // 首先检查是否选择了文件
        if (fileName == null || fileName.isEmpty()) {
            return null; // 没有选择文件
        }

        // 检查文件大小
        if (fileContent.length > MAX_FILE_SIZE) {
            throw new IOException("文件大小超过限制（最大5MB）");
        }

        // 检查文件扩展名
        String fileExtension = getFileExtension(fileName);
        if (fileExtension == null || !ALLOWED_FILE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IOException("不支持的文件类型，仅支持JPG、PNG、GIF格式");
        }

        // 生成唯一的文件名，统一使用小写扩展名
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension.toLowerCase();

        // 创建上传目录
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.write(filePath, fileContent);

        return "img/" + uniqueFileName;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名
     */
    private static String getFileExtension(String fileName) {
        if (fileName == null) return null;
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex);
        }
        return null;
    }
}