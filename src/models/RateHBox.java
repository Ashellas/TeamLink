package models;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class RateHBox extends HBox
{
    // properties
    TeamMember teamMember;
    Label nameLabel;
    Slider ratingSlider;
    CheckBox didPlayerAttend;

    // constructors
    public RateHBox(TeamMember teamMember )
    {
        this.teamMember = teamMember;
        // creating label
        nameLabel = new Label( "Player name: " + teamMember.getFullName() );
        nameLabel.setStyle( "-fx-alignment: TOP_CENTER; -fx-content-display: CENTER; -fx-pref-height: 66.0; -fx-pref-width: 140.0; " );
        nameLabel.getStyleClass().add( "text" );

        // creating slider
        ratingSlider = new Slider();
        ratingSlider.setStyle( "-fx-major-tick-unit: 1.0; -fx-minor-tick-count: 0; -fx-pref-height: 14.0; -fx-pref-width: 194.0; -fx-alignment: CENTER" );
        ratingSlider.setMin( 0 );
        ratingSlider.setMax( 10 );
        ratingSlider.setShowTickLabels( true );
        ratingSlider.setShowTickMarks( true );
        ratingSlider.setSnapToTicks( true );

        // creating a check box
        didPlayerAttend = new CheckBox();
        didPlayerAttend.setSelected( true );
        didPlayerAttend.setText( "Did Attend" );
        didPlayerAttend.getStyleClass().add( "text" );

        // adding it to HBox
        this.getChildren().addAll( nameLabel, ratingSlider, didPlayerAttend );
    }

    /**
     * This method sets the value of the slider as player's rating
     * @param rateValue
     */
    public void setRateValue( int rateValue )
    {
        ratingSlider.setValue( rateValue );
    }

    /**
     * This method ticks the attendance box if the player attended to the training
     * @param didAttend
     */
    public void setAttendance( boolean didAttend )
    {
        didPlayerAttend.setSelected( didAttend );
    }

    /**
     *
     * @return the team member of the current rating
     */
    public TeamMember getTeamMember()
    {
        return teamMember;
    }

    /**
     *
     * @return the slider value as the training rating
     */
    public int getSliderValue()
    {
        return ( int ) ratingSlider.getValue();
    }

    /**
     *
     * @return true if the player attended the training
     */
    public boolean getAttendance()
    {
        return didPlayerAttend.isSelected();
    }


}
