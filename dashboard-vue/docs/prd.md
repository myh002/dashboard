# 贵州师范大学"一张表"数据可视化仪表盘 - 产品需求文档

## 1. 项目概述

### 1.1 项目名称
贵州师范大学"一张表"

### 1.2 项目背景
本项目旨在创建一个综合性的数据可视化仪表盘，用于展示学校的各项核心指标数据，包括师资队伍、科学研究、学科建设、人才培养、办学条件等关键业务领域的数据。

### 1.3 项目目标
- 提供一个直观、美观的数据展示界面
- 实现多维度数据的可视化呈现
- 支持导航切换查看不同业务模块
- 作为 Vue3 技术栈学习和作品展示项目

### 1.4 技术栈
- **前端框架**: Vue 3.x (Composition API)
- **UI 组件库**: Element Plus 2.x
- **图表库**: ECharts 5.x
- **状态管理**: Pinia 2.x
- **路由管理**: Vue Router 4.x
- **构建工具**: Vite 5.x
- **样式方案**: SCSS + CSS Variables

---

## 2. 问题陈述

用户需要一个学习型的 Dashboard 项目，能够：
1. 展示类似参考图片中的多模块数据可视化界面
2. 使用 Vue3 生态系统的主流技术栈
3. 包含完整的前端架构（路由、状态管理、组件化）
4. 使用静态模拟数据进行演示
5. 代码结构清晰，适合作为学习案例和作品集

---

## 3. 解决方案

### 3.1 整体架构
采用单页面应用（SPA）架构，首页为综合校情总览，整体布局分为五个区域：

- **顶部标题栏**（60px）: 深蓝色渐变背景，居中标题 + 左右科技感装饰线条
- **中央导航区**: 8 个导航按钮围绕中心时钟环形排列，中心显示实时时钟 + "综合校情"
- **左侧区域（师资队伍）**: 数据卡片 + 专任教师职级横向条形图 + 国家高层次人才分类统计
- **右侧区域（人才培养）**: 数据卡片 + 培养层次学生数量列表 + 在校生培养层次环形图
- **底部三栏**: 科学研究（左）、学科建设（中）、办学条件（右）

### 3.2 页面结构
```
首页（综合校情）- 单页全览布局
├── 顶部标题栏（60px）
│   ├── 左侧科技感装饰线条
│   ├── 居中标题"贵州师范大学'一张表'"
│   └── 右侧科技感装饰线条
├── 中央导航区（环形布局）
│   ├── 中心：圆形时钟（实时时间 + 年份）+ "综合校情"文字
│   └── 环绕：8个导航按钮（从正上方开始，顺时针排列）
│           校园安全 → 党建思政 → 科学研究 → 学科建设 →
│           人才培养 → 社会服务 → 人事人才 → 财务资产
├── 左侧区域（师资队伍）
│   ├── 数据卡片组：在职教职工(4,447人)、专任教师(2,035人)、研究生导师数(1,679人)
│   ├── 专任教师职级横向条形图（高级专业技术人员167、正高级153、初级1208、中级1174）
│   └── 国家高层次人才分类统计（教授427人、副教授427人、国务院津贴427人、其他人才427人）
├── 右侧区域（人才培养）
│   ├── 数据卡片组：系部数(69个)、专业数(45个)、课程数(5个)
│   ├── 培养层次学生数量列表（在校生总数44,920、本科生数10,457、硕士研究生数33,442、博士研究生数310）
│   └── 在校生培养层次环形图（本专科生33,227、硕士生10,475、博士生1,218）
└── 底部三栏
    ├── 科学研究：双行数据卡片（公开发布论文、出版著作、专利、基地，各含上年度+今年新增）
    │   + 科研经费情况折线图(2018-2023) + 科研项目情况统计列表
    ├── 学科建设：数据卡片（学位点92、省一流专业13、ESI前1%学科19、省一流学科76）
    │   + 学校第四轮学科评估分布柱状图(A+~未参评)
    └── 办学条件：双行数据卡片（占地面积、学生食堂面积、行政面积、固有资产，各含上年度+今年新增）
        + 资产概况列表 + 科研设备总值折线图(2018-2023)
```

### 3.3 核心功能模块
1. **布局系统**: 固定全屏布局（100vw × 100vh），五区域 CSS Grid 划分
2. **中央时钟导航**: 实时时钟 + 8 个按钮环形排列，点击切换路由
3. **数据卡片**: 各区域关键指标数字卡片，带发光边框效果
4. **图表组件**: 环形图、折线图、柱状图、横向条形图，深蓝科技风格
5. **状态管理**: Pinia 统一管理全局数据和导航状态
6. **主题系统**: 深蓝色科技风格，CSS Variables 统一管理

---

## 4. 用户故事

1. 作为访问者，我希望打开页面就能看到学校整体数据概览，以便快速了解学校基本情况

2. 作为访问者，我希望点击导航按钮可以切换到不同的业务模块页面，以便查看特定领域的详细数据

3. 作为访问者，我希望看到各种直观的图表（折线图、柱状图、饼图等），以便更好地理解数据趋势和分布

4. 作为访问者，我希望页面有美观的深蓝色科技风格界面，以便获得良好的视觉体验

5. 作为访问者，我希望关键数据指标以卡片形式突出显示，以便快速捕捉重要信息

6. 作为访问者，我希望页面加载速度快，图表渲染流畅，以便获得良好的使用体验

7. 作为访问者，我希望在数据变化时图表能够动态更新，以便看到实时数据效果

8. 作为访问者，我希望页面布局清晰，导航明确，以便不会迷失方向

9. 作为开发者，我希望代码结构清晰、注释完善，以便学习和理解 Vue3 项目开发

10. 作为开发者，我希望使用 Pinia 管理状态，以便统一管理全局数据和业务逻辑

11. 作为开发者，我希望组件高度可复用，以便后续扩展和维护

12. 作为开发者，我希望有完整的模拟数据，以便在没有后端的情况下进行开发和演示

---

## 5. 功能需求

### 5.1 布局组件

#### 5.1.1 主布局 (MainLayout)
- **功能描述**: 提供整体页面框架，固定全屏 100vw × 100vh
- **组成部分**:
  - 顶部标题栏：高度 60px，深蓝色渐变背景，居中标题，左右科技感装饰线条
  - 内容区：CSS Grid 五区域布局（左侧 + 中央 + 右侧，底部三栏）
- **布局网格**:
  ```
  [  左侧(师资)  ] [  中央(导航)  ] [  右侧(人才)  ]
  [ 科学研究(底左)] [ 学科建设(底中)] [ 办学条件(底右)]
  ```
- **响应式要求**: 以 1920×1080 为基准，支持 1366×768 缩放适配

#### 5.1.2 顶部标题栏 (Header)
- **高度**: 60px
- **背景**: 深蓝色渐变 `linear-gradient(90deg, #0a1628, #0d2347, #0a1628)`
- **标题**: "贵州师范大学'一张表'" — 大号粗体，白色，居中
- **装饰**: 左右两侧科技感线条（渐变色，带发光效果）
- **底部边框**: 1px `#2d4a7c` + 蓝色外发光

### 5.2 导航系统

#### 5.2.1 中央环形导航 (CenterNavigation)
- **布局**: 8 个导航按钮围绕中心圆形时钟等角度环形排列（每隔 45°）
- **排列顺序**: 从正上方（12 点钟方向）开始，顺时针依次为：校园安全、党建思政、科学研究、学科建设、人才培养、社会服务、人事人才、财务资产
- **中心组件**:
  - 圆形容器，带蓝色渐变边框 + 外发光 + 科技感旋转光环装饰
  - 显示实时时钟（HH:MM 格式，大号数字），每秒更新
  - 显示完整日期（YYYY/M/D 格式，如 2025/6/5）
  - "综合校情" 文字标签
- **导航按钮**（8 个）:
  - 标签：校园安全、党建思政、科学研究、学科建设、人才培养、社会服务、人事人才、财务资产
  - 样式：圆角矩形，蓝色渐变背景，每个按钮配主题图标
  - 交互：悬停放大 + 高亮发光，点击切换路由，激活状态明显标识
- **图标方案**: 使用 Element Plus 图标或自定义 SVG 图标

#### 5.2.2 路由配置
```
路由表:
- / (首页 - 综合校情，全览仪表盘)
- /safety (校园安全)
- /party (党建思政)
- /research (科学研究)
- /discipline (学科建设)
- /talent (人才培养)
- /service (社会服务)
- /hr (人事人才)
- /finance (财务资产)
```

#### 5.2.3 二级页面占位策略
- 8 个导航按钮各对应一个独立路由页面，每个页面有独立的文件夹和 `index.vue`
- 点击导航按钮跳转到对应路由后，页面显示空白占位视图
- 占位视图仅显示页面标题（如"校园安全"）和"内容建设中..."提示
- 样式与整体主题保持一致（深蓝背景），不显示任何数据或图表
- 后续各模块内容独立补充，不影响首页综合校情的开发和展示

### 5.3 数据展示组件

#### 5.3.1 数据卡片 (DataCard)
- **功能描述**: 展示单个关键指标，支持两种模式
- **模式一：简单数值卡片**
  - 指标名称（如"在职教职工"）
  - 数值（如"4,447"，DIN Alternate Bold 字体）
  - 单位（如"人"）
- **模式二：双行数据卡片**（科学研究、办学条件区域使用）
  - 指标名称（如"公开发布论文"）
  - 上年度数值 + 标签
  - 今年新增数值 + 标签
- **样式要求**:
  - 背景：`#12264a`，圆角 8px
  - 边框：1px `#2d4a7c` + 蓝色外发光 `box-shadow: 0 4px 12px rgba(0,102,255,0.15)`
  - 数字：放大加粗，白色
  - 标签：`#a0aec0` 灰白色

#### 5.3.2 各区域数据卡片规格

**左侧（师资队伍）卡片组**:
- 在职教职工：4,447 人
- 专任教师：2,035 人
- 研究生导师数：1,679 人

> 注：此处"研究生导师数"指具有研究生指导资格的教师人数，与右侧"人才培养"中的研究生学生数为不同概念。

**左侧（师资队伍）附加区域**:
- 专任教师职级（横向条形图）:
  - 高级专业技术人员：167
  - 正高级：153
  - 初级：1,208
  - 中级：1,174
- 国家高层次人才（分类统计条目，带图标）:
  - 教授：427 人
  - 副教授：427 人
  - 国务院津贴：427 人
  - 其他人才：427 人

**右侧（人才培养）卡片组**:
- 系部数：69 个
- 专业数：45 个
- 课程数：5 个

**右侧（人才培养）附加区域**:
- 培养层次学生数量（列表）:
  - 在校生总数：44,920（新增 0）
  - 本科生数：10,457（新增 0）
  - 硕士研究生数：33,442 人（新增 0）
  - 博士研究生数：310 人（新增 0）
- 在校生培养层次（环形图）:
  - 本专科生：33,227
  - 硕士生：10,475
  - 博士生：1,218

> 注：在校生总数 44,920 = 本专科生 33,227 + 硕士生 10,475 + 博士生 1,218。列表中的"本科生数"10,457 为本专科生中的本科部分，"硕士研究生数"33,442 包含全日制与非全日制硕士。

**底部左（科学研究）卡片组**（每项含上年度 + 今年新增两行数据）:
- 公开发布论文：上年度 45 / 今年新增 11
- 出版著作：上年度 0 / 今年新增 0
- 专利：上年度 0 / 今年新增 0
- 基地：上年度 0 / 今年新增 0

**底部左（科学研究）附加区域**:
- 科研经费情况折线图（2018-2023，纵向项目经费 + 横向项目经费）
- 科研项目情况（统计列表）:
  - 科研项目总数：1,284 个
  - 省级项目：19,237 个
  - 地厅级项目：23,821 个
  - 院级项目：421 个

**底部中（学科建设）卡片组**:
- 学位点：92
- 省一流专业：13
- ESI前1%学科：19
- 省一流学科：76

**底部中（学科建设）附加区域**:
- 学校第四轮学科评估分布柱状图:
  - X 轴：A+、A、A-、B+、B、B-、C+、C、C-、未参评
  - Y 轴：数量

**底部右（办学条件）卡片组**（每项含上年度 + 今年新增两行数据）:
- 占地面积：上年度 411 / 今年新增 0
- 学生食堂面积：上年度 123 / 今年新增 0
- 行政面积：上年度 0 / 今年新增 0
- 固有资产：上年度 0 / 今年新增 0

**底部右（办学条件）附加区域**:
- 资产概况（统计列表）:
  - 教学科研资产：1,218 万平方米
  - 设备资产总值：10,473 万元
  - 图书总量：82,394 本
  - 数据库：310 个
- 科研设备总值折线图（2018-2023，纵向项目经费 + 横向项目经费）

### 5.4 图表组件

#### 5.4.1 通用图表组件 (BaseChart)
- **功能描述**: 封装 ECharts 的基础图表组件
- **支持类型**: 折线图、柱状图、饼图、环形图、横向条形图
- **Props 定义**:
  ```typescript
  interface BaseChartProps {
    option: EChartsOption       // ECharts 配置项
    width?: string              // 容器宽度，默认 '100%'
    height?: string             // 容器高度，默认 '300px'
    autoResize?: boolean        // 是否自动响应容器尺寸变化，默认 true
  }
  ```
- **通用配置**:
  - 背景透明，文字白色
  - 图例文字颜色 `#ffffff`
  - 提示框深蓝背景
  - 渐变填充色
- **交互功能**: 鼠标悬停显示详情，点击图例筛选，响应式自适应

#### 5.4.2 专用图表

1. **专任教师职级横向条形图**（左侧区域）
   - 横向条形图，展示各职级人数
   - 数据：高级专业技术人员 167、正高级 153、初级 1208、中级 1174
   - 蓝色系渐变条形，带数值标签
   - 配色：蓝色渐变 `#409EFF` → `#0066ff`

2. **在校生培养层次环形图**（右侧区域）
   - 中心大字显示 33,227（本专科生人数）
   - 外环展示本专科生/硕士生/博士生占比
   - 图例：本专科生 33,227、硕士生 10,475、博士生 1,218
   - 蓝色系渐变配色

3. **科研经费情况折线图**（底部左侧）
   - X 轴：年份（2018-2023）
   - Y 轴：经费金额（亿元）
   - 双折线对比：纵向项目经费 / 横向项目经费
   - 带渐变面积填充
   - 配色：蓝色 + 绿色

4. **学校第四轮学科评估分布柱状图**（底部中间）
   - X 轴：A+、A、A-、B+、B、B-、C+、C、C-、未参评
   - Y 轴：数量（0-10）
   - 渐变色柱状图（蓝色系），每根柱子上方显示数值
   - 示例数据：A+(3)、A(9)、A-(5)、B+(9)、B(8)、B-(7)、C+(3)、C(7)、C-(10)、未参评(10)

5. **科研设备总值折线图**（底部右侧）
   - X 轴：年份（2018-2023）
   - Y 轴：总值（亿元）
   - 双折线对比：纵向项目经费 / 横向项目经费
   - 样式同科研经费折线图

### 5.5 状态管理 (Pinia)

#### 5.5.1 数据模块 (DataStore)

**数据策略**: 初始阶段模拟数据直接内置在 Pinia Store 的 state 中，后续后端接口就绪后，通过 actions 调用 API 覆盖 state 数据，组件层无需任何改动。

```typescript
interface DashboardData {
  // 师资队伍数据
  faculty: {
    totalStaff: number          // 在职教职工：4447
    totalTeachers: number       // 专任教师：2035
    graduateSupervisors: number // 研究生导师数：1679
    // 专任教师职级
    titleDistribution: {
      seniorTech: number        // 高级专业技术人员：167
      seniorTitle: number       // 正高级：153
      junior: number            // 初级：1208
      middle: number            // 中级：1174
    }
    // 国家高层次人才分类统计
    topTalents: Array<{
      category: string          // 类别：教授、副教授、国务院津贴、其他人才
      count: number             // 人数
      icon?: string             // 图标
    }>
  }

  // 科学研究数据
  research: {
    // 卡片数据（每项含上年度 + 今年新增）
    papers: { lastYear: number; newThisYear: number }       // 公开发布论文
    publications: { lastYear: number; newThisYear: number }  // 出版著作
    patents: { lastYear: number; newThisYear: number }       // 专利
    bases: { lastYear: number; newThisYear: number }         // 基地
    // 科研项目情况
    projectStats: {
      total: number             // 科研项目总数：1284
      provincial: number        // 省级项目：19237
      prefectural: number       // 地厅级项目：23821
      institutional: number     // 院级项目：421
    }
    // 科研经费趋势（2018-2023）
    fundingTrend: Array<{ year: number; vertical: number; horizontal: number }>
  }

  // 学科建设数据
  discipline: {
    degreePoints: number            // 学位点：92
    firstClassMajors: number        // 省一流专业：13
    esiDisciplines: number          // ESI前1%学科：19
    firstClassDisciplines: number   // 省一流学科：76
    // 第四轮学科评估分布
    evaluationDistribution: Array<{
      grade: string   // A+, A, A-, B+, B, B-, C+, C, C-, 未参评
      count: number
    }>
  }

  // 人才培养数据
  talent: {
    departmentCount: number     // 系部数：69
    majorCount: number          // 专业数：45
    courseCount: number          // 课程数：5
    // 培养层次学生数量
    studentStats: {
      total: number             // 在校生总数：44920
      undergraduate: number     // 本科生数：10457
      master: number            // 硕士研究生数：33442
      phd: number               // 博士研究生数：310
    }
    // 在校生培养层次（环形图数据）
    studentLevels: {
      undergraduateTotal: number  // 本专科生：33227
      masterTotal: number         // 硕士生：10475
      phdTotal: number            // 博士生：1218
    }
  }

  // 办学条件数据
  condition: {
    // 卡片数据（每项含上年度 + 今年新增）
    landArea: { lastYear: number; newThisYear: number }       // 占地面积
    canteenArea: { lastYear: number; newThisYear: number }    // 学生食堂面积
    officeArea: { lastYear: number; newThisYear: number }     // 行政面积
    fixedAssets: { lastYear: number; newThisYear: number }    // 固有资产
    // 资产概况
    assetOverview: {
      teachingResearchArea: number   // 教学科研资产：1218 万平方米
      equipmentValue: number         // 设备资产总值：10473 万元
      bookTotal: number              // 图书总量：82394 本
      databaseCount: number          // 数据库：310 个
    }
    // 科研设备总值趋势（2018-2023）
    equipmentTrend: Array<{ year: number; vertical: number; horizontal: number }>
  }
}
```

#### 5.5.2 导航模块 (NavigationStore)
```typescript
interface NavigationState {
  activeRoute: string
  activeButton: string
}
```

#### 5.5.3 主题模块 (ThemeStore)
```typescript
interface ThemeState {
  primaryColor: string
  backgroundColor: string
  textColor: string
  chartColors: string[]
}
```

### 5.6 数据策略

#### 5.6.1 初始阶段：Pinia 内置模拟数据
- 模拟数据直接写在 Pinia Store 的 `state` 初始值中
- 组件通过 `storeToRefs()` 或 `store.xxx` 读取数据
- 无需额外的 mock 目录或 API 模拟层

#### 5.6.2 后续阶段：切换后端接口
- 在 `/src/api/` 目录下创建各模块的 API 请求函数
- Pinia Store 的 `actions` 中调用 API，获取数据后更新 `state`
- 组件层代码无需任何改动，数据来源对组件透明

```typescript
// 示例：后续接入后端时的 Store action
actions: {
  async fetchFacultyData() {
    // 初始阶段：不调用，使用 state 默认值
    // 后续阶段：取消注释，调用后端接口
    // const data = await api.getFacultyData()
    // this.faculty = data
  }
}
```

#### 5.6.3 API 目录结构（后续阶段，初始阶段不创建）
```
src/api/
├── index.ts          # API 统一导出
├── faculty.ts        # 师资队伍接口
├── research.ts       # 科学研究接口
├── discipline.ts     # 学科建设接口
├── talent.ts         # 人才培养接口
└── condition.ts      # 办学条件接口
```

---

## 6. 非功能需求

### 6.1 性能要求
- **首屏加载时间**: < 2 秒
- **图表渲染时间**: < 500 毫秒
- **路由切换时间**: < 300 毫秒
- **页面帧率**: 保持 60 FPS

### 6.2 兼容性要求
- **浏览器**: Chrome 90+、Edge 90+、Firefox 90+
- **分辨率**: 支持 1920x1080、1366x768、2560x1440
- **移动端**: 基础适配（非核心需求）

### 6.3 代码质量
- **代码规范**: 遵循 ESLint + Prettier 配置
- **类型安全**: 全面使用 TypeScript
- **组件文档**: 每个组件有清晰的注释
- **提交规范**: 遵循 Conventional Commits

### 6.4 可维护性
- **组件化**: 高度模块化的组件设计
- **代码复用**: 提取公共逻辑为 Composables
- **配置分离**: 图表配置、主题配置独立管理
- **目录结构**: 清晰的分层目录结构

---

## 7. 实施决策

### 7.1 项目结构
```
dashboard-vue/
├── public/                      # 静态资源
├── src/
│   ├── assets/                  # 项目资源（图片、样式）
│   │   ├── images/
│   │   └── styles/
│   │       ├── variables.scss   # CSS Variables（运行时主题变量）
│   │       ├── mixins.scss      # SCSS Mixins（编译时复用样式）
│   │       ├── theme.scss       # 主题样式
│   │       └── global.scss      # 全局样式
│   ├── components/              # 公共组件
│   │   ├── common/              # 通用组件
│   │   │   ├── DataCard.vue
│   │   │   └── BaseChart.vue
│   │   └── charts/              # 图表组件
│   │       ├── LineChart.vue
│   │       ├── BarChart.vue
│   │       ├── HorizontalBarChart.vue
│   │       ├── PieChart.vue
│   │       └── DonutChart.vue
│   ├── layouts/                 # 布局组件
│   │   ├── MainLayout.vue
│   │   ├── Header.vue
│   │   └── Navigation.vue
│   ├── views/                   # 页面视图
│   │   ├── Home/                # 首页（综合校情）
│   │   │   └── index.vue
│   │   ├── Safety/              # 校园安全
│   │   │   └── index.vue
│   │   ├── Party/               # 党建思政
│   │   │   └── index.vue
│   │   ├── Research/            # 科学研究
│   │   │   └── index.vue
│   │   ├── Discipline/          # 学科建设
│   │   │   └── index.vue
│   │   ├── Talent/              # 人才培养
│   │   │   └── index.vue
│   │   ├── Service/             # 社会服务
│   │   │   └── index.vue
│   │   ├── Hr/                  # 人事人才
│   │   │   └── index.vue
│   │   └── Finance/             # 财务资产
│   │       └── index.vue
│   ├── router/                  # 路由配置
│   │   └── index.ts
│   ├── stores/                  # Pinia 状态管理（内置模拟数据）
│   │   ├── index.ts
│   │   ├── data.ts              # 核心数据 Store，模拟数据内置于 state
│   │   ├── navigation.ts
│   │   └── theme.ts
│   ├── composables/             # 组合式函数
│   │   └── useChartTheme.ts
│   ├── types/                   # TypeScript 类型定义
│   │   └── index.ts
│   ├── utils/                   # 工具函数
│   │   └── format.ts
│   ├── App.vue
│   └── main.ts
├── docs/                        # 项目文档
│   └── prd.md
├── package.json
└── vite.config.ts
```

> 样式说明：`variables.scss` 中使用 `:root` 定义 CSS Variables，用于运行时主题切换和组件样式引用；`mixins.scss` 中定义 SCSS Mixins，通过 Vite 的 `additionalData` 全局注入，用于编译时复用样式片段（如卡片样式、发光边框等）。两者配合使用，互不冲突。

### 7.2 组件设计原则

#### 7.2.1 单一职责
每个组件只负责一个明确的功能：
- `DataCard`: 只负责展示单个数据指标
- `BaseChart`: 只负责渲染 ECharts 图表
- `Navigation`: 只负责导航按钮的渲染和交互

#### 7.2.2 Props 定义
```vue
<script setup lang="ts">
interface Props {
  title: string
  value: number | string
  unit?: string
  trend?: 'up' | 'down' | 'neutral'
  trendValue?: number
}

const props = withDefaults(defineProps<Props>(), {
  unit: '',
  trend: 'neutral',
  trendValue: 0
})
</script>
```

#### 7.2.3 事件发射
```vue
<script setup lang="ts">
const emit = defineEmits<{
  (e: 'click'): void
  (e: 'update', value: number): void
}>()
</script>
```

### 7.3 图表配置管理

#### 7.3.1 主题配置
```typescript
// src/utils/chartTheme.ts
export const chartTheme = {
  colors: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'],
  backgroundColor: 'transparent',
  textStyle: {
    color: '#ffffff'
  },
  // ... 其他主题配置
}
```

#### 7.3.2 通用配置
```typescript
// src/composables/useChartOption.ts
export function useChartOption(type: ChartType) {
  const baseOption = {
    tooltip: { trigger: 'axis' },
    legend: { data: [], textStyle: { color: '#fff' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    // ... 通用配置
  }
  
  return baseOption
}
```

### 7.4 路由懒加载
```typescript
// src/router/index.ts
const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home/index.vue')
  },
  {
    path: '/safety',
    name: 'Safety',
    component: () => import('@/views/Safety/index.vue')
  },
  {
    path: '/party',
    name: 'Party',
    component: () => import('@/views/Party/index.vue')
  },
  {
    path: '/research',
    name: 'Research',
    component: () => import('@/views/Research/index.vue')
  },
  {
    path: '/discipline',
    name: 'Discipline',
    component: () => import('@/views/Discipline/index.vue')
  },
  {
    path: '/talent',
    name: 'Talent',
    component: () => import('@/views/Talent/index.vue')
  },
  {
    path: '/service',
    name: 'Service',
    component: () => import('@/views/Service/index.vue')
  },
  {
    path: '/hr',
    name: 'Hr',
    component: () => import('@/views/Hr/index.vue')
  },
  {
    path: '/finance',
    name: 'Finance',
    component: () => import('@/views/Finance/index.vue')
  }
]
```

### 7.5 Pinia Store 设计

#### 7.5.1 数据 Store
```typescript
// src/stores/data.ts
export const useDataStore = defineStore('data', {
  state: (): DashboardData => ({
    // 模拟数据直接内置，后续替换为 API 返回值
    faculty: {
      totalStaff: 4447,
      totalTeachers: 2035,
      graduateSupervisors: 1679,
      titleDistribution: { seniorTech: 167, seniorTitle: 153, junior: 1208, middle: 1174 },
      topTalents: [
        { category: '教授', count: 427 },
        { category: '副教授', count: 427 },
        { category: '国务院津贴', count: 427 },
        { category: '其他人才', count: 427 }
      ]
    },
    research: { /* 模拟数据 */ },
    discipline: { /* 模拟数据 */ },
    talent: { /* 模拟数据 */ },
    condition: { /* 模拟数据 */ }
  }),
  
  getters: {
    totalStaff: (state) => state.faculty.totalStaff,
    totalStudents: (state) => state.talent.studentStats.total
  },
  
  actions: {
    // 后续后端接口就绪后，取消注释即可切换数据来源
    async fetchFacultyData() {
      // const data = await api.getFacultyData()
      // this.faculty = data
    },
    async fetchAllData() {
      // await Promise.all([
      //   this.fetchFacultyData(),
      //   this.fetchResearchData(),
      //   ...
      // ])
    }
  }
})
```

### 7.6 样式方案

#### 7.6.1 颜色方案
```scss
// src/assets/styles/variables.scss
:root {
  // 主色
  --primary-color: #0066ff;       // 亮蓝
  --primary-dark: #004ecc;

  // 背景
  --background-dark: #0a1628;     // 深蓝黑（页面背景）
  --background-card: #12264a;     // 深蓝（卡片背景）

  // 边框
  --border-color: #2d4a7c;        // 中蓝
  --border-glow: rgba(0, 102, 255, 0.4); // 发光效果

  // 文字
  --text-primary: #ffffff;        // 白色
  --text-secondary: #a0aec0;      // 灰白

  // 图表配色
  --chart-color-1: #409EFF;       // 蓝
  --chart-color-2: #67C23A;       // 绿
  --chart-color-3: #E6A23C;       // 橙
  --chart-color-4: #F56C6C;       // 红
  --chart-color-5: #909399;       // 灰
}
```

#### 7.6.2 字体方案
```scss
// 标题字体
--font-title: 'Microsoft YaHei Bold', 'PingFang SC', sans-serif;

// 数字字体（科技感）
--font-number: 'DIN Alternate Bold', 'Arial', sans-serif;

// 正文字体
--font-body: 'Microsoft YaHei', 'PingFang SC', sans-serif;
```

#### 7.6.3 混入
```scss
// src/assets/styles/mixins.scss
@mixin card-style {
  background: var(--background-card);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 4px 12px rgba(0, 102, 255, 0.15),
              0 0 8px rgba(0, 102, 255, 0.1);
}

@mixin border-glow {
  border: 1px solid var(--border-color);
  box-shadow: 0 0 10px var(--border-glow),
              inset 0 0 10px rgba(0, 102, 255, 0.05);
}

@mixin tech-line {
  // 科技感装饰线条
  height: 2px;
  background: linear-gradient(90deg, transparent, #0066ff, #00d4ff, transparent);
  box-shadow: 0 0 8px rgba(0, 212, 255, 0.6);
}
```

### 7.7 开发环境配置

#### 7.7.1 Vite 配置
```typescript
// vite.config.ts
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
          @use "@/assets/styles/variables.scss" as *;
          @use "@/assets/styles/mixins.scss" as *;
        `
      }
    }
  }
})
```

#### 7.7.2 ESLint 配置
```json
{
  "extends": [
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:vue/vue3-recommended"
  ],
  "rules": {
    "vue/multi-word-component-names": "off"
  }
}
```

---

## 8. 测试决策

### 8.1 测试策略
本项目以学习和展示为目的，测试重点在于：
- 组件渲染正确性
- 用户交互功能
- 数据展示准确性

### 8.2 测试范围
- **单元测试**: 工具函数、Composables
- **组件测试**: 关键组件的渲染和交互
- **E2E 测试**: 主要用户流程（可选）

### 8.3 测试工具（可选）
- **Vitest**: 单元测试框架
- **Vue Test Utils**: Vue 组件测试工具
- **Playwright**: E2E 测试工具

### 8.4 测试优先级
由于是学习项目，测试不是核心需求，可根据时间情况选择性实现。

---

## 9. 范围外内容

### 9.1 不包含的功能
1. **真实后端接口**: 初始阶段使用 Pinia 内置模拟数据，后续后端接口就绪后通过 Store actions 切换数据来源
2. **用户登录系统**: 不涉及用户认证和权限管理
3. **数据编辑功能**: 只读展示，不支持数据修改
4. **数据导出**: 不支持导出 Excel、PDF 等格式
5. **实时数据推送**: 不使用 WebSocket 等技术实现实时数据
6. **多语言支持**: 仅支持中文
7. **完整的移动端适配**: 仅做基础响应式，不专门优化移动端

### 9.2 不关注的指标
1. **SEO 优化**: 作为内部系统，不关注搜索引擎优化
2. **无障碍访问**: 作为学习项目，暂不遵循 WCAG 标准
3. **性能监控**: 不集成性能监控工具

---

## 10. 进一步说明

### 10.1 开发建议

#### 10.1.1 开发顺序
1. 搭建项目基础架构（Vite + Vue3 + TypeScript）
2. 配置 Element Plus 和 ECharts
3. 实现主布局组件（Header、Navigation）
4. 创建公共组件（DataCard、BaseChart）
5. 配置 Pinia Store 和模拟数据
6. 实现首页（综合校情）
7. 实现 8 个二级页面占位视图
8. 优化样式和交互细节
9. 性能优化和测试

### 10.2 扩展方向

完成基础版本后，可以考虑以下扩展：
1. **主题切换**: 实现明/暗主题切换
2. **图表交互**: 增加图表联动、下钻等功能
3. **数据更新**: 实现定时刷新、手动刷新
4. **动画效果**: 增加页面过渡、数字滚动等动画
5. **全屏展示**: 支持全屏查看模式
6. **打印功能**: 支持打印当前页面
7. **后端接口**: 在 `src/api/` 目录下创建各模块 API 请求函数，通过 Store actions 切换数据来源

### 10.3 参考资料
- [Vue3 官方文档](https://vuejs.org/)
- [Element Plus 文档](https://element-plus.org/)
- [ECharts 文档](https://echarts.apache.org/)
- [Pinia 文档](https://pinia.vuejs.org/)
- [Vue Router 文档](https://router.vuejs.org/)

### 10.4 项目里程碑

#### Phase 1: 基础架构（1-2 天）
- [ ] 初始化项目
- [ ] 配置依赖和工具
- [ ] 创建项目结构
- [ ] 配置主题样式

#### Phase 2: 核心组件（2-3 天）
- [ ] 实现布局组件
- [ ] 实现导航系统
- [ ] 实现数据卡片组件
- [ ] 实现图表组件

#### Phase 3: 页面开发（3-4 天）
- [ ] 实现首页（综合校情）
- [ ] 实现 8 个二级页面占位视图（校园安全、党建思政、科学研究、学科建设、人才培养、社会服务、人事人才、财务资产）

#### Phase 4: 优化完善（1-2 天）
- [ ] 优化样式细节
- [ ] 优化交互体验
- [ ] 性能优化
- [ ] 代码审查和重构

#### Phase 5: 文档和部署（1 天）
- [ ] 编写 README 文档
- [ ] 部署到 GitHub Pages 或 Vercel
- [ ] 项目总结

---

## 11. 验收标准

### 11.1 功能验收
- [ ] 所有 9 个页面（首页 + 8 个二级页面）可以正常访问
- [ ] 8 个导航按钮可以正确切换页面
- [ ] 所有图表可以正常渲染
- [ ] 数据展示正确无误
- [ ] 响应式布局正常工作
- [ ] 实时时钟正常运行并每秒更新

### 11.2 视觉验收
- [ ] 整体风格与参考图片一致
- [ ] 配色方案统一协调
- [ ] 字体大小和间距合理
- [ ] 图表配色美观
- [ ] 动画过渡流畅

### 11.3 代码验收
- [ ] 代码结构清晰合理
- [ ] 组件命名规范统一
- [ ] TypeScript 类型定义完整
- [ ] 关键代码有注释说明
- [ ] 无 ESLint 错误和警告

---

**文档版本**: v1.3
**创建日期**: 2026-03-18
**最后更新**: 2026-03-18
**状态**: 已修复数据一致性问题、文字错误，精简项目结构，补齐 8 个二级页面目录