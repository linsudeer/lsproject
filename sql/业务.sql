
DROP TABLE IF EXISTS `b_project`;
CREATE TABLE `b_project`  (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `manager` int NULL DEFAULT NULL COMMENT '项目经理',
    `project_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目名称',
    `financial_no` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '财务编号',
    `project_no` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目编号',
    `project_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目类型',
    `project_custom` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目客户',
    `project_class` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '项目分类',
    `status` int NULL DEFAULT NULL COMMENT '1-正常 2-停用',
    `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `start_date` date NULL DEFAULT NULL COMMENT '项目启动时间',
    `end_date` date NULL DEFAULT NULL COMMENT '项目终止时间',
    `del_flag` int NULL DEFAULT 0 COMMENT '删除标记',
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `idx_proj_manager`(`manager` ASC) USING BTREE,
     INDEX `idx_proj_no`(`project_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '项目管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_task
-- ----------------------------
DROP TABLE IF EXISTS `b_task`;
CREATE TABLE `b_task`  (
    `id` int NOT NULL AUTO_INCREMENT,
    `project_id` int NULL DEFAULT NULL COMMENT '项目ID',
    `user_id` int NULL DEFAULT NULL COMMENT '任务所属人',
    `task_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '任务名称',
    `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
    `predict_hours` double NULL DEFAULT 0 COMMENT '预估工时（小时）',
    `reality_hours` double NULL DEFAULT 0 COMMENT '实际工时（小时）最终统计工时',
    `status` int NULL DEFAULT 0 COMMENT '任务状态 默认 0 初始状态 1-已完成 2-过期',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `update_by` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
    `remark` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
    `work_year` int NULL DEFAULT NULL COMMENT '年份，按自然年度',
     PRIMARY KEY (`id`) USING BTREE,
     INDEX `idx_task_projectid`(`project_id` ASC) USING BTREE,
     INDEX `idx_task_userid`(`user_id` ASC) USING BTREE,
     INDEX `idx_task_year`(`work_year` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for b_work
-- ----------------------------
DROP TABLE IF EXISTS `b_work`;
CREATE TABLE `b_work`  (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` int NULL DEFAULT NULL COMMENT '用户id',
    `project_id` int NULL DEFAULT NULL COMMENT '项目id',
    `work_month` int NULL DEFAULT NULL COMMENT '月份',
    `work_days` double NULL DEFAULT NULL COMMENT '工作天数',
    `work_year` int NULL DEFAULT NULL COMMENT '工时年度',
    `remark` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '更新人',
    `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '创建人',
     PRIMARY KEY (`id`) USING BTREE,
     UNIQUE INDEX `idx_work_unq`(`user_id` ASC, `project_id` ASC, `work_month` ASC, `work_year` ASC) USING BTREE,
     INDEX `idx_work_userid`(`user_id` ASC) USING BTREE,
     INDEX `idx_work_projectid`(`project_id` ASC) USING BTREE,
     INDEX `idx_work_year`(`work_year` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69396 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '工时记录表' ROW_FORMAT = Dynamic;
