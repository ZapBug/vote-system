package dao;

import bean.User;

import java.io.InputStream;
import java.util.List;

/**
 * 用户数据访问接口
 * 定义了对用户表的各种操作方法
 */
public interface UserDao {

    /**
     * 检查用户是否存在
     *
     * @param userId 用户ID
     * @return 用户是否存在
     */
    boolean isExist(Integer userId);

    /**
     * 添加用户
     *
     * @param userId       用户ID
     * @param userName     用户名
     * @param userPassword 用户密码
     * @param userAge      用户年龄
     * @param userSex      用户性别
     * @return 是否添加成功
     */
    boolean addUser(Integer userId, String userName, String userPassword, String userAge, String userSex);

    /**
     * 根据用户ID和密码获取用户信息
     *
     * @param userId       用户ID
     * @param userPassword 用户密码
     * @param in           输入流
     * @return 用户对象
     */
    User getUserByUP(Integer userId, String userPassword, InputStream in);

    /**
     * 获取所有用户
     *
     * @return 用户列表
     */
    List<User> getAllUsers();

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户对象
     */
    User getUserById(Integer userId);

    /**
     * 更新用户信息
     *
     * @param user 用户对象
     * @return 是否更新成功
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUser(Integer userId);
}
