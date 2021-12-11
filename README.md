# 数据结构考试系统

## 0、feature

- [x] 图片采用base64储存
- [x] 题目采用markdown格式

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

Exam

```sql
CREATE TABLE `exam`  (
  `exam_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主id',
  `exam_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '考试名称',
  `exam_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '考试描述',
  `exam_create_time` bigint(20) NULL DEFAULT NULL COMMENT '考试创建时间',
  `exam_update_time` bigint(20) NULL DEFAULT NULL COMMENT '考试更新时间',
  `exam_start_time` bigint(20) NULL DEFAULT NULL COMMENT '考试开始时间',
  `exam_end_time` bigint(20) NULL DEFAULT NULL COMMENT '考试结束时间',
  PRIMARY KEY (`exam_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;
```

Paper

```sql
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `paper_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '试卷id',
  `paper_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '试卷答题人',
  `paper_link` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '试卷所属的考试',
  `paper_time_join` bigint(20) NULL DEFAULT NULL COMMENT '参加考试的时间，填写后代表已经参加考试',
  `paper_time_update` bigint(20) NULL DEFAULT NULL COMMENT '提交答案时间，填写后代表已经提交答案',
  `paper_score_full` int(11) NULL DEFAULT NULL COMMENT '满分',
  `paper_score_actual` int(11) NULL DEFAULT NULL COMMENT '得分',
  PRIMARY KEY (`paper_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;
```

Question

```sql
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `question_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目d',
  `question_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '题目内容',
  `question_picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '题目图片，采用base64编码',
  `question_score` int(16) NOT NULL COMMENT '题目分值',
  `question_type` int(8) NOT NULL COMMENT '题目类型，定义0为单选，1为多选，2为不定项选择，3为填空，4为判断，5为简答，6为综合题',
  `question_link` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '题目属于哪一张试卷',
  `question_opinion_1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '题目的第一个选项，可以为空',
  `question_opinion_2` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `question_opinion_3` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `question_opinion_4` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `question_opinion_5` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `question_right_choice` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '选择类为选项编号，用-连接；填空，简答选择第一个选项。内容未null表示无法自动判断。',
  `question_create_time` bigint(20) NULL DEFAULT NULL COMMENT '题目创建时间',
  `question_update_time` bigint(20) NULL DEFAULT NULL COMMENT '题目更新时间',
  PRIMARY KEY (`question_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

```

Question_collect

```sql
DROP TABLE IF EXISTS `question_collect`;
CREATE TABLE `question_collect`  (
  `collect_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'id',
  `collect_link_paper` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联到的试卷',
  `collect_link_question` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联到的题目',
  `collect_text` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '答题内容',
  `collect_score` int(11) NULL DEFAULT NULL COMMENT '得分',
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

```





## 2、接口

### 2.1 User

接口url：/user

#### 2.1.1 login

描述：通用登录接口

接口url：/login

请求方式：POST

请求参数：

| 参数名称 | 参数类型 | 必填 | 说明 |
| -------- | -------- | ---- | ---- |
| account  | string   | √    | 账号 |
| password | string   | √    | 密码 |

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

描述：由管理员增加用户

接口url：/adduser

请求方式：POST

请求参数：

| 参数名称     | 参数类型 | 必填 | 说明                                    |
| ------------ | -------- | ---- | --------------------------------------- |
| token        | string   | √    | 令牌                                    |
| account      | string   | √    | 账号                                    |
| password     | string   | √    | 密码                                    |
| userNickname | string   | √    | 昵称                                    |
| userGroup    | int      | √    | 权限组，0为管理员，1为老师，2为普通用户 |

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



### 2.2 Exam

接口url：/exam

#### 2.2.1 listExam

描述：由管理员或者教师列出所有考试

todo：根据Group鉴权

接口url：/listexam

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 必填 | 说明         |
| ------------- | -------- | ---- | ------------ |
| authorization | string   | √    | Bearer token |

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

描述：由管理员或者教师增加一场考试

接口url：/addexam

请求方式：POST

请求参数：

| 参数名称        | 参数类型 | 必填 | 说明                     |
| --------------- | -------- | ---- | ------------------------ |
| authorization   | string   | √    | Bearer token             |
| examName        | string   | √    | 考试名称                 |
| examDescription | string   | √    | 考试描述                 |
| examStartTime   | string   |      | 考试开始时间，Unix时间戳 |
| examEndTime     | string   |      | 考试结束时间，同上       |

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
ERROR_PARAMETER(452,"参数错误"),
//有必填项未填写
```



#### 2.2.3 setExamEnd

描述：由管理员或者教师结束一场考试

接口url：/setexamend/{examid}/{timestamp}

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 必填 | 说明                     |
| ------------- | -------- | ---- | ------------------------ |
| authorization | string   | √    | Bearer token             |
| examid        | string   | √    | 考试id                   |
| timestamp     | long     |      | 结束时间戳，为空则为现在 |

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
ERROR_PARAMETER(452,"参数错误"),
//有必填项未填写
OBJECT_MISSED(404,"不存在的对象"),
//不存在的考试
```



#### 2.2.4 setExamStart

描述：由管理员或者教师开始一场考试

接口url：/setexamstart/{examid}/{timestamp}

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 必填 | 说明                     |
| ------------- | -------- | ---- | ------------------------ |
| authorization | string   | √    | Bearer token             |
| examid        | string   | √    | 考试id                   |
| timestamp     | long     |      | 开始时间戳，为空则为现在 |

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
ERROR_PARAMETER(452,"参数错误"),
//有必填项未填写
OBJECT_MISSED(404,"不存在的对象"),
//不存在的考试
```



### 2.3 Question

接口url：/ques

#### 2.3.1 listQuestion

描述：管理员或教师列出所有题目

接口url：/listques

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 必填 | 说明         |
| ------------- | -------- | ---- | ------------ |
| authorization | string   | √    | Bearer token |

返回数据：

~~~json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "questionId": "1469577443713650690",
            "questionText": "测试测试测试选A",
            "questionPicture": null,
            "questionScore": 3,
            "questionType": 0,
            "questionLink": "1467830221552652290",
            "questionOpinion1": "选项1",
            "questionOpinion2": "选项2",
            "questionOpinion3": "选项3",
            "questionOpinion4": "选项4",
            "questionOpinion5": null,
            "questionRightChoice": "1"
        },
        {
            "questionId": "1469581767806226434",
            "questionText": "测试测试测试选A",
            "questionPicture": null,
            "questionScore": 3,
            "questionType": 0,
            "questionLink": "1469578484903481345",
            "questionOpinion1": "选项1",
            "questionOpinion2": "选项2",
            "questionOpinion3": "选项3",
            "questionOpinion4": "选项4",
            "questionOpinion5": null,
            "questionRightChoice": "1"
        },
        {
            "questionId": "1469592620932681729",
            "questionText": "多选测试测试测试选AD",
            "questionPicture": null,
            "questionScore": 3,
            "questionType": 1,
            "questionLink": "1469578484903481345",
            "questionOpinion1": "选项1",
            "questionOpinion2": "选项2",
            "questionOpinion3": "选项3",
            "questionOpinion4": "选项4",
            "questionOpinion5": null,
            "questionRightChoice": "1-4"
        }
    ]
}
~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
```

#### 2.2.2 addQuestion

描述：由管理员或教师添加一道题目

接口url：/addques

请求方式：POST

请求参数：

| 参数名称            | 参数类型 | 必填 | 说明                                                         |
| ------------------- | -------- | ---- | ------------------------------------------------------------ |
| authorization       | string   | √    | Bearer token                                                 |
| questionText        | string   | √    | 题目                                                         |
| questionPicture     | int      |      | 题目图片                                                     |
| questionScore       | int      | √    | 题目类型<br/>定义0为单选，1为多选，2为不定项选择，3为填空，4为判断，5为简答，6为综合题 |
| questionLink        | string   |      | 关联试卷，用于将题目关联至试卷，可以为空                     |
| questionOpinion1    | string   |      | 选项1<br/>单、多、不定项选择按需填写，判断填写前两项<br/>填空、简答、综合题无需填写 |
| questionOpinion2    | string   |      | 同上                                                         |
| questionOpinion3    | string   |      | 同上                                                         |
| questionOpinion4    | string   |      | 同上                                                         |
| questionOpinion5    | string   |      | 同上                                                         |
| questionRightChoice | string   |      | 题目答案<br/>填写选项时在此填写正确选项，多项正确时用-连接   |

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
ERROR_PARAMETER(452,"参数错误"),
//有必填项未填写

```



### 2.4 Paper

接口url：/paper

#### 2.4.1 listMyExam

描述：学生查询自己的考试

接口url：/listmyexam

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
            "examId": "1469578484903481345",
            "examName": "数据结构-4",
            "examDescription": "用来测试的考试",
            "examStartTime": 1639210555563,
            "examEndTime": null
        },
        {
            "examId": "1467830221552652290",
            "examName": "数据结构-1",
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
//未提交token/token无效
```

#### 2.4.2 startExam

描述：学生开始一场考试

接口url：/startexam/{{examId}}

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 说明         |
| ------------- | -------- | ------------ |
| authorization | string   | Bearer token |
| examId        | string   | 考试id       |

返回数据：

```json
{
    "success": true,
    "code": 200,
    "msg": "success",
    "data": [
        {
            "questionId": "1469581767806226434",
            "questionText": "测试测试测试选A",
            "questionPicture": null,
            "questionScore": 3,
            "questionType": 0,
            "questionOpinion1": "选项1",
            "questionOpinion2": "选项2",
            "questionOpinion3": "选项3",
            "questionOpinion4": "选项4",
            "questionOpinion5": null
        },
        {
            "questionId": "1469592620932681729",
            "questionText": "多选测试测试测试选AD",
            "questionPicture": null,
            "questionScore": 3,
            "questionType": 1,
            "questionOpinion1": "选项1",
            "questionOpinion2": "选项2",
            "questionOpinion3": "选项3",
            "questionOpinion4": "选项4",
            "questionOpinion5": null
        }
    ]
}
```

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
```



#### 2.4.3 addPaper

描述：由管理员或教师添加一张试卷

接口url：/addpaper

请求方式：POST

请求参数：

| 参数名称      | 参数类型 | 必填 | 说明           |
| ------------- | -------- | ---- | -------------- |
| authorization | string   | √    | Bearer token   |
| paperUser     | string   | √    | 考试者         |
| paperLink     | string   | √    | 考试id(examId) |

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
ERROR_PARAMETER(452,"参数错误"),
//有必填项未填写
```

#### 2.4.4 getPaper

接口url：/getpaper/{examid}

请求方式：GET

请求参数：

| 参数名称      | 参数类型 | 说明         |
| ------------- | -------- | ------------ |
| authorization | string   | Bearer token |
| examid        | string   | 考试id       |

返回数据：

~~~json

~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
```

#### 2.4.3 updateOne

接口url：/updateone

请求方式：POST

请求参数：

| 参数名称       | 参数类型 | 说明         |
| -------------- | -------- | ------------ |
| authorization  | string   | Bearer token |
| questionId     | string   | 问题id       |
| examId         | string   | 考试id       |
| questionAnswer | String   | 作答         |

返回数据：

~~~json

~~~

失败返回数据：

```java
NO_PERMISSION(401,"无访问权限"),
//未提交token/token无效/token权限不足
```

