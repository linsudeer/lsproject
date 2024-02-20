<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
      <el-form-item label="员工姓名" prop="userId">
        <el-select v-model="queryParams.userId" clearable @keyup.enter="handleQuery" @change="handlerUserChange" filterable placeholder="项目经理">
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
    <el-table v-loading="loading" :data="workList" show-summary>
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column label="员工" align="center" prop="nickName" />
      <el-table-column label="项目" width="200" align="center" prop="projectName" show-overflow-tooltip />
      <el-table-column label="汇总" align="center" prop="total" />
      <el-table-column label="一月" align="center" prop="hours1" />
      <el-table-column label="二月" align="center" prop="hours2" />
      <el-table-column label="三月" align="center" prop="hours3" />
      <el-table-column label="四月" align="center" prop="hours4" />
      <el-table-column label="五月" align="center" prop="hours5" />
      <el-table-column label="六月" align="center" prop="hours6" />
      <el-table-column label="七月" align="center" prop="hours7" />
      <el-table-column label="八月" align="center" prop="hours8" />
      <el-table-column label="九月" align="center" prop="hours9" />
      <el-table-column label="十月" align="center" prop="hours10" />
      <el-table-column label="十一月" align="center" prop="hours11" />
      <el-table-column label="十二月" align="center" prop="hours12" />
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script setup name="Work">
import {listByYear} from "@/api/proj/work";
import {listUser} from "@/api/system/user.js";
import {listProject} from "@/api/proj/project.js";

const { proxy } = getCurrentInstance();

const projectList = ref([]);
const workList = ref([]);
const sysUserList = ref([]);
const loading = ref(true);
const total = ref(0);

const data = reactive({
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
  listByYear(queryParams.value).then(response => {
    workList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}


// 用户变化时
function handlerUserChange() {
  queryParams.value.manager = queryParams.value.userId
  getProjectList()
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
  listUser({userName: query, pageSize: 10000}).then(res => {
    sysUserList.value = res.rows
  })
}

// 查所有项目
function getProjectList() {
  listProject({manager: queryParams.value.manager, pageSize: 10000}).then(response => {
    projectList.value = response.rows;
  });
}

getList();
getSysUserList()
getProjectList()
</script>
