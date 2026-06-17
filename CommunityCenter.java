import java.util.ArrayList;

public class CommunityCenter{
    private String centerId;
    private String centerName;
    private String location;
    private ArrayList<FitnessEvent> fitnessEvents;
 
    public CommunityCenter(String centerId, String centerName, String location){
        this.centerId= centerId;
        this.centerName= centerName;
        this.location= location;
        this.fitnessEvents= new ArrayList<>();
    }
 
    public void addEvent(FitnessEvent event){
        fitnessEvents.add(event);
        System.out.println("Event \""+ event.getEventName()+ "\" added to "+ centerName+"...");
    }
 
    public void removeEvent(String eventId){
        //! lambda function to remove the event if hit same eventId
        boolean removed= fitnessEvents.removeIf(e -> e.getEventId().equals(eventId));
        if(removed){
            System.out.println("Event ["+eventId+ "] removed from "+ centerName+ "...");
        } else{
            System.out.println("Event ["+ eventId+ "] not found...");
        }
    }
 
    public void updateEvent(FitnessEvent event){
        for(int i= 0; i<fitnessEvents.size(); i++){
            if(fitnessEvents.get(i).getEventId().equals(event.getEventId())){
                fitnessEvents.set(i, event);
                System.out.println("Event \""+ event.getEventName()+ "\" updated!");
                return;
            }
        }
        System.out.println("Event ["+ event.getEventId()+ "] not found for update.");
    }

    public ArrayList<FitnessEvent> getFitnessEvents() { return fitnessEvents; }
}
