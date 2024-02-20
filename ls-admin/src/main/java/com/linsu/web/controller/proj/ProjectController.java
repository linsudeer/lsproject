package com.linsu.web.controller.proj;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.linsu.proj.domain.Project;
import com.linsu.proj.service.IProjectService;
import com.linsu.common.utils.poi.ExcelUtil;
import com.linsu.common.core.page.TableDataInfo;

/**
 * 项目管理Controller
 *
 * @author lisl
 * @date 2024-01-11
 */
@RestController
@RequestMapping("/proj/project")
public class ProjectController extends BaseController
{
    @Autowired
    private IProjectService projectService;

    /**
     * 查询项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('proj:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(Project project)
    {
        startPage();
        List<Project> list = projectService.selectProjectList(project);
        return getDataTable(list);
    }

    /**
     * 导出项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('proj:project:export')")
    @Log(title = "项目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Project project)
    {
        List<Project> list = projectService.selectProjectList(project);
        ExcelUtil<Project> util = new ExcelUtil<Project>(Project.class);
        util.exportExcel(response, list, "项目管理数据");
    }

    /**
     * 获取项目管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('proj:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(projectService.selectProjectById(id));
    }

    /**
     * 新增项目管理
     */
    @PreAuthorize("@ss.hasPermi('proj:project:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Project project)
    {
        return toAjax(projectService.insertProject(project));
    }

    /**
     * 修改项目管理
     */
    @PreAuthorize("@ss.hasPermi('proj:project:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Project project)
    {
        return toAjax(projectService.updateProject(project));
    }

    /**
     * 删除项目管理
     */
    @PreAuthorize("@ss.hasPermi('proj:project:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(projectService.deleteProjectByIds(ids));
    }
}
