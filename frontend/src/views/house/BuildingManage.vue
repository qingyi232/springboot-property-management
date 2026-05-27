<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="楼栋名称">
          <el-input v-model="query.name" placeholder="请输入楼栋名称" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增楼栋</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="楼栋名称" />
      <el-table-column prop="totalFloors" label="总楼层" width="100" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog :title="editForm.id ? '编辑楼栋' : '新增楼栋'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="楼栋名称" prop="name">
          <el-input v-model="editForm.name" />
        </el-form-item>
        <el-form-item label="总楼层" prop="totalFloors">
          <el-input-number v-model="editForm.totalFloors" :min="1" :max="50" />
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
  </div>
</template>

<script>
import { buildingApi } from '@/api'

export default {
  name: 'BuildingManage',
  data() {
    return {
      query: { current: 1, size: 10, name: '' },
      tableData: [], total: 0,
      dialogVisible: false,
      editForm: {},
      formRules: {
        name: [{ required: true, message: '请输入楼栋名称', trigger: 'blur' }],
        totalFloors: [{ required: true, message: '请输入楼层数', trigger: 'blur' }]
      }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await buildingApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() { this.editForm = {}; this.dialogVisible = true },
    handleEdit(row) { this.editForm = { ...row }; this.dialogVisible = true },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) await buildingApi.update(this.editForm)
        else await buildingApi.add(this.editForm)
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该楼栋？', '提示', { type: 'warning' })
      await buildingApi.delete(row.id)
      this.$message.success('删除成功')
      this.loadData()
    }
  }
}
</script>

<style scoped>
.page-container { background: #fff; border-radius: 10px; padding: 20px; }
.page-header { margin-bottom: 16px; }
.page-pagination { margin-top: 16px; text-align: right; }
</style>
