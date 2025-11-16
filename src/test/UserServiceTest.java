package test;

import bean.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.UserService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * UserService测试类
 * 用于测试用户相关的业务逻辑
 * 采用"先增后删"的方式，确保不污染数据库
 */
public class UserServiceTest {

    private static final int TEST_USER_ID = 888888;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_AGE = "25";
    private static final String TEST_SEX = "M";
    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService();
    }

    @After
    public void tearDown() {
        // 清理测试数据
        try {
            // 删除测试用户（如果存在）
            userService.deleteUser(TEST_USER_ID);
        } catch (Exception e) {
            // 忽略清理过程中的异常
        }
        userService = null;
    }

    @Test
    public void testRegisterAndDeleteUser() {
        try {
            // 确保测试用户不存在
            userService.deleteUser(TEST_USER_ID);

            // 测试注册成功的情况
            boolean result = userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);
            assertTrue("用户注册应该成功", result);

            // 验证用户是否真的注册成功
            User user = userService.login(TEST_USER_ID, TEST_PASSWORD);
            assertNotNull("注册后的用户应该能够登录", user);
            assertEquals("用户ID应该匹配", Integer.valueOf(TEST_USER_ID), user.getUserId());
            assertEquals("用户名应该匹配", TEST_USERNAME, user.getUserName());

            // 清理测试数据
            boolean deleteResult = userService.deleteUser(TEST_USER_ID);
            assertTrue("应该能够删除测试用户", deleteResult);
        } catch (IOException e) {
            fail("注册过程不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterUserExists() {
        try {
            // 确保测试用户不存在
            userService.deleteUser(TEST_USER_ID);

            // 先注册一个用户
            userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);

            // 再次尝试注册相同ID的用户，应该失败
            boolean result = userService.register(TEST_USER_ID, "anotheruser", "anotherpassword", "30", "F");
            assertFalse("已存在的用户注册应该失败", result);

            // 清理测试数据
            userService.deleteUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("注册过程不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testLoginSuccess() {
        try {
            // 确保测试用户不存在
            userService.deleteUser(TEST_USER_ID);

            // 先注册一个用户
            userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);

            // 测试登录成功的情况
            User user = userService.login(TEST_USER_ID, TEST_PASSWORD);
            assertNotNull("登录应该成功，返回用户对象", user);
            assertEquals("用户ID应该匹配", Integer.valueOf(TEST_USER_ID), user.getUserId());
            assertEquals("用户名应该匹配", TEST_USERNAME, user.getUserName());

            // 清理测试数据
            userService.deleteUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("登录过程不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testLoginFailure() {
        try {
            // 确保测试用户不存在
            userService.deleteUser(TEST_USER_ID);

            // 先注册一个用户
            userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);

            // 测试登录失败的情况（错误的密码）
            User user = userService.login(TEST_USER_ID, "wrongpassword");
            assertNull("使用错误密码登录应该返回null", user);

            // 测试登录失败的情况（不存在的用户）
            User user2 = userService.login(999999, TEST_PASSWORD);
            assertNull("使用不存在的用户ID登录应该返回null", user2);

            // 清理测试数据
            userService.deleteUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("登录过程不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllUsers() {
        try {
            // 测试获取所有用户
            List<User> users = userService.getAllUsers();
            assertNotNull("用户列表不应该为null", users);
            // 不对列表是否为空做硬性断言，因为数据库可能确实没有数据
        } catch (IOException e) {
            fail("获取用户列表不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testGetUserById() {
        try {
            // 确保测试用户不存在
            userService.deleteUser(TEST_USER_ID);

            // 先注册一个用户
            userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);

            // 测试根据ID获取用户
            User user = userService.getUserById(TEST_USER_ID);
            assertNotNull("应该能够根据ID获取用户", user);
            assertEquals("用户ID应该匹配", Integer.valueOf(TEST_USER_ID), user.getUserId());
            assertEquals("用户名应该匹配", TEST_USERNAME, user.getUserName());

            // 清理测试数据
            userService.deleteUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("根据ID获取用户不应该抛出异常: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateUser() {
        try {
            // 确保测试用户不存在
            userService.deleteUser(TEST_USER_ID);

            // 先注册一个用户
            userService.register(TEST_USER_ID, TEST_USERNAME, TEST_PASSWORD, TEST_AGE, TEST_SEX);

            // 获取用户并更新信息
            User user = userService.getUserById(TEST_USER_ID);
            user.setUserName("updateduser");
            user.setUserAge(30);

            // 测试更新用户
            boolean result = userService.updateUser(user);
            assertTrue("更新用户应该成功", result);

            // 验证更新是否成功
            User updatedUser = userService.getUserById(TEST_USER_ID);
            assertEquals("用户名应该已更新", "updateduser", updatedUser.getUserName());
            assertEquals("用户年龄应该已更新", Integer.valueOf(30), updatedUser.getUserAge());

            // 清理测试数据
            userService.deleteUser(TEST_USER_ID);
        } catch (IOException e) {
            fail("更新用户不应该抛出异常: " + e.getMessage());
        }
    }
}