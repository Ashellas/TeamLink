package models;

public class TeamApplication {
    private int applicationId;
    private TeamMember applicant;
    private Team appliedTeam;
    private String applicationStatus;

    public TeamApplication(int applicationId, TeamMember applicant, Team appliedTeam, boolean isRejected) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.appliedTeam = appliedTeam;
        if(isRejected){
            applicationStatus = "Rejected";
        }
        else{
            applicationStatus = "Pending";
        }

    }

    public int getApplicationId() {
        return applicationId;
    }

    public TeamMember getApplicant() {
        return applicant;
    }

    public Team getAppliedTeam() {
        return appliedTeam;
    }

    public String getTeamName(){
        return appliedTeam.getTeamName();
    }

    public String getAgeGroup(){
        return appliedTeam.getAgeGroup();
    }

    public String getCity(){
        return appliedTeam.getCity();
    }

    public String getApplicantFullName(){ return applicant.getFullName() ;}

    public String getApplicantTeamRole(){ return applicant.getTeamRole();}

    public String getApplicationStatus() {
        return applicationStatus;
    }
}
