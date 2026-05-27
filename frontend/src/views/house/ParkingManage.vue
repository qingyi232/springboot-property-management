<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="车位编号">
          <el-input v-model="query.spotNumber" placeholder="请输入车位编号" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="loadData">
            <el-option label="空闲" :value="0" />
            <el-option label="已绑定" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增车位</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="spotNumber" label="车位编号" />
      <el-table-column prop="area" label="区域" />
      <el-table-column prop="monthlyFee" label="月租金(元)" width="100" />
      <el-table-column label="状态" width="80">
        <template slot-scope="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : 'warning'" size="small">
            {{ row.status === 0 ? '空闲' : '已绑定' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog :title="editForm.id ? '编辑车位' : '新增车位'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="车位编号" prop="spotNumber">
          <el-input v-model="editForm.spotNumber" />
        </el-form-item>
        <el-form-item label="区域" prop="area">
          <el-input v-model="editForm.area" />
        </el-form-item>
        <el-form-item label="月租金">
          <el-input-number v-model="editForm.monthlyFee" :precision="2" :min="0" />
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
import { parkingApi } from '@/api'

export default {
  name: 'ParkingManage',
  data() {
    return {
      query: { current: 1, size: 10, spotNumber: '', status: null },
      tableData: [], total: 0,
      dialogVisible: false, editForm: {},
      formRules: {
        spotNumber: [{ required: true, message: '请输入车位编号', trigger: 'blur' }],
        area: [{ required: true, message: '请输入区域', trigger: 'blur' }]
      }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await parkingApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() { this.editForm = {}; this.dialogVisible = true },
    handleEdit(row) { this.editForm = { ...row }; this.dialogVisible = true },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) await parkingApi.update(this.editForm)
        else await parkingApi.add(this.editForm)
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该车位？', '提示', { type: 'warning' })
      await parkingApi.delete(row.id)
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
