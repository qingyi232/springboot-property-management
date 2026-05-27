<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="姓名">
          <el-input v-model="query.realName" placeholder="请输入姓名" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="query.phone" placeholder="请输入手机号" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增员工</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="状态" width="80">
        <template slot-scope="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="300" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="warning" @click="handleAssignRole(row)">分配角色</el-button>
          <el-button size="mini" type="info" @click="handleResetPwd(row)">重置密码</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background layout="total, prev, pager, next"
      :total="total" :page-size="query.size" :current-page.sync="query.current"
      @current-change="loadData" class="page-pagination"
    />

    <el-dialog :title="editForm.id ? '编辑员工' : '新增员工'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="editForm.username" :disabled="!!editForm.id" />
        </el-form-item>
        <el-form-item v-if="!editForm.id" label="密码" prop="password">
          <el-input v-model="editForm.password" type="password" placeholder="默认123456" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="editForm.realName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="editForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="分配角色" :visible.sync="roleDialogVisible" width="500px">
      <el-checkbox-group v-model="selectedRoleIds">
        <el-checkbox v-for="role in allRoles" :key="role.id" :label="role.id">
          {{ role.roleName }}
        </el-checkbox>
      </el-checkbox-group>
      <div slot="footer">
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRoles">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { sysUserApi, roleApi } from '@/api'

export default {
  name: 'UserManage',
  data() {
    return {
      query: { current: 1, size: 10, realName: '', phone: '' },
      tableData: [], total: 0,
      dialogVisible: false,
      editForm: { status: 1 },
      formRules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      roleDialogVisible: false,
      allRoles: [],
      selectedRoleIds: [],
      currentUserId: null
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await sysUserApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() {
      this.editForm = { status: 1, password: '123456' }
      this.dialogVisible = true
      this.$nextTick(() => this.$refs.editForm && this.$refs.editForm.resetFields())
    },
    handleEdit(row) {
      this.editForm = { ...row }
      this.dialogVisible = true
    },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) {
          await sysUserApi.update(this.editForm)
        } else {
          await sysUserApi.add(this.editForm)
        }
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该员工？', '提示', { type: 'warning' })
      await sysUserApi.delete(row.id)
      this.$message.success('删除成功')
      this.loadData()
    },
    async handleResetPwd(row) {
      await this.$confirm('确认重置该员工密码为123456？', '提示', { type: 'warning' })
      await sysUserApi.resetPwd(row.id)
      this.$message.success('密码已重置为123456')
    },
    async handleAssignRole(row) {
      this.currentUserId = row.id
      const [rolesRes, userRolesRes] = await Promise.all([
        roleApi.all(),
        sysUserApi.getRoles(row.id)
      ])
      this.allRoles = rolesRes.data
      this.selectedRoleIds = userRolesRes.data
      this.roleDialogVisible = true
    },
    async submitRoles() {
      await sysUserApi.assignRoles({ userId: this.currentUserId, roleIds: this.selectedRoleIds })
      this.$message.success('角色分配成功')
      this.roleDialogVisible = false
    }
  }
}
</script>

<style scoped>
.page-container { background: #fff; border-radius: 10px; padding: 20px; }
.page-header { margin-bottom: 16px; }
.page-pagination { margin-top: 16px; text-align: right; }
</style>
