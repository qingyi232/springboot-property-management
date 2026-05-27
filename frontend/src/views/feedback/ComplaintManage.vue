<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="处理状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="loadData">
            <el-option label="待处理" :value="0" />
            <el-option label="已处理" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="userName" label="业主姓名" width="100" />
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
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="{ row }">
          <el-button v-if="row.status === 0" size="mini" type="primary" @click="handleReply(row)">处理</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog title="处理反馈" :visible.sync="replyDialogVisible" width="500px">
      <el-form label-width="60px">
        <el-form-item label="标题">
          <span>{{ replyForm.title }}</span>
        </el-form-item>
        <el-form-item label="内容">
          <span>{{ replyForm.content }}</span>
        </el-form-item>
        <el-form-item label="回复">
          <el-input v-model="replyForm.reply" type="textarea" :rows="4" placeholder="请输入回复内容" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">确认处理</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { complaintApi } from '@/api'

export default {
  name: 'ComplaintManage',
  data() {
    return {
      query: { current: 1, size: 10, status: null },
      tableData: [], total: 0,
      replyDialogVisible: false, replyForm: {}
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await complaintApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleReply(row) {
      this.replyForm = { ...row, reply: '' }
      this.replyDialogVisible = true
    },
    async submitReply() {
      if (!this.replyForm.reply) return this.$message.warning('请输入回复内容')
      await complaintApi.handle({ id: this.replyForm.id, reply: this.replyForm.reply })
      this.$message.success('处理成功')
      this.replyDialogVisible = false
      this.loadData()
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该反馈？', '提示', { type: 'warning' })
      await complaintApi.delete(row.id)
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
