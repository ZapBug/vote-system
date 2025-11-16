package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * FileUploadUtil测试类
 * 用于测试文件上传工具类的功能
 */
public class FileUploadUtilTest {

    private static final String TEST_UPLOAD_DIR = "test_upload";
    private static final String TEST_IMAGE_CONTENT = "test image content";
    private static final byte[] TEST_IMAGE_BYTES = TEST_IMAGE_CONTENT.getBytes();

    @Before
    public void setUp() {
        // 创建测试上传目录
        File uploadDir = new File(TEST_UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    @After
    public void tearDown() {
        // 清理测试文件和目录
        File uploadDir = new File(TEST_UPLOAD_DIR);
        if (uploadDir.exists()) {
            // 删除目录中的所有文件
            File[] files = uploadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            // 删除目录
            uploadDir.delete();
        }
    }

    @Test
    public void testSaveFileWithValidJpg() throws IOException {
        String fileName = "test.jpg";
        String result = FileUploadUtil.saveFile(fileName, TEST_IMAGE_BYTES, TEST_UPLOAD_DIR);

        // 验证返回的路径不为空
        assertNotNull("返回的文件路径不应该为null", result);
        assertTrue("返回的文件路径应该以img/开头", result.startsWith("img/"));

        // 验证文件确实被保存
        String savedFileName = result.substring(4); // 去掉"img/"前缀
        File savedFile = new File(TEST_UPLOAD_DIR, savedFileName);
        assertTrue("文件应该被保存到指定目录", savedFile.exists());

        // 验证文件内容
        String content = new String(Files.readAllBytes(savedFile.toPath()));
        assertEquals("文件内容应该匹配", TEST_IMAGE_CONTENT, content);
    }

    @Test
    public void testSaveFileWithValidPng() throws IOException {
        String fileName = "test.png";
        String result = FileUploadUtil.saveFile(fileName, TEST_IMAGE_BYTES, TEST_UPLOAD_DIR);

        // 验证返回的路径不为空
        assertNotNull("返回的文件路径不应该为null", result);
        assertTrue("返回的文件路径应该以img/开头", result.startsWith("img/"));
    }

    @Test
    public void testSaveFileWithValidGif() throws IOException {
        String fileName = "test.gif";
        String result = FileUploadUtil.saveFile(fileName, TEST_IMAGE_BYTES, TEST_UPLOAD_DIR);

        // 验证返回的路径不为空
        assertNotNull("返回的文件路径不应该为null", result);
        assertTrue("返回的文件路径应该以img/开头", result.startsWith("img/"));
    }

    @Test
    public void testSaveFileWithValidJpgUppercase() throws IOException {
        String fileName = "test.JPG";
        String result = FileUploadUtil.saveFile(fileName, TEST_IMAGE_BYTES, TEST_UPLOAD_DIR);

        // 验证返回的路径不为空
        assertNotNull("返回的文件路径不应该为null", result);
        assertTrue("返回的文件路径应该以img/开头", result.startsWith("img/"));

        // 验证文件扩展名是小写的
        assertTrue("文件扩展名应该是小写的", result.endsWith(".jpg"));
    }

    @Test
    public void testSaveFileWithEmptyFileName() throws IOException {
        String fileName = "";
        String result = FileUploadUtil.saveFile(fileName, TEST_IMAGE_BYTES, TEST_UPLOAD_DIR);

        // 验证返回null
        assertNull("空文件名应该返回null", result);
    }

    @Test
    public void testSaveFileWithNullFileName() throws IOException {
        String result = FileUploadUtil.saveFile(null, TEST_IMAGE_BYTES, TEST_UPLOAD_DIR);

        // 验证返回null
        assertNull("null文件名应该返回null", result);
    }

    @Test
    public void testSaveFileWithInvalidFileType() throws IOException {
        String fileName = "test.txt";
        try {
            FileUploadUtil.saveFile(fileName, TEST_IMAGE_BYTES, TEST_UPLOAD_DIR);
            fail("应该抛出IOException异常");
        } catch (IOException e) {
            // 验证异常消息
            assertTrue("异常消息应该包含文件类型错误信息", e.getMessage().contains("不支持的文件类型"));
        }
    }

    @Test
    public void testSaveFileWithLargeFileSize() throws IOException {
        // 创建一个大于5MB的字节数组
        byte[] largeFileBytes = new byte[6 * 1024 * 1024]; // 6MB
        String fileName = "test.jpg";
        try {
            FileUploadUtil.saveFile(fileName, largeFileBytes, TEST_UPLOAD_DIR);
            fail("应该抛出IOException异常");
        } catch (IOException e) {
            // 验证异常消息
            assertTrue("异常消息应该包含文件大小错误信息", e.getMessage().contains("文件大小超过限制"));
        }
    }

    @Test
    public void testGetFileExtension() throws Exception {
        // 使用反射访问私有方法
        java.lang.reflect.Method method = FileUploadUtil.class.getDeclaredMethod("getFileExtension", String.class);
        method.setAccessible(true);

        // 测试各种文件扩展名
        assertEquals("应该返回.jpg", ".jpg", method.invoke(null, (Object) "test.jpg"));
        assertEquals("应该返回.png", ".png", method.invoke(null, (Object) "test.png"));
        assertEquals("应该返回.gif", ".gif", method.invoke(null, (Object) "test.gif"));
        assertEquals("应该返回.JPG", ".JPG", method.invoke(null, (Object) "test.JPG"));
        assertNull("应该返回null", method.invoke(null, (Object) "test"));
        assertNull("应该返回null", method.invoke(null, new Object[]{null}));
    }
}