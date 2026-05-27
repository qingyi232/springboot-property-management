import request from '@/utils/request'

// 角色
export const roleApi = {
  list: params => request({ url: '/role/list', params }),
  all: () => request({ url: '/role/all' }),
  add: data => request({ url: '/role', method: 'post', data }),
  update: data => request({ url: '/role', method: 'put', data }),
  delete: id => request({ url: `/role/${id}`, method: 'delete' }),
  menuTree: () => request({ url: '/role/menu/tree' }),
  getRoleMenus: roleId => request({ url: `/role/menu/${roleId}` }),
  assignMenus: data => request({ url: '/role/menu', method: 'post', data })
}

// 员工
export const sysUserApi = {
  list: params => request({ url: '/sysuser/list', params }),
  add: data => request({ url: '/sysuser', method: 'post', data }),
  update: data => request({ url: '/sysuser', method: 'put', data }),
  delete: id => request({ url: `/sysuser/${id}`, method: 'delete' }),
  getRoles: userId => request({ url: `/sysuser/roles/${userId}` }),
  assignRoles: data => request({ url: '/sysuser/role', method: 'post', data }),
  resetPwd: id => request({ url: `/sysuser/resetPwd/${id}`, method: 'put' })
}

// 业主
export const liveUserApi = {
  list: params => request({ url: '/liveuser/list', params }),
  all: () => request({ url: '/liveuser/all' }),
  add: data => request({ url: '/liveuser', method: 'post', data }),
  update: data => request({ url: '/liveuser', method: 'put', data }),
  delete: id => request({ url: `/liveuser/${id}`, method: 'delete' }),
  getBindInfo: userId => request({ url: `/liveuser/bindInfo/${userId}` }),
  bindHouse: (userId, houseId) => request({ url: '/liveuser/bindHouse', method: 'post', params: { userId, houseId } }),
  unbindHouse: (userId, houseId) => request({ url: '/liveuser/unbindHouse', method: 'post', params: { userId, houseId } }),
  bindParking: (userId, parkingId) => request({ url: '/liveuser/bindParking', method: 'post', params: { userId, parkingId } }),
  unbindParking: (userId, parkingId) => request({ url: '/liveuser/unbindParking', method: 'post', params: { userId, parkingId } }),
  myInfo: () => request({ url: '/liveuser/myInfo' }),
  updateMyInfo: data => request({ url: '/liveuser/myInfo', method: 'put', data }),
  resetPwd: id => request({ url: `/liveuser/resetPwd/${id}`, method: 'put' })
}

// 楼栋
export const buildingApi = {
  list: params => request({ url: '/building/list', params }),
  all: () => request({ url: '/building/all' }),
  add: data => request({ url: '/building', method: 'post', data }),
  update: data => request({ url: '/building', method: 'put', data }),
  delete: id => request({ url: `/building/${id}`, method: 'delete' })
}

// 单元
export const unitApi = {
  list: params => request({ url: '/unit/list', params }),
  byBuilding: buildingId => request({ url: `/unit/byBuilding/${buildingId}` }),
  add: data => request({ url: '/unit', method: 'post', data }),
  update: data => request({ url: '/unit', method: 'put', data }),
  delete: id => request({ url: `/unit/${id}`, method: 'delete' })
}

// 房屋
export const roomApi = {
  list: params => request({ url: '/room/list', params }),
  free: () => request({ url: '/room/free' }),
  add: data => request({ url: '/room', method: 'post', data }),
  update: data => request({ url: '/room', method: 'put', data }),
  delete: id => request({ url: `/room/${id}`, method: 'delete' })
}

// 车位
export const parkingApi = {
  list: params => request({ url: '/parking/list', params }),
  free: () => request({ url: '/parking/free' }),
  add: data => request({ url: '/parking', method: 'post', data }),
  update: data => request({ url: '/parking', method: 'put', data }),
  delete: id => request({ url: `/parking/${id}`, method: 'delete' })
}

// 水费
export const waterFeeApi = {
  list: params => request({ url: '/fee/water/list', params }),
  my: params => request({ url: '/fee/water/my', params }),
  add: data => request({ url: '/fee/water', method: 'post', data }),
  update: data => request({ url: '/fee/water', method: 'put', data }),
  pay: id => request({ url: `/fee/water/pay/${id}`, method: 'put' }),
  delete: id => request({ url: `/fee/water/${id}`, method: 'delete' })
}

// 电费
export const powerFeeApi = {
  list: params => request({ url: '/fee/power/list', params }),
  my: params => request({ url: '/fee/power/my', params }),
  add: data => request({ url: '/fee/power', method: 'post', data }),
  update: data => request({ url: '/fee/power', method: 'put', data }),
  pay: id => request({ url: `/fee/power/pay/${id}`, method: 'put' }),
  delete: id => request({ url: `/fee/power/${id}`, method: 'delete' })
}

// 停车费
export const parkingFeeApi = {
  list: params => request({ url: '/fee/parking/list', params }),
  my: params => request({ url: '/fee/parking/my', params }),
  add: data => request({ url: '/fee/parking', method: 'post', data }),
  update: data => request({ url: '/fee/parking', method: 'put', data }),
  pay: id => request({ url: `/fee/parking/pay/${id}`, method: 'put' }),
  delete: id => request({ url: `/fee/parking/${id}`, method: 'delete' })
}

// 反馈
export const complaintApi = {
  list: params => request({ url: '/complaint/list', params }),
  my: params => request({ url: '/complaint/my', params }),
  add: data => request({ url: '/complaint', method: 'post', data }),
  handle: data => request({ url: '/complaint/handle', method: 'put', data }),
  delete: id => request({ url: `/complaint/${id}`, method: 'delete' })
}

// 维修
export const repairApi = {
  list: params => request({ url: '/repair/list', params }),
  my: params => request({ url: '/repair/my', params }),
  add: data => request({ url: '/repair', method: 'post', data }),
  handle: data => request({ url: '/repair/handle', method: 'put', data }),
  delete: id => request({ url: `/repair/${id}`, method: 'delete' })
}

// 公告
export const noticeApi = {
  list: params => request({ url: '/notice/list', params }),
  add: data => request({ url: '/notice', method: 'post', data }),
  update: data => request({ url: '/notice', method: 'put', data }),
  delete: id => request({ url: `/notice/${id}`, method: 'delete' }),
  detail: id => request({ url: `/notice/${id}` })
}

// 仪表盘
export const dashboardApi = {
  stats: () => request({ url: '/dashboard/stats' })
}
