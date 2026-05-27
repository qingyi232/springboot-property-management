import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/views/layout/LayoutView.vue'),
    redirect: () => {
      const userType = store.getters.userType
      return userType === 'live' ? '/owner' : '/dashboard'
    },
    children: [
      { path: 'dashboard', component: () => import('@/views/dashboard/DashboardView.vue'), meta: { title: '首页' } },
      { path: 'system/role', component: () => import('@/views/system/RoleManage.vue'), meta: { title: '角色管理' } },
      { path: 'system/user', component: () => import('@/views/system/UserManage.vue'), meta: { title: '员工管理' } },
      { path: 'system/notice', component: () => import('@/views/system/NoticeManage.vue'), meta: { title: '公告管理' } },
      { path: 'house/building', component: () => import('@/views/house/BuildingManage.vue'), meta: { title: '楼栋管理' } },
      { path: 'house/unit', component: () => import('@/views/house/UnitManage.vue'), meta: { title: '单元管理' } },
      { path: 'house/room', component: () => import('@/views/house/RoomManage.vue'), meta: { title: '房屋管理' } },
      { path: 'house/parking', component: () => import('@/views/house/ParkingManage.vue'), meta: { title: '车位管理' } },
      { path: 'owner/list', component: () => import('@/views/owner/OwnerManage.vue'), meta: { title: '业主管理' } },
      { path: 'fee/water', component: () => import('@/views/fee/WaterFeeManage.vue'), meta: { title: '水费管理' } },
      { path: 'fee/power', component: () => import('@/views/fee/PowerFeeManage.vue'), meta: { title: '电费管理' } },
      { path: 'fee/parking', component: () => import('@/views/fee/ParkingFeeManage.vue'), meta: { title: '停车费管理' } },
      { path: 'feedback/complaint', component: () => import('@/views/feedback/ComplaintManage.vue'), meta: { title: '反馈管理' } },
      { path: 'feedback/repair', component: () => import('@/views/feedback/RepairManage.vue'), meta: { title: '维修管理' } }
    ]
  },
  {
    path: '/owner',
    component: () => import('@/views/layout/OwnerLayout.vue'),
    children: [
      { path: '', component: () => import('@/views/my/OwnerHome.vue'), meta: { title: '业主首页' } },
      { path: '/my/info', component: () => import('@/views/my/MyInfo.vue'), meta: { title: '个人信息' } },
      { path: '/my/fee', component: () => import('@/views/my/MyFee.vue'), meta: { title: '我的费用' } },
      { path: '/my/complaint', component: () => import('@/views/my/MyComplaint.vue'), meta: { title: '我的反馈' } },
      { path: '/my/repair', component: () => import('@/views/my/MyRepair.vue'), meta: { title: '我的报修' } },
      { path: '/my/notice', component: () => import('@/views/my/MyNotice.vue'), meta: { title: '公告查看' } }
    ]
  },
  { path: '*', redirect: '/login' }
]

const router = new VueRouter({
  routes
})

// 修复 Vue Router NavigationDuplicated / NavigationCancelled 错误
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(() => {})
}
const originalReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function replace(location) {
  return originalReplace.call(this, location).catch(() => {})
}

// 业主端路由前缀
const ownerPaths = ['/owner', '/my/']

router.beforeEach((to, from, next) => {
  document.title = (to.meta.title || '物业管理系统') + ' - 翠湖苑物业'
  if (to.path === '/login') {
    next()
  } else {
    if (store.getters.isLoggedIn) {
      const userType = store.getters.userType
      const isOwnerPage = ownerPaths.some(p => to.path.startsWith(p))
      // 业主访问管理端页面 -> 重定向到业主首页
      if (userType === 'live' && !isOwnerPage) {
        next('/owner')
      }
      // 管理员访问业主端页面 -> 重定向到仪表盘
      else if (userType === 'sys' && isOwnerPage) {
        next('/dashboard')
      }
      else {
        next()
      }
    } else {
      next('/login')
    }
  }
})

export default router
