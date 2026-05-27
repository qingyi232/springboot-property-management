<template>
  <div class="dashboard">
    <div class="welcome-banner">
      <div class="banner-text">
        <h2>{{ greeting }}，{{ realName }}</h2>
        <p>欢迎使用翠湖苑物业管理系统</p>
      </div>
      <img src="https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=400&q=80" class="banner-img" />
    </div>

    <template v-if="userType === 'sys'">
      <div class="stat-cards">
        <div class="stat-card" v-for="item in statCards" :key="item.label">
          <div class="stat-icon" :style="{ background: item.color }">
            <i :class="item.icon"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </div>
      </div>

      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="16">
          <div class="panel">
            <div class="panel-header">
              <h3><i class="el-icon-data-line"></i> 月度费用统计</h3>
            </div>
            <div class="panel-body">
              <div ref="feeChart" style="height: 320px;"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="panel">
            <div class="panel-header">
              <h3><i class="el-icon-pie-chart"></i> 房屋使用情况</h3>
            </div>
            <div class="panel-body">
              <div ref="roomChart" style="height: 320px;"></div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="8">
          <div class="panel">
            <div class="panel-header">
              <h3><i class="el-icon-data-analysis"></i> 缴费情况</h3>
            </div>
            <div class="panel-body">
              <div ref="payChart" style="height: 280px;"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="panel">
            <div class="panel-header">
              <h3><i class="el-icon-chat-dot-round"></i> 反馈与维修</h3>
            </div>
            <div class="panel-body">
              <div ref="feedbackChart" style="height: 280px;"></div>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="panel">
            <div class="panel-header">
              <h3><i class="el-icon-warning-outline"></i> 待处理事项</h3>
            </div>
            <div class="panel-body">
              <div class="todo-item" v-for="item in todoItems" :key="item.label">
                <span class="todo-label">{{ item.label }}</span>
                <el-badge :value="item.value" :type="item.value > 0 ? 'danger' : 'info'" />
              </div>
              <div class="info-row" style="margin-top: 16px; padding-top: 12px; border-top: 1px solid #e8f0ec;">
                <span class="info-label">公告总数</span>
                <span class="info-value">{{ stats.noticeCount || 0 }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </template>

    <template v-else>
      <div class="owner-quick-actions">
        <div class="quick-card" @click="$router.push('/my/fee')">
          <i class="el-icon-money"></i>
          <span>我的费用</span>
        </div>
        <div class="quick-card" @click="$router.push('/my/complaint')">
          <i class="el-icon-chat-dot-round"></i>
          <span>提交反馈</span>
        </div>
        <div class="quick-card" @click="$router.push('/my/repair')">
          <i class="el-icon-s-tools"></i>
          <span>在线报修</span>
        </div>
        <div class="quick-card" @click="$router.push('/my/notice')">
          <i class="el-icon-bell"></i>
          <span>查看公告</span>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { dashboardApi } from '@/api'
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'

export default {
  name: 'DashboardView',
  data() {
    return { stats: {} }
  },
  computed: {
    ...mapGetters(['realName', 'userType']),
    greeting() {
      const h = new Date().getHours()
      if (h < 12) return '上午好'
      if (h < 18) return '下午好'
      return '晚上好'
    },
    statCards() {
      return [
        { label: '业主总数', value: this.stats.ownerCount || 0, icon: 'el-icon-user-solid', color: '#2d8a6e' },
        { label: '楼栋总数', value: this.stats.buildingCount || 0, icon: 'el-icon-office-building', color: '#e6954b' },
        { label: '房屋总数', value: this.stats.roomCount || 0, icon: 'el-icon-house', color: '#5b8fc9' },
        { label: '车位总数', value: this.stats.parkingCount || 0, icon: 'el-icon-truck', color: '#c75d5d' }
      ]
    },
    todoItems() {
      return [
        { label: '未缴水费', value: this.stats.unpaidWater || 0 },
        { label: '未缴电费', value: this.stats.unpaidPower || 0 },
        { label: '未缴停车费', value: this.stats.unpaidParking || 0 },
        { label: '待处理反馈', value: this.stats.pendingComplaint || 0 },
        { label: '待处理维修', value: this.stats.pendingRepair || 0 }
      ]
    }
  },
  created() {
    if (this.userType === 'live') {
      this.$router.replace('/owner')
      return
    }
    if (this.userType === 'sys') {
      this.loadStats()
    }
  },
  methods: {
    async loadStats() {
      const res = await dashboardApi.stats()
      this.stats = res.data
      this.$nextTick(() => {
        this.renderFeeChart()
        this.renderRoomChart()
        this.renderPayChart()
        this.renderFeedbackChart()
      })
    },
    renderFeeChart() {
      const chart = echarts.init(this.$refs.feeChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['水费', '电费', '停车费'], bottom: 0 },
        grid: { top: 20, right: 20, bottom: 40, left: 50 },
        xAxis: { type: 'category', data: this.stats.feeMonths || [] },
        yAxis: { type: 'value', name: '金额(元)' },
        color: ['#2d8a6e', '#e6954b', '#5b8fc9'],
        series: [
          { name: '水费', type: 'bar', data: this.stats.waterAmounts || [], barWidth: 20 },
          { name: '电费', type: 'bar', data: this.stats.powerAmounts || [], barWidth: 20 },
          { name: '停车费', type: 'bar', data: this.stats.parkAmounts || [], barWidth: 20 }
        ]
      })
      window.addEventListener('resize', () => chart.resize())
    },
    renderRoomChart() {
      const chart = echarts.init(this.$refs.roomChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        color: ['#2d8a6e', '#e0ebe6', '#e6954b', '#f5e0c8'],
        series: [{
          type: 'pie', radius: ['40%', '65%'],
          label: { formatter: '{b}: {c}' },
          data: [
            { value: this.stats.usedRoom || 0, name: '已入住房屋' },
            { value: this.stats.freeRoom || 0, name: '空闲房屋' },
            { value: this.stats.usedParking || 0, name: '已租车位' },
            { value: this.stats.freeParking || 0, name: '空闲车位' }
          ]
        }]
      })
      window.addEventListener('resize', () => chart.resize())
    },
    renderPayChart() {
      const chart = echarts.init(this.$refs.payChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { bottom: 0 },
        grid: { top: 20, right: 20, bottom: 40, left: 50 },
        xAxis: { type: 'category', data: ['水费', '电费', '停车费'] },
        yAxis: { type: 'value', name: '笔数' },
        color: ['#5ce0b8', '#f5a461'],
        series: [
          {
            name: '已缴', type: 'bar', stack: 'total',
            data: [this.stats.paidWater || 0, this.stats.paidPower || 0, this.stats.paidParking || 0]
          },
          {
            name: '未缴', type: 'bar', stack: 'total',
            data: [this.stats.unpaidWater || 0, this.stats.unpaidPower || 0, this.stats.unpaidParking || 0]
          }
        ]
      })
      window.addEventListener('resize', () => chart.resize())
    },
    renderFeedbackChart() {
      const chart = echarts.init(this.$refs.feedbackChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0 },
        color: ['#f5a461', '#5b8fc9', '#2d8a6e'],
        series: [{
          type: 'pie', radius: '60%',
          label: { formatter: '{b}\n{c}件' },
          data: [
            { value: this.stats.pendingComplaint || 0, name: '待处理反馈' },
            { value: this.stats.doneComplaint || 0, name: '已处理反馈' },
            { value: this.stats.pendingRepair || 0, name: '待处理维修' },
            { value: this.stats.processingRepair || 0, name: '处理中维修' },
            { value: this.stats.doneRepair || 0, name: '已完成维修' }
          ].filter(d => d.value > 0)
        }]
      })
      window.addEventListener('resize', () => chart.resize())
    }
  }
}
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
}
.welcome-banner {
  background: linear-gradient(135deg, #2d8a6e, #3bb896);
  border-radius: 12px;
  padding: 28px 36px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
  overflow: hidden;
}
.banner-text h2 {
  font-size: 22px;
  color: #fff;
  font-weight: 600;
}
.banner-text p {
  color: rgba(255,255,255,0.8);
  margin-top: 8px;
  font-size: 14px;
}
.banner-img {
  width: 160px;
  height: 90px;
  object-fit: cover;
  border-radius: 8px;
  opacity: 0.7;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}
.stat-card {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: transform 0.2s;
}
.stat-card:hover {
  transform: translateY(-2px);
}
.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 22px;
}
.stat-value {
  font-size: 26px;
  font-weight: 700;
  color: #1a3a2e;
}
.stat-label {
  font-size: 13px;
  color: #8fa8a0;
  margin-top: 2px;
}

.panel {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  overflow: hidden;
}
.panel-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e8f0ec;
}
.panel-header h3 {
  font-size: 15px;
  color: #1a3a2e;
  font-weight: 600;
}
.panel-header h3 i {
  margin-right: 6px;
  color: #2d8a6e;
}
.panel-body {
  padding: 16px 20px;
}
.todo-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #f4f8f6;
}
.todo-item:last-child {
  border-bottom: none;
}
.todo-label {
  font-size: 14px;
  color: #3d6b5a;
}
.info-row {
  display: flex;
  justify-content: space-between;
}
.info-label {
  font-size: 13px;
  color: #8fa8a0;
}
.info-value {
  font-size: 13px;
  color: #1a3a2e;
  font-weight: 500;
}

.owner-quick-actions {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}
.quick-card {
  background: #fff;
  border-radius: 12px;
  padding: 40px 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
}
.quick-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(45,138,110,0.12);
}
.quick-card i {
  font-size: 36px;
  color: #2d8a6e;
  display: block;
  margin-bottom: 12px;
}
.quick-card span {
  font-size: 15px;
  color: #1a3a2e;
  font-weight: 500;
}
</style>
