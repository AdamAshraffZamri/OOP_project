import java.time.LocalDate;
import java.time.LocalTime;

public class ClinicStaff extends User {
    private String staffId;
    private String position;

    public ClinicStaff(String userId, String fullName, String email, String password, String phoneNo, String staffId, String position) {
        super(userId, fullName, email, password, phoneNo);
        this.staffId = staffId;
        this.position = position;
    }

    public void manageMedicineInventory(MedicineInventory inventory) {
        System.out.println("Staff " + fullName + " is managing the medicine inventory.");
        inventory.display();
    }

    public Appointment bookAppointment(CommunityClinic clinic, Resident resident,
                                       LocalDate appointmentDate, LocalTime appointmentTime) {
        String appointmentId = "APT-" + resident.getUserId() + "-" + appointmentDate + "-" + appointmentTime;
        Appointment appointment = new Appointment(appointmentId, appointmentDate, appointmentTime, resident);
        clinic.addAppointment(appointment);
        return appointment;
    }

    public void setAppointmentPriority(Appointment appointment, int priorityLevel) {
        appointment.setPriorityLevel(priorityLevel);
        System.out.println("Appointment priority updated.");
    }

    public void applyClinicPriority(ClinicPriorityReward reward, Appointment appointment) {
        reward.applyToAppointment(appointment);
    }

    public String getStaffId() {
        return staffId;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public void displayProfile() {
        System.out.println("=== Clinic Staff Profile ===");
        super.displayProfile();
        System.out.println("Staff ID: " + staffId);
        System.out.println("Position: " + position);
    }
}
