package com.linsu.proj.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.linsu.common.annotation.Excel;
import com.linsu.common.core.domain.BaseEntity;

/**
 * 项目管理对象 b_project
 *
 * @author lisl
 * @date 2024-01-11
 */
@Data
public class Project extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 项目经理ID
     */
    private Long manager;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称")
    private String projectName;

    /**
     * 财务编号
     */
    @Excel(name = "财务编号")
    private String financialNo;

    /**
     * 项目编号
     */
    @Excel(name = "项目编号")
    private String projectNo;

    /**
     * 项目类型
     */
    @Excel(name = "项目类型")
    private String projectType;

    /**
     * 项目客户
     */
    @Excel(name = "项目客户")
    private String projectCustom;

    /**
     * 项目分类
     */
    @Excel(name = "项目分类")
    private String projectClass;

    /**
     * 1-正常 2-停用
     */
    @Excel(name = "1-正常 2-停用")
    private Long status;

    /**
     * 删除标记
     * 0-正常 2-删除
     */
    private Integer delFlag;

    /**
     * 项目启动时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目启动时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 项目终止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目终止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 项目经理名称
     */
    private String managerName;
}
