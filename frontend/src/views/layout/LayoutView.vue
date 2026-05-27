<template>
  <el-container class="layout-container">
    <el-aside :width="sidebarCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo-area">
        <i class="el-icon-office-building logo-icon"></i>
        <span v-show="!sidebarCollapse" class="logo-text">翠湖苑物业</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="sidebarCollapse"
        :unique-opened="true"
        background-color="#1a3a2e"
        text-color="#b8d4c8"
        active-text-color="#5ce0b8"
        router
        class="sidebar-menu"
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-home"></i>
          <span slot="title">首页概览</span>
        </el-menu-item>
        <template v-if="userType === 'sys'">
          <template v-for="menu in sysMenus">
            <el-submenu v-if="menu.children && menu.children.length" :key="menu.path" :index="menu.path">
              <template slot="title">
                <i :class="menu.icon"></i>
                <span>{{ menu.name }}</span>
              </template>
              <el-menu-item
                v-for="child in menu.children.filter(c => c.menuType !== 2 && c.visible === 1)"
                :key="child.path"
                :index="child.path"
              >
                <i :class="child.icon"></i>
                <span>{{ child.name }}</span>
              </el-menu-item>
            </el-submenu>
          </template>
        </template>
        <template v-else>
          <el-menu-item index="/my/fee">
            <i class="el-icon-money"></i>
            <span slot="title">我的费用</span>
          </el-menu-item>
          <el-menu-item index="/my/complaint">
            <i class="el-icon-chat-dot-round"></i>
            <span slot="title">我的反馈</span>
          </el-menu-item>
          <el-menu-item index="/my/repair">
            <i class="el-icon-s-tools"></i>
            <span slot="title">我的报修</span>
          </el-menu-item>
          <el-menu-item index="/my/notice">
            <i class="el-icon-bell"></i>
            <span slot="title">公告查看</span>
          </el-menu-item>
          <el-menu-item index="/my/info">
            <i class="el-icon-user"></i>
            <span slot="title">个人信息</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    <el-container class="layout-main">
      <el-header class="layout-header">
        <div class="header-left">
          <i
            :class="sidebarCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"
            class="collapse-btn"
            @click="$store.commit('TOGGLE_SIDEBAR')"
          ></i>
          <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item>{{ $route.meta.title || '首页' }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <span class="user-name">
            <i class="el-icon-user-solid"></i>
            {{ realName }}
          </span>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout" icon="el-icon-switch-button">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="layout-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters, mapState } from 'vuex'

export default {
  name: 'LayoutView',
  computed: {
    ...mapGetters(['userType', 'realName']),
    ...mapState(['sidebarCollapse', 'menus']),
    activeMenu() {
      return this.$route.path
    },
    sysMenus() {
      return this.menus.filter(m => m.menuType !== 2 && m.visible === 1)
    }
  },
  created() {
    if (this.$store.state.token) {
      this.$store.dispatch('fetchUserInfo').then(() => {
        if (this.$store.getters.userType === 'live') {
          this.$router.replace('/owner')
        }
      }).catch(() => {
        this.$store.commit('LOGOUT')
        this.$router.push('/login')
      })
    }
  },
  methods: {
    handleCommand(cmd) {
      if (cmd === 'logout') {
        this.$store.commit('LOGOUT')
        this.$router.push('/login')
        this.$message.success('已退出登录')
      }
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.layout-aside {
  background: #1a3a2e;
  transition: width 0.3s;
  overflow: hidden;
}
.logo-area {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255,255,255,0.08);
}
.logo-icon {
  font-size: 28px;
  color: #5ce0b8;
}
.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #e8f5f0;
  margin-left: 10px;
  white-space: nowrap;
  letter-spacing: 1px;
}
.sidebar-menu {
  border-right: none !important;
}
.sidebar-menu >>> .el-menu-item.is-active {
  background: rgba(92, 224, 184, 0.12) !important;
  border-right: 3px solid #5ce0b8;
}
.sidebar-menu >>> .el-submenu__title:hover,
.sidebar-menu >>> .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.06) !important;
}

.layout-header {
  background: #fff;
  border-bottom: 1px solid #e8f0ec;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}
.header-left {
  display: flex;
  align-items: center;
}
.collapse-btn {
  font-size: 20px;
  color: #2d8a6e;
  cursor: pointer;
  margin-right: 16px;
}
.breadcrumb {
  font-size: 14px;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-name {
  font-size: 14px;
  color: #3d6b5a;
}
.el-dropdown-link {
  cursor: pointer;
  color: #3d6b5a;
}

.layout-content {
  background: #f4f8f6;
  padding: 20px;
}
</style>
