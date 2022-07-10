package racing.boathub.race;

public class TrackTime {
    Boolean finished = false;
    Long start;
    Long end;
    Track track;
    Racer racer;
    public TrackTime(Long start, Racer racer, Track track) {
        this.start = start;
        this.track = track;
        this.racer = racer;
    }

    public Boolean getFinished() {
        return finished;
    }
    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
    public Long getStart() {
        return start;
    }
    public Long getEnd() {
        return end;
    }
    public void setEnd(Long end) {
        this.end = end;
    }
    public Track getTrack() {
        return track;
    }
    public Racer getRacer() {
        return racer;
    }
}
