package com.example.pingstack.email.dto;

public class UpdatePreferenceResponse {

    private boolean trackOpens;
    private boolean trackClicks;
    private boolean bounceAlerts;

    public UpdatePreferenceResponse(boolean trackOpens, boolean trackClicks, boolean bounceAlerts) {
        this.trackOpens = trackOpens;
        this.trackClicks = trackClicks;
        this.bounceAlerts = bounceAlerts;
    }

    public boolean isTrackOpens() {
        return trackOpens;
    }

    public void setTrackOpens(boolean trackOpens) {
        this.trackOpens = trackOpens;
    }

    public boolean isTrackClicks() {
        return trackClicks;
    }

    public void setTrackClicks(boolean trackClicks) {
        this.trackClicks = trackClicks;
    }

    public boolean isBounceAlerts() {
        return bounceAlerts;
    }

    public void setBounceAlerts(boolean bounceAlerts) {
        this.bounceAlerts = bounceAlerts;
    }
}
