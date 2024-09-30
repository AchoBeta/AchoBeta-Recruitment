create table user_feedback
(
    id          bigint auto_increment comment '反馈id' primary key,
    user_id     bigint                                 not null comment '用户id',
    batch_id    bigint                                 not null comment '招新批次',
    message_id  bigint       default null                       comment '处理结果的消息id',
    title       varchar(256) default ''                not null comment '反馈标题',
    content     text                                   not null comment '反馈内容',
    attachment   bigint      default null                       comment '附件链接',
    feedback_time datetime   default CURRENT_TIMESTAMP not null comment '反馈时间'
    is_handle   bit          default b'0'              not null comment '是否处理标记',
    version     int          default 1                 not null comment '乐观锁',
    is_deleted  bit          default b'0'              not null comment '伪删除标记',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '插入时间',
    update_time datetime     default CURRENT_TIMESTAMP not null comment '更新时间'
);

create index idx_batch_id
    on user_feedback (batch_id);

create index idx_user_id
    on user_feedback (user_id);