import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser = null;
    private static FileManager fileManager = new FileManager("users.txt", "medicine.txt");
    private static CommunityClinic clinic = new CommunityClinic("CL-001", "EcoHealth Clinic", "123 Green Ave");
    private static CommunityCenter center = new CommunityCenter("CC-001", "EcoHealth Center", "124 Green Ave");
    private static Leaderboard leaderboard = new Leaderboard("LB-001");

    public static void main(String[] args) {
        System.out.println("Loading system data...");
        users = fileManager.loadUsers();
        
        boolean hasAdmin = false;
        boolean hasStaff = false;
        for (User u : users) {
            if (u instanceof Admin) hasAdmin = true;
            if (u instanceof ClinicStaff) hasStaff = true;
        }

        if (!hasAdmin) {
            users.add(new Admin("A001", "System Administrator", "admin@admin.com", "admin123", "00-00", "SuperAdmin"));
        }
        if (!hasStaff) {
            users.add(new ClinicStaff("S001", "Primary Nurse", "staff@clinic.com", "staff123", "00-01", "ST-001", "Nurse"));
        }

        boolean running = true;

        while (running) {
            if (currentUser == null) {
                running = showLoginMenu();
            } else {
                if (currentUser instanceof Resident) {
                    showResidentMenu((Resident) currentUser);
                } else if (currentUser instanceof Admin) {
                    showAdminMenu((Admin) currentUser);
                } else if (currentUser instanceof ClinicStaff) {
                    showStaffMenu((ClinicStaff) currentUser);
                }
            }
        }

        System.out.println("Saving system data...");
        fileManager.saveUsers(users);
        System.out.println("Goodbye!");
    }

    private static boolean showLoginMenu() {
        System.out.println("\n=== EcoHealth Master Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register Resident");
        System.out.println("3. Exit");
        System.out.print("Option: ");
        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                System.out.print("Email: ");
                String email = scanner.nextLine().trim();
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();

                for (User u : users) {
                    if (u.login(email, password)) {
                        currentUser = u;
                        return true;
                    }
                }
                return true;
            case 2:
                System.out.print("ID: "); String rId = scanner.nextLine().trim();
                System.out.print("Name: "); String n = scanner.nextLine().trim();
                System.out.print("Email: "); String e = scanner.nextLine().trim();
                System.out.print("Pass: "); String p = scanner.nextLine().trim();
                System.out.print("Phone: "); String ph = scanner.nextLine().trim();
                System.out.print("Addr: "); String a = scanner.nextLine().trim();
                users.add(new Resident(rId, n, e, p, ph, a));
                System.out.println("Registered!");
                return true;
            case 3:
                return false;
            default:
                System.out.println("Invalid.");
                return true;
        }
    }

    private static void showResidentMenu(Resident resident) {
        System.out.println("\n=== Resident: " + resident.getFullName() + " ===");
        System.out.println("1. Log Activity");
        System.out.println("2. Join Fitness Event");
        System.out.println("3. Book Clinic Appointment");
        System.out.println("4. View Leaderboard");
        System.out.println("5. Redeem Reward");
        System.out.println("6. Logout");
        System.out.print("Option: ");
        
        switch (getIntInput()) {
            case 1:
                System.out.println("1.Recycling 2.Step 3.Trade");
                int act = getIntInput();
                System.out.print("ID: "); String i = scanner.nextLine();
                System.out.print("Desc: "); String d = scanner.nextLine();
                if (act == 1) {
                    System.out.print("Material: "); String m = scanner.nextLine();
                    System.out.print("Weight(g): "); double w = getDoubleInput();
                    try {
                        resident.logActivity(new RecyclingActivity(i, LocalDate.now(), d, m, w));
                    } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
                } else if (act == 2) {
                    System.out.print("Steps: "); int s = getIntInput();
                    try {
                        resident.logActivity(new FitnessStepActivity(i, LocalDate.now(), d, s, 0));
                    } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
                } else if (act == 3) {
                    System.out.print("Item: "); String itemName = scanner.nextLine();
                    System.out.print("Value(RM): "); double val = getDoubleInput();
                    try {
                        resident.logActivity(new TradeActivity(i, LocalDate.now(), d, itemName, val));
                    } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
                }
                break;
            case 2:
                ArrayList<FitnessEvent> events = center.getFitnessEvents();
                if (events.isEmpty()) { System.out.println("No events."); break; }
                for (int idx=0; idx<events.size(); idx++) {
                    System.out.println(idx + ". " + events.get(idx).getEventName());
                }
                System.out.print("Select: "); int eIdx = getIntInput();
                if (eIdx >= 0 && eIdx < events.size()) {
                    boolean success = events.get(eIdx).registerParticipant(resident);
                    if (success) System.out.println("Registered!");
                    else System.out.println("Failed to register.");
                }
                break;
            case 3:
                System.out.print("Appt ID: "); String appId = scanner.nextLine();
                System.out.print("Date (YYYY-MM-DD): "); String dateStr = scanner.nextLine();
                System.out.print("Time (HH:MM): "); String timeStr = scanner.nextLine();
                try {
                    LocalDate ld = LocalDate.parse(dateStr);
                    LocalTime lt = LocalTime.parse(timeStr);
                    Appointment app = new Appointment(appId, ld, lt, resident);
                    clinic.addAppointment(app);
                    System.out.println("Booked appointment at " + clinic.getClinicName());
                } catch (DateTimeParseException ex) {
                    System.out.println("Invalid date/time format.");
                }
                break;
            case 4:
                ArrayList<Resident> rl = new ArrayList<>();
                for (User u : users) if (u instanceof Resident) rl.add((Resident) u);
                leaderboard.generateRanking(rl);
                leaderboard.displayRanking();
                break;
            case 5:
                 System.out.println("1. Voucher (100pt)  2. Clinic Priority (150pt)");
                 int rew = getIntInput();
                 if (rew == 1) {
                     resident.redeemReward(new Voucher("V1", "CODE1", "Voucher", 100, LocalDate.now().plusMonths(1)));
                 } else if (rew == 2) {
                     resident.redeemReward(new ClinicPriorityReward("P1", 150, 1));
                 }
                 break;
            case 6:
                currentUser.logout();
                currentUser = null;
                break;
        }
    }

    private static void showAdminMenu(Admin admin) {
        System.out.println("\n=== Admin: " + admin.getFullName() + " ===");
        System.out.println("1. Add Fitness Event");
        System.out.println("2. Remove Fitness Event");
        System.out.println("3. Issue Voucher");
        System.out.println("4. View Center Events");
        System.out.println("5. Logout");
        System.out.print("Option: ");
        
        switch (getIntInput()) {
            case 1:
                System.out.print("Event ID: "); String eId = scanner.nextLine();
                System.out.print("Name: "); String n = scanner.nextLine();
                System.out.print("Location: "); String l = scanner.nextLine();
                System.out.print("Max Participants: "); int mx = getIntInput();
                try {
                    center.addEvent(new FitnessEvent(eId, n, LocalDate.now().plusWeeks(1), l, mx));
                } catch (Exception ex) { System.out.println("Error: " + ex.getMessage()); }
                break;
            case 2:
                System.out.print("Event ID to remove: ");
                center.removeEvent(scanner.nextLine());
                break;
            case 3:
                System.out.println("Find resident to issue Voucher");
                break;
            case 4:
                for(FitnessEvent fe : center.getFitnessEvents()) {
                    System.out.println(fe.getEventId() + " - " + fe.getEventName());
                }
                break;
            case 5:
                currentUser.logout();
                currentUser = null;
                break;
        }
    }

    private static void showStaffMenu(ClinicStaff staff) {
        System.out.println("\n=== Staff: " + staff.getFullName() + " ===");
        System.out.println("1. View Appointments");
        System.out.println("2. Update Appointment Priority");
        System.out.println("3. Add Medicine to Stock");
        System.out.println("4. Dispense Medicine");
        System.out.println("5. Logout");
        System.out.print("Option: ");
        
        switch (getIntInput()) {
            case 1:
                for (Appointment ap : clinic.viewAppointments()) {
                    System.out.println(ap.getAppointmentId() + " - " + ap.getResident().getFullName() + " - " + ap.getAppointmentDate() + " - Prio:" + ap.getPriorityLevel());
                }
                break;
            case 2:
                System.out.print("Appt ID: "); String apId = scanner.nextLine();
                System.out.print("New Priority(0-5): "); int pr = getIntInput();
                for (Appointment ap : clinic.viewAppointments()) {
                    if (ap.getAppointmentId().equals(apId)) {
                        staff.setAppointmentPriority(ap, pr);
                        break;
                    }
                }
                break;
            case 3:
                System.out.print("Med ID: "); String mId = scanner.nextLine();
                System.out.print("Name: "); String mn = scanner.nextLine();
                System.out.print("Quantity: "); int q = getIntInput();
                int existing = clinic.getMedicineInventory().checkStock(mId);
                if (existing >= 0) {
                    clinic.getMedicineInventory().updateStock(mId, q);
                    System.out.println("Stock updated.");
                } else {
                    clinic.getMedicineInventory().addMedicine(new Medicine(mId, mn, q, LocalDate.now().plusMonths(6)));
                    System.out.println("Medicine added.");
                }
                break;
            case 4:
                System.out.print("Med ID to dispense: "); String dm = scanner.nextLine();
                System.out.print("Quantity: "); int dq = getIntInput();
                try {
                    clinic.getMedicineInventory().dispenseMedicine(dm, dq);
                } catch (OutOfStockException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            case 5:
                currentUser.logout();
                currentUser = null;
                break;
        }
    }

    private static int getIntInput() {
         try { return Integer.parseInt(scanner.nextLine().trim()); } 
         catch (Exception e) { return -1; }
    }
    
    private static double getDoubleInput() {
         try { return Double.parseDouble(scanner.nextLine().trim()); } 
         catch (Exception e) { return -1.0; }
    }
}
