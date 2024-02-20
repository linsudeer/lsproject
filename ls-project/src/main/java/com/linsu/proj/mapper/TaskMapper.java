package com.linsu.proj.mapper;

import java.util.List;
import com.linsu.proj.domain.Task;

/**
 * 任务Mapper接口
 *
 * @author lisl
 * @date 2024-01-19
 */
public interface TaskMapper
{
    /**
     * 查询任务
     *
     * @param id 任务主键
     * @return 任务
     */
    public Task selectTaskById(Long id);

    /**
     * 查询任务列表
     *
     * @param task 任务
     * @return 任务集合
     */
    public List<Task> selectTaskList(Task task);

    /**
     * 新增任务
     *
     * @param task 任务
     * @return 结果
     */
    public int insertTask(Task task);

    /**
     * 修改任务
     *
     * @param task 任务
     * @return 结果
     */
    public int updateTask(Task task);

    /**
     * 删除任务
     *
     * @param id 任务主键
     * @return 结果
     */
    public int deleteTaskById(Long id);

    /**
     * 批量删除任务
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaskByIds(Long[] ids);
}
