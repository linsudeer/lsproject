import request from '@/utils/request'

// 查询项目管理列表
export function listProject(query) {
  return request({
    url: '/proj/project/list',
    method: 'get',
    params: query
  })
}

// 查询项目管理详细
export function getProject(id) {
  return request({
    url: '/proj/project/' + id,
    method: 'get'
  })
}

// 新增项目管理
export function addProject(data) {
  return request({
    url: '/proj/project',
    method: 'post',
    data: data
  })
}

// 修改项目管理
export function updateProject(data) {
  return request({
    url: '/proj/project',
    method: 'put',
    data: data
  })
}

// 删除项目管理
export function delProject(id) {
  return request({
    url: '/proj/project/' + id,
    method: 'delete'
  })
}
