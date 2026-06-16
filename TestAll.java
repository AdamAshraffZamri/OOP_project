import java.time.LocalDate;
import java.time.LocalTime;

public class TestAll {

    private static int passCount = 0;
    private static int failCount = 0;

    public static void main(String[] args) {
        // Hanya jalankan test untuk kelas yang dipilih sahaja
        testMedicine();
        testMedicineInventory();
        testCommunityClinic();
        testAppointment();

        System.out.println("\n========================================");
        System.out.println("Total: " + (passCount + failCount) +
                " | Passed: " + passCount + " | Failed: " + failCount);
        System.out.println("========================================");
    }

    // ---------------- Medicine ----------------
    private static void testMedicine() {
        System.out.println("\n--- Testing Medicine ---");

        Medicine medicine = new Medicine("MED001", "Paracetamol", 50, LocalDate.now().plusYears(1));

        check("getMedicineId", medicine.getMedicineId().equals("MED001"));
        check("getMedicineName", medicine.getMedicineName().equals("Paracetamol"));
        check("getStockQuantity initial", medicine.getStockQuantity() == 50);

        medicine.increaseStock(20);
        check("increaseStock", medicine.getStockQuantity() == 70);

        medicine.reduceStock(10);
        check("reduceStock normal", medicine.getStockQuantity() == 60);

        medicine.reduceStock(1000);
        check("reduceStock clamps to 0", medicine.getStockQuantity() == 0);

        Medicine future = new Medicine("MED002", "Vitamin C", 10, LocalDate.now().plusMonths(6));
        check("isExpired false for future date", !future.isExpired());

        Medicine expired = new Medicine("MED003", "OldDrug", 10, LocalDate.now().minusDays(1));
        check("isExpired true for past date", expired.isExpired());

        // Diubah: Memaparkan secara visual isi kandungan fungsi display() Medicine anda
        System.out.println("\n[Visual Display Output for Medicine]:");
        System.out.println("----------------------------------------");
        medicine.display(); 
        System.out.println("----------------------------------------");
        check("display function executed", true);
    }

    // ---------------- MedicineInventory ----------------
    private static void testMedicineInventory() {
        System.out.println("\n--- Testing MedicineInventory ---");

        MedicineInventory inventory = new MedicineInventory("INV001");
        Medicine paracetamol = new Medicine("MED001", "Paracetamol", 50, LocalDate.now().plusYears(1));
        Medicine vitaminC = new Medicine("MED002", "Vitamin C", 0, LocalDate.now().plusMonths(6));

        inventory.addMedicine(paracetamol);
        inventory.addMedicine(vitaminC);

        check("addMedicine count", inventory.getMedicineList().size() == 2);

        inventory.removeMedicine("MED001");
        check("removeMedicine count", inventory.getMedicineList().size() == 1);

        inventory.addMedicine(paracetamol);

        inventory.updateStock("MED001", 25);
        check("updateStock", inventory.checkStock("MED001") == 75);

        check("checkStock for unknown returns 0", inventory.checkStock("MED999") == 0);

        try {
            inventory.dispenseMedicine("MED001", 10);
            check("dispenseMedicine normal", inventory.checkStock("MED001") == 65);
        } catch (OutOfStockException e) {
            check("dispenseMedicine normal", false);
        }

        try {
            inventory.dispenseMedicine("MED002", 1);
            check("dispenseMedicine throws when stock is 0", false);
        } catch (OutOfStockException e) {
            check("dispenseMedicine throws when stock is 0", e.getMessage().toLowerCase().contains("out of stock"));
        }

        try {
            inventory.dispenseMedicine("MED001", 1000);
            check("dispenseMedicine throws when quantity exceeds stock", false);
        } catch (OutOfStockException e) {
            // Mengikut kod dispenseMedicine anda, ia memulangkan ID ubat (MED001)
            check("dispenseMedicine throws when quantity exceeds stock", e.getMessage().contains("MED001"));
        }

        try {
            inventory.dispenseMedicine("MED999", 1);
            check("dispenseMedicine throws when not found", false);
        } catch (OutOfStockException e) {
            check("dispenseMedicine throws when not found", e.getMessage().contains("not found"));
        }

        check("getInventoryId", inventory.getInventoryId().equals("INV001"));
    }

    // ---------------- CommunityClinic ----------------
    private static void testCommunityClinic() {
        System.out.println("\n--- Testing CommunityClinic ---");

        CommunityClinic clinic = new CommunityClinic("CL001", "Taman Senai Clinic", "Jalan Senai 1");
        Resident resident = new Resident("U001", "Alice Tan", "alice@mail.com", "pass123", "0123456789", "Block A-1-1");

        check("getClinicId", clinic.getClinicId().equals("CL001"));
        check("getClinicName", clinic.getClinicName().equals("Taman Senai Clinic"));
        check("getAddress", clinic.getAddress().equals("Jalan Senai 1"));

        check("getMedicineInventory not null", clinic.getMedicineInventory() != null);

        check("viewAppointments initially empty", clinic.viewAppointments().isEmpty());

        Appointment apt1 = new Appointment("APT001", LocalDate.now().plusDays(1), LocalTime.of(10, 0), resident);
        clinic.addAppointment(apt1);
        check("addAppointment size", clinic.viewAppointments().size() == 1);
        check("addAppointment contains", clinic.viewAppointments().contains(apt1));

        Appointment apt2 = new Appointment("APT002", LocalDate.now().plusDays(2), LocalTime.of(14, 30), resident);
        clinic.addAppointment(apt2);
        check("multiple appointments size", clinic.viewAppointments().size() == 2);
        check("appointment order index 0", clinic.viewAppointments().get(0).getAppointmentId().equals("APT001"));
        check("appointment order index 1", clinic.viewAppointments().get(1).getAppointmentId().equals("APT002"));

        Medicine medicine = new Medicine("MED001", "Paracetamol", 100, LocalDate.now().plusYears(1));
        clinic.getMedicineInventory().addMedicine(medicine);
        check("inventory medicine list size", clinic.getMedicineInventory().getMedicineList().size() == 1);
        check("inventory checkStock", clinic.getMedicineInventory().checkStock("MED001") == 100);
    }

    // ---------------- Appointment ----------------
    private static void testAppointment() {
        System.out.println("\n--- Testing Appointment ---");

        Resident resident = new Resident("U001", "Alice Tan", "alice@mail.com", "pass123", "0123456789", "Block A-1-1");
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(10, 30);
        Appointment appointment = new Appointment("APT001", date, time, resident);

        check("getAppointmentId", appointment.getAppointmentId().equals("APT001"));
        check("getAppointmentDate", appointment.getAppointmentDate().equals(date));
        check("getAppointmentTime", appointment.getAppointmentTime().equals(time));
        check("getResident", appointment.getResident() == resident);

        check("default priorityLevel is 0", appointment.getPriorityLevel() == 0);
        check("default status is SCHEDULED", appointment.getStatus().equals("SCHEDULED"));

        appointment.setPriorityLevel(2);
        check("setPriorityLevel", appointment.getPriorityLevel() == 2);

        appointment.updateStatus("COMPLETED");
        check("updateStatus", appointment.getStatus().equals("COMPLETED"));

        // Diubah: Memaparkan secara visual isi kandungan fungsi display() Appointment anda
        System.out.println("\n[Visual Display Output for Appointment]:");
        System.out.println("----------------------------------------");
        appointment.display();
        System.out.println("----------------------------------------");
        check("display function executed", true);
    }

    // ---------------- Helper ----------------
    private static void check(String testName, boolean passed) {
        if (passed) {
            passCount++;
        } else {
            failCount++;
        }
        System.out.println((passed ? "PASS" : "FAIL") + " - " + testName);
    }
}