<template>
  <div class="page-container">
    <div class="page-header">
      <el-button type="success" icon="el-icon-plus" @click="handleAdd">提交反馈</el-button>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" show-overflow-tooltip />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template slot-scope="{ row }">
          <el-tag :type="row.status === 0 ? 'warning' : 'success'" size="small">
            {{ row.status === 0 ? '待处理' : '已处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reply" label="回复" show-overflow-tooltip />
      <el-table-column prop="createTime" label="提交时间" width="170" />
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog title="提交反馈" :visible.sync="dialogVisible" width="500px">
      <el-form ref="addForm" :model="addForm" :rules="formRules" label-width="60px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="addForm.title" placeholder="请简要描述问题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="addForm.content" type="textarea" :rows="5" placeholder="请详细描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { complaintApi } from '@/api'

export default {
  name: 'MyComplaint',
  data() {
    return {
      query: { current: 1, size: 10 },
      tableData: [], total: 0,
      dialogVisible: false, addForm: {},
      formRules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await complaintApi.my(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() { this.addForm = {}; this.dialogVisible = true },
    async submitForm() {
      this.$refs.addForm.validate(async valid => {
        if (!valid) return
        await complaintApi.add(this.addForm)
        this.$message.success('提交成功')
        this.dialogVisible = false
        this.loadData()
      })
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
.page-header { margin-bottom: 16px; }
.page-pagination { margin-top: 16px; text-align: right; }
</style>
