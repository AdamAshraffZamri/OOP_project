import java.time.LocalDate;
import java.util.ArrayList;

public class FitnessEvent {
    private String eventId;
    private String eventName;
    private LocalDate eventDate;
    private String location;
    private int maxParticipants;
    private ArrayList<Resident> participants;

    public FitnessEvent(String eventId, String eventName, LocalDate eventDate,
                        String location, int maxParticipants) {
        if (eventId == null || eventId.isBlank()) {
            throw new IllegalArgumentException("Event ID cannot be empty.");
        }

        if (eventName == null || eventName.isBlank()) {
            throw new IllegalArgumentException("Event name cannot be empty.");
        }

        if (eventDate == null) {
            throw new IllegalArgumentException("Event date cannot be null.");
        }

        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }

        if (maxParticipants <= 0) {
            throw new IllegalArgumentException("Max participants must be greater than 0.");
        }

        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.participants = new ArrayList<>();
    }

    public boolean registerParticipant(Resident resident) {
        if (resident == null) {
            return false;
        }

        if (isFull()) {
            return false;
        }

        if (participants.contains(resident)) {
            return false;
        }

        participants.add(resident);
        return true;
    }

    public void removeParticipant(Resident resident) {
        participants.remove(resident);
    }

    public boolean isFull() {
        return participants.size() >= maxParticipants;
    }

    public ArrayList<Resident> getParticipants() {
        return participants;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public String getLocation() {
        return location;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    @Override
    public String toString(){
        String result= "Event ID    : "+ eventId+
                        "\nEvent Name  : "+ eventName+
                        "\nDate        : "+ eventDate+
                        "\nLocation    : "+ location+
                        "\nParticipants: " + participants.size()+ "/"+ maxParticipants+
                        "\nStatus      : " + (isFull() ? "Full" : "Open");

        if(!participants.isEmpty()){
            result+= "\nRegistered  :";
            for(Resident r : participants) {
                result+= "\n  - " + r.getFullName();
            }
        }
        return result;
    }

}
