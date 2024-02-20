package com.linsu.proj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linsu.common.annotation.Excel;
import com.linsu.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 任务对象 b_task
 *
 * @author lisl
 * @date 2024-01-19
 */
@Data
public class Task extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String projectName;

    /**
     * 任务所属人
     */
    private Long userId;

    @Excel(name = "所属人")
    private String nickName;

    /**
     * 任务名称
     */
    @Excel(name = "任务名称")
    private String taskName;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 预估工时（小时）
     */
    @Excel(name = "预估工时", readConverterExp = "小=时")
    private Double predictHours;

    /**
     * 实际工时（小时）最终统计工时
     */
    @Excel(name = "实际工时", readConverterExp = "小=时")
    private Double realityHours;

    /**
     * 任务状态 默认 0 初始状态 1-已完成 2-过期
     */
    @Excel(name = "任务状态 默认 0 初始状态 1-已完成 2-过期")
    private Integer status;

    /**
     * 年份，按自然年度
     */
    @Excel(name = "年份，按自然年度")
    private Integer workYear;


}
