package models;

import java.util.Date;

public class Injury {
    private int injuryId;
    private String injuryType;
    private Date startDate;
    private Date approximateEndDate;
    private boolean isRecovered;


    public Injury(int injuryId, String injuryType, Date startDate, Date approximateEndDate, boolean isRecovered) {
        this.injuryId = injuryId;
        this.injuryType = injuryType;
        this.startDate = startDate;
        this.approximateEndDate = approximateEndDate;
        this.isRecovered = isRecovered;
    }


    public int getInjuryId() {
        return injuryId;
    }

    public String getInjuryType() {
        return injuryType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getApproximateEndDate() {
        return approximateEndDate;
    }

    public boolean isRecovered() {
        return isRecovered;
    }

}
