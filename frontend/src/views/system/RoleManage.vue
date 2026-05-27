<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="角色名称">
          <el-input v-model="query.roleName" placeholder="请输入角色名称" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe class="page-table">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="260" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="warning" @click="handleAssignMenu(row)">分配权限</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="total, prev, pager, next"
      :total="total"
      :page-size="query.size"
      :current-page.sync="query.current"
      @current-change="loadData"
      class="page-pagination"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="editForm.id ? '编辑角色' : '新增角色'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="editForm.roleName" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="editForm.roleCode" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog title="分配权限" :visible.sync="menuDialogVisible" width="500px">
      <el-tree
        ref="menuTree"
        :data="menuTree"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedMenuIds"
        :props="{ label: 'name', children: 'children' }"
      />
      <div slot="footer">
        <el-button @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitMenus">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { roleApi } from '@/api'

export default {
  name: 'RoleManage',
  data() {
    return {
      query: { current: 1, size: 10, roleName: '' },
      tableData: [],
      total: 0,
      dialogVisible: false,
      editForm: {},
      formRules: {
        roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
      },
      menuDialogVisible: false,
      menuTree: [],
      checkedMenuIds: [],
      currentRoleId: null
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await roleApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() {
      this.editForm = {}
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
          await roleApi.update(this.editForm)
        } else {
          await roleApi.add(this.editForm)
        }
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该角色？', '提示', { type: 'warning' })
      await roleApi.delete(row.id)
      this.$message.success('删除成功')
      this.loadData()
    },
    async handleAssignMenu(row) {
      this.currentRoleId = row.id
      const [treeRes, checkedRes] = await Promise.all([
        roleApi.menuTree(),
        roleApi.getRoleMenus(row.id)
      ])
      this.menuTree = treeRes.data
      this.checkedMenuIds = checkedRes.data
      this.menuDialogVisible = true
    },
    async submitMenus() {
      const checkedKeys = this.$refs.menuTree.getCheckedKeys()
      const halfCheckedKeys = this.$refs.menuTree.getHalfCheckedKeys()
      const menuIds = [...checkedKeys, ...halfCheckedKeys]
      await roleApi.assignMenus({ roleId: this.currentRoleId, menuIds })
      this.$message.success('权限分配成功')
      this.menuDialogVisible = false
    }
  }
}
</script>

<style scoped>
.page-container { background: #fff; border-radius: 10px; padding: 20px; }
.page-header { margin-bottom: 16px; }
.page-table { width: 100%; }
.page-pagination { margin-top: 16px; text-align: right; }
</style>
