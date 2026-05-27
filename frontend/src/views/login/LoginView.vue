<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-overlay"></div>
      <img src="https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=1920&q=80" alt="bg" class="bg-image" />
    </div>
    <div class="login-content">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-icon">
            <i class="el-icon-office-building"></i>
          </div>
          <h1>翠湖苑物业管理系统</h1>
          <p class="subtitle">Community Property Management System</p>
        </div>
        <el-form ref="loginForm" :model="form" :rules="rules" class="login-form">
          <el-form-item prop="userType">
            <div class="user-type-switch">
              <div
                :class="['type-btn', form.userType === 'sys' ? 'active' : '']"
                @click="form.userType = 'sys'"
              >
                <i class="el-icon-s-custom"></i> 管理端
              </div>
              <div
                :class="['type-btn', form.userType === 'live' ? 'active' : '']"
                @click="form.userType = 'live'"
              >
                <i class="el-icon-user"></i> 业主端
              </div>
            </div>
          </el-form-item>
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              prefix-icon="el-icon-user"
              placeholder="请输入账号"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              prefix-icon="el-icon-lock"
              placeholder="请输入密码"
              type="password"
              size="large"
              show-password
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              class="login-btn"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登 录' }}
            </el-button>
          </el-form-item>
        </el-form>
        <div class="login-tips">
          <p>管理端账号：admin / 123456</p>
          <p>业主端账号：owner1 / 123456</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/auth'

export default {
  name: 'LoginView',
  data() {
    return {
      loading: false,
      form: {
        username: '',
        password: '',
        userType: 'sys'
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const res = await login(this.form)
          this.$store.commit('SET_TOKEN', res.data.token)
          this.$store.commit('SET_USER_INFO', {
            userId: res.data.userId,
            username: res.data.username,
            realName: res.data.realName,
            userType: res.data.userType
          })
          await this.$store.dispatch('fetchUserInfo')
          this.$message.success('登录成功')
          if (res.data.userType === 'live') {
            this.$router.push('/owner')
          } else {
            this.$router.push('/dashboard')
          }
        } catch (e) {
          // handled by interceptor
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
}
.login-bg {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  z-index: 0;
}
.bg-image {
  width: 100%; height: 100%;
  object-fit: cover;
}
.bg-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(135deg, rgba(16, 78, 55, 0.85), rgba(22, 101, 82, 0.7));
  z-index: 1;
}
.login-content {
  position: relative;
  z-index: 2;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  padding: 48px 40px 36px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}
.login-header {
  text-align: center;
  margin-bottom: 36px;
}
.logo-icon {
  width: 64px; height: 64px;
  margin: 0 auto 16px;
  background: linear-gradient(135deg, #2d8a6e, #3bb896);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: #fff;
}
.login-header h1 {
  font-size: 22px;
  color: #1a3a2e;
  font-weight: 600;
  letter-spacing: 1px;
}
.subtitle {
  font-size: 12px;
  color: #8fa8a0;
  margin-top: 6px;
  letter-spacing: 0.5px;
}
.user-type-switch {
  display: flex;
  background: #f0f7f4;
  border-radius: 8px;
  padding: 4px;
}
.type-btn {
  flex: 1;
  text-align: center;
  padding: 10px 0;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  color: #6b8f83;
  transition: all 0.3s;
}
.type-btn.active {
  background: #2d8a6e;
  color: #fff;
  box-shadow: 0 2px 8px rgba(45, 138, 110, 0.3);
}
.type-btn i {
  margin-right: 4px;
}
.login-form {
  margin-top: 20px;
}
.login-form .el-input >>> .el-input__inner {
  height: 44px;
  border-radius: 8px;
  border: 1.5px solid #e0ebe6;
  font-size: 14px;
}
.login-form .el-input >>> .el-input__inner:focus {
  border-color: #2d8a6e;
}
.login-btn {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(135deg, #2d8a6e, #3bb896);
  border: none;
  letter-spacing: 4px;
}
.login-btn:hover {
  background: linear-gradient(135deg, #26755d, #33a385);
}
.login-tips {
  text-align: center;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #eef4f1;
}
.login-tips p {
  font-size: 12px;
  color: #a0b8ad;
  line-height: 1.8;
}
</style>
