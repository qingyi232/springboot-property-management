<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="所属楼栋">
          <el-select v-model="query.buildingId" placeholder="全部" clearable @change="loadData">
            <el-option v-for="b in buildings" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增单元</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="buildingName" label="所属楼栋" />
      <el-table-column prop="name" label="单元名称" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog :title="editForm.id ? '编辑单元' : '新增单元'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="所属楼栋" prop="buildingId">
          <el-select v-model="editForm.buildingId" placeholder="请选择楼栋">
            <el-option v-for="b in buildings" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="单元名称" prop="name">
          <el-input v-model="editForm.name" />
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
import { unitApi, buildingApi } from '@/api'

export default {
  name: 'UnitManage',
  data() {
    return {
      query: { current: 1, size: 10, buildingId: null },
      tableData: [], total: 0, buildings: [],
      dialogVisible: false, editForm: {},
      formRules: {
        buildingId: [{ required: true, message: '请选择楼栋', trigger: 'change' }],
        name: [{ required: true, message: '请输入单元名称', trigger: 'blur' }]
      }
    }
  },
  created() { this.loadBuildings(); this.loadData() },
  methods: {
    async loadBuildings() {
      const res = await buildingApi.all()
      this.buildings = res.data
    },
    async loadData() {
      const res = await unitApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() { this.editForm = {}; this.dialogVisible = true },
    handleEdit(row) { this.editForm = { ...row }; this.dialogVisible = true },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) await unitApi.update(this.editForm)
        else await unitApi.add(this.editForm)
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该单元？', '提示', { type: 'warning' })
      await unitApi.delete(row.id)
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
