USE property_db;

-- ========== 菜单/权限数据 ==========
INSERT INTO sys_menu (id, parent_id, name, path, component, permission, icon, sort_order, menu_type, visible) VALUES
-- 系统管理
(1, 0, '系统管理', '/system', NULL, NULL, 'el-icon-setting', 1, 0, 1),
(2, 1, '角色管理', '/system/role', 'system/RoleManage', 'sys:role:list', 'el-icon-user', 1, 1, 1),
(3, 1, '员工管理', '/system/user', 'system/UserManage', 'sys:user:list', 'el-icon-s-custom', 2, 1, 1),
(4, 1, '公告管理', '/system/notice', 'system/NoticeManage', 'sys:notice:list', 'el-icon-bell', 3, 1, 1),
-- 楼房车位管理
(10, 0, '楼房车位管理', '/house', NULL, NULL, 'el-icon-office-building', 2, 0, 1),
(11, 10, '楼栋管理', '/house/building', 'house/BuildingManage', 'house:building:list', 'el-icon-school', 1, 1, 1),
(12, 10, '单元管理', '/house/unit', 'house/UnitManage', 'house:unit:list', 'el-icon-menu', 2, 1, 1),
(13, 10, '房屋管理', '/house/room', 'house/RoomManage', 'house:room:list', 'el-icon-house', 3, 1, 1),
(14, 10, '车位管理', '/house/parking', 'house/ParkingManage', 'house:parking:list', 'el-icon-truck', 4, 1, 1),
-- 业主信息管理
(20, 0, '业主信息管理', '/owner', NULL, NULL, 'el-icon-user-solid', 3, 0, 1),
(21, 20, '业主列表', '/owner/list', 'owner/OwnerManage', 'owner:list', 'el-icon-s-check', 1, 1, 1),
-- 收费管理
(30, 0, '收费管理', '/fee', NULL, NULL, 'el-icon-money', 4, 0, 1),
(31, 30, '水费管理', '/fee/water', 'fee/WaterFeeManage', 'fee:water:list', 'el-icon-coffee-cup', 1, 1, 1),
(32, 30, '电费管理', '/fee/power', 'fee/PowerFeeManage', 'fee:power:list', 'el-icon-lightning', 2, 1, 1),
(33, 30, '停车费管理', '/fee/parking', 'fee/ParkingFeeManage', 'fee:parking:list', 'el-icon-bicycle', 3, 1, 1),
-- 反馈维修管理
(40, 0, '反馈维修管理', '/feedback', NULL, NULL, 'el-icon-chat-dot-round', 5, 0, 1),
(41, 40, '反馈管理', '/feedback/complaint', 'feedback/ComplaintManage', 'feedback:complaint:list', 'el-icon-chat-line-round', 1, 1, 1),
(42, 40, '维修管理', '/feedback/repair', 'feedback/RepairManage', 'feedback:repair:list', 'el-icon-s-tools', 2, 1, 1),
-- 按钮级权限
(50, 2, '新增角色', NULL, NULL, 'sys:role:add', NULL, 1, 2, 0),
(51, 2, '编辑角色', NULL, NULL, 'sys:role:edit', NULL, 2, 2, 0),
(52, 2, '删除角色', NULL, NULL, 'sys:role:delete', NULL, 3, 2, 0),
(53, 3, '新增员工', NULL, NULL, 'sys:user:add', NULL, 1, 2, 0),
(54, 3, '编辑员工', NULL, NULL, 'sys:user:edit', NULL, 2, 2, 0),
(55, 3, '删除员工', NULL, NULL, 'sys:user:delete', NULL, 3, 2, 0),
(56, 4, '新增公告', NULL, NULL, 'sys:notice:add', NULL, 1, 2, 0),
(57, 4, '编辑公告', NULL, NULL, 'sys:notice:edit', NULL, 2, 2, 0),
(58, 4, '删除公告', NULL, NULL, 'sys:notice:delete', NULL, 3, 2, 0),
(60, 11, '新增楼栋', NULL, NULL, 'house:building:add', NULL, 1, 2, 0),
(61, 11, '编辑楼栋', NULL, NULL, 'house:building:edit', NULL, 2, 2, 0),
(62, 11, '删除楼栋', NULL, NULL, 'house:building:delete', NULL, 3, 2, 0),
(63, 12, '新增单元', NULL, NULL, 'house:unit:add', NULL, 1, 2, 0),
(64, 12, '编辑单元', NULL, NULL, 'house:unit:edit', NULL, 2, 2, 0),
(65, 12, '删除单元', NULL, NULL, 'house:unit:delete', NULL, 3, 2, 0),
(66, 13, '新增房屋', NULL, NULL, 'house:room:add', NULL, 1, 2, 0),
(67, 13, '编辑房屋', NULL, NULL, 'house:room:edit', NULL, 2, 2, 0),
(68, 13, '删除房屋', NULL, NULL, 'house:room:delete', NULL, 3, 2, 0),
(69, 14, '新增车位', NULL, NULL, 'house:parking:add', NULL, 1, 2, 0),
(70, 14, '编辑车位', NULL, NULL, 'house:parking:edit', NULL, 2, 2, 0),
(71, 14, '删除车位', NULL, NULL, 'house:parking:delete', NULL, 3, 2, 0),
(72, 21, '新增业主', NULL, NULL, 'owner:add', NULL, 1, 2, 0),
(73, 21, '编辑业主', NULL, NULL, 'owner:edit', NULL, 2, 2, 0),
(74, 21, '删除业主', NULL, NULL, 'owner:delete', NULL, 3, 2, 0),
(75, 31, '新增水费', NULL, NULL, 'fee:water:add', NULL, 1, 2, 0),
(76, 31, '编辑水费', NULL, NULL, 'fee:water:edit', NULL, 2, 2, 0),
(77, 31, '删除水费', NULL, NULL, 'fee:water:delete', NULL, 3, 2, 0),
(78, 32, '新增电费', NULL, NULL, 'fee:power:add', NULL, 1, 2, 0),
(79, 32, '编辑电费', NULL, NULL, 'fee:power:edit', NULL, 2, 2, 0),
(80, 32, '删除电费', NULL, NULL, 'fee:power:delete', NULL, 3, 2, 0),
(81, 33, '新增停车费', NULL, NULL, 'fee:parking:add', NULL, 1, 2, 0),
(82, 33, '编辑停车费', NULL, NULL, 'fee:parking:edit', NULL, 2, 2, 0),
(83, 33, '删除停车费', NULL, NULL, 'fee:parking:delete', NULL, 3, 2, 0),
(84, 41, '处理反馈', NULL, NULL, 'feedback:complaint:handle', NULL, 1, 2, 0),
(85, 41, '删除反馈', NULL, NULL, 'feedback:complaint:delete', NULL, 2, 2, 0),
(86, 42, '处理维修', NULL, NULL, 'feedback:repair:handle', NULL, 1, 2, 0),
(87, 42, '删除维修', NULL, NULL, 'feedback:repair:delete', NULL, 2, 2, 0);

-- ========== 角色数据 ==========
INSERT INTO sys_role (id, role_name, role_code, description) VALUES
(1, '系统管理员', 'ADMIN', '拥有所有权限的超级管理员'),
(2, '楼房车位管理员', 'HOUSE_ADMIN', '负责楼栋、单元、房屋及车位信息管理'),
(3, '业主信息管理员', 'OWNER_ADMIN', '负责业主信息及财产绑定管理'),
(4, '收费管理员', 'FEE_ADMIN', '负责水费、电费、停车费管理'),
(5, '反馈管理员', 'FEEDBACK_ADMIN', '负责反馈与维修信息管理');

-- ========== 角色菜单关联 ==========
-- 系统管理员 - 所有菜单
INSERT INTO sys_role_menu (role_id, menu_id) SELECT 1, id FROM sys_menu;

-- 楼房车位管理员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(2, 10),(2, 11),(2, 12),(2, 13),(2, 14),
(2, 60),(2, 61),(2, 62),(2, 63),(2, 64),(2, 65),
(2, 66),(2, 67),(2, 68),(2, 69),(2, 70),(2, 71);

-- 业主信息管理员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(3, 20),(3, 21),(3, 72),(3, 73),(3, 74),
(3, 10),(3, 13),(3, 14);

-- 收费管理员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(4, 30),(4, 31),(4, 32),(4, 33),
(4, 75),(4, 76),(4, 77),(4, 78),(4, 79),(4, 80),(4, 81),(4, 82),(4, 83);

-- 反馈管理员
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(5, 40),(5, 41),(5, 42),(5, 84),(5, 85),(5, 86),(5, 87),
(5, 1),(5, 4),(5, 56),(5, 57),(5, 58);

-- ========== 系统用户(管理员/员工) ==========
-- 密码均为 123456 (BCrypt加密)
INSERT INTO sys_user (id, username, password, real_name, phone, email, status) VALUES
(1, 'admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '系统管理员', '13800000001', 'admin@property.com', 1),
(2, 'zhangwei', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '张伟', '13800000002', 'zhangwei@property.com', 1),
(3, 'lina', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '李娜', '13800000003', 'lina@property.com', 1),
(4, 'wangfang', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '王芳', '13800000004', 'wangfang@property.com', 1),
(5, 'liuyang', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '刘洋', '13800000005', 'liuyang@property.com', 1);

-- ========== 用户角色关联 ==========
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- ========== 业主数据 ==========
INSERT INTO live_user (id, username, password, real_name, gender, phone, id_card, status) VALUES
(1, 'owner1', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '赵一鸣', 1, '15900000001', '520102199001011234', 1),
(2, 'owner2', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '钱二妹', 2, '15900000002', '520102199202023456', 1),
(3, 'owner3', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '孙三强', 1, '15900000003', '520102198803035678', 1),
(4, 'owner4', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '李四维', 1, '15900000004', '520102199504047890', 1),
(5, 'owner5', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36PICvhRBW.XYD0UJBOt6Vu', '周五芳', 2, '15900000005', '520102199305051234', 1);

-- ========== 楼栋数据 ==========
INSERT INTO house_building (id, name, total_floors, description) VALUES
(1, '翠湖苑1栋', 18, '高层住宅，南北通透'),
(2, '翠湖苑2栋', 18, '高层住宅，带电梯'),
(3, '翠湖苑3栋', 6, '多层住宅，环境优美'),
(4, '翠湖苑4栋', 12, '小高层住宅，近花园');

-- ========== 单元数据 ==========
INSERT INTO house_unit (id, building_id, name) VALUES
(1, 1, '1单元'), (2, 1, '2单元'),
(3, 2, '1单元'), (4, 2, '2单元'),
(5, 3, '1单元'),
(6, 4, '1单元'), (7, 4, '2单元');

-- ========== 房屋数据 ==========
INSERT INTO house_room (id, unit_id, room_number, area, status) VALUES
(1, 1, '101', 89.50, 1), (2, 1, '102', 102.30, 1),
(3, 1, '201', 89.50, 1), (4, 1, '202', 102.30, 0),
(5, 2, '101', 95.00, 1), (6, 2, '102', 110.20, 0),
(7, 3, '101', 88.60, 1), (8, 3, '102', 99.80, 0),
(9, 5, '101', 75.00, 0), (10, 5, '201', 75.00, 0),
(11, 6, '101', 120.50, 0), (12, 7, '101', 85.00, 0);

-- ========== 车位数据 ==========
INSERT INTO parking_lot (id, spot_number, area, monthly_fee, status) VALUES
(1, 'A-001', 'A区地下车库', 300.00, 1),
(2, 'A-002', 'A区地下车库', 300.00, 1),
(3, 'A-003', 'A区地下车库', 300.00, 0),
(4, 'B-001', 'B区地下车库', 280.00, 1),
(5, 'B-002', 'B区地下车库', 280.00, 0),
(6, 'C-001', 'C区地面车位', 200.00, 1),
(7, 'C-002', 'C区地面车位', 200.00, 0),
(8, 'C-003', 'C区地面车位', 200.00, 0);

-- ========== 业主房屋绑定 ==========
INSERT INTO live_user_house (user_id, house_id) VALUES
(1, 1), (2, 2), (3, 3), (4, 5), (5, 7);

-- ========== 业主车位绑定 ==========
INSERT INTO live_user_parking (user_id, parking_id) VALUES
(1, 1), (2, 2), (3, 4), (5, 6);

-- ========== 水费数据 ==========
INSERT INTO fee_water (house_id, user_id, amount, usage_amount, fee_month, status, pay_time) VALUES
(1, 1, 45.60, 12.0, '2026-01', 1, '2026-02-05 10:00:00'),
(2, 2, 38.20, 10.0, '2026-01', 1, '2026-02-03 14:30:00'),
(3, 3, 52.80, 14.0, '2026-01', 0, NULL),
(5, 4, 41.00, 11.0, '2026-01', 1, '2026-02-08 09:15:00'),
(7, 5, 35.50, 9.0, '2026-01', 0, NULL),
(1, 1, 48.90, 13.0, '2026-02', 0, NULL),
(2, 2, 42.10, 11.2, '2026-02', 0, NULL),
(3, 3, 55.00, 14.5, '2026-02', 0, NULL);

-- ========== 电费数据 ==========
INSERT INTO fee_power (house_id, user_id, amount, usage_amount, fee_month, status, pay_time) VALUES
(1, 1, 126.50, 230.0, '2026-01', 1, '2026-02-05 10:05:00'),
(2, 2, 98.00, 178.0, '2026-01', 1, '2026-02-03 14:35:00'),
(3, 3, 145.20, 264.0, '2026-01', 0, NULL),
(5, 4, 110.80, 201.0, '2026-01', 0, NULL),
(7, 5, 88.60, 161.0, '2026-01', 1, '2026-02-10 16:20:00'),
(1, 1, 132.00, 240.0, '2026-02', 0, NULL),
(2, 2, 105.50, 192.0, '2026-02', 0, NULL);

-- ========== 停车费数据 ==========
INSERT INTO fee_parking (parking_id, user_id, amount, fee_month, status, pay_time) VALUES
(1, 1, 300.00, '2026-01', 1, '2026-01-05 08:00:00'),
(2, 2, 300.00, '2026-01', 1, '2026-01-06 09:30:00'),
(4, 3, 280.00, '2026-01', 0, NULL),
(6, 5, 200.00, '2026-01', 1, '2026-01-03 15:00:00'),
(1, 1, 300.00, '2026-02', 0, NULL),
(2, 2, 300.00, '2026-02', 0, NULL);

-- ========== 反馈数据 ==========
INSERT INTO user_complaint (user_id, title, content, status, reply) VALUES
(1, '小区绿化带垃圾太多', '翠湖苑1栋门口的绿化带最近垃圾很多，希望物业能安排清理。', 1, '已安排保洁人员进行清理，感谢您的反馈！'),
(2, '电梯经常故障', '2栋1单元的电梯最近经常出现故障，上下班高峰期影响很大。', 0, NULL),
(3, '噪音扰民', '3栋楼上住户经常在晚上10点后制造噪音，严重影响休息。', 1, '已联系相关住户进行沟通，后续会加强管理。'),
(4, '快递柜数量不足', '小区快递柜数量太少，经常满柜，建议增设快递柜。', 0, NULL);

-- ========== 维修数据 ==========
INSERT INTO user_repair (user_id, title, content, status, reply) VALUES
(1, '厨房水管漏水', '厨房水管连接处漏水，需要维修师傅上门处理。', 2, '已派维修师傅上门修理完成。'),
(2, '客厅灯具损坏', '客厅的主灯不亮了，疑似灯管烧坏。', 1, '已安排电工师傅，预计明天上门维修。'),
(3, '门锁故障', '入户门锁转动困难，需要维修或更换。', 0, NULL),
(5, '卫生间下水道堵塞', '卫生间下水道排水缓慢，疑似堵塞。', 0, NULL);

-- ========== 公告数据 ==========
INSERT INTO sys_notice (title, content, creator_id) VALUES
('关于小区停水通知', '尊敬的各位业主：\n因市政管网维修，定于2026年3月20日（周五）上午8:00至下午6:00，小区将临时停水。请各位业主提前做好储水准备。\n给您带来不便，敬请谅解！\n\n翠湖苑物业管理处\n2026年3月14日', 1),
('2026年物业费缴纳通知', '尊敬的各位业主：\n2026年度物业管理费开始收缴，请各位业主于2026年3月31日前完成缴纳。\n缴费方式：\n1. 线上缴费：登录物业管理系统在线缴纳\n2. 线下缴费：前往物业服务中心柜台办理\n\n感谢您的配合！', 1),
('小区绿化改造公告', '为提升小区环境品质，物业管理处计划对小区绿化进行全面改造升级。施工期间（3月25日-4月15日），部分区域可能临时封闭，请业主配合绕行。\n\n改造内容包括：\n1. 补种花草树木\n2. 修缮步行道\n3. 增设休闲座椅\n\n期待与您共建美好家园！', 1);
