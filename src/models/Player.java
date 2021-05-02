package models;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Player extends TeamMember{
    private TrainingPerformanceReport trainingPerformanceReport;

    public Player(int memberId, String firstName, String lastName, LocalDate birthday, String teamRole, String email, String sportBranch, Image profilePhoto, TrainingPerformanceReport trainingPerformanceReport) {
        super(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, profilePhoto);
        this.trainingPerformanceReport = trainingPerformanceReport;
    }

    public TrainingPerformanceReport getTrainingPerformanceReport() {
        return trainingPerformanceReport;
    }
}
