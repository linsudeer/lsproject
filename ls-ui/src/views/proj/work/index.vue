<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工姓名" prop="userId">
        <el-select v-model="queryParams.userId" clearable @keyup.enter="handleQuery" filterable placeholder="员工姓名">
          <el-option
              v-for="dict in sysUserList"
              :key="dict.userId"
              :label="dict.nickName"
              :value="dict.userId"
          />
        </el-select>
      </el-form-item>
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
          v-hasPermi="['proj:work:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['proj:work:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['proj:work:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['proj:work:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Upload"
            @click="handleImport"
            v-hasPermi="['system:user:import']"
        >导入财务工时</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工" align="center" prop="nickname" />
      <el-table-column label="项目" align="center" prop="projectName" />
      <el-table-column label="月份" align="center" prop="workMonth" />
      <el-table-column label="工时（人/天）" align="center" prop="workDays" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['proj:work:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['proj:work:remove']">删除</el-button>
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

    <!-- 添加或修改工时记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="workRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="年度" prop="workYear">
          <el-input v-model="form.workYear" placeholder="请输入工时年度" />
        </el-form-item>
        <el-form-item label="项目" prop="projectId">
          <el-select v-model="form.projectId"  filterable placeholder="项目">
            <el-option
                v-for="dict in projectList"
                :key="dict.id"
                :label="dict.projectName"
                :value="dict.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="月份" prop="workMonth">
          <el-input v-model="form.workMonth" placeholder="请输入月份" />
        </el-form-item>
        <el-form-item label="工作天数" prop="workDays">
          <el-input v-model="form.workDays" placeholder="请输入工作天数" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
      <el-upload
          ref="uploadRef"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url + '?updateSupport=' + upload.updateSupport"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="Work">
import { getToken } from "@/utils/auth";
import { listWork, getWork, delWork, addWork, updateWork } from "@/api/proj/work";
import {listUser} from "@/api/system/user.js";
import {listProject} from "@/api/proj/project.js";

const { proxy } = getCurrentInstance();

const projectList = ref([]);
const workList = ref([]);
const sysUserList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
/*** 导入参数 */
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 弹出层标题（用户导入）
  title: "",
  // 是否禁用上传
  isUploading: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: { Authorization: "Bearer " + getToken() },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/proj/work/importFinanceReportHours"
});


const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    userId: null,
    projectId: null,
    workYear: null,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询工时记录列表 */
function getList() {
  loading.value = true;
  listWork(queryParams.value).then(response => {
    workList.value = response.rows;
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
    userId: null,
    projectId: null,
    workMonth: null,
    workDays: null,
    workYear: null,
    remark: null,
    updateTime: null,
    createTime: null,
    updateBy: null,
    createBy: null
  };
  proxy.resetForm("workRef");
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

// 查项目经理
function getSysUserList(query) {
  listUser({userName: query, pageSize: 1000}).then(res => {
    sysUserList.value = res.rows
  })
}

// 查所有项目
function getProjectList() {
  listProject({pageSize: 10000}).then(response => {
    projectList.value = response.rows;
  });
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function handleImport() {
  upload.title = "工时导入";
  upload.open = true;
};

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加工时记录";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  getWork(_id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改工时记录";
  });
}

/** 下载模板操作 */
function importTemplate() {
  proxy.download("proj/work/importTemplate", {
  }, `user_template_${new Date().getTime()}.xlsx`);
};
/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList();
};
/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
};

/** 提交按钮 */
function submitForm() {
  proxy.$refs["workRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateWork(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addWork(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除工时记录编号为"' + _ids + '"的数据项？').then(function() {
    return delWork(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('proj/work/export', {
    ...queryParams.value
  }, `work_${new Date().getTime()}.xlsx`)
}

getList();
getSysUserList()
getProjectList()
</script>
