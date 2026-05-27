<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="标题">
          <el-input v-model="query.title" placeholder="请输入标题" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">发布公告</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" show-overflow-tooltip />
      <el-table-column prop="creatorName" label="发布人" width="100" />
      <el-table-column prop="createTime" label="发布时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleView(row)">查看</el-button>
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background layout="total, prev, pager, next"
      :total="total" :page-size="query.size" :current-page.sync="query.current"
      @current-change="loadData" class="page-pagination"
    />

    <el-dialog :title="editForm.id ? '编辑公告' : '发布公告'" :visible.sync="dialogVisible" width="600px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="editForm.title" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="editForm.content" type="textarea" :rows="8" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="公告详情" :visible.sync="viewDialogVisible" width="600px">
      <h3 style="margin-bottom: 12px; color: #1a3a2e;">{{ viewData.title }}</h3>
      <p style="color: #8fa8a0; font-size: 13px; margin-bottom: 16px;">发布时间：{{ viewData.createTime }}</p>
      <div style="white-space: pre-wrap; color: #3d6b5a; line-height: 1.8;">{{ viewData.content }}</div>
    </el-dialog>
  </div>
</template>

<script>
import { noticeApi } from '@/api'

export default {
  name: 'NoticeManage',
  data() {
    return {
      query: { current: 1, size: 10, title: '' },
      tableData: [], total: 0,
      dialogVisible: false,
      editForm: {},
      formRules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      },
      viewDialogVisible: false,
      viewData: {}
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await noticeApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() {
      this.editForm = {}
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.editForm = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewData = row
      this.viewDialogVisible = true
    },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) {
          await noticeApi.update(this.editForm)
        } else {
          await noticeApi.add(this.editForm)
        }
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该公告？', '提示', { type: 'warning' })
      await noticeApi.delete(row.id)
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
