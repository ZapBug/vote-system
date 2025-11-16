package dao;

import bean.User;
import bean.UserExample;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class UserDaoIm implements UserDao {


    private final SqlSessionFactory ssf;


    public UserDaoIm(SqlSessionFactory ssf) {
        this.ssf = ssf;
    }

    public boolean isExist(Integer userId) {

        SqlSession session = ssf.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User u = mapper.selectByPrimaryKey(userId);

        //  User u=session.selectOne("UserMapper.selectByPrimaryKey", userId);
        return u != null;

    }

    public boolean addUser(Integer userId, String userName, String userPassword, String userAge, String userSex) {
        SqlSession session = ssf.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);

        // 输入验证和清理
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID必须是正整数");
        }

        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        if (userPassword == null || userPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        if (userAge == null || userAge.trim().isEmpty()) {
            throw new IllegalArgumentException("年龄不能为空");
        }

        if (userSex == null || userSex.trim().isEmpty()) {
            throw new IllegalArgumentException("性别不能为空");
        }

        // 数据验证
        int age;
        try {
            age = Integer.parseInt(userAge.trim());
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("年龄必须在0-150之间");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("年龄格式不正确");
        }

        // 清理输入数据
        userName = userName.trim();
        userPassword = userPassword.trim();

        // 验证用户名长度
        if (userName.length() < 3 || userName.length() > 50) {
            throw new IllegalArgumentException("用户名长度必须在3-50个字符之间");
        }

        // 验证密码长度
        if (userPassword.length() < 6 || userPassword.length() > 100) {
            throw new IllegalArgumentException("密码长度必须在6-100个字符之间");
        }

        // 将用户输入的性别值转换为数据库中定义的枚举值
        String sexEnumValue = "Other"; // 默认值
        String trimmedSex = userSex.trim();
        if ("男".equals(trimmedSex) || "M".equalsIgnoreCase(trimmedSex)) {
            sexEnumValue = "M";
        } else if ("女".equals(trimmedSex) || "F".equalsIgnoreCase(trimmedSex)) {
            sexEnumValue = "F";
        }

        User u = new User(userId, userName, userPassword, age, sexEnumValue, "user", new java.sql.Timestamp(System.currentTimeMillis()));
        mapper.insert(u);
        session.commit();
        session.close();
        return isExist(userId);
    }

    public User getUserByUP(Integer userId, String userPassword, InputStream in) {
        SqlSession session = ssf.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        UserExample example = new UserExample();
        example.createCriteria().andUserIdEqualTo(userId).andUserPasswordEqualTo(userPassword);
        List<User> userList = mapper.selectByExample(example);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.close();
        if (userList.isEmpty()) {
            return null;
        }
        return userList.get(0);
    }

    public List<User> getAllUsers() {
        SqlSession session = ssf.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        UserExample example = new UserExample();
        List<User> userList = mapper.selectByExample(example);
        session.close();
        return userList;
    }

    public User getUserById(Integer userId) {
        SqlSession session = ssf.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = mapper.selectByPrimaryKey(userId);
        session.close();
        return user;
    }

    public boolean updateUser(User user) {
        SqlSession session = ssf.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        int result = mapper.updateByPrimaryKey(user);
        session.commit();
        session.close();
        return result > 0;
    }

    public boolean deleteUser(Integer userId) {
        SqlSession session = ssf.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        int result = mapper.deleteByPrimaryKey(userId);
        session.commit();
        session.close();
        return result > 0;
    }
}
