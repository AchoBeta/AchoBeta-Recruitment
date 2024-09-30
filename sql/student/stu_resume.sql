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