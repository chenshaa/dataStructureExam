# 数据结构考试系统

## 1、feature

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

