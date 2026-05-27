import Vue from 'vue'
import Vuex from 'vuex'
import { getUserInfo } from '@/api/auth'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
    menus: [],
    permissions: [],
    sidebarCollapse: false
  },
  mutations: {
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    SET_USER_INFO(state, info) {
      state.userInfo = info
      localStorage.setItem('userInfo', JSON.stringify(info))
    },
    SET_MENUS(state, menus) {
      state.menus = menus
    },
    SET_PERMISSIONS(state, permissions) {
      state.permissions = permissions
    },
    TOGGLE_SIDEBAR(state) {
      state.sidebarCollapse = !state.sidebarCollapse
    },
    LOGOUT(state) {
      state.token = ''
      state.userInfo = {}
      state.menus = []
      state.permissions = []
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    }
  },
  actions: {
    async fetchUserInfo({ commit }) {
      const res = await getUserInfo()
      const data = res.data
      commit('SET_USER_INFO', {
        userId: data.userId,
        username: data.username,
        realName: data.realName,
        userType: data.userType
      })
      if (data.menus) {
        commit('SET_MENUS', data.menus)
      }
      if (data.permissions) {
        const perms = data.permissions.map(p => p.authority || p)
        commit('SET_PERMISSIONS', perms)
      }
      return data
    }
  },
  getters: {
    isLoggedIn: state => !!state.token,
    userType: state => state.userInfo.userType,
    realName: state => state.userInfo.realName || state.userInfo.username
  }
})
