import request from './request'
import type {
  FacultyData,
  ResearchData,
  DisciplineData,
  TalentData,
  ConditionData,
  YearlyResearchData,
  YearlyFinanceData,
  YearlyHrData,
  YearlyPartyData,
  YearlyServiceData,
  YearlyDisciplineData,
  YearlyTalentData,
} from '@/types'

export const facultyApi = {
  getData: () => request.get<FacultyData>('/faculty'),
}

export const researchApi = {
  getData: () => request.get<ResearchData>('/research'),
  getYearlyData: () => request.get<YearlyResearchData[]>('/research/yearly'),
}

export const disciplineApi = {
  getData: () => request.get<DisciplineData>('/discipline'),
  getYearlyData: () => request.get<YearlyDisciplineData[]>('/discipline/yearly'),
}

export const talentApi = {
  getData: () => request.get<TalentData>('/talent'),
  getYearlyData: () => request.get<YearlyTalentData[]>('/talent/yearly'),
}

export const conditionApi = {
  getData: () => request.get<ConditionData>('/condition'),
}

export const financeApi = {
  getData: () => request.get<YearlyFinanceData[]>('/finance'),
}

export const hrApi = {
  getData: () => request.get<YearlyHrData[]>('/hr'),
}

export const partyApi = {
  getData: () => request.get<YearlyPartyData[]>('/party'),
}

export const serviceApi = {
  getData: () => request.get<YearlyServiceData[]>('/service'),
}
