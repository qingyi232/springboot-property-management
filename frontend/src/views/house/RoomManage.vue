<template>
  <div class="page-container">
    <div class="page-header">
      <el-form inline>
        <el-form-item label="房屋编号">
          <el-input v-model="query.roomNumber" placeholder="请输入房屋编号" clearable @clear="loadData" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="loadData">
            <el-option label="空闲" :value="0" />
            <el-option label="已绑定" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">搜索</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增房屋</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="buildingName" label="楼栋" />
      <el-table-column prop="unitName" label="单元" />
      <el-table-column prop="roomNumber" label="房屋编号" />
      <el-table-column prop="area" label="面积(㎡)" width="100" />
      <el-table-column label="状态" width="80">
        <template slot-scope="{ row }">
          <el-tag :type="row.status === 0 ? 'success' : 'warning'" size="small">
            {{ row.status === 0 ? '空闲' : '已绑定' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="{ row }">
          <el-button size="mini" @click="handleEdit(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size" :current-page.sync="query.current" @current-change="loadData" class="page-pagination" />

    <el-dialog :title="editForm.id ? '编辑房屋' : '新增房屋'" :visible.sync="dialogVisible" width="500px">
      <el-form ref="editForm" :model="editForm" :rules="formRules" label-width="80px">
        <el-form-item label="所属楼栋" prop="selectedBuildingId">
          <el-select v-model="editForm.selectedBuildingId" placeholder="请选择楼栋" @change="loadUnits">
            <el-option v-for="b in buildings" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属单元" prop="unitId">
          <el-select v-model="editForm.unitId" placeholder="请选择单元">
            <el-option v-for="u in units" :key="u.id" :label="u.name" :value="u.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="房屋编号" prop="roomNumber">
          <el-input v-model="editForm.roomNumber" />
        </el-form-item>
        <el-form-item label="面积(㎡)">
          <el-input-number v-model="editForm.area" :precision="2" :min="0" />
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
import { roomApi, buildingApi, unitApi } from '@/api'

export default {
  name: 'RoomManage',
  data() {
    return {
      query: { current: 1, size: 10, roomNumber: '', status: null },
      tableData: [], total: 0,
      buildings: [], units: [],
      dialogVisible: false, editForm: {},
      formRules: {
        unitId: [{ required: true, message: '请选择单元', trigger: 'change' }],
        roomNumber: [{ required: true, message: '请输入房屋编号', trigger: 'blur' }]
      }
    }
  },
  created() { this.loadBuildings(); this.loadData() },
  methods: {
    async loadBuildings() {
      const res = await buildingApi.all()
      this.buildings = res.data
    },
    async loadUnits(buildingId) {
      if (!buildingId) { this.units = []; return }
      const res = await unitApi.byBuilding(buildingId)
      this.units = res.data
    },
    async loadData() {
      const res = await roomApi.list(this.query)
      this.tableData = res.data.records
      this.total = res.data.total
    },
    handleAdd() { this.editForm = {}; this.units = []; this.dialogVisible = true },
    async handleEdit(row) {
      this.editForm = { ...row, selectedBuildingId: row.buildingId }
      if (row.buildingId) await this.loadUnits(row.buildingId)
      this.dialogVisible = true
    },
    async submitForm() {
      this.$refs.editForm.validate(async valid => {
        if (!valid) return
        const data = { ...this.editForm }
        delete data.selectedBuildingId
        if (data.id) await roomApi.update(data)
        else await roomApi.add(data)
        this.$message.success('操作成功')
        this.dialogVisible = false
        this.loadData()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该房屋？', '提示', { type: 'warning' })
      await roomApi.delete(row.id)
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
