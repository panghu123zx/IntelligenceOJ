create database if not exists `ojdata`;

use ojdata;

-- 题目表
-- auto-generated definition
create table if not exists question
(
    id          bigint auto_increment comment 'id'
        primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表 难度（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptNum   int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例(输出，输入) json数组',
    judgeConfig text                               null comment '判题配置(时间，内存) json数组',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'
)
    comment '题目表' engine = InnoDB
                     collate = utf8mb4_unicode_ci;

create index idx_userId
    on question (userId);
create index idx_title
    on question (title);


-- 题目ai表
-- auto-generated definition
create table if not exists question_ai
(
    id               bigint auto_increment comment 'id'
        primary key,
    genContent       text                               null comment 'ai提问的内容',
    genRes           text                               null comment 'ai输出结果',
    useNum           int      default 0                 not null comment '使用次数',
    remainNum        int      default 3                 not null comment '剩余次数',
    questionId       bigint                             null comment '题目 id',
    userId           bigint                             not null comment '创建用户 id',
    status           tinyint  default 0                 null comment 'ai判断状态 0-等待中 1-执行中 2-成功 3-失败',
    questionSubmitId bigint                             null comment '提交 id',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除'
)
    comment '题目表' engine = InnoDB
                     collate = utf8mb4_unicode_ci;

create index idx_userId
    on question_ai (userId);
create index idx_questionId
    on question_ai (questionId);

-- 评论
-- auto-generated definition
create table if not exists question_comment
(
    id         bigint                                     not null comment '主键id'
        primary key,
    questionId bigint           default 0                 not null comment '问题id',
    userId     bigint           default 0                 not null comment '用户id',
    userName   varchar(50)                                null comment '用户昵称',
    userAvatar varchar(255)                               null comment '用户头像',
    content    varchar(500)                               null comment '评论内容',
    parentId   bigint           default -1                null comment '父级评论id',
    commentNum int              default 0                 null comment '回复条数',
    likeCount  int              default 0                 null comment '点赞数量',
    inputShow  tinyint(1)       default 0                 null comment '是否显示输入框',
    fromId     bigint                                     null comment '回复记录id',
    fromName   varchar(255) collate utf8mb4_bin           null comment '回复人名称',
    createTime datetime         default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime         default CURRENT_TIMESTAMP not null comment '更新时间',
    isDeleted  tinyint unsigned default '0'               not null comment '逻辑删除 1（true）已删除， 0（false）未删除'
)
    comment '评论' engine = InnoDB
                   collate = utf8mb4_general_ci
                   row_format = DYNAMIC;

create index idx_questionId
    on question_comment (questionId);

create index idx_userId
    on question_comment (userId);


-- 题目提交
-- auto-generated definition
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id'
        primary key,
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    language   varchar(128)                       not null comment '使用语言',
    code       text                               not null comment '题目代码',
    judgeInfo  text                               null comment '判题信息:各种失败信息',
    status     tinyint  default 0                 not null comment '提交状态 0-待判题 1-判题中 2-成功 3-失败',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '题目提交' engine = InnoDB;

create index idx_postId
    on question_submit (questionId);

create index idx_userId
    on question_submit (userId);

-- 用户
-- auto-generated definition
create table if not exists user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
)
    comment '用户' engine = InnoDB
                   collate = utf8mb4_unicode_ci;

create index idx_unionId
    on user (unionId);