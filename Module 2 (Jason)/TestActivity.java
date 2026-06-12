import java.time.LocalDate;

public class TestActivity {
    public static void main(String[] args) {
        System.out.println("=== Activity Tracking Test ===");
        System.out.println();

        Resident resident = new Resident("R001", "Jason Chew");

        Activity recycling = new RecyclingActivity(
                "A001",
                LocalDate.of(2026, 6, 13),
                "Recycled plastic bottles",
                "Plastic",
                500.0
        );

        Activity trade = new TradeActivity(
                "A002",
                LocalDate.of(2026, 6, 13),
                "Traded used books",
                "Used Books",
                20.0
        );

        Activity steps = new FitnessStepActivity(
                "A003",
                LocalDate.of(2026, 6, 13),
                "Walked 4500 steps",
                4500,
                0.0
        );

        FitnessEvent event = new FitnessEvent(
                "E001",
                "Community Morning Run",
                LocalDate.of(2026, 6, 13),
                "Community Park",
                50
        );

        boolean registered = event.registerParticipant(resident);

        FitnessEventActivity eventActivity = new FitnessEventActivity(
                "A004",
                LocalDate.of(2026, 6, 13),
                "Joined " + event.getEventName(),
                event
        );

        if (registered) {
            eventActivity.markAttendance();
        }

        Activity fitnessEvent = eventActivity;

        recycling.displayActivityDetails();
        trade.displayActivityDetails();
        steps.displayActivityDetails();
        fitnessEvent.displayActivityDetails();

        System.out.println("=== Fitness Event Registration Test ===");
        System.out.println("Resident Name: " + resident.getFullName());
        System.out.println("Event Name: " + event.getEventName());
        System.out.println("Registration Successful: " + registered);
        System.out.println("Total Participants: " + event.getParticipants().size());
    }
}