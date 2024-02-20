package com.linsu.proj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linsu.common.exception.ServiceException;
import com.linsu.common.utils.DateUtils;
import com.linsu.common.utils.StringUtils;
import com.linsu.proj.domain.Work;
import com.linsu.proj.mapper.WorkMapper;
import com.linsu.proj.service.IWorkService;
import com.linsu.proj.vo.WorkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 工时记录Service业务层处理
 *
 * @author lisl
 * @date 2024-01-18
 */
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements IWorkService {
    @Autowired
    private WorkMapper workMapper;

    /**
     * 查询工时记录
     *
     * @param id 工时记录主键
     * @return 工时记录
     */
    @Override
    public Work selectWorkById(Long id) {
        return workMapper.selectWorkById(id);
    }

    /**
     * 查询工时记录列表
     *
     * @param work 工时记录
     * @return 工时记录
     */
    @Override
    public List<Work> selectWorkList(Work work) {
        return workMapper.selectWorkList(work);
    }

    /**
     * 新增工时记录
     *
     * @param work 工时记录
     * @return 结果
     */
    @Override
    public int insertWork(Work work) {
        work.setCreateTime(DateUtils.getNowDate());
        return workMapper.insertWork(work);
    }

    /**
     * 修改工时记录
     *
     * @param work 工时记录
     * @return 结果
     */
    @Override
    public int updateWork(Work work) {
        work.setUpdateTime(DateUtils.getNowDate());
        return workMapper.updateWork(work);
    }

    /**
     * 批量删除工时记录
     *
     * @param ids 需要删除的工时记录主键
     * @return 结果
     */
    @Override
    public int deleteWorkByIds(Long[] ids) {
        return workMapper.deleteWorkByIds(ids);
    }

    /**
     * 删除工时记录信息
     *
     * @param id 工时记录主键
     * @return 结果
     */
    @Override
    public int deleteWorkById(Long id) {
        return workMapper.deleteWorkById(id);
    }

    @Transactional
    @Override
    public String importFinanceReportHours(List<Work> workList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(workList) || workList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        Date now = new Date();
        for (Work work : workList) {
            try {
                // 验证是否存在这个
                if (checkUnique(work.getUserId(), work.getProjectId(), work.getWorkYear(), work.getWorkMonth())) {
                    work.setCreateTime(now);
                    work.setCreateBy(operName);
                    workMapper.insertWork(work);
                    successNum++;
                } else if (updateSupport) {
                    work.setUpdateTime(now);
                    work.setUpdateBy(operName);
                    workMapper.updateWork(work);
                    successNum++;
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、用户ID " + work.getUserId() + "、项目ID：" + work.getProjectId() + "、月份" + work.getWorkMonth() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                failureMsg.append(e.getMessage());
                log.error(e.getMessage());
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public boolean checkUnique(Long userId, Long projectId, Integer workYear, Integer month) {
        Work work  = workMapper.checkUnique(userId, projectId, workYear, month);
        return work == null;
    }

    @Override
    public List<WorkVo> listByYear(Work work) {
        return workMapper.listByYear(work);
    }

}
