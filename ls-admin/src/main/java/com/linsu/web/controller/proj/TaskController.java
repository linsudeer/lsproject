package com.linsu.web.controller.proj;

import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.linsu.common.utils.DateUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.linsu.common.annotation.Log;
import com.linsu.common.core.controller.BaseController;
import com.linsu.common.core.domain.AjaxResult;
import com.linsu.common.enums.BusinessType;
import com.linsu.proj.domain.Task;
import com.linsu.proj.service.ITaskService;
import com.linsu.common.utils.poi.ExcelUtil;
import com.linsu.common.core.page.TableDataInfo;

/**
 * 任务Controller
 *
 * @author lisl
 * @date 2024-01-19
 */
@RestController
@RequestMapping("/proj/task")
public class TaskController extends BaseController {
    @Autowired
    private ITaskService taskService;

    /**
     * 查询任务列表
     */
    @PreAuthorize("@ss.hasPermi('proj:task:list')")
    @GetMapping("/list")
    public TableDataInfo list(Task task) {
        startPage();
        List<Task> list = taskService.selectTaskList(task);
        return getDataTable(list);
    }

    /**
     * 导出任务列表
     */
    @PreAuthorize("@ss.hasPermi('proj:task:export')")
    @Log(title = "任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Task task) {
        List<Task> list = taskService.selectTaskList(task);
        ExcelUtil<Task> util = new ExcelUtil<Task>(Task.class);
        util.exportExcel(response, list, "任务数据");
    }

    /**
     * 获取任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('proj:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(taskService.selectTaskById(id));
    }

    /**
     * 新增任务
     */
    @PreAuthorize("@ss.hasPermi('proj:task:add')")
    @Log(title = "任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Task task) {
        task.setWorkYear(LocalDate.now().getYear());
        return toAjax(taskService.insertTask(task));
    }

    /**
     * 修改任务
     */
    @PreAuthorize("@ss.hasPermi('proj:task:edit')")
    @Log(title = "任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Task task) {
        return toAjax(taskService.updateTask(task));
    }

    /**
     * 删除任务
     */
    @PreAuthorize("@ss.hasPermi('proj:task:remove')")
    @Log(title = "任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(taskService.deleteTaskByIds(ids));
    }
}
