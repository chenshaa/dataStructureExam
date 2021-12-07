# 数据结构考试系统

## 0、feature

- [ ] 图片采用base64储存
- [ ] 题目采用markdown格式

## 1、数据库设计

User

```sql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户id',
  `user_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户登录账号',
  `user_nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户昵称',
  `user_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户密码',
  `user_group` int(8) NOT NULL COMMENT '用户所属权限组',
  `user_create_time` bigint(20) NULL DEFAULT NULL,
  `user_update_time` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;
```



## 2、接口

### 2.1 User

接口url：/user

#### 2.1.1 login

接口url：/login

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 说明 |
| -------- | -------- | ---- |
| account  | string   | 账号 |
| password | string   | 密码 |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": {
        "account": "admin",
        "nickname": "管理员",
        "token": "token"
    }
}
~~~

失败返回数据：

```java
ERROR_PARAMETER(452,"参数错误"),
//未填写用户名/密码
ERROR_USERNAME(453,"用户名/密码错误"),
//如你所见
```



#### 2.1.2 addUser

接口url：/adduser

请求方式：POST

请求参数：

| 参数名称     | 参数类型 | 说明                                    |
| ------------ | -------- | --------------------------------------- |
| token        | string   | 令牌                                    |
| account      | string   | 账号                                    |
| password     | string   | 密码                                    |
| userNickname | string   | 昵称                                    |
| userGroup    | int      | 权限组，0为管理员，1为老师，2为普通用户 |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": "成功"
}
~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
OBJECT_EXISTS(409,"对象已经存在");
//account已经存在
```



#### 2.1.2  

### 2.2 Exam

接口url：/exam

#### 2.2.1 listExam

接口url：/listexam

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 说明         |
| ------------- | -------- | ------------ |
| authorization | string   | Bearer token |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "examId": "1467830221552652290",
            "examName": "数据结构-1",
            "examDescription": "用来测试的考试",
            "examStartTime": 1638693549256,
            "examEndTime": 1638693559256
        },
        {
            "examId": "1467833204239667201",
            "examName": "数据结构-2",
            "examDescription": "用来测试的考试",
            "examStartTime": 1638693549256,
            "examEndTime": 1638693559256
        }
    ]
}
~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
```



#### 2.2.2 addExam

接口url：/addexam

请求方式：POST

请求参数：

| 参数名称        | 参数类型 | 说明                     |
| --------------- | -------- | ------------------------ |
| authorization   | string   | Bearer token             |
| examName        | string   | 考试名称                 |
| examDescription | string   | 考试描述                 |
| examStartTime   | string   | 考试开始时间，Unix时间戳 |
| examEndTime     | string   | 考试结束时间，同上       |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": "成功"
}
~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
OBJECT_EXISTS(409,"对象已经存在"),
//已存在同名考试
```



### 2.3 Question

接口url：/ques

#### 2.3.1 listQuestion

接口url：/listques

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 说明         |
| ------------- | -------- | ------------ |
| authorization | string   | Bearer token |

返回数据：

~~~json

~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
```

#### 2.2.2 addExam

接口url：/addexam

请求方式：POST

请求参数：

| 参数名称            | 参数类型 | 说明                                                         |
| ------------------- | -------- | ------------------------------------------------------------ |
| authorization       | string   | Bearer token                                                 |
| questionText        | string   | 题目                                                         |
| questionPicture     | int      | 分值                                                         |
| questionScore       | int      | 题目类型<br/>定义0为单选，1为多选，2为不定项选择，3为填空，4为判断，5为简答，6为综合题 |
| questionLink        | string   | 关联试卷，用于将题目关联至试卷，可以为空                     |
| questionOpinion1    | string   | 选项1<br/>单、多、不定项选择按需填写，判断填写前两项<br/>填空、简答、综合题无需填写 |
| questionOpinion2    | string   | 同上                                                         |
| questionOpinion3    | string   | 同上                                                         |
| questionOpinion4    | string   | 同上                                                         |
| questionOpinion5    | string   | 同上                                                         |
| questionRightChoice | string   | 题目答案<br/>填写选项时在此填写正确选项，多项正确时用-连接   |

返回数据：

~~~json

~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
```

