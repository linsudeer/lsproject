package com.linsu.proj.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linsu.proj.domain.Work;
import com.linsu.proj.vo.WorkVo;
import org.apache.ibatis.annotations.Param;

/**
 * 工时记录Mapper接口
 *
 * @author lisl
 * @date 2024-01-18
 */
public interface WorkMapper extends BaseMapper<Work> {
    /**
     * 查询工时记录
     *
     * @param id 工时记录主键
     * @return 工时记录
     */
    public Work selectWorkById(Long id);

    /**
     * 查询工时记录列表
     *
     * @param work 工时记录
     * @return 工时记录集合
     */
    public List<Work> selectWorkList(Work work);

    /**
     * 新增工时记录
     *
     * @param work 工时记录
     * @return 结果
     */
    public int insertWork(Work work);

    /**
     * 修改工时记录
     *
     * @param work 工时记录
     * @return 结果
     */
    public int updateWork(Work work);

    /**
     * 删除工时记录
     *
     * @param id 工时记录主键
     * @return 结果
     */
    public int deleteWorkById(Long id);

    /**
     * 批量删除工时记录
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkByIds(Long[] ids);

    /**
     * 根据唯一条件查
     * @param userId
     * @param projectId
     * @param workYear
     * @param month
     * @return
     */
    Work checkUnique(@Param("userId") Long userId, @Param("projectId") Long projectId, @Param("workYear")  Integer workYear, @Param("month") Integer month);

    /**
     * 查员工一年工时
     * @param work
     * @return
     */
    List<WorkVo> listByYear(Work work);
}
