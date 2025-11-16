package bean;

/**
 * 用户实体类
 * 用于表示系统中的用户信息
 */
public class User {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户年龄
     */
    private Integer userAge;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 注册时间
     */
    private java.sql.Timestamp registerTime;

    /**
     * 无参构造函数
     */
    public User() {
        super();
    }

    /**
     * 有参构造函数
     *
     * @param userId       用户ID
     * @param userName     用户名
     * @param userPassword 用户密码
     * @param userAge      用户年龄
     * @param userSex      用户性别
     * @param userRole     用户角色
     * @param registerTime 注册时间
     */
    public User(Integer userId, String userName, String userPassword, Integer userAge, String userSex, String userRole, java.sql.Timestamp registerTime) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userAge = userAge;
        this.userSex = userSex;
        this.userRole = userRole;
        this.registerTime = registerTime;
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取用户密码
     *
     * @return 用户密码
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * 设置用户密码
     *
     * @param userPassword 用户密码
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    /**
     * 获取用户年龄
     *
     * @return 用户年龄
     */
    public Integer getUserAge() {
        return userAge;
    }

    /**
     * 设置用户年龄
     *
     * @param userAge 用户年龄
     */
    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    /**
     * 获取用户性别
     *
     * @return 用户性别
     */
    public String getUserSex() {
        return userSex;
    }

    /**
     * 设置用户性别
     *
     * @param userSex 用户性别
     */
    public void setUserSex(String userSex) {
        this.userSex = userSex == null ? null : userSex.trim();
    }

    /**
     * 获取用户角色
     *
     * @return 用户角色
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * 设置用户角色
     *
     * @param userRole 用户角色
     */
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    /**
     * 获取注册时间
     *
     * @return 注册时间
     */
    public java.sql.Timestamp getRegisterTime() {
        return registerTime;
    }

    /**
     * 设置注册时间
     *
     * @param registerTime 注册时间
     */
    public void setRegisterTime(java.sql.Timestamp registerTime) {
        this.registerTime = registerTime;
    }
}