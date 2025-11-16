-- 设置客户端连接编码为 utf8mb4
SET NAMES utf8mb4;

-- 为防止重复执行脚本出错，先尝试删除已存在的表
-- 由于 vote_record 表有外键依赖，需要先删除它
DROP TABLE IF EXISTS `vote_record`;
DROP TABLE IF EXISTS `candidate`;
DROP TABLE IF EXISTS `user`;


-- =================================================================
-- 1. 用户表 (user)
-- =================================================================
CREATE TABLE `user`
(
    `user_id`       INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID，主键自增',
    `user_name`     VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名，唯一且不为空',
    `user_password` VARCHAR(255) NOT NULL COMMENT '用户密码',
    `user_age`      TINYINT UNSIGNED COMMENT '用户年龄，无符号小整数',
    `user_sex`      ENUM('M', 'F', 'Other') COMMENT '用户性别：M-男, F-女, Other-其他',
    `user_role`     ENUM('user', 'admin') DEFAULT 'user' COMMENT '用户角色，默认为普通用户',
    `register_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间，默认为当前时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admintest', 'admintest', 11, 'M', 'admin', '2019-11-16 17:03:35');
INSERT INTO `user` VALUES (2, 'usertest', 'usertest', 12, 'F', 'user', '2019-11-17 17:03:35');

-- =================================================================
-- 2. 候选人表 (candidate)
-- =================================================================
CREATE TABLE `candidate`
(
    `candidate_id`          INT PRIMARY KEY AUTO_INCREMENT COMMENT '候选人ID，主键自增',
    `candidate_name`        VARCHAR(100) NOT NULL COMMENT '候选人姓名',
    `candidate_description` TEXT COMMENT '候选人描述',
    `candidate_photo`       VARCHAR(255) COMMENT '候选人照片URL或路径',
    `vote_count`            INT DEFAULT 0 COMMENT '所得票数，默认为0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='候选人表';

-- 插入测试候选人数据（基于已有的候选人）
INSERT INTO candidate (candidate_id, candidate_name, candidate_description, candidate_photo, vote_count)
VALUES (1, '陈xx', '一号候选人', 'img/1号.jpg', 0),
       (2, '黄xx', '二号候选人', 'img/2号.jpg', 0),
       (3, '蓝xx', '三号候选人', 'img/3号.jpg', 0);

-- =================================================================
-- 3. 投票记录表 (vote_record)
-- =================================================================
CREATE TABLE `vote_record`
(
    `vote_id`      INT PRIMARY KEY AUTO_INCREMENT COMMENT '投票记录ID，主键自增',
    `user_id`      INT NOT NULL COMMENT '投票的用户ID',
    `candidate_id` INT NOT NULL COMMENT '被投票的候选人ID',
    `vote_time`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '投票时间',

    -- 外键约束，确保 user_id 存在于 user 表中
    CONSTRAINT `fk_vote_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),

    -- 外键约束，确保 candidate_id 存在于 candidate 表中
    CONSTRAINT `fk_vote_candidate` FOREIGN KEY (`candidate_id`) REFERENCES `candidate` (`candidate_id`),

    -- 唯一约束，确保一个用户只能给一个候选人投一票
    UNIQUE KEY `unique_user_candidate` (`user_id`, `candidate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投票记录表';

-- 插入测试投票记录数据
INSERT INTO vote_record (vote_id, user_id, candidate_id, vote_time)
VALUES (1, 1, 1, '2020-01-02 10:00:00'),
       (2, 2, 2, '2020-01-03 11:00:00');