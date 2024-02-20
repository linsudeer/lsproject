package com.linsu.proj.domain;

import com.linsu.common.annotation.Excel;
import com.linsu.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 工时记录对象 b_work
 *
 * @author lisl
 * @date 2024-01-18
 */
@Data
public class Work extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 项目id
     */
    @Excel(name = "项目id")
    private Long projectId;

    /**
     * 月份
     */
    @Excel(name = "月份")
    private int workMonth;

    /**
     * 工作天数
     */
    @Excel(name = "工作天数")
    private Double workDays;

    /**
     * 工时年度
     */
    @Excel(name = "工时年度")
    private Integer workYear;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 项目名称
     */
    private String projectName;

}
