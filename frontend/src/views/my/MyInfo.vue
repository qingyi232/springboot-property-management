<template>
  <div class="page-container">
    <div class="info-card">
      <h3 class="card-title"><i class="el-icon-user"></i> 个人信息</h3>
      <el-form ref="infoForm" :model="form" label-width="80px" style="max-width: 500px; margin-top: 20px;">
        <el-form-item label="账号">
          <el-input v-model="form.username" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="form.password" type="password" placeholder="不修改请留空" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { liveUserApi } from '@/api'

export default {
  name: 'MyInfo',
  data() {
    return { form: {} }
  },
  created() { this.loadInfo() },
  methods: {
    async loadInfo() {
      const res = await liveUserApi.myInfo()
      this.form = res.data || {}
      this.form.password = ''
    },
    async handleSave() {
      await liveUserApi.updateMyInfo(this.form)
      this.$message.success('修改成功')
      this.loadInfo()
    }
  }
}
</script>

<style scoped>
.page-container {
  background: #fff;
  border-radius: 14px;
  padding: 28px;
  border: 1px solid #e8f0ec;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.card-title { color: #1a3a2e; font-size: 16px; padding-bottom: 12px; border-bottom: 1px solid #e8f0ec; }
.card-title i { margin-right: 6px; color: #2d8a6e; }
</style>
