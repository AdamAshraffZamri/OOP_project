import java.time.LocalDate;

public class FitnessEventActivity extends Activity {
    private FitnessEvent event;
    private boolean attendanceStatus;

    public FitnessEventActivity(String activityId, LocalDate activityDate, String description,
                                FitnessEvent event) {
        super(activityId, activityDate, description);

        if (event == null) {
            throw new IllegalArgumentException("Fitness event cannot be null.");
        }

        this.event = event;
        this.attendanceStatus = false;
        this.pointsEarned = calculatePoints();
    }

    public void markAttendance() {
        this.attendanceStatus = true;
        this.pointsEarned = calculatePoints();
    }

    @Override
    public int calculatePoints() {
        if (attendanceStatus) {
            return 20;
        }

        return 0;
    }

    @Override
    public void displayActivityDetails() {
        super.displayActivityDetails();
        System.out.println("Event Name: " + event.getEventName());
        System.out.println("Attendance Status: " + attendanceStatus);
        System.out.println();
    }

    public FitnessEvent getEvent() {
        return event;
    }

    public boolean getAttendanceStatus() {
        return attendanceStatus;
    }
}
