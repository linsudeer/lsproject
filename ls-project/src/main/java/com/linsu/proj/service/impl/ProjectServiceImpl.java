package com.linsu.proj.service.impl;

import java.util.List;

import com.linsu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linsu.proj.mapper.ProjectMapper;
import com.linsu.proj.domain.Project;
import com.linsu.proj.service.IProjectService;

/**
 * 项目管理Service业务层处理
 *
 * @author lisl
 * @date 2024-01-11
 */
@Service
public class ProjectServiceImpl implements IProjectService {
    @Autowired
    private ProjectMapper projectMapper;

    /**
     * 查询项目管理
     *
     * @param id 项目管理主键
     * @return 项目管理
     */
    @Override
    public Project selectProjectById(Long id) {
        return projectMapper.selectProjectById(id);
    }

    /**
     * 查询项目管理列表
     *
     * @param project 项目管理
     * @return 项目管理
     */
    @Override
    public List<Project> selectProjectList(Project project) {
        return projectMapper.selectProjectList(project);
    }

    /**
     * 新增项目管理
     *
     * @param project 项目管理
     * @return 结果
     */
    @Override
    public int insertProject(Project project) {
        project.setCreateTime(DateUtils.getNowDate());
        return projectMapper.insertProject(project);
    }

    /**
     * 修改项目管理
     *
     * @param project 项目管理
     * @return 结果
     */
    @Override
    public int updateProject(Project project) {
        project.setUpdateTime(DateUtils.getNowDate());
        return projectMapper.updateProject(project);
    }

    /**
     * 批量删除项目管理
     *
     * @param ids 需要删除的项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProjectByIds(Long[] ids) {
        return projectMapper.deleteProjectByIds(ids);
    }

    /**
     * 删除项目管理信息
     *
     * @param id 项目管理主键
     * @return 结果
     */
    @Override
    public int deleteProjectById(Long id) {
        return projectMapper.deleteProjectById(id);
    }

    @Override
    public Project selectProjectByNo(String projectNo) {

        return projectMapper.selectProjectByNo(projectNo);
    }
}
