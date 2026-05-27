<template>
  <div class="owner-home">
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h1>{{ greeting }}，{{ realName }}</h1>
          <p class="hero-subtitle">欢迎使用翠湖苑智慧物业服务平台</p>
          <div class="hero-badges">
            <span class="badge"><i class="el-icon-phone"></i> 24小时服务</span>
            <span class="badge"><i class="el-icon-star-on"></i> 品质社区</span>
            <span class="badge"><i class="el-icon-location"></i> 智慧物业</span>
          </div>
        </div>
        <div class="hero-image">
          <img src="https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=500&q=80" alt="社区" />
        </div>
      </div>
    </section>

    <section class="quick-services">
      <h2 class="section-title">
        <span class="title-line"></span>
        快捷服务
        <span class="title-line"></span>
      </h2>
      <div class="service-grid">
        <div class="service-card" @click="$router.push('/my/fee')">
          <div class="service-icon" style="background: linear-gradient(135deg, #2d8a6e, #5ce0b8);">
            <i class="el-icon-wallet"></i>
          </div>
          <h3>费用缴纳</h3>
          <p>水费 / 电费 / 停车费</p>
          <div class="service-stat" v-if="stats.unpaidCount > 0">
            <el-badge :value="stats.unpaidCount" class="badge-item">
              <span class="stat-text">待缴费</span>
            </el-badge>
          </div>
          <div class="service-stat" v-else>
            <span class="stat-text stat-ok"><i class="el-icon-circle-check"></i> 已全部缴清</span>
          </div>
        </div>
        <div class="service-card" @click="$router.push('/my/complaint')">
          <div class="service-icon" style="background: linear-gradient(135deg, #e8a838, #f5d078);">
            <i class="el-icon-chat-dot-round"></i>
          </div>
          <h3>投诉建议</h3>
          <p>提交反馈 / 查看进度</p>
          <div class="service-stat">
            <span class="stat-text">已提交 {{ stats.complaintCount }} 条</span>
          </div>
        </div>
        <div class="service-card" @click="$router.push('/my/repair')">
          <div class="service-icon" style="background: linear-gradient(135deg, #5b8def, #8bb8ff);">
            <i class="el-icon-s-tools"></i>
          </div>
          <h3>在线报修</h3>
          <p>提交报修 / 跟踪处理</p>
          <div class="service-stat">
            <span class="stat-text">已提交 {{ stats.repairCount }} 条</span>
          </div>
        </div>
        <div class="service-card" @click="$router.push('/my/notice')">
          <div class="service-icon" style="background: linear-gradient(135deg, #e05f8a, #f5a0b8);">
            <i class="el-icon-bell"></i>
          </div>
          <h3>社区公告</h3>
          <p>查看最新通知消息</p>
          <div class="service-stat">
            <span class="stat-text">{{ stats.noticeCount }} 条公告</span>
          </div>
        </div>
      </div>
    </section>

    <section class="info-section">
      <div class="info-grid">
        <div class="notice-card">
          <div class="card-header">
            <h3><i class="el-icon-bell"></i> 最新公告</h3>
            <a class="more-link" @click="$router.push('/my/notice')">查看全部 →</a>
          </div>
          <div class="notice-list">
            <div
              v-for="item in notices"
              :key="item.id"
              class="notice-item"
              @click="viewNotice(item)"
            >
              <div class="notice-dot"></div>
              <div class="notice-content">
                <span class="notice-title">{{ item.title }}</span>
                <span class="notice-time">{{ item.createTime }}</span>
              </div>
            </div>
            <el-empty v-if="notices.length === 0" description="暂无公告" :image-size="60" />
          </div>
        </div>
        <div class="profile-card">
          <div class="card-header">
            <h3><i class="el-icon-user"></i> 我的信息</h3>
            <a class="more-link" @click="$router.push('/my/info')">编辑 →</a>
          </div>
          <div class="profile-info" v-if="userProfile.username">
            <div class="profile-avatar">
              <el-avatar :size="64" style="background: linear-gradient(135deg, #2d8a6e, #5ce0b8); font-size: 28px;">
                {{ (userProfile.realName || '').charAt(0) }}
              </el-avatar>
            </div>
            <div class="profile-details">
              <div class="detail-row">
                <span class="detail-label">姓名</span>
                <span class="detail-value">{{ userProfile.realName }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">手机</span>
                <span class="detail-value">{{ userProfile.phone || '未填写' }}</span>
              </div>
              <div class="detail-row">
                <span class="detail-label">账号</span>
                <span class="detail-value">{{ userProfile.username }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="community-section">
      <h2 class="section-title">
        <span class="title-line"></span>
        社区风采
        <span class="title-line"></span>
      </h2>
      <div class="community-grid">
        <div class="community-item">
          <img src="https://images.unsplash.com/photo-1582268611958-ebfd161ef9cf?w=400&q=80" alt="社区花园" />
          <div class="community-overlay">
            <span>社区花园</span>
          </div>
        </div>
        <div class="community-item">
          <img src="https://images.unsplash.com/photo-1558618666-fcd25c85f82e?w=400&q=80" alt="休闲设施" />
          <div class="community-overlay">
            <span>休闲设施</span>
          </div>
        </div>
        <div class="community-item">
          <img src="https://images.unsplash.com/photo-1574362848149-11496d93a7c7?w=400&q=80" alt="停车场" />
          <div class="community-overlay">
            <span>智慧停车</span>
          </div>
        </div>
        <div class="community-item">
          <img src="https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=400&q=80" alt="社区环境" />
          <div class="community-overlay">
            <span>品质生活</span>
          </div>
        </div>
      </div>
    </section>

    <el-dialog title="公告详情" :visible.sync="noticeVisible" width="600px">
      <h3 style="margin-bottom: 12px; color: #1a3a2e;">{{ noticeDetail.title }}</h3>
      <p style="color: #8fa8a0; font-size: 13px; margin-bottom: 16px;">{{ noticeDetail.createTime }}</p>
      <div style="white-space: pre-wrap; color: #3d6b5a; line-height: 1.8;">{{ noticeDetail.content }}</div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { noticeApi, liveUserApi, waterFeeApi, powerFeeApi, parkingFeeApi, complaintApi, repairApi } from '@/api'

export default {
  name: 'OwnerHome',
  data() {
    return {
      notices: [],
      userProfile: {},
      noticeVisible: false,
      noticeDetail: {},
      stats: {
        unpaidCount: 0,
        complaintCount: 0,
        repairCount: 0,
        noticeCount: 0
      }
    }
  },
  computed: {
    ...mapGetters(['realName']),
    greeting() {
      const h = new Date().getHours()
      if (h < 6) return '夜深了'
      if (h < 12) return '早上好'
      if (h < 14) return '中午好'
      if (h < 18) return '下午好'
      return '晚上好'
    }
  },
  created() {
    this.loadAll()
  },
  methods: {
    async loadAll() {
      try {
        const [noticeRes, profileRes] = await Promise.all([
          noticeApi.list({ current: 1, size: 5 }),
          liveUserApi.myInfo()
        ])
        this.notices = noticeRes.data.records || []
        this.stats.noticeCount = noticeRes.data.total || 0
        this.userProfile = profileRes.data || {}
      } catch (e) { /* ignore */ }

      try {
        const [waterRes, powerRes, parkRes, compRes, repRes] = await Promise.all([
          waterFeeApi.my({ current: 1, size: 100 }),
          powerFeeApi.my({ current: 1, size: 100 }),
          parkingFeeApi.my({ current: 1, size: 100 }),
          complaintApi.my({ current: 1, size: 1 }),
          repairApi.my({ current: 1, size: 1 })
        ])
        const unpaidWater = (waterRes.data.records || []).filter(r => r.status === 0).length
        const unpaidPower = (powerRes.data.records || []).filter(r => r.status === 0).length
        const unpaidPark = (parkRes.data.records || []).filter(r => r.status === 0).length
        this.stats.unpaidCount = unpaidWater + unpaidPower + unpaidPark
        this.stats.complaintCount = compRes.data.total || 0
        this.stats.repairCount = repRes.data.total || 0
      } catch (e) { /* ignore */ }
    },
    viewNotice(item) {
      this.noticeDetail = item
      this.noticeVisible = true
    }
  }
}
</script>

<style scoped>
.owner-home {
  padding-bottom: 20px;
}

.hero-section {
  background: linear-gradient(135deg, #1a5c42, #2d8a6e, #3bb896);
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 32px;
  overflow: hidden;
}

.hero-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.hero-text {
  flex: 1;
}

.hero-text h1 {
  font-size: 28px;
  color: #fff;
  font-weight: 700;
  margin: 0 0 8px;
}

.hero-subtitle {
  font-size: 15px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0 0 20px;
}

.hero-badges {
  display: flex;
  gap: 12px;
}

.badge {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(8px);
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  color: #fff;
}

.badge i {
  margin-right: 4px;
}

.hero-image {
  width: 200px;
  height: 140px;
  border-radius: 12px;
  overflow: hidden;
  flex-shrink: 0;
  margin-left: 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.section-title {
  text-align: center;
  font-size: 20px;
  color: #1a3a2e;
  margin: 0 0 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.title-line {
  width: 40px;
  height: 2px;
  background: linear-gradient(90deg, transparent, #2d8a6e);
}

.title-line:last-child {
  background: linear-gradient(90deg, #2d8a6e, transparent);
}

.quick-services {
  margin-bottom: 32px;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.service-card {
  background: #fff;
  border-radius: 14px;
  padding: 28px 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e8f0ec;
}

.service-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(45, 138, 110, 0.12);
  border-color: #c8e0d6;
}

.service-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
}

.service-icon i {
  font-size: 26px;
  color: #fff;
}

.service-card h3 {
  font-size: 16px;
  color: #1a3a2e;
  margin: 0 0 6px;
  font-weight: 600;
}

.service-card p {
  font-size: 12px;
  color: #8fa8a0;
  margin: 0 0 12px;
}

.service-stat {
  margin-top: 8px;
}

.stat-text {
  font-size: 12px;
  color: #8fa8a0;
}

.stat-ok {
  color: #2d8a6e;
}

.info-section {
  margin-bottom: 32px;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.notice-card, .profile-card {
  background: #fff;
  border-radius: 14px;
  padding: 24px;
  border: 1px solid #e8f0ec;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f5f2;
}

.card-header h3 {
  font-size: 15px;
  color: #1a3a2e;
  margin: 0;
  font-weight: 600;
}

.card-header h3 i {
  margin-right: 6px;
  color: #2d8a6e;
}

.more-link {
  font-size: 13px;
  color: #2d8a6e;
  cursor: pointer;
}

.more-link:hover {
  text-decoration: underline;
}

.notice-list {
  min-height: 120px;
}

.notice-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px 0;
  cursor: pointer;
  transition: background 0.2s;
  border-radius: 6px;
  padding: 8px 10px;
}

.notice-item:hover {
  background: #f8fbf9;
}

.notice-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #2d8a6e;
  margin-top: 7px;
  flex-shrink: 0;
}

.notice-content {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notice-title {
  font-size: 13px;
  color: #3d6b5a;
  font-weight: 500;
}

.notice-time {
  font-size: 12px;
  color: #a8c4b8;
  flex-shrink: 0;
  margin-left: 12px;
}

.profile-info {
  display: flex;
  align-items: center;
  gap: 24px;
  padding: 12px 0;
}

.profile-details {
  flex: 1;
}

.detail-row {
  display: flex;
  align-items: center;
  padding: 6px 0;
}

.detail-label {
  width: 48px;
  font-size: 13px;
  color: #8fa8a0;
  flex-shrink: 0;
}

.detail-value {
  font-size: 14px;
  color: #1a3a2e;
  font-weight: 500;
}

.community-section {
  margin-bottom: 16px;
}

.community-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.community-item {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  height: 160px;
  cursor: pointer;
}

.community-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
}

.community-item:hover img {
  transform: scale(1.08);
}

.community-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 12px 16px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
}

.community-overlay span {
  color: #fff;
  font-size: 14px;
  font-weight: 500;
}
</style>
