import request from '@/utils/request'

// 查询工时记录列表
export function listWork(query) {
  return request({
    url: '/proj/work/list',
    method: 'get',
    params: query
  })
}

// 查询一年工时记录列表
export function listByYear(query) {
  return request({
    url: '/proj/work/listByYear',
    method: 'get',
    params: query
  })
}

// 查询工时记录详细
export function getWork(id) {
  return request({
    url: '/proj/work/' + id,
    method: 'get'
  })
}

// 新增工时记录
export function addWork(data) {
  return request({
    url: '/proj/work',
    method: 'post',
    data: data
  })
}

// 修改工时记录
export function updateWork(data) {
  return request({
    url: '/proj/work',
    method: 'put',
    data: data
  })
}

// 删除工时记录
export function delWork(id) {
  return request({
    url: '/proj/work/' + id,
    method: 'delete'
  })
}
