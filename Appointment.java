//! temporary from AI cuz need to integrate the module

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private String appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private int priorityLevel;
    private String status;
    private Resident resident;

    public Appointment(String appointmentId, LocalDate appointmentDate, LocalTime appointmentTime, Resident resident) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.resident = resident;
        this.priorityLevel = 1; // default lowest priority
        this.status = "Scheduled";
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public String getAppointmentId() { return appointmentId; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public int getPriorityLevel() { return priorityLevel; }
    public String getStatus() { return status; }
    public Resident getResident() { return resident; }

    @Override
    public String toString() {
        return "Appointment[" + appointmentId + "] " + resident.getFullName()
                + " on " + appointmentDate + " at " + appointmentTime
                + " | Priority: " + priorityLevel + " | Status: " + status;
    }
}