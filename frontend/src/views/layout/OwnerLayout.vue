<template>
  <div class="owner-layout">
    <header class="owner-header">
      <div class="header-inner">
        <div class="header-left">
          <div class="logo" @click="$router.push('/owner')">
            <i class="el-icon-office-building"></i>
            <span class="logo-text">翠湖苑物业</span>
          </div>
          <nav class="nav-links">
            <router-link to="/owner" class="nav-item" exact>
              <i class="el-icon-s-home"></i> 首页
            </router-link>
            <router-link to="/my/fee" class="nav-item">
              <i class="el-icon-money"></i> 我的费用
            </router-link>
            <router-link to="/my/complaint" class="nav-item">
              <i class="el-icon-chat-dot-round"></i> 我的反馈
            </router-link>
            <router-link to="/my/repair" class="nav-item">
              <i class="el-icon-s-tools"></i> 我的报修
            </router-link>
            <router-link to="/my/notice" class="nav-item">
              <i class="el-icon-bell"></i> 公告信息
            </router-link>
          </nav>
        </div>
        <div class="header-right">
          <div class="user-area">
            <el-avatar :size="32" style="background: #2d8a6e;">
              <i class="el-icon-user-solid" style="font-size: 16px;"></i>
            </el-avatar>
            <el-dropdown @command="handleCommand" trigger="click">
              <span class="user-name">
                {{ realName }} <i class="el-icon-arrow-down"></i>
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="info" icon="el-icon-user">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout" icon="el-icon-switch-button" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </div>
    </header>
    <main class="owner-main">
      <router-view />
    </main>
    <footer class="owner-footer">
      <div class="footer-inner">
        <p>翠湖苑物业管理系统 &copy; 2026 | 服务热线：400-888-9999</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'OwnerLayout',
  computed: {
    ...mapGetters(['realName', 'userType'])
  },
  created() {
    if (this.$store.state.token) {
      this.$store.dispatch('fetchUserInfo').then(() => {
        if (this.userType === 'sys') {
          this.$router.replace('/dashboard')
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
      } else if (cmd === 'info') {
        this.$router.push('/my/info')
      }
    }
  }
}
</script>

<style scoped>
.owner-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f8f6;
}

.owner-header {
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 40px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  flex-shrink: 0;
}

.logo i {
  font-size: 28px;
  color: #2d8a6e;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #1a3a2e;
  letter-spacing: 1px;
}

.nav-links {
  display: flex;
  gap: 6px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
  border-radius: 8px;
  color: #5a7a6e;
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.25s;
}

.nav-item:hover {
  background: #f0f7f4;
  color: #2d8a6e;
}

.nav-item.router-link-active {
  background: linear-gradient(135deg, #2d8a6e, #3bb896);
  color: #fff;
  box-shadow: 0 3px 10px rgba(45, 138, 110, 0.3);
}

.nav-item i {
  font-size: 15px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  font-size: 14px;
  color: #3d6b5a;
  cursor: pointer;
  font-weight: 500;
}

.user-name:hover {
  color: #2d8a6e;
}

.owner-main {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 24px;
}

.owner-footer {
  background: #1a3a2e;
  padding: 20px 0;
  margin-top: auto;
}

.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.footer-inner p {
  color: #8fb8a8;
  font-size: 13px;
  margin: 0;
}
</style>
