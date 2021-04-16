package models;

public class TeamApplication {
    private int applicationId;
    private TeamMember applicant;
    private Team appliedTeam;
    private boolean isRejected;

    public TeamApplication(int applicationId, TeamMember applicant, Team appliedTeam, boolean isRejected) {
        this.applicationId = applicationId;
        this.applicant = applicant;
        this.appliedTeam = appliedTeam;
        this.isRejected = isRejected;
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

    public boolean isRejected() {
        return isRejected;
    }
}
