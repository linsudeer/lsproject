package com.linsu.proj.mapper;

import java.util.List;
import com.linsu.proj.domain.Project;

/**
 * 项目管理Mapper接口
 *
 * @author lisl
 * @date 2024-01-11
 */
public interface ProjectMapper
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
     * 删除项目管理
     *
     * @param id 项目管理主键
     * @return 结果
     */
    public int deleteProjectById(Long id);

    /**
     * 批量删除项目管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProjectByIds(Long[] ids);

    /**
     * 根据项目编号查项目
     * @param projectNo
     * @return
     */
    Project selectProjectByNo(String projectNo);
}
