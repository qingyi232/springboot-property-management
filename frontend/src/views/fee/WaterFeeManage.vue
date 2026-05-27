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
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增水费</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="roomNumber" label="房屋编号" />
      <el-table-column prop="userName" label="业主姓名" />
      <el-table-column prop="usageAmount" label="用水量(吨)" width="100" />
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

    <el-dialog :title="editForm.id ? '编辑水费' : '新增水费'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="房屋" prop="houseId">
          <el-select v-model="editForm.houseId" placeholder="请选择房屋" filterable @change="onHouseChange">
            <el-option v-for="r in rooms" :key="r.id" :label="(r.buildingName||'') + ' ' + (r.unitName||'') + ' ' + r.roomNumber" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="业主" prop="userId">
          <el-select v-model="editForm.userId" placeholder="请选择业主" filterable>
            <el-option v-for="u in owners" :key="u.id" :label="u.realName" :value="u.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="用水量(吨)">
          <el-input-number v-model="editForm.usageAmount" :precision="1" :min="0" />
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
import { waterFeeApi, roomApi, liveUserApi } from '@/api'

export default {
  name: 'WaterFeeManage',
  data() {
    return {
      query: { current: 1, size: 10, status: null, feeMonth: '' },
      tableData: [], total: 0,
      dialogVisible: false, editForm: {},
      rooms: [], owners: [],
      formRules: {
        houseId: [{ required: true, message: '请选择房屋', trigger: 'change' }],
        userId: [{ required: true, message: '请选择业主', trigger: 'change' }],
        amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
        feeMonth: [{ required: true, message: '请选择月份', trigger: 'change' }]
      }
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await waterFeeApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    async handleAdd() {
      this.editForm = {}
      const [roomRes, ownerRes] = await Promise.all([roomApi.list({ current: 1, size: 999 }), liveUserApi.all()])
      this.rooms = roomRes.data.records
      this.owners = ownerRes.data
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.editForm = { ...row }
      const [roomRes, ownerRes] = await Promise.all([roomApi.list({ current: 1, size: 999 }), liveUserApi.all()])
      this.rooms = roomRes.data.records
      this.owners = ownerRes.data
      this.dialogVisible = true
    },
    onHouseChange() {},
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) await waterFeeApi.update(this.editForm)
        else await waterFeeApi.add(this.editForm)
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该水费记录？', '提示', { type: 'warning' })
      await waterFeeApi.delete(row.id)
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
