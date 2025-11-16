package service;

import bean.User;
import dao.UserDaoIm;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 用户业务逻辑处理类
 * 提供用户注册和登录相关的业务操作方法
 */
public class UserService {

    /**
     * 用户注册方法
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param password 密码
     * @param age      年龄
     * @param sex      性别
     * @return 是否注册成功
     * @throws IOException 配置文件读取异常
     */
    public boolean register(Integer userId, String username, String password, String age, String sex) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);

        UserDaoIm userDao = new UserDaoIm(ssf);
        boolean isExist = userDao.isExist(userId);
        if (isExist) {
            in.close();
            return false;
        } else {
            userDao.addUser(userId, username, password, age, sex);
            in.close();
            return true;
        }
    }

    /**
     * 用户登录方法
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 用户对象，如果登录失败返回null
     * @throws IOException 配置文件读取异常
     */
    public User login(Integer userId, String password) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);

        return new UserDaoIm(ssf).getUserByUP(userId, password, in);
    }

    /**
     * 获取所有用户
     *
     * @return 用户列表
     * @throws IOException 配置文件读取异常
     */
    public List<User> getAllUsers() throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);

        return new UserDaoIm(ssf).getAllUsers();
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户对象
     * @throws IOException 配置文件读取异常
     */
    public User getUserById(Integer userId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);

        return new UserDaoIm(ssf).getUserById(userId);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户对象
     * @return 是否更新成功
     * @throws IOException 配置文件读取异常
     */
    public boolean updateUser(User user) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);

        return new UserDaoIm(ssf).updateUser(user);
    }

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 是否删除成功
     * @throws IOException 配置文件读取异常
     */
    public boolean deleteUser(Integer userId) throws IOException {
        InputStream in = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
        SqlSessionFactory ssf = ssfb.build(in);

        return new UserDaoIm(ssf).deleteUser(userId);
    }
}
