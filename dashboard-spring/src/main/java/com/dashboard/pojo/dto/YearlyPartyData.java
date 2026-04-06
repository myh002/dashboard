package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class YearlyPartyData {
    private String year;
    private PartyMembersData partyMembers;
    private DemocraticPartiesData democraticParties;
    private YouthLeagueData youthLeague;
    private SpecialGroupsData specialGroups;
    private IdeologicalTeamsData ideologicalTeams;

    @Data
    public static class PartyMembersData {
        private Integer total;
        private Integer undergraduate;
        private Integer graduate;
    }

    @Data
    public static class DemocraticPartiesData {
        private Integer revolutionary;
        private Integer league;
        private Integer construction;
        private Integer progress;
        private Integer farmersWorkers;
        private Integer ZhiGong;
        private Integer JiuSan;
        private Integer taiwanLeague;
    }

    @Data
    public static class YouthLeagueData {
        private Integer undergraduate;
        private Integer graduate;
    }

    @Data
    public static class SpecialGroupsData {
        private Integer minority;
        private Integer disabled;
    }

    @Data
    public static class IdeologicalTeamsData {
        private Integer nationalTeam;
        private Integer provincialTeam;
    }
}
