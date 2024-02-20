package com.linsu.proj.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linsu.proj.domain.Work;
import com.linsu.proj.vo.WorkVo;

/**
 * 工时记录Service接口
 *
 * @author lisl
 * @date 2024-01-18
 */
public interface IWorkService extends IService<Work> {
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
     * 批量删除工时记录
     *
     * @param ids 需要删除的工时记录主键集合
     * @return 结果
     */
    public int deleteWorkByIds(Long[] ids);

    /**
     * 删除工时记录信息
     *
     * @param id 工时记录主键
     * @return 结果
     */
    public int deleteWorkById(Long id);

    /**
     * 导入财务报表工时
     * @param workList
     * @param updateSupport
     * @param operName
     * @return
     */
    String importFinanceReportHours(List<Work> workList, boolean updateSupport, String operName);

    /**
     * 判断条件下是否唯一
     * @param userId
     * @param projectId
     * @param workYear
     * @param month
     * @return
     */
    boolean checkUnique(Long userId, Long projectId, Integer workYear, Integer month);

    /**
     * 查一年工时
     * @param work
     * @return
     */
    List<WorkVo> listByYear(Work work);
}
