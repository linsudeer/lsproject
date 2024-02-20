<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="projectName">
        <el-select v-model="queryParams.projectId" clearable @keyup.enter="handleQuery" filterable placeholder="项目名称">
          <el-option
              v-for="dict in projectList"
              :key="dict.id"
              :label="dict.projectName"
              :value="dict.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所属人" prop="userId">
        <el-select v-model="queryParams.userId" clearable @keyup.enter="handleQuery" filterable placeholder="所属人">
          <el-option
              v-for="dict in sysUserList"
              :key="dict.userId"
              :label="dict.nickName"
              :value="dict.userId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="任务状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable>
          <el-option
            v-for="dict in b_task_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="年度" prop="workYear">
        <el-input
          v-model="queryParams.workYear"
          placeholder="请输入年度"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['proj:task:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['proj:task:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['proj:task:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['proj:task:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column type="index" label="序号" width="50" align="center" />
      <el-table-column label="项目名称" align="center" width="200" prop="projectName" show-overflow-tooltip />
      <el-table-column label="所属人" align="center" prop="nickName" />
      <el-table-column label="任务名称" align="center" width="200" prop="taskName" show-overflow-tooltip />
      <el-table-column label="预估工时" align="center" prop="predictHours" />
      <el-table-column label="实际工时" align="center" prop="realityHours" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="任务状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="b_task_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['proj:task:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['proj:task:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改任务对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="taskRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" type="text" placeholder="请输入任务名称" maxlength="200" />
        </el-form-item>
        <el-form-item label="所属项目" prop="projectId">
          <el-select v-model="form.projectId" filterable placeholder="请选择项目">
            <el-option
                v-for="dict in projectList"
                :key="dict.id"
                :label="dict.projectName"
                :value="dict.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属人" prop="userId">
          <el-select v-model="form.userId" filterable placeholder="请选择任务人">
            <el-option
                v-for="dict in sysUserList"
                :key="dict.userId"
                :label="dict.nickName"
                :value="dict.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="预估工时" prop="predictHours">
          <el-input-number precision="1" step="0.5" v-model="form.predictHours" placeholder="请输入预估工时" />
        </el-form-item>
        <el-form-item label="实际工时" prop="realityHours">
          <el-input-number precision="1" step="0.5" v-model="form.realityHours" placeholder="请输入实际工时" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Task">
import {addTask, delTask, getTask, listTask, updateTask} from "@/api/proj/task";
import {listUser} from "@/api/system/user.js";
import {listProject} from "@/api/proj/project.js";

const { proxy } = getCurrentInstance();
const { b_task_status } = proxy.useDict('b_task_status');

const projectList = ref([]);
const sysUserList = ref([]);
const taskList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    projectId: null,
    userId: null,
    taskName: null,
    startTime: null,
    endTime: null,
    status: null,
    workYear: null
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询任务列表 */
function getList() {
  loading.value = true;
  listTask(queryParams.value).then(response => {
    taskList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    projectId: null,
    userId: null,
    taskName: null,
    startTime: null,
    endTime: null,
    predictHours: null,
    realityHours: null,
    status: null,
    createTime: null,
    createBy: null,
    updateTime: null,
    updateBy: null,
    remark: null,
    workYear: null
  };
  proxy.resetForm("taskRef");
}

// 查项目经理
function getSysUserList(query) {
  listUser({userName: query, pageSize: 10000}).then(res => {
    sysUserList.value = res.rows
  })
}

// 查所有项目
function getProjectList() {
  listProject({pageSize: 10000}).then(response => {
    projectList.value = response.rows;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加任务";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getTask(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改任务";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["taskRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateTask(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addTask(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除任务编号为"' + _ids + '"的数据项？').then(function() {
    return delTask(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('proj/task/export', {
    ...queryParams.value
  }, `task_${new Date().getTime()}.xlsx`)
}

getList();
getSysUserList()
getProjectList()
</script>
