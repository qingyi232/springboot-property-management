<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="缴费状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="loadData">
            <el-option label="未缴费" :value="0" />
            <el-option label="已缴费" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="费用月份">
          <el-date-picker v-model="query.feeMonth" type="month" format="yyyy-MM" value-format="yyyy-MM" placeholder="选择月份" clearable @change="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增停车费</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="spotNumber" label="车位编号" />
      <el-table-column prop="userName" label="业主姓名" />
      <el-table-column prop="amount" label="金额(元)" width="100" />
      <el-table-column prop="feeMonth" label="费用月份" width="100" />
      <el-table-column label="状态" width="80">
        <template slot-scope="{ row }">
          <el-tag :type="row.status === 0 ? 'danger' : 'success'" size="small">
            {{ row.status === 0 ? '未缴费' : '已缴费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="payTime" label="缴费时间" width="170" />
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="{ row }">
          <el-button v-if="row.status === 0" size="mini" type="warning" @click="handleEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 1" size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog :title="editForm.id ? '编辑停车费' : '新增停车费'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="车位" prop="parkingId">
          <el-select v-model="editForm.parkingId" placeholder="请选择车位" filterable>
            <el-option v-for="p in parkings" :key="p.id" :label="p.spotNumber + ' (' + p.area + ')'" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="业主" prop="userId">
          <el-select v-model="editForm.userId" placeholder="请选择业主" filterable>
            <el-option v-for="u in owners" :key="u.id" :label="u.realName" :value="u.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额(元)" prop="amount">
          <el-input-number v-model="editForm.amount" :precision="2" :min="0" />
        </el-form-item>
        <el-form-item label="费用月份" prop="feeMonth">
          <el-date-picker v-model="editForm.feeMonth" type="month" format="yyyy-MM" value-format="yyyy-MM" />
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
import { parkingFeeApi, parkingApi, liveUserApi } from '@/api'

export default {
  name: 'ParkingFeeManage',
  data() {
    return {
      query: { current: 1, size: 10, status: null, feeMonth: '' },
      tableData: [], total: 0,
      dialogVisible: false, editForm: {},
      parkings: [], owners: [],
      formRules: {
        parkingId: [{ required: true, message: '请选择车位', trigger: 'change' }],
        userId: [{ required: true, message: '请选择业主', trigger: 'change' }],
        amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
        feeMonth: [{ required: true, message: '请选择月份', trigger: 'change' }]
      }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await parkingFeeApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    async handleAdd() {
      this.editForm = {}
      const [parkRes, ownerRes] = await Promise.all([parkingApi.list({ current: 1, size: 999 }), liveUserApi.all()])
      this.parkings = parkRes.data.records
      this.owners = ownerRes.data
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.editForm = { ...row }
      const [parkRes, ownerRes] = await Promise.all([parkingApi.list({ current: 1, size: 999 }), liveUserApi.all()])
      this.parkings = parkRes.data.records
      this.owners = ownerRes.data
      this.dialogVisible = true
    },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) await parkingFeeApi.update(this.editForm)
        else await parkingFeeApi.add(this.editForm)
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该停车费记录？', '提示', { type: 'warning' })
      await parkingFeeApi.delete(row.id)
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
