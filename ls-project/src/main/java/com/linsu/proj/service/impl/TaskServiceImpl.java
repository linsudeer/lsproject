package com.linsu.proj.service.impl;

import java.util.List;
import com.linsu.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linsu.proj.mapper.TaskMapper;
import com.linsu.proj.domain.Task;
import com.linsu.proj.service.ITaskService;

/**
 * 任务Service业务层处理
 *
 * @author lisl
 * @date 2024-01-19
 */
@Service
public class TaskServiceImpl implements ITaskService
{
    @Autowired
    private TaskMapper taskMapper;

    /**
     * 查询任务
     *
     * @param id 任务主键
     * @return 任务
     */
    @Override
    public Task selectTaskById(Long id)
    {
        return taskMapper.selectTaskById(id);
    }

    /**
     * 查询任务列表
     *
     * @param task 任务
     * @return 任务
     */
    @Override
    public List<Task> selectTaskList(Task task)
    {
        return taskMapper.selectTaskList(task);
    }

    /**
     * 新增任务
     *
     * @param task 任务
     * @return 结果
     */
    @Override
    public int insertTask(Task task)
    {
        task.setCreateTime(DateUtils.getNowDate());
        return taskMapper.insertTask(task);
    }

    /**
     * 修改任务
     *
     * @param task 任务
     * @return 结果
     */
    @Override
    public int updateTask(Task task)
    {
        task.setUpdateTime(DateUtils.getNowDate());
        return taskMapper.updateTask(task);
    }

    /**
     * 批量删除任务
     *
     * @param ids 需要删除的任务主键
     * @return 结果
     */
    @Override
    public int deleteTaskByIds(Long[] ids)
    {
        return taskMapper.deleteTaskByIds(ids);
    }

    /**
     * 删除任务信息
     *
     * @param id 任务主键
     * @return 结果
     */
    @Override
    public int deleteTaskById(Long id)
    {
        return taskMapper.deleteTaskById(id);
    }
}
