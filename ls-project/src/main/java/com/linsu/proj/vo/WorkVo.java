package com.linsu.proj.vo;

import lombok.Data;

/**
 * 工时展示
 */
@Data
public class WorkVo {

    private String id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 1月工时
     */
    private Double hours1;

    /**
     * 2月工时
     */
    private Double hours2;

    /**
     * 3月工时
     */
    private Double hours3;

    /**
     * 4月工时
     */
    private Double hours4;

    /**
     * 5月工时
     */
    private Double hours5;

    /**
     * 6月工时
     */
    private Double hours6;

    /**
     * 7月工时
     */
    private Double hours7;

    /**
     * 8月工时
     */
    private Double hours8;

    /**
     * 9月工时
     */
    private Double hours9;

    /**
     * 10月工时
     */
    private Double hours10;

    /**
     * 11月工时
     */
    private Double hours11;

    /**
     * 12月工时
     */
    private Double hours12;

    /**
     * 总工时
     */
    private Double total;
}
