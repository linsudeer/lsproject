package com.linsu.proj.service;

import java.util.List;
import com.linsu.proj.domain.Project;

/**
 * 项目管理Service接口
 *
 * @author lisl
 * @date 2024-01-11
 */
public interface IProjectService
{
    /**
     * 查询项目管理
     *
     * @param id 项目管理主键
     * @return 项目管理
     */
    public Project selectProjectById(Long id);

    /**
     * 查询项目管理列表
     *
     * @param project 项目管理
     * @return 项目管理集合
     */
    public List<Project> selectProjectList(Project project);

    /**
     * 新增项目管理
     *
     * @param project 项目管理
     * @return 结果
     */
    public int insertProject(Project project);

    /**
     * 修改项目管理
     *
     * @param project 项目管理
     * @return 结果
     */
    public int updateProject(Project project);

    /**
     * 批量删除项目管理
     *
     * @param ids 需要删除的项目管理主键集合
     * @return 结果
     */
    public int deleteProjectByIds(Long[] ids);

    /**
     * 删除项目管理信息
     *
     * @param id 项目管理主键
     * @return 结果
     */
    public int deleteProjectById(Long id);

    /**
     * 根据项目编号查找项目ID
     * @param projectNo
     * @return
     */
    Project selectProjectByNo(String projectNo);
}
