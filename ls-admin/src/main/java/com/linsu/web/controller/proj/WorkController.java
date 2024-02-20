package com.linsu.web.controller.proj;

import com.linsu.common.annotation.Log;
import com.linsu.common.core.controller.BaseController;
import com.linsu.common.core.domain.AjaxResult;
import com.linsu.common.core.domain.entity.SysUser;
import com.linsu.common.core.page.TableDataInfo;
import com.linsu.common.enums.BusinessType;
import com.linsu.common.utils.DateUtils;
import com.linsu.common.utils.StringUtils;
import com.linsu.common.utils.poi.ExcelUtil;
import com.linsu.proj.domain.Project;
import com.linsu.proj.domain.Work;
import com.linsu.proj.service.IProjectService;
import com.linsu.proj.service.IWorkService;
import com.linsu.proj.vo.WorkVo;
import com.linsu.system.service.ISysUserService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 工时记录Controller
 *
 * @author lisl
 * @date 2024-01-18
 */
@RestController
@RequestMapping("/proj/work")
public class WorkController extends BaseController {
    @Autowired
    private IWorkService workService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IProjectService projectService;

    /**
     * 查询工时记录列表
     */
    @PreAuthorize("@ss.hasPermi('proj:work:list')")
    @GetMapping("/list")
    public TableDataInfo list(Work work) {
        startPage();
        List<Work> list = workService.selectWorkList(work);
        return getDataTable(list);
    }

    /**
     * 导出工时记录列表
     */
    @PreAuthorize("@ss.hasPermi('proj:work:export')")
    @Log(title = "工时记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Work work) {
        List<Work> list = workService.selectWorkList(work);
        ExcelUtil<Work> util = new ExcelUtil<Work>(Work.class);
        util.exportExcel(response, list, "工时记录数据");
    }

    /**
     * 获取工时记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('proj:work:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(workService.selectWorkById(id));
    }

    /**
     * 新增工时记录
     */
    @PreAuthorize("@ss.hasPermi('proj:work:add')")
    @Log(title = "工时记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Work work) {
        return toAjax(workService.insertWork(work));
    }

    /**
     * 修改工时记录
     */
    @PreAuthorize("@ss.hasPermi('proj:work:edit')")
    @Log(title = "工时记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Work work) {
        return toAjax(workService.updateWork(work));
    }

    /**
     * 删除工时记录
     */
    @PreAuthorize("@ss.hasPermi('proj:work:remove')")
    @Log(title = "工时记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(workService.deleteWorkByIds(ids));
    }

    /**
     * 查每个员工一年的工时
     * @param work
     * @return
     */
    @PreAuthorize("@ss.hasPermi('proj:work:list')")
    @Log(title = "工时记录", businessType = BusinessType.OTHER)
    @GetMapping("/listByYear")
    public TableDataInfo listByYear(Work work) {
        startPage();
        List<WorkVo> list = workService.listByYear(work);
        return getDataTable(list);
    }

    /**
     * 导入财务报表工时
     *
     * @return
     */
    @Log(title = "财务报表工时导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importFinanceReportHours")
    public AjaxResult importFinanceReportHours(MultipartFile file, boolean updateSupport, Integer workYear) throws Exception {
        List<Work> workList = new ArrayList<>();
        ExcelUtil util = new ExcelUtil();
        Workbook web = WorkbookFactory.create(file.getInputStream());
        // 获取sheet 个数
        int sheetNumber = web.getNumberOfSheets();
        for (int i = 0; i < sheetNumber; i++) {
            List<Work> monthWorkList = new ArrayList<>();
            Sheet sheet = web.getSheetAt(i);
            int rowNum = sheet.getLastRowNum();
            if (rowNum > 0) {
                // ----------  读取表头 ---------------
                Row header = sheet.getRow(0);
                int cellNum = header.getLastCellNum();
                // 从第三列开始读取员工姓名
                Map<Integer, Long> userMap = new HashMap<>();
                for (int j = 2; j < cellNum; j++) {
                    Object username = util.getCellValue(header, j);
                    // 根据姓名查找用户id
                    SysUser user = userService.selectUserByUserName(username.toString());
                    // 存储索引与用户id关系
                    if (user != null) {
                        userMap.put(j, user.getUserId());
                    }
                }
                // 开始读取具体数据
                for (int m = 1; m <= rowNum; m++) {
                    Row row = sheet.getRow(m);
                    Object projectId = util.getCellValue(row, 0);
                    Project project = projectService.selectProjectById(Long.valueOf(projectId.toString()));
                    // 根据项目编号查找项目ID
                    if (project != null) {
                        for (int n = 2; n < cellNum; n++) {
                            Work work = new Work();
                            Object hours = util.getCellValue(row, n);
                            if (hours == null || "".equals(hours.toString()) || "0".equals(hours.toString())) {
                                continue;
                            }
                            work.setProjectId(project.getId());
                            work.setUserId(userMap.get(n));
                            work.setWorkMonth((i + 1));
                            work.setWorkYear(workYear);
                            work.setWorkDays(Double.valueOf(hours.toString()));
                            monthWorkList.add(work);
                        }
                    }
                }
            }

            // 每个月的数据集合
            workList.addAll(monthWorkList);
        }
        String operName = getUsername();
        String message = workService.importFinanceReportHours(workList, updateSupport, operName);
        return success(message);
    }
}
