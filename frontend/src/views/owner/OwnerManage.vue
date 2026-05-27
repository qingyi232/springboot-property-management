<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="姓名">
          <el-input v-model="query.realName" placeholder="请输入姓名" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="query.phone" placeholder="请输入手机号" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增业主</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column label="性别" width="60">
        <template slot-scope="{ row }">{{ row.gender === 1 ? '男' : '女' }}</template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="idCard" label="身份证号" show-overflow-tooltip />
      <el-table-column label="状态" width="80">
        <template slot-scope="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="primary" @click="handleBind(row)">绑定管理</el-button>
          <el-button size="mini" type="info" @click="handleResetPwd(row)">重置密码</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <!-- 新增/编辑 -->
    <el-dialog :title="editForm.id ? '编辑业主' : '新增业主'" :visible.sync="dialogVisible" width="550px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="editForm.username" :disabled="!!editForm.id" />
        </el-form-item>
        <el-form-item v-if="!editForm.id" label="密码" prop="password">
          <el-input v-model="editForm.password" type="password" placeholder="默认123456" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="editForm.realName" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="editForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.phone" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="editForm.idCard" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="editForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 绑定管理 -->
    <el-dialog title="绑定管理" :visible.sync="bindDialogVisible" width="600px">
      <h4 style="margin-bottom: 10px; color: #1a3a2e;">已绑定房屋</h4>
      <el-tag v-for="id in bindInfo.houseIds" :key="'h'+id" closable style="margin: 4px" @close="unbindHouse(id)">
        房屋ID: {{ id }}
      </el-tag>
      <div v-if="!bindInfo.houseIds || bindInfo.houseIds.length === 0" style="color: #999; font-size: 13px;">暂无绑定房屋</div>

      <div style="margin-top: 12px;">
        <el-select v-model="selectedHouseId" placeholder="选择空闲房屋绑定" style="width: 300px">
          <el-option v-for="r in freeRooms" :key="r.id" :label="r.buildingName + ' ' + r.unitName + ' ' + r.roomNumber" :value="r.id" />
        </el-select>
        <el-button type="primary" size="small" style="margin-left: 8px" @click="bindHouse">绑定</el-button>
      </div>

      <h4 style="margin: 20px 0 10px; color: #1a3a2e;">已绑定车位</h4>
      <el-tag v-for="id in bindInfo.parkingIds" :key="'p'+id" closable type="warning" style="margin: 4px" @close="unbindParking(id)">
        车位ID: {{ id }}
      </el-tag>
      <div v-if="!bindInfo.parkingIds || bindInfo.parkingIds.length === 0" style="color: #999; font-size: 13px;">暂无绑定车位</div>

      <div style="margin-top: 12px;">
        <el-select v-model="selectedParkingId" placeholder="选择空闲车位绑定" style="width: 300px">
          <el-option v-for="p in freeParkings" :key="p.id" :label="p.spotNumber + ' (' + p.area + ')'" :value="p.id" />
        </el-select>
        <el-button type="primary" size="small" style="margin-left: 8px" @click="bindParking">绑定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { liveUserApi, roomApi, parkingApi } from '@/api'

export default {
  name: 'OwnerManage',
  data() {
    return {
      query: { current: 1, size: 10, realName: '', phone: '' },
      tableData: [], total: 0,
      dialogVisible: false,
      editForm: { gender: 1, status: 1, password: '123456' },
      formRules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      bindDialogVisible: false,
      currentUserId: null,
      bindInfo: { houseIds: [], parkingIds: [] },
      freeRooms: [], freeParkings: [],
      selectedHouseId: null, selectedParkingId: null
    }
  },
  created() { this.loadData() },
  methods: {
    async loadData() {
      const res = await liveUserApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() {
      this.editForm = { gender: 1, status: 1, password: '123456' }
      this.dialogVisible = true
    },
    handleEdit(row) { this.editForm = { ...row }; this.dialogVisible = true },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        if (this.editForm.id) await liveUserApi.update(this.editForm)
        else await liveUserApi.add(this.editForm)
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该业主？', '提示', { type: 'warning' })
      await liveUserApi.delete(row.id)
      this.$message.success('删除成功')
      this.loadData()
    },
    async handleResetPwd(row) {
      await this.$confirm('确认重置该业主密码为123456？', '提示', { type: 'warning' })
      await liveUserApi.resetPwd(row.id)
      this.$message.success('密码已重置为123456')
    },
    async handleBind(row) {
      this.currentUserId = row.id
      this.selectedHouseId = null
      this.selectedParkingId = null
      await this.refreshBindInfo()
      const [roomRes, parkingRes] = await Promise.all([roomApi.free(), parkingApi.free()])
      this.freeRooms = roomRes.data
      this.freeParkings = parkingRes.data
      this.bindDialogVisible = true
    },
    async refreshBindInfo() {
      const res = await liveUserApi.getBindInfo(this.currentUserId)
      this.bindInfo = res.data
    },
    async bindHouse() {
      if (!this.selectedHouseId) return this.$message.warning('请选择房屋')
      await liveUserApi.bindHouse(this.currentUserId, this.selectedHouseId)
      this.$message.success('绑定成功')
      this.selectedHouseId = null
      await this.refreshBindInfo()
      const roomRes = await roomApi.free()
      this.freeRooms = roomRes.data
    },
    async unbindHouse(houseId) {
      await this.$confirm('确认解绑该房屋？', '提示', { type: 'warning' })
      await liveUserApi.unbindHouse(this.currentUserId, houseId)
      this.$message.success('解绑成功')
      await this.refreshBindInfo()
    },
    async bindParking() {
      if (!this.selectedParkingId) return this.$message.warning('请选择车位')
      await liveUserApi.bindParking(this.currentUserId, this.selectedParkingId)
      this.$message.success('绑定成功')
      this.selectedParkingId = null
      await this.refreshBindInfo()
      const parkingRes = await parkingApi.free()
      this.freeParkings = parkingRes.data
    },
    async unbindParking(parkingId) {
      await this.$confirm('确认解绑该车位？', '提示', { type: 'warning' })
      await liveUserApi.unbindParking(this.currentUserId, parkingId)
      this.$message.success('解绑成功')
      await this.refreshBindInfo()
    }
  }
}
</script>

<style scoped>
.page-container { background: #fff; border-radius: 10px; padding: 20px; }
.page-header { margin-bottom: 16px; }
.page-pagination { margin-top: 16px; text-align: right; }
</style>
