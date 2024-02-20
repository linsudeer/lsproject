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

      <el-form-item label="项目经理" prop="manager">
        <el-select v-model="queryParams.manager" clearable @keyup.enter="handleQuery" filterable placeholder="项目经理">
          <el-option
              v-for="dict in sysUserList"
              :key="dict.userId"
              :label="dict.nickName"
              :value="dict.userId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="项目性质" prop="projectType">
        <el-select v-model="form.projectType" clearable @keyup.enter="handleQuery" placeholder="项目性质">
          <el-option
              v-for="dict in b_project_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="项目分类" prop="projectClass">
        <el-select v-model="form.projectClass" clearable @keyup.enter="handleQuery" placeholder="项目分类">
          <el-option
              v-for="dict in b_project_class"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="项目客户" prop="projectCustom">
        <el-select v-model="form.projectCustom" clearable @keyup.enter="handleQuery" placeholder="项目客户">
          <el-option
              v-for="dict in b_project_custom"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="项目状态" prop="status">
        <el-select v-model="queryParams.status" clearable @keyup.enter="handleQuery" placeholder="项目状态">
          <el-option
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
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
            v-hasPermi="['proj:project:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['proj:project:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['proj:project:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['proj:project:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="项目编号" align="center" prop="projectNo"/>
      <el-table-column label="项目名称" align="center" prop="projectName" width="250" show-overflow-tooltip/>
      <el-table-column label="项目经理" align="center" prop="managerName" width="120" />
      <el-table-column label="财务编号" align="center" prop="financialNo"/>
      <el-table-column label="项目类型" align="center" prop="projectType"/>
      <el-table-column label="项目客户" align="center" prop="projectCustom" show-overflow-tooltip/>
      <el-table-column label="项目分类" align="center" prop="projectClass"/>
      <el-table-column label="项目状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['proj:project:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['proj:project:remove']">删除
          </el-button>
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

    <!-- 添加或修改项目管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="projectRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称"/>
        </el-form-item>
        <el-form-item label="财务编号" prop="financialNo">
          <el-input v-model="form.financialNo" placeholder="请输入财务编号"/>
        </el-form-item>
        <el-form-item label="项目编号" prop="projectNo">
          <el-input v-model="form.projectNo" placeholder="请输入项目编号"/>
        </el-form-item>
        <el-form-item label="项目经理" prop="manager">
          <el-select v-model="form.manager" @keyup.enter="handleQuery" filterable placeholder="项目经理">
            <el-option
                v-for="dict in sysUserList"
                :key="dict.userId"
                :label="dict.nickName"
                :value="dict.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目客户" prop="projectCustom">
          <el-select v-model="form.projectCustom" placeholder="项目客户">
            <el-option
                v-for="dict in b_project_custom"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目分类" prop="projectClass">
          <el-select v-model="form.projectClass" placeholder="项目分类">
            <el-option
                v-for="dict in b_project_class"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目性质" prop="projectType">
          <el-select v-model="form.projectType" placeholder="项目性质">
            <el-option
                v-for="dict in b_project_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="项目状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="启动时间" prop="startDate">
          <el-date-picker clearable
                          v-model="form.startDate"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选目启动时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="终止时间" prop="endDate">
          <el-date-picker clearable
                          v-model="form.endDate"
                          type="date"
                          value-format="YYYY-MM-DD"
                          placeholder="请选择终止时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="对项目简单说明"/>
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

<script setup name="Project">
import {listProject, getProject, delProject, addProject, updateProject} from "@/api/proj/project";
import {listUser} from "@/api/system/user";

const {proxy} = getCurrentInstance();
const {sys_normal_disable} = proxy.useDict('sys_normal_disable');
const {b_project_class} = proxy.useDict('b_project_class');
const {b_project_type} = proxy.useDict('b_project_type');
const {b_project_custom} = proxy.useDict('b_project_custom');

const projectList = ref([]);
const sysUserList = ref([]);
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
    manager: null,
    projectName: null,
    projectType: null,
    projectClass: null,
    projectCustom: null,
    status: null,
  },
  rules: {}
});

const {queryParams, form, rules} = toRefs(data);

/** 查询项目管理列表 */
function getList() {
  loading.value = true;
  listProject(queryParams.value).then(response => {
    projectList.value = response.rows;
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
    projectName: null,
    financialNo: null,
    projectNo: null,
    projectType: null,
    projectCustom: null,
    projectClass: null,
    status: null,
    remark: null,
    createTime: null,
    updateTime: null,
    startDate: null,
    endDate: null
  };
  proxy.resetForm("projectRef");
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

function getSysUserList(query) {
  listUser({userName: query, pageSize: 100}).then(res => {
    sysUserList.value = res.rows
  })
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
  title.value = "添加项目";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getProject(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改项目";
  });
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["projectRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateProject(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addProject(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除项目编号为"' + _ids + '"的数据项？').then(function () {
    return delProject(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('proj/project/export', {
    ...queryParams.value
  }, `project_${new Date().getTime()}.xlsx`)
}

getList();
getSysUserList()
</script>
