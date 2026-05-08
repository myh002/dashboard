/** 双行数据项（上年度 + 今年新增） */
export interface DualYearData {
  lastYear: number
  newThisYear: number
}

/** 高层次人才条目 */
export interface TalentItem {
  category: string
  count: number
  newIncrement?: number
  icon?: string
  unit: string
}

/** 师资职称趋势数据项 */
export interface TitleTrendItem {
  year: number
  seniorTitle: number
  seniorHigh: number
  middle: number
  junior: number
  seniorTech: number
}

/** 师资队伍数据 */
export interface FacultyData {
  totalStaff: number
  totalTeachers: number
  graduateSupervisors: number
  titleDistribution: {
    seniorHigh: number
    seniorTech: number
    seniorTitle: number
    junior: number
    middle: number
  }
  topTalents: TalentItem[]
  titleTrend: TitleTrendItem[]
}

/** 师资职称趋势数据 */
export interface FacultyTitleTrendData {
  yearlyData: Array<{
    year: number
    seniorTitle: number
    seniorHigh: number
    middle: number
    junior: number
    seniorTech: number
  }>
}

/** 科研项目统计 */
export interface ProjectStats {
  total: number
  provincial: number
  prefectural: number
  institutional: number
}

/** 经费趋势数据点 */
export interface FundingTrendItem {
  year: number
  vertical: number
  horizontal: number
}

/** 科学研究数据 */
export interface ResearchData {
  papers: DualYearData
  publications: DualYearData
  patents: DualYearData
  bases: DualYearData
  projectStats: ProjectStats
  fundingTrend: FundingTrendItem[]
}

/** 学科评估条目 */
export interface EvaluationItem {
  name: string
  count: number
}

/** 学科建设数据 */
export interface DisciplineData {
  degreePoints: number
  firstClassMajors: number
  esiDisciplines: number
  firstClassDisciplines: number
  evaluationDistribution: EvaluationItem[]
}

/** 学生统计 */
export interface StudentStats {
  total: number
  undergraduate: number
  master: number
  phd: number
  totalIncrement: number
  undergraduateIncrement: number
  masterIncrement: number
  phdIncrement: number
}

/** 在校生培养层次 */
export interface StudentLevels {
  undergraduateTotal: number
  masterTotal: number
  phdTotal: number
}

/** 人才培养数据 */
export interface TalentData {
  departmentCount: number
  majorCount: number
  courseCount: number
  studentStats: StudentStats
  studentLevels: StudentLevels
}

/** 在校生培养层次趋势数据 */
export interface StudentLevelTrendData {
  year: string
  undergraduate: number
  master: number
  phd: number
}

/** 资产概况 */
export interface AssetOverview {
  teachingResearchArea: number
  equipmentValue: number
  bookTotal: number
  databaseCount: number
}

/** 办学条件数据 */
export interface ConditionData {
  landArea: DualYearData
  labArea: DualYearData
  teachingAdminArea: DualYearData
  fixedAssets: DualYearData
  assetOverview: AssetOverview
  equipmentTrend: FundingTrendItem[]
}

/** 仪表盘全量数据 */
export interface DashboardData {
  faculty: FacultyData
  research: ResearchData
  discipline: DisciplineData
  talent: TalentData
  condition: ConditionData
}

/** 导航项 */
export interface NavItem {
  label: string
  path: string
  icon: string
}

/** 导航状态 */
export interface NavigationState {
  activeRoute: string
  activeButton: string
  pageTitle: string
}

/** 主题状态 */
export interface ThemeState {
  primaryColor: string
  backgroundColor: string
  textColor: string
  chartColors: string[]
}

/** 知识产权-专利数据 */
export interface PatentsData {
  invention: number
  utility: number
  design: number
}

/** 专著与知识产权数据 */
export interface PublicationsData {
  books: number
  patents: PatentsData
  varieties: number
  standards: number
  software: number
  total: number
}

/** 项目数据 */
export interface ProjectsData {
  national: number
  ministerial: number
  provincial: number
  prefectural: number
  school: number
  total: number
}

/** 平台数据 */
export interface PlatformsData {
  national: number
  provincial: number
  other: number
}

/** 经费数据 */
export interface FundingData {
  vertical: number
  horizontal: number
  total: number
}

/** 奖项数据 */
export interface AwardsData {
  first: number
  second: number
  third: number
}

/** 论文数据 */
export interface PapersData {
  sciQ1: number
  sciQ2: number
  sciQ3: number
  sciQ4: number
  ei: number
  cscd: number
  core: number
  other: number
  total: number
}

/** 科学研究全量数据（科技处+图书馆） */
export interface ScienceResearchData {
  projects: ProjectsData
  platforms: PlatformsData
  funding: FundingData
  awards: AwardsData
  publications: PublicationsData
  papers: PapersData
}

/** 单年份科学研究数据 */
export interface YearlyResearchData {
  year: string
  projects: ProjectsData
  platforms: PlatformsData
  funding: FundingData
  awards: AwardsData
  publications: PublicationsData
  papers: PapersData
}

/** 多年科学研究状态 */
export interface ScienceResearchState {
  yearlyData: YearlyResearchData[]
  selectedYear: string
  loading: boolean
  error: string | null
}

/** 学科数据 */
export interface DisciplinesData {
  provincialKey: number
  provincialEmphasis: number
  firstClassConstruction: number
  firstClass: number
}

/** 专业数据 */
export interface MajorsData {
  undergraduateMale: number
  undergraduateFemale: number
  undergraduateTotal: number
  newThisYear: number
  discontinued: number
}

/** 教学成果数据 */
export interface TeachingAchievementsData {
  provincialTeachingAward: number
  provincialReformProject: number
  schoolReformProject: number
}

/** 职称分布数据 */
export interface TitleDistributionData {
  professorCount: number
  associateProfessorCount: number
  lecturerCount: number
  juniorCount: number
}

/** 学位点数据 */
export interface DegreePointsData {
  postdoctoral: number
  doctoralFirst: number
  masterFirst: number
  masterSecond: number
  professional: number
  total: number
}

/** 单年份学科建设数据 */
export interface YearlyDisciplineData {
  year: string
  disciplines: DisciplinesData
  majors: MajorsData
  teachingAchievements: TeachingAchievementsData
  titleDistribution: TitleDistributionData
  degreePoints: DegreePointsData
  colleges: number
}

/** 学科建设状态 */
export interface DisciplineState {
  yearlyData: YearlyDisciplineData[]
  selectedYear: string
  loading: boolean
  error: string | null
}

/** 师资人员数据 */
export interface StaffData {
  total: number
  fullTime: number
  management: number
  supporting: number
  external: number
}

/** 学历结构数据 */
export interface EducationData {
  doctorate: number
  master: number
  bachelor: number
}

/** 职称结构数据 */
export interface TitleData {
  professor: number
  associate: number
  lecturer: number
  assistant: number
}

/** 教学成果数据 */
export interface TalentsData {
  provincialTeachingAward: number
}

/** 年龄分布数据 */
export interface AgeDistribution {
  under35: number
  age36to45: number
  age46to55: number
  over56: number
}

/** 单年份人事人才数据 */
export interface YearlyHrData {
  year: string
  staff: StaffData
  education: EducationData
  title: TitleData
  talents: TalentsData
  ageDistribution: AgeDistribution
}

/** 人事人才状态 */
export interface HrState {
  yearlyData: YearlyHrData[]
  selectedYear: string
  loading: boolean
  error: string | null
}

/** 产学研合作数据 */
export interface CooperationData {
  horizontalFunding: number
  inventionPatents: number
  utilityPatents: number
  designPatents: number
  varieties: number
  standards: number
  software: number
}

/** 就业数据 */
export interface EmploymentData {
  undergraduate: number
  undergraduateRate: number
  master: number
  phd: number
  graduateRate: number
}

/** 国际交流数据 */
export interface InternationalData {
  internationalStudents: number
  cooperativePrograms: number
  practiceBases: number
}

/** 外聘专家数据 */
export interface ExpertsData {
  externalTeachers: number
}

/** 单年份社会服务数据 */
export interface YearlyServiceData {
  year: string
  cooperation: CooperationData
  employment: EmploymentData
  international: InternationalData
  experts: ExpertsData
}

/** 社会服务状态 */
export interface ServiceState {
  yearlyData: YearlyServiceData[]
  selectedYear: string
  loading: boolean
  error: string | null
}

/** 党员数据 */
export interface PartyMembersData {
  total: number
  undergraduate: number
  graduate: number
}

/** 民主党派数据 */
export interface DemocraticPartiesData {
  revolutionary: number
  league: number
  construction: number
  progress: number
  farmersWorkers: number
  zhiGong: number
  jiuSan: number
  taiwanLeague: number
}

/** 共青团数据 */
export interface YouthLeagueData {
  undergraduate: number
  graduate: number
}

/** 特殊群体数据 */
export interface SpecialGroupsData {
  minority: number
  disabled: number
}

/** 思政团队数据 */
export interface IdeologicalTeamsData {
  nationalTeam: number
  provincialTeam: number
}

/** 学生规模数据（学历层次×性别） */
export interface StudentScaleData {
  undergraduateMale: number
  undergraduateFemale: number
  masterMale: number
  masterFemale: number
  phdMale: number
  phdFemale: number
}

/** 硕博政治面貌数据 */
export interface GraduatePoliticalData {
  partyMember: number
  youthLeague: number
}

/** 单年份党建思政数据 */
export interface YearlyPartyData {
  year: string
  partyMembers: PartyMembersData
  democraticParties: DemocraticPartiesData
  youthLeague: YouthLeagueData
  specialGroups: SpecialGroupsData
  ideologicalTeams: IdeologicalTeamsData
  studentScale: StudentScaleData
  graduatePolitical: GraduatePoliticalData
}

/** 党建思政状态 */
export interface PartyState {
  yearlyData: YearlyPartyData[]
  selectedYear: string
  loading: boolean
  error: string | null
}

/** 本科生数据 */
export interface UndergraduateData {
  total: number
  male: number
  female: number
  graduates: number
  graduateRate: number
  grantRate: number
  degreeGranted: number
  employment: number
  employmentRate: number
}

/** 硕士生数据 */
export interface MasterData {
  total: number
  male: number
  female: number
  supervisors: number
  graduates: number
  graduateRate: number
  grantRate: number
  degreeGranted: number
  employment: number
  employmentRate: number
}

/** 博士生数据 */
export interface PhdData {
  total: number
  male: number
  female: number
  supervisors: number
  graduates: number
  graduateRate: number
  employment: number
  employmentRate: number
}

/** 教学数据 */
export interface TeachingData {
  courses: number
  professorCourses: number
  nationalReform: number
  provincialReform: number
  schoolReform: number
  teachingAward: number
  practiceBases: number
  nationalTeams: number
  provincialTeams: number
}

/** 国际化数据 */
export interface TalentInternationalData {
  cooperativePrograms: number
  internationalStudents: number
}

/** 单年份人才培养数据 */
export interface YearlyTalentData {
  year: string
  undergraduate: UndergraduateData
  master: MasterData
  phd: PhdData
  teaching: TeachingData
  international: TalentInternationalData
}

/** 人才培养状态 */
export interface TalentState {
  yearlyData: YearlyTalentData[]
  selectedYear: string
  loading: boolean
  error: string | null
}

/** 校园数据 */
export interface CampusData {
  schoolArea: number | null
  teachingArea: number | null
  labArea: number | null
  dormitoryArea: number | null
  horizontalFunding: number
}

/** 资产数据 */
export interface AssetsData {
  fixedAssets: number | null
  equipmentCount: number
  equipmentValue: number
  largeEquipmentCount: number
  largeEquipmentValue: number
}

/** 科研经费数据 */
export interface FinanceResearchData {
  verticalFunding: number
}

/** 图书馆数据 */
export interface LibraryData {
  bookCount: number | null
  ebookCount: number | null
  ejournalCount: number | null
}

/** 单年份财政资产数据 */
export interface YearlyFinanceData {
  year: string
  campus: CampusData
  assets: AssetsData
  research: FinanceResearchData
  library: LibraryData
}

/** 财政资产状态 */
export interface FinanceState {
  yearlyData: YearlyFinanceData[]
  selectedYear: string
  loading: boolean
  error: string | null
}
