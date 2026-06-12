public class ClinicStaff extends User {
    private String staffId;
    private String position;

    public ClinicStaff(String userId, String fullName, String email, String password, String phoneNo, String staffId, String position) {
        super(userId, fullName, email, password, phoneNo);
        this.staffId = staffId;
        this.position = position;
    }

    // Stubs for clinic operations
    public void manageInventory(MedicineInventory inventory) {
        System.out.println("Staff " + fullName + " is managing the inventory.");
    }

    public void setAppointmentPriority(Appointment appointment, int priorityLevel) {
        appointment.setPriorityLevel(priorityLevel);
        System.out.println("Appointment priority updated.");
    }
}
