package com.dashboard.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class DisciplineData {
    private Integer degreePoints;
    private Integer firstClassMajors;
    private Integer esiDisciplines;
    private Integer firstClassDisciplines;
    private List<EvaluationItem> evaluationDistribution;

    @Data
    public static class EvaluationItem {
        private String name;
        private Integer count;

        public EvaluationItem() {}

        public EvaluationItem(String name, Integer count) {
            this.name = name;
            this.count = count;
        }
    }
}
