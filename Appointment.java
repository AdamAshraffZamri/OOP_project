import java.time.LocalDate;
import java.time.LocalTime ; 

public class Appointment {
    private String appointmentId;
    private LocalDate appointmentDate ;
    private LocalTime appointmentTime ;
    private int priorityLevel;
    private String status ;
    private Resident resident ; 

    public Appointment( String appointmentId, LocalDate appointmentDate, LocalTime appointmentTime, Resident resident ){
        this.appointmentId = appointmentId ;
        this.appointmentDate = appointmentDate ;
        this.appointmentTime = appointmentTime ;
        this.resident = resident ;
        this.priorityLevel = 0;
        this.status = "SCHEDULED" ;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel ; 
    }

    public void updateStatus (String status){
        this.status = status ;
    }

    public String getAppointmentId (){ 
        return appointmentId ;
    }

    public LocalTime getAppointmentTime (){ 
        return appointmentTime ;
    }

    public int getPriorityLevel (){
        return priorityLevel ;
    }

    public Resident getResident (){
        return resident ;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate ;
    }

    public String getStatus (){
        return status ;
    }

    public void display() {
        System.out.println("Appointment[" + appointmentId + ", " + appointmentDate + " " + appointmentTime +
                ", resident=" + resident.getFullName() + ", priority=" + priorityLevel + ", status=" + status + "]");
    }
}
