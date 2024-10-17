SET character_set_client = utf8;
SET character_set_results = utf8;
SET character_set_connection = utf8;

drop database if exists achobeta_recruitment;
create database achobeta_recruitment character set utf8mb4 collate utf8mb4_bin;
use achobeta_recruitment;

drop table if exists `user`;
create table `user`
(
    `id` bigint primary key auto_increment comment '用户唯一 id',
    `username` varchar(50) not null default '' comment '用户名',
    `nickname` varchar(50) not null default '' comment '用户昵称',
    `email` varchar(50) not null default '' comment '邮箱',
    `phone_number` varchar(11) not null default '' comment '手机号码',
    `password` varchar(100) not null default '' comment '密码',
    `user_type` int not null default 1 comment '用户类型：1.普通用户 2. 管理员',
    `avatar` bigint comment '头像地址',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_email`(`email` asc) using btree,
    index `idx_phone`(`phone_number` asc) using btree,
    unique `uni_username`(`username` asc) using btree
) auto_increment = 10000 comment = '用户基本信息表';

-- root 管理员，username: root; password: AchoBeta666
insert into user(`username`, `nickname`, `password`, `user_type`)
    values('root', 'root', '$2a$10$YPKp0kzLjnNrW5CgKuDdiuF4tZO0KXacmhy2KT7N9Zey49Cmi/rfu', 2);

drop table if exists `member`;
create table `member`
(
    `id` bigint primary key auto_increment comment '正式成员 id',
    `resume_id` bigint not null comment '简历 id',
    `manager_id` bigint unique not null comment '新开的管理员账号 id',
    `parent_id` bigint not null comment '父管理员 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_resume_id`(`resume_id` asc) using btree,
    unique index `uni_manager_id`(`manager_id` asc) using btree,
    index `idx_parent_id`(`parent_id` asc) using btree
) comment '正式成员表';

drop table if exists `user_feedback`;
create table `user_feedback`
(
    `id` bigint primary key auto_increment comment '反馈 id' ,
    `user_id` bigint not null comment '用户id',
    `batch_id` bigint not null comment '招新批次 id',
    `message_id` bigint default null comment '处理结果的消息 id',
    `title` varchar(256) default '' not null comment '反馈标题',
    `content` text not null comment '反馈内容',
    `attachment` bigint default null comment '附件链接',
    `feedback_time` datetime   default CURRENT_TIMESTAMP not null comment '反馈时间',
    `is_handle` bit default b'0' not null comment '是否处理标记',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_user_id`(`user_id` asc) using btree,
    index `idx_batch_id`(`batch_id` asc) using btree,
    index `idx_message_id`(`message_id` asc) using btree
) comment = '用户反馈表';

drop table if exists `interview`;
create table `interview`
(
    `id` bigint primary key auto_increment comment '面试 id',
    `schedule_id` bigint unsigned not null comment '面试预约 id',
    `paper_id` bigint default null comment '面试试卷 id',
    `title` varchar(100) not null default '' comment '面试主题',
    `description` text not null comment '面试说明',
    `status` tinyint not null default 0 comment '是否开始（0未开始、1进行中、2已结束）',
    `address` varchar(500) not null default '' comment '线下面试地址/显示面试会议链接',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_schedule_id`(`schedule_id` asc) using btree,
    index `idx_paper_id`(`paper_id` asc) using btree
) comment '面试表';

drop table if exists `interview_comment`;
create table `interview_comment`
(
    `id` bigint primary key auto_increment comment '面评 id',
    `interview_id` bigint not null comment '面试 id',
    `manager_id` bigint not null comment '管理员 id',
    `content` text not null comment '评论内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_interview_id`(`interview_id` asc) using btree,
    index `idx_manager_id`(`manager_id` asc) using btree
) comment '面试评论表';

drop table if exists `interview_question_score`;
create table `interview_question_score`
(
    `id` bigint primary key auto_increment comment '面试题评分 id',
    `interview_id` bigint not null comment '面试 id',
    `question_id` bigint not null comment '问题 id',
    `score` int not null comment '评分（0-10，-1为超纲）',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_interview_id`(`interview_id` asc) using btree,
    index `idx_question_id`(`question_id` asc) using btree
) comment '面试题评分关联表';

drop table if exists `interview_schedule`;
create table `interview_schedule`
(
    `id` bigint primary key auto_increment comment '面试预约 id',
    `participation_id` bigint not null comment '用户的“活动参与” id',
    `start_time` datetime not null comment '预约开始时间',
    `end_time` datetime not null comment '预约结束时间',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_participation_id`(`participation_id` asc) using btree
) comment '面试预约表';

drop table if exists `interview_summary`;
create table `interview_summary`
(
    `id` bigint primary key auto_increment comment '面试总结 id',
    `interview_id` bigint not null comment '面试 id',
    `basis` tinyint not null default 0 comment '基础理论知识掌握（0-5）',
    `coding` tinyint not null default 0 comment '代码能力（0-5）',
    `thinking` tinyint not null default 0 comment '思维能力（0-5）',
    `express` tinyint not null default 0 comment '表达能力（0-5）',
    `evaluate` varchar(500) not null default '' comment '面试总评',
    `suggest` varchar(500) not null default '' comment '复习建议',
    `playback` varchar(256) not null default '' comment '面试回放链接',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_interview_id`(`interview_id` asc) using btree
) comment '面试总结表';

drop table if exists `interviewer`;
create table `interviewer`
(
    `id` bigint primary key auto_increment comment '面试官 id',
    `manager_id` bigint not null comment '管理员 id',
    `schedule_id` bigint not null comment '面试预约 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_manager_id`(`manager_id` asc) using btree,
    index `idx_schedule_id`(`schedule_id` asc) using btree
) comment '面试官表';

drop table if exists `message`;
create table `message`
(
    `id` bigint primary key auto_increment comment '消息 id',
    `manager_id` bigint not null comment '发送消息的管理员 id',
    `user_id` bigint not null comment '用户 id',
    `tittle` varchar(256) not null default '' comment '消息标题',
    `content` text not null comment '消息内容',
    `send_time` datetime not null default current_timestamp comment '发送时间',
    `attachment` bigint null default null comment '附件 url',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_manager_id`(`manager_id` asc) using btree,
    index `idx_user_id`(`user_id` asc) using btree
) comment = '消息表';

drop table if exists `message_template`;
create table `message_template`
(
    `id` bigint primary key auto_increment comment '模板消息 id',
    `template_title` varchar(100) not null default '' comment '模板消息标题',
    `template_content` text not null comment '模板消息内容',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment = '模板消息表';

drop table if exists `library_paper_link`;
create table `library_paper_link`
(
    `id` bigint primary key auto_increment comment 'id',
    `lib_id` bigint not null comment '试卷库 id',
    `paper_id` bigint not null comment '试卷 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_lp_id`(`lib_id` asc, `paper_id`) using btree
) comment '试卷库-试卷关联表';

drop table if exists `paper_question_link`;
create table `paper_question_link`
(
    `id` bigint primary key auto_increment comment 'id',
    `paper_id` bigint not null comment '试卷 id',
    `question_id` bigint not null comment '问题 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index idx_pq_id(`paper_id` asc, `question_id`) using btree
) comment '试卷-问题关联表';

drop table if exists `question_paper`;
create table `question_paper`
(
    `id` bigint primary key auto_increment comment '试卷 id',
    `title` varchar(100) not null default '' comment '试卷标题',
    `description` text not null comment '试卷说明',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '试卷表';

drop table if exists `question_paper_library`;
create table `question_paper_library`
(
    `id` bigint primary key auto_increment comment '试卷库 id',
    `lib_type` varchar(100) not null default '' comment '试卷库的类别，例如技术类的后端试卷，前端试卷；非技术的信息收集试卷；筛选的笔试试卷...',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_lib_type`(`lib_type` asc) using btree
) comment '试卷库表';

drop table if exists `library_question_link`;
create table `library_question_link`
(
    `id` bigint primary key auto_increment comment 'id',
    `lib_id` bigint not null comment '题库 id',
    `question_id` bigint not null comment '问题 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_lq_id`(`lib_id` asc, `question_id`) using btree
) comment '题库-问题关联表';

drop table if exists `question`;
create table `question`
(
    `id` bigint primary key auto_increment comment '问题 id',
    `title` varchar(2048) not null default '' comment '问题标题',
    `standard` text not null comment '问题标答',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间'
) comment '问题表';

drop table if exists `question_library`;
create table `question_library`
(
    `id` bigint primary key auto_increment comment '题库 id',
    `lib_type` varchar(100) not null default '' comment '题库的类别',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_lib_type`(`lib_type` asc) using btree
) comment '题库表';

drop table if exists `activity_participation`;
create table `activity_participation`
(
    `id` bigint primary key auto_increment comment '“活动参与” id',
    `stu_id` bigint not null comment '学生 id',
    `act_id` bigint not null comment '活动 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_stu_id`(`stu_id` asc) using btree,
    index `idx_act_id`(`act_id` asc) using btree
) comment '“活动参与”表';

drop table if exists `participation_period_link`;
create table `participation_period_link`
(
    `id` bigint primary key auto_increment comment 'id',
    `participation_id` bigint not null comment '“活动参与” id',
    `period_id` bigint not null comment '时间段 id',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_pp_id`(`participation_id` asc, `period_id` asc) using btree
) comment '“活动参与”-时间段关联表';

drop table if exists `participation_question_link`;
create table `participation_question_link`
(
    `id` bigint primary key auto_increment comment 'id',
    `participation_id` bigint not null comment '“活动参与” id',
    `question_id` bigint not null comment '问题 id',
    `answer` text not null comment '回答',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_pq_id`(`participation_id` asc, `question_id` asc) using btree
) comment '“活动参与”-问题关联表';

drop table if exists `recruitment_activity`;
create table `recruitment_activity`
(
    `id` bigint primary key auto_increment comment '招新活动 id',
    `batch_id` bigint not null comment '招新批次 id',
    `paper_id` bigint default null comment '试卷 id',
    `title` varchar(100) not null default '' comment '活动标题',
    `target` json not null comment '面向的人群',
    `description` text not null comment '活动说明',
    `deadline` datetime not null comment '截止时间',
    `is_run` bit not null default b'0' comment '是否启动',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_batch_id`(`batch_id` asc) using btree,
    index `idx_paper_id`(`paper_id` asc) using btree
) comment '招新活动表';

drop table if exists `recruitment_batch`;
create table `recruitment_batch`
(
    `id` bigint primary key auto_increment comment '招新批次 id',
    `batch` int not null comment 'AchoBeta 届数',
    `title` varchar(100) not null default '' comment '招新标题',
    `deadline` datetime not null comment '截止时间',
    `is_run` bit not null default b'0' comment '是否启动',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_batch`(`batch` asc) using btree
) comment '招新批次表';

drop table if exists `time_period`;
create table `time_period`
(
    `id` bigint primary key auto_increment comment '时间段 id',
    `act_id` bigint not null comment '招新活动 id',
    `start_time` datetime not null comment '开始时间',
    `end_time` datetime not null comment '结束时间',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_act_id`(`act_id` asc) using btree
) comment '时间段表';

drop table if exists `digital_resource`;
create table `digital_resource`
(
    `id` bigint primary key auto_increment comment '资源 id',
    `code` bigint unique not null comment '资源码',
    `user_id` bigint not null comment '上传文件的用户 id',
    `access_level` int not null default 2 comment '访问权限',
    `original_name` varchar(100) not null comment '上传时的文件名',
    `file_name` varchar(256) not null comment '在对象存储服务中存储的对象名',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    unique index `uni_code`(`code` asc) using btree,
    index `idx_user_id`(`user_id` asc) using btree
) comment '资源表';

drop table if exists `feishu_resource`;
create table `feishu_resource`
(
    `id` bigint primary key auto_increment comment '飞书资源 id',
    `ticket` varchar(50) unique not null comment '任务 ID',
    `original_name` varchar(100) not null comment '上传时的文件名',
    `token` varchar(50) not null default '' comment '导入云文档的 token',
    `type` varchar(20) not null default '' comment '导入的在线云文档类型',
    `url` varchar(200) not null default '' comment '导入云文档的 URL',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    unique index `uni_ticket`(`ticket`) using btree
) comment '飞书资源表';

drop table if exists `short_link`;
create table `short_link`
(
    `id` bigint primary key auto_increment comment '短链接编号',
    `origin_url` varchar(512) default '' comment '原链接',
    `short_code` char(6) unique default '' comment '短链code',
    `is_used` bit default b'0' comment '是否使用过',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `idx_short_code`(`short_code` asc) using btree
) comment '长短链关系表';

drop table if exists `resume_status_process`;
create table `resume_status_process`
(
    `id` bigint primary key auto_increment comment 'id',
    `resume_id` bigint not null comment '简历 id',
    `resume_status` int not null comment '简历状态',
    `resume_event` int not null comment '简历事件',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- 索引
    index `idx_resume_id`(`resume_id` asc) using btree
) comment '招新简历状态过程表';

drop table if exists `stu_attachment`;
create table `stu_attachment`
(
    `id` bigint primary key auto_increment comment 'id',
    `resume_id` bigint not null comment '学生表主键 id',
    `filename` varchar(256) not null default '' comment '附件名',
    `attachment` bigint not null comment '附件资源码',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_resume_id`(`resume_id` asc) using btree
) comment = '学生附件表';

-- 简历状态 comment 说明
-- 范围：0~16，简历状态{
--       0-草稿
--       1-待筛选
--       2-筛选不通过
--
--       3-待安排初试
--       4-待初试
--       5-初试通过（仅当初试为最后一个流程时显示）
--       6-初试不通过（仅当初试为最后一个流程时显示）
--
--       7-待安排复试
--       8-待复试
--       9-复试通过（仅当复试为最后一个流程时显示）
--      10-复试不通过（仅当复试为最后一个流程时显示）
--
--      11-待安排终试
--      12-待终试
--      13-终试通过（仅当复试为最后一个流程时显示）
--      14-终试不通过（仅当复试为最后一个流程时显示）
--
--      15-待处理（反馈异常/或管理员主动设置为该状态）
--      16-挂起（管理员可以主动设置该状态）
-- }
-- ----------------------------
-- 创建学生简历表
drop table if exists `stu_resume`;
create table `stu_resume`
(
    `id` bigint primary key auto_increment comment 'id',
    `user_id` bigint not null comment '用户 id',
    `batch_id` bigint not null comment '招新批次 id',
    `student_id` varchar(13) not null default '' comment '学号',
    `name` varchar(10) not null default '' comment '姓名',
    `gender` tinyint not null default 0 comment '性别',
    `grade` int not null comment '年级',
    `major` varchar(20) not null default '' comment '专业',
    `class` varchar(30) not null default '' comment '班级',
    `email` varchar(50) not null default '' comment '邮箱',
    `phone_number` varchar(11) not null default '' comment '手机号码',
    `reason` text not null comment '加入 achobeta 的理由',
    `introduce` text not null comment '个人介绍（自我认知）',
    `experience` text not null comment '个人经历（项目经历、职业规划等）',
    `awards` text not null comment '获奖经历',
    `image` bigint not null comment '照片',
    `remark` varchar(500) not null default '' comment '备注',
    `status` int not null default 1 comment '简历状态，范围：0~16',
    `submit_count` int not null default 0 comment '提交次数',
    -- common column
    `version` int not null default 0 comment '乐观锁',
    `is_deleted` bit not null default b'0' comment '伪删除标记',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    -- index
    index `idx_student_id` (`student_id` asc) using btree,
    index `idx_email` (`email` asc) using btree,
    index `idx_user_id` (`user_id` asc) using btree,
    index `idx_batch_id` (`batch_id` asc) using btree,
    index `idx_class` (`class` asc) using btree,
    index `idx_major` (`major` asc) using btree,
    index `idx_name` (`name` asc) using btree
) comment = '学生简历表';
