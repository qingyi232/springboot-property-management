<template>
  <div class="page-container">
    <el-tabs v-model="activeTab" @tab-click="loadData">
      <el-tab-pane label="水费" name="water">
        <el-table :data="waterData" border stripe>
          <el-table-column prop="roomNumber" label="房屋" />
          <el-table-column prop="usageAmount" label="用水量(吨)" width="100" />
          <el-table-column prop="amount" label="金额(元)" width="100" />
          <el-table-column prop="feeMonth" label="月份" width="100" />
          <el-table-column label="状态" width="80">
            <template slot-scope="{ row }">
              <el-tag :type="row.status === 0 ? 'danger' : 'success'" size="small">
                {{ row.status === 0 ? '未缴' : '已缴' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payTime" label="缴费时间" width="170" />
          <el-table-column label="操作" width="100" fixed="right">
            <template slot-scope="{ row }">
              <el-button v-if="row.status === 0" size="mini" type="success" @click="payWater(row)">缴费</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination background layout="total, prev, pager, next" :total="waterTotal" :page-size="10" :current-page.sync="waterPage" @current-change="loadWater" class="page-pagination" />
      </el-tab-pane>

      <el-tab-pane label="电费" name="power">
        <el-table :data="powerData" border stripe>
          <el-table-column prop="roomNumber" label="房屋" />
          <el-table-column prop="usageAmount" label="用电量(度)" width="100" />
          <el-table-column prop="amount" label="金额(元)" width="100" />
          <el-table-column prop="feeMonth" label="月份" width="100" />
          <el-table-column label="状态" width="80">
            <template slot-scope="{ row }">
              <el-tag :type="row.status === 0 ? 'danger' : 'success'" size="small">
                {{ row.status === 0 ? '未缴' : '已缴' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payTime" label="缴费时间" width="170" />
          <el-table-column label="操作" width="100" fixed="right">
            <template slot-scope="{ row }">
              <el-button v-if="row.status === 0" size="mini" type="success" @click="payPower(row)">缴费</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination background layout="total, prev, pager, next" :total="powerTotal" :page-size="10" :current-page.sync="powerPage" @current-change="loadPower" class="page-pagination" />
      </el-tab-pane>

      <el-tab-pane label="停车费" name="parking">
        <el-table :data="parkingData" border stripe>
          <el-table-column prop="spotNumber" label="车位" />
          <el-table-column prop="amount" label="金额(元)" width="100" />
          <el-table-column prop="feeMonth" label="月份" width="100" />
          <el-table-column label="状态" width="80">
            <template slot-scope="{ row }">
              <el-tag :type="row.status === 0 ? 'danger' : 'success'" size="small">
                {{ row.status === 0 ? '未缴' : '已缴' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="payTime" label="缴费时间" width="170" />
          <el-table-column label="操作" width="100" fixed="right">
            <template slot-scope="{ row }">
              <el-button v-if="row.status === 0" size="mini" type="success" @click="payParking(row)">缴费</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination background layout="total, prev, pager, next" :total="parkingTotal" :page-size="10" :current-page.sync="parkingPage" @current-change="loadParking" class="page-pagination" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { waterFeeApi, powerFeeApi, parkingFeeApi } from '@/api'

export default {
  name: 'MyFee',
  data() {
    return {
      activeTab: 'water',
      waterData: [], waterTotal: 0, waterPage: 1,
      powerData: [], powerTotal: 0, powerPage: 1,
      parkingData: [], parkingTotal: 0, parkingPage: 1
    }
  },
  created() { this.loadData() },
  methods: {
    loadData() {
      this.loadWater()
      this.loadPower()
      this.loadParking()
    },
    async loadWater() {
      const res = await waterFeeApi.my({ current: this.waterPage, size: 10 })
      this.waterData = res.data.records
      this.waterTotal = res.data.total
    },
    async loadPower() {
      const res = await powerFeeApi.my({ current: this.powerPage, size: 10 })
      this.powerData = res.data.records
      this.powerTotal = res.data.total
    },
    async loadParking() {
      const res = await parkingFeeApi.my({ current: this.parkingPage, size: 10 })
      this.parkingData = res.data.records
      this.parkingTotal = res.data.total
    },
    async payWater(row) {
      await this.$confirm(`确认缴纳水费 ¥${row.amount}？`, '缴费确认', { type: 'info' })
      await waterFeeApi.pay(row.id)
      this.$message.success('缴费成功')
      this.loadWater()
    },
    async payPower(row) {
      await this.$confirm(`确认缴纳电费 ¥${row.amount}？`, '缴费确认', { type: 'info' })
      await powerFeeApi.pay(row.id)
      this.$message.success('缴费成功')
      this.loadPower()
    },
    async payParking(row) {
      await this.$confirm(`确认缴纳停车费 ¥${row.amount}？`, '缴费确认', { type: 'info' })
      await parkingFeeApi.pay(row.id)
      this.$message.success('缴费成功')
      this.loadParking()
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
.page-pagination { margin-top: 16px; text-align: right; }
</style>
