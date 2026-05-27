<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="标题搜索">
          <el-input v-model="query.title" placeholder="请输入关键词" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="notice-list">
      <div
        v-for="item in tableData"
        :key="item.id"
        class="notice-item"
        @click="viewDetail(item)"
      >
        <div class="notice-title">
          <i class="el-icon-bell"></i>
          {{ item.title }}
        </div>
        <div class="notice-time">{{ item.createTime }}</div>
      </div>
      <el-empty v-if="tableData.length === 0" description="暂无公告" />
    </div>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog title="公告详情" :visible.sync="detailVisible" width="600px">
      <h3 style="margin-bottom: 12px; color: #1a3a2e;">{{ detail.title }}</h3>
      <p style="color: #8fa8a0; font-size: 13px; margin-bottom: 16px;">{{ detail.createTime }}</p>
      <div style="white-space: pre-wrap; color: #3d6b5a; line-height: 1.8;">{{ detail.content }}</div>
    </el-dialog>
  </div>
</template>

<script>
import { noticeApi } from '@/api'

export default {
  name: 'MyNotice',
  data() {
    return {
      query: { current: 1, size: 10, title: '' },
      tableData: [], total: 0,
      detailVisible: false, detail: {}
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await noticeApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    viewDetail(item) {
      this.detail = item
      this.detailVisible = true
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
.notice-list { min-height: 200px; }
.notice-item {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f5f2;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  transition: background 0.2s;
}
.notice-item:hover {
  background: #f8fbf9;
}
.notice-title {
  font-size: 14px;
  color: #1a3a2e;
  font-weight: 500;
}
.notice-title i {
  color: #2d8a6e;
  margin-right: 8px;
}
.notice-time {
  font-size: 12px;
  color: #8fa8a0;
}
</style>
