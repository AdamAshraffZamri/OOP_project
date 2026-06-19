import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser = null;
    private static FileManager fileManager = new FileManager("users.txt", "medicine_inventory.txt");
    private static CommunityClinic clinic = new CommunityClinic("CL-001", "EcoHealth Clinic", "123 Green Ave");
    private static CommunityCenter center = new CommunityCenter("CC-001", "EcoHealth Center", "124 Green Ave");
    private static Leaderboard leaderboard = new Leaderboard("LB-001");
    private static int appointmentCounter = 1;
    private static int fitnessEventActivityCounter = 1;

    public static void main(String[] args) {
        System.out.println("Loading system data...");
        users = fileManager.loadUsers();
        MedicineInventory loadedInventory = fileManager.loadMedicineInventory();
        clinic.getMedicineInventory().getMedicineList().clear();
        clinic.getMedicineInventory().getMedicineList().addAll(loadedInventory.getMedicineList());

        boolean hasAdmin = false;
        boolean hasStaff = false;
        for (User u : users) {
            if (u instanceof Admin) hasAdmin = true;
            if (u instanceof ClinicStaff) hasStaff = true;
        }

        if (!hasAdmin) {
            users.add(new Admin("A001", "System Administrator", "admin@admin.com", "admin123", "011-9990000", "SuperAdmin"));
        }
        if (!hasStaff) {
            users.add(new ClinicStaff("S001", "Primary Nurse", "staff@clinic.com", "staff123", "012-9000000", "ST-001", "Nurse"));
        }

        boolean running = true;

        while (running) {
            if (currentUser == null) {
                running = showLoginMenu();
            } else if (currentUser instanceof Resident) {
                showResidentMenu((Resident) currentUser);
            } else if (currentUser instanceof Admin) {
                showAdminMenu((Admin) currentUser);
            } else if (currentUser instanceof ClinicStaff) {
                showStaffMenu((ClinicStaff) currentUser);
            }
        }

        fileManager.saveUsers(users);
        fileManager.saveMedicineInventory(clinic.getMedicineInventory());
        System.out.println("Goodbye!");
    }

    private static boolean showLoginMenu() {
        printHeader("EcoHealth Master Menu");
        System.out.println("1. Login");
        System.out.println("2. Register Resident");
        System.out.println("3. Exit");
        int choice = readMenuOption("Enter your option: ");

        switch (choice) {
            case 1:
                while (true) {
                    String email = readNonEmptyString("Enter email: ");
                    String password = readNonEmptyString("Enter password: ");

                    User matchedUser = null;
                    for (User u : users) {
                        if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                            matchedUser = u;
                            break;
                        }
                    }

                    if (matchedUser != null) {
                        matchedUser.login(email, password);
                        currentUser = matchedUser;
                        return true;
                    }

                    System.out.println("Login failed. Please check your email or password.");
                    if (!confirmRetry("Try again? (Y/N): ")) {
                        return true;
                    }
                }
            case 2:
                String rId = readNonEmptyString("Enter ID: ");
                String n = readNonEmptyString("Enter name: ");
                String e = readNonEmptyString("Enter email: ");
                String p = readNonEmptyString("Enter password: ");
                String ph = readNonEmptyString("Enter phone: ");
                String a = readNonEmptyString("Enter home address: ");
                users.add(new Resident(rId, n, e, p, ph, a));
                fileManager.saveUsers(users);
                System.out.println("Registration successful.");
                return true;
            case 3:
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
                return true;
        }
    }

    private static void showResidentMenu(Resident resident) {
        printHeader("Resident Menu - " + resident.getFullName());
        System.out.println("1. View Profile");
        System.out.println("2. Log Activity");
        System.out.println("3. View Activity History");
        System.out.println("4. View Reward History");
        System.out.println("5. View Points");
        System.out.println("6. Redeem Reward");
        System.out.println("7. Request Appointment");
        System.out.println("8. View My Appointments");
        System.out.println("9. View Leaderboard");
        System.out.println("10. Change Password");
        System.out.println("11. Logout");

        switch (readMenuOption("Enter your option: ")) {
            case 1:
                resident.displayProfile();
                break;
            case 2:
                showLogActivityMenu(resident);
                break;
            case 3:
                resident.viewActivityHistory();
                break;
            case 4:
                displayRewardHistory(resident);
                break;
            case 5:
                System.out.println("Community Points: " + resident.viewPoints());
                break;
            case 6:
                showRedeemRewardMenu(resident);
                break;
            case 7:
                requestResidentAppointment(resident);
                break;
            case 8:
                displayAppointmentsForResident(resident);
                break;
            case 9:
                ArrayList<Resident> rl = new ArrayList<>();
                for (User u : users) if (u instanceof Resident) rl.add((Resident) u);
                leaderboard.generateRanking(rl);
                leaderboard.displayRanking();
                break;
            case 10:
                changePassword(resident);
                break;
            case 11:
                currentUser.logout();
                currentUser = null;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void showLogActivityMenu(Resident resident) {
        printHeader("Log Activity");
        System.out.println("1. Recycling Activity");
        System.out.println("2. Fitness Step Activity");
        System.out.println("3. Fitness Event Activity");
        System.out.println("4. Trade Activity");
        System.out.println("5. Back");
        int act = readMenuOption("Enter your option: ");

        if (act == 5) {
            return;
        }

        while (act < 1 || act > 5) {
            System.out.println("Invalid option. Please try again.");
            act = readMenuOption("Enter your option: ");
            if (act == 5) {
                return;
            }
        }

        try {
            if (act == 1) {
                String activityId = readNonEmptyString("Enter activity ID: ");
                String description = readNonEmptyString("Enter description: ");
                String material = readNonEmptyString("Enter material type: ");
                double weight = readPositiveDouble("Enter weight(g): ");
                logActivityIfPointsEarned(resident, new RecyclingActivity(activityId, LocalDate.now(), description, material, weight));
            } else if (act == 2) {
                String activityId = readNonEmptyString("Enter activity ID: ");
                String description = readNonEmptyString("Enter description: ");
                int steps = readPositiveInt("Enter steps: ");
                logActivityIfPointsEarned(resident, new FitnessStepActivity(activityId, LocalDate.now(), description, steps, 0));
            } else if (act == 3) {
                FitnessEvent event = selectFitnessEventForActivity(resident);
                if (event == null) return;
                if (!event.registerParticipant(resident) && !event.getParticipants().contains(resident)) {
                    System.out.println("Failed to register for fitness event.");
                    return;
                }
                String activityId = generateFitnessEventActivityId();
                String description = "Joined fitness event: " + event.getEventName();
                FitnessEventActivity eventActivity = new FitnessEventActivity(activityId, LocalDate.now(), description, event);
                eventActivity.markAttendance();
                logActivityIfPointsEarned(resident, eventActivity);
            } else if (act == 4) {
                String activityId = readNonEmptyString("Enter activity ID: ");
                String description = readNonEmptyString("Enter description: ");
                String itemName = readNonEmptyString("Enter item name: ");
                double value = readPositiveDouble("Enter value(RM): ");
                logActivityIfPointsEarned(resident, new TradeActivity(activityId, LocalDate.now(), description, itemName, value));
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void showRedeemRewardMenu(Resident resident) {
        int rew;
        while (true) {
            printHeader("Redeem Reward");
            System.out.println("Current Points: " + resident.viewPoints());
            System.out.println("1. Voucher - Required Points: 30");
            System.out.println("2. Clinic Priority Reward - Required Points: 30");
            System.out.println("3. Back");
            rew = readMenuOption("Enter your option: ");
            if (rew >= 1 && rew <= 3) {
                break;
            }
            System.out.println("Invalid option. Please try again.");
        }

        if (rew == 1) {
            Voucher voucher = new Voucher("V-" + System.currentTimeMillis(), "ECO30", "EcoHealth demo voucher", 30, LocalDate.now().plusMonths(1));
            if (resident.redeemReward(voucher)) {
                displayRewardRedemptionResult(resident, voucher);
            } else {
                System.out.println("Reward redemption failed.");
            }
        } else if (rew == 2) {
            Appointment targetAppointment = selectResidentAppointment(resident);
            if (targetAppointment == null) return;

            System.out.println("Before applying reward:");
            targetAppointment.display();
            ClinicPriorityReward priorityReward = new ClinicPriorityReward("P-" + System.currentTimeMillis(), 30, 5);
            if (resident.redeemReward(priorityReward)) {
                priorityReward.applyToAppointment(targetAppointment);
                displayRewardRedemptionResult(resident, priorityReward);
                System.out.println("After applying reward:");
                targetAppointment.display();
            } else {
                System.out.println("Reward redemption failed.");
            }
        }
    }

    private static void requestResidentAppointment(Resident resident) {
        LocalDate appointmentDate = readFlexibleDateOrCancel("Enter date (YYYY-MM-DD) or 0 to cancel: ");
        if (appointmentDate == null) {
            System.out.println("Appointment request cancelled.");
            return;
        }

        LocalTime appointmentTime = readFlexibleTimeOrCancel("Enter time (HH:mm) or 0 to cancel: ");
        if (appointmentTime == null) {
            System.out.println("Appointment request cancelled.");
            return;
        }

        String appointmentId = generateAppointmentId();
        Appointment app = new Appointment(appointmentId, appointmentDate, appointmentTime, resident);
        clinic.addAppointment(app);
        System.out.println("Appointment request submitted successfully.");
        System.out.println("Appointment ID: " + appointmentId);
    }

    private static void showAdminMenu(Admin admin) {
        printHeader("Admin Menu - " + admin.getFullName());
        System.out.println("1. View Profile");
        System.out.println("2. Add Fitness Event");
        System.out.println("3. Edit Fitness Event");
        System.out.println("4. Remove Fitness Event");
        System.out.println("5. Issue Voucher");
        System.out.println("6. Revoke Voucher");
        System.out.println("7. View Center Events");
        System.out.println("8. Generate Leaderboard");
        System.out.println("9. Change Password");
        System.out.println("10. Logout");

        switch (readMenuOption("Enter your option: ")) {
            case 1:
                admin.displayProfile();
                break;
            case 2:
                addFitnessEvent(admin);
                break;
            case 3:
                editFitnessEvent(admin);
                break;
            case 4:
                removeFitnessEvent(admin);
                break;
            case 5:
                issueVoucherToResident(admin);
                break;
            case 6:
                revokeVoucherFromResident(admin);
                break;
            case 7:
                displayFitnessEvents();
                break;
            case 8:
                Leaderboard adminLeaderboard = admin.generateLeaderboard(getResidents());
                adminLeaderboard.displayRanking();
                break;
            case 9:
                changePassword(admin);
                break;
            case 10:
                currentUser.logout();
                currentUser = null;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void addFitnessEvent(Admin admin) {
        String eventId = readUniqueFitnessEventId();
        if (eventId == null) {
            System.out.println("Add fitness event cancelled.");
            return;
        }
        String name = readNonEmptyString("Enter event name: ");
        LocalDate date = readFlexibleDate("Enter event date (e.g. 2026-06-20 or 2026-6-20): ");
        String location = readNonEmptyString("Enter location: ");
        int max = readPositiveInt("Enter max participants: ");

        try {
            FitnessEvent event = new FitnessEvent(eventId, name, date, location, max);
            admin.createEvent(event);
            center.addEvent(event);
            displayFitnessEventDetails(event);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void editFitnessEvent(Admin admin) {
        if (center.getFitnessEvents().isEmpty()) {
            System.out.println("No fitness events available.");
            return;
        }

        displayFitnessEvents();
        FitnessEvent existing = findEventByIdWithRetry("Enter event ID to edit: ");
        if (existing == null) {
            return;
        }
        String editId = existing.getEventId();

        String editName = readNonEmptyString("Enter new event name: ");
        LocalDate editDate = readFlexibleDate("Enter new date (e.g. 2026-06-20 or 2026-6-20): ");
        String editLocation = readNonEmptyString("Enter new location: ");
        int editMax = readPositiveInt("Enter new max participants: ");

        try {
            FitnessEvent updatedEvent = new FitnessEvent(editId, editName, editDate, editLocation, editMax);
            admin.editEvent(updatedEvent);
            center.updateEvent(updatedEvent);
            System.out.println("Updated event details:");
            System.out.println(updatedEvent);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void removeFitnessEvent(Admin admin) {
        if (center.getFitnessEvents().isEmpty()) {
            System.out.println("No fitness events available.");
            return;
        }

        displayFitnessEvents();
        FitnessEvent event = findEventByIdWithRetry("Enter event ID to remove: ");
        if (event == null) {
            return;
        }
        String deleteId = event.getEventId();
        admin.deleteEvent(deleteId);
        center.removeEvent(deleteId);
    }

    private static void issueVoucherToResident(Admin admin) {
        ArrayList<Resident> residents = getResidents();
        if (residents.isEmpty()) {
            System.out.println("No residents available.");
            return;
        }

        displayResidents(residents);
        Resident resident = findResidentByIdOrEmailWithRetry("Enter resident ID or email: ");
        if (resident == null) {
            return;
        }

        int beforePoints = resident.viewPoints();
        Voucher voucher = new Voucher("V-" + System.currentTimeMillis(), "ADMIN30", "Admin-issued EcoHealth voucher", 0, LocalDate.now().plusMonths(1));
        admin.issueVoucher(voucher, resident);
        printHeader("Voucher Issued");
        System.out.println("Resident       : " + resident.getFullName());
        System.out.println("Email          : " + resident.getEmail());
        System.out.println("Voucher Code   : " + voucher.getVoucherCode());
        System.out.println("Description    : " + voucher.getDescription());
        System.out.println("Expiry Date    : " + voucher.getExpiryDate());
        System.out.println("Status         : " + voucher.getStatus());
        System.out.println("Points Deducted: No");
        System.out.println("Points Before  : " + beforePoints);
        System.out.println("Points After   : " + resident.viewPoints());
    }

    private static void revokeVoucherFromResident(Admin admin) {
        ArrayList<Resident> residents = getResidents();
        if (residents.isEmpty()) {
            System.out.println("No residents available.");
            return;
        }

        displayResidents(residents);
        Resident resident = findResidentByIdOrEmailWithRetry("Enter resident ID or email: ");
        if (resident == null) {
            return;
        }

        ArrayList<Voucher> vouchers = getResidentVouchers(resident);
        if (vouchers.isEmpty()) {
            System.out.println("No vouchers available for this resident.");
            return;
        }

        printHeader("Resident Vouchers");
        System.out.printf("%-4s %-12s %-30s %-14s %-24s%n", "No.", "Code", "Description", "Valid Until", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 0; i < vouchers.size(); i++) {
            Voucher voucher = vouchers.get(i);
            System.out.printf("%-4d %-12s %-30s %-14s %-24s%n",
                    i + 1,
                    voucher.getVoucherCode(),
                    voucher.getDescription(),
                    voucher.getExpiryDate(),
                    voucher.getStatus());
        }

        int voucherIndex = readIndexWithRetry("Enter voucher number to revoke: ", vouchers.size());
        if (voucherIndex < 0) return;

        Voucher voucher = vouchers.get(voucherIndex);
        admin.revokeVoucher(voucher);
        System.out.println("Voucher revoked successfully.");
    }

    private static void showStaffMenu(ClinicStaff staff) {
        printHeader("Clinic Staff Menu - " + staff.getFullName());
        System.out.println("1. View Profile");
        System.out.println("2. View Appointments");
        System.out.println("3. Update Appointment Priority");
        System.out.println("4. Add Medicine to Stock");
        System.out.println("5. Dispense Medicine");
        System.out.println("6. Display Medicine Inventory");
        System.out.println("7. Change Password");
        System.out.println("8. Logout");

        switch (readMenuOption("Enter your option: ")) {
            case 1:
                staff.displayProfile();
                break;
            case 2:
                displayAllAppointments();
                break;
            case 3:
                updateAppointmentPriority(staff);
                break;
            case 4:
                addOrUpdateMedicine();
                break;
            case 5:
                dispenseMedicine();
                break;
            case 6:
                clinic.getMedicineInventory().display();
                break;
            case 7:
                changePassword(staff);
                break;
            case 8:
                currentUser.logout();
                currentUser = null;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private static void updateAppointmentPriority(ClinicStaff staff) {
        if (clinic.viewAppointments().isEmpty()) {
            System.out.println("No appointments available.");
            return;
        }

        displayAllAppointments();
        Appointment appointment = findAppointmentByIdWithRetry("Enter appointment ID: ");
        if (appointment == null) {
            return;
        }

        int priority = readIntInRange("Enter new priority level (0-5): ", 0, 5);

        staff.setAppointmentPriority(appointment, priority);
        System.out.println("Updated appointment details:");
        appointment.display();
    }

    private static void addOrUpdateMedicine() {
        while (true) {
            printHeader("Medicine Stock");
            System.out.println("1. Add New Medicine");
            System.out.println("2. Update Existing Stock");
            System.out.println("3. Back");
            int choice = readMenuOption("Enter your option: ");

            if (choice == 1) {
                addMedicine();
                return;
            } else if (choice == 2) {
                updateMedicineStock();
                return;
            } else if (choice == 3) {
                return;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addMedicine() {
        String medicineId;
        while (true) {
            medicineId = readNonEmptyString("Enter medicine ID: ");
            if (medicineId.equals("0")) {
                return;
            }
            if (findMedicineById(medicineId) == null) {
                break;
            }
            System.out.println("Medicine ID already exists. Enter a different ID, or enter 0 to go back.");
        }

        String medicineName = readNonEmptyString("Enter medicine name: ");
        int quantity = readPositiveInt("Enter stock quantity: ");
        LocalDate expiryDate = readFlexibleDate("Enter expiry date (e.g. 2026-06-20 or 2026-6-20): ");

        Medicine medicine = new Medicine(medicineId, medicineName, quantity, expiryDate);
        clinic.getMedicineInventory().addMedicine(medicine);
        fileManager.saveMedicineInventory(clinic.getMedicineInventory());
        System.out.println("Medicine added successfully.");
        medicine.display();
    }

    private static void updateMedicineStock() {
        if (clinic.getMedicineInventory().getMedicineList().isEmpty()) {
            System.out.println("No medicine available.");
            return;
        }

        if (!clinic.getMedicineInventory().getMedicineList().isEmpty()) {
            clinic.getMedicineInventory().display();
        }

        Medicine existing = findMedicineByIdWithRetry("Enter medicine ID: ");
        if (existing == null) {
            return;
        }
        int quantity = readPositiveInt("Enter stock quantity to add: ");

        clinic.getMedicineInventory().updateStock(existing.getMedicineId(), quantity);
        fileManager.saveMedicineInventory(clinic.getMedicineInventory());
        System.out.println("Stock updated successfully.");
        existing.display();
    }

    private static void dispenseMedicine() {
        if (clinic.getMedicineInventory().getMedicineList().isEmpty()) {
            System.out.println("No medicine available.");
            return;
        }

        clinic.getMedicineInventory().display();
        Medicine medicine = findMedicineByIdWithRetry("Enter medicine ID: ");
        if (medicine == null) {
            return;
        }
        String medicineId = medicine.getMedicineId();

        while (true) {
            int quantity = readPositiveInt("Enter quantity: ");
            try {
                clinic.getMedicineInventory().dispenseMedicine(medicineId, quantity);
                fileManager.saveMedicineInventory(clinic.getMedicineInventory());
                return;
            } catch (OutOfStockException ex) {
                System.out.println("OutOfStockException handled: " + ex.getMessage() + ". Dispense failed.");
                if (!confirmRetry("Try another quantity? (Y/N): ")) {
                    return;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                if (!confirmRetry("Try another quantity? (Y/N): ")) {
                    return;
                }
            }
        }
    }

    private static void changePassword(User user) {
        while (true) {
            String oldPassword = readNonEmptyString("Enter old password: ");
            if (!user.getPassword().equals(oldPassword)) {
                System.out.println("Incorrect old password.");
                if (!confirmRetry("Try again? (Y/N): ")) {
                    return;
                }
                continue;
            }

            String newPassword = readNonEmptyString("Enter new password: ");
            if (user.changePassword(oldPassword, newPassword)) {
                fileManager.saveUsers(users);
            }
            return;
        }
    }

    private static void logActivityIfPointsEarned(Resident resident, Activity activity) {
        resident.logActivity(activity);
    }

    private static void displayRewardHistory(Resident resident) {
        printHeader("Reward History");
        if (resident.getRewardHistory().isEmpty()) {
            System.out.println("No reward history available.");
            return;
        }

        for (Rewardable reward : resident.getRewardHistory()) {
            displayRewardDetails(reward);
        }
    }

    private static void displayRewardRedemptionResult(Resident resident, Rewardable reward) {
        printHeader("Reward Redemption Result");
        System.out.println("Reward redeemed successfully.");
        displayRewardDetails(reward);
        System.out.println("Remaining Points : " + resident.viewPoints());
    }

    private static void displayRewardDetails(Rewardable reward) {
        if (reward instanceof Voucher) {
            Voucher voucher = (Voucher) reward;
            System.out.println("Reward Type      : Voucher");
            System.out.println("Voucher Code     : " + voucher.getVoucherCode());
            System.out.println("Description      : " + voucher.getDescription());
            System.out.println("Valid Until      : " + voucher.getExpiryDate());
            System.out.println("Required Points  : " + voucher.getRequiredPoints());
            System.out.println("Status           : " + voucher.getStatus());
            System.out.println();
        } else if (reward instanceof ClinicPriorityReward) {
            ClinicPriorityReward priorityReward = (ClinicPriorityReward) reward;
            System.out.println("Reward Type      : Clinic Priority Reward");
            System.out.println("Priority Level   : " + priorityReward.getPriorityLevel());
            System.out.println("Required Points  : " + priorityReward.getRequiredPoints());
            System.out.println("Status           : " + (priorityReward.getIsIssued() ? "Issued" : "Not issued"));
            System.out.println();
        } else {
            System.out.println("Reward Type      : " + reward.getClass().getSimpleName());
            System.out.println("Required Points  : " + reward.getRequiredPoints());
            System.out.println();
        }
    }

    private static ArrayList<Voucher> getResidentVouchers(Resident resident) {
        ArrayList<Voucher> vouchers = new ArrayList<>();
        for (Rewardable reward : resident.getRewardHistory()) {
            if (reward instanceof Voucher) {
                vouchers.add((Voucher) reward);
            }
        }
        return vouchers;
    }

    private static FitnessEvent selectFitnessEventForActivity(Resident resident) {
        if (center.getFitnessEvents().isEmpty()) {
            System.out.println("No fitness events available.");
            return null;
        }

        displayFitnessEvents();
        while (true) {
            FitnessEvent event = findEventByIdWithRetry("Enter event ID, or 0 to go back: ");
            if (event == null) {
                return null;
            }
            if (isFitnessEventExpired(event)) {
                System.out.println("This fitness event has expired and cannot be joined.");
                return null;
            }
            if (event.getParticipants().contains(resident)) {
                System.out.println("You have already joined this fitness event.");
                return null;
            }
            if (event.isFull()) {
                System.out.println("Fitness event is full.");
                return null;
            }
            return event;
        }
    }

    private static Appointment selectResidentAppointment(Resident resident) {
        ArrayList<Appointment> residentAppointments = getAppointmentsForResident(resident);
        if (residentAppointments.isEmpty()) {
            System.out.println("Request an appointment before redeeming clinic priority.");
            return null;
        }

        displayAppointmentsForResident(resident);
        return findResidentAppointmentByIdWithRetry(residentAppointments, "Enter appointment ID: ");
    }

    private static void displayAppointmentsForResident(Resident resident) {
        ArrayList<Appointment> residentAppointments = getAppointmentsForResident(resident);
        if (residentAppointments.isEmpty()) {
            System.out.println("No appointments found for this resident.");
            return;
        }

        for (Appointment appointment : residentAppointments) {
            appointment.display();
        }
    }

    private static ArrayList<Appointment> getAppointmentsForResident(Resident resident) {
        ArrayList<Appointment> residentAppointments = new ArrayList<>();
        for (Appointment appointment : clinic.viewAppointments()) {
            if (appointment.getResident() == resident || appointment.getResident().getUserId().equals(resident.getUserId())) {
                residentAppointments.add(appointment);
            }
        }
        return residentAppointments;
    }

    private static void displayAllAppointments() {
        ArrayList<Appointment> appointments = clinic.viewAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
            return;
        }

        for (Appointment appointment : appointments) {
            appointment.display();
        }
    }

    private static Appointment findAppointmentById(String appointmentId) {
        for (Appointment appointment : clinic.viewAppointments()) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    private static Medicine findMedicineById(String medicineId) {
        for (Medicine medicine : clinic.getMedicineInventory().getMedicineList()) {
            if (medicine.getMedicineId().equals(medicineId)) {
                return medicine;
            }
        }
        return null;
    }

    private static FitnessEvent findFitnessEventById(String eventId) {
        for (FitnessEvent event : center.getFitnessEvents()) {
            if (event.getEventId().equalsIgnoreCase(eventId)) {
                return event;
            }
        }
        return null;
    }

    private static ArrayList<Resident> getResidents() {
        ArrayList<Resident> residents = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Resident) {
                residents.add((Resident) user);
            }
        }
        return residents;
    }

    private static Resident findResidentByIdOrEmail(String key) {
        for (Resident resident : getResidents()) {
            if (resident.getUserId().equals(key) || resident.getEmail().equals(key)) {
                return resident;
            }
        }
        return null;
    }

    private static void displayResidents(ArrayList<Resident> residents) {
        printHeader("Residents");
        System.out.printf("%-12s %-24s %-28s %-10s%n", "ID", "Name", "Email", "Points");
        System.out.println("----------------------------------------------------------------------------");
        for (Resident resident : residents) {
            System.out.printf("%-12s %-24s %-28s %-10d%n",
                    resident.getUserId(),
                    resident.getFullName(),
                    resident.getEmail(),
                    resident.viewPoints());
        }
    }

    private static void displayFitnessEvents() {
        if (center.getFitnessEvents().isEmpty()) {
            System.out.println("No fitness events available.");
            return;
        }

        System.out.println();
        System.out.println("              Fitness Events");
        System.out.println("====================================================================");
        System.out.printf("%-10s %-20s %-12s %-16s %-6s %-12s%n",
                "ID", "Event Name", "Date", "Location", "Max", "Status");
        System.out.println("--------------------------------------------------------------------");
        for (FitnessEvent event : center.getFitnessEvents()) {
            System.out.printf("%-10s %-20s %-12s %-16s %-6d %-12s%n",
                    event.getEventId(),
                    event.getEventName(),
                    event.getEventDate(),
                    event.getLocation(),
                    event.getMaxParticipants(),
                    getFitnessEventStatus(event));
        }
        System.out.println("====================================================================");
        System.out.println();
    }

    private static void displayFitnessEventDetails(FitnessEvent event) {
        printHeader("Fitness Event Added");
        System.out.println("Event ID   : " + event.getEventId());
        System.out.println("Event Name : " + event.getEventName());
        System.out.println("Date       : " + event.getEventDate());
        System.out.println("Location   : " + event.getLocation());
        System.out.println("Max        : " + event.getMaxParticipants());
        System.out.println("Status     : " + getFitnessEventStatus(event));
    }

    private static String readUniqueFitnessEventId() {
        while (true) {
            String eventId = readNonEmptyString("Enter event ID, or 0 to cancel: ");
            if (eventId.equals("0")) {
                return null;
            }
            if (findFitnessEventById(eventId) == null) {
                return eventId;
            }
            System.out.println("Event ID already exists. Please enter a different Event ID.");
        }
    }

    private static boolean isFitnessEventExpired(FitnessEvent event) {
        return event.getEventDate().isBefore(LocalDate.now());
    }

    private static String getFitnessEventStatus(FitnessEvent event) {
        if (isFitnessEventExpired(event)) {
            return "Expired";
        }
        if (event.isFull()) {
            return "Full";
        }
        return "Available";
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Invalid input. This field cannot be empty.");
        }
    }

    private static int readMenuOption(String prompt) {
        return readInt(prompt);
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a valid number:");
            }
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Invalid input. Value must be greater than 0.");
        }
    }

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Invalid input. Please enter a value from " + min + " to " + max + ".");
        }
    }

    private static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Invalid input. Value must be greater than 0.");
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Please enter a valid number:");
            }
        }
    }

    private static LocalDate readFlexibleDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            LocalDate date = parseFlexibleDate(scanner.nextLine());
            if (date != null) {
                return date;
            }
            System.out.println("Invalid date format. Please enter again.");
        }
    }

    private static LocalDate readFlexibleDateOrCancel(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return null;
            }
            LocalDate date = parseFlexibleDate(input);
            if (date != null) {
                return date;
            }
            System.out.println("Invalid date format. Please enter again.");
        }
    }


    private static LocalTime readFlexibleTimeOrCancel(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return null;
            }
            LocalTime time = parseFlexibleTime(input);
            if (time != null) {
                return time;
            }
            System.out.println("Invalid time format. Please enter again.");
        }
    }

    private static boolean confirmRetry(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = scanner.nextLine().trim();
            if (answer.equalsIgnoreCase("Y")) {
                return true;
            }
            if (answer.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.println("Invalid input. Please enter Y or N.");
        }
    }

    private static Resident findResidentByIdOrEmailWithRetry(String prompt) {
        while (true) {
            System.out.print(prompt);
            String key = scanner.nextLine().trim();
            if (key.equals("0")) {
                return null;
            }
            Resident resident = findResidentByIdOrEmail(key);
            if (resident != null) {
                return resident;
            }
            System.out.println("Resident not found. Please enter again, or enter 0 to go back.");
        }
    }

    private static Appointment findAppointmentByIdWithRetry(String prompt) {
        while (true) {
            System.out.print(prompt);
            String appointmentId = scanner.nextLine().trim();
            if (appointmentId.equals("0")) {
                return null;
            }
            Appointment appointment = findAppointmentById(appointmentId);
            if (appointment != null) {
                return appointment;
            }
            System.out.println("Appointment not found. Please enter again, or enter 0 to go back.");
        }
    }

    private static Appointment findResidentAppointmentByIdWithRetry(ArrayList<Appointment> appointments, String prompt) {
        while (true) {
            System.out.print(prompt);
            String appointmentId = scanner.nextLine().trim();
            if (appointmentId.equals("0")) {
                return null;
            }
            for (Appointment appointment : appointments) {
                if (appointment.getAppointmentId().equals(appointmentId)) {
                    return appointment;
                }
            }
            System.out.println("Appointment not found. Please enter again, or enter 0 to go back.");
        }
    }

    private static Medicine findMedicineByIdWithRetry(String prompt) {
        while (true) {
            System.out.print(prompt);
            String medicineId = scanner.nextLine().trim();
            if (medicineId.equals("0")) {
                return null;
            }
            Medicine medicine = findMedicineById(medicineId);
            if (medicine != null) {
                return medicine;
            }
            System.out.println("Medicine not found. Please enter again, or enter 0 to go back.");
        }
    }

    private static FitnessEvent findEventByIdWithRetry(String prompt) {
        while (true) {
            System.out.print(prompt);
            String eventId = scanner.nextLine().trim();
            if (eventId.equals("0")) {
                return null;
            }
            FitnessEvent event = findFitnessEventById(eventId);
            if (event != null) {
                return event;
            }
            System.out.println("Event not found. Please enter again, or enter 0 to go back.");
        }
    }

    private static int readIndexWithRetry(String prompt, int size) {
        while (true) {
            int index = readInt(prompt);
            if (index == 0) {
                return -1;
            }
            if (index >= 1 && index <= size) {
                return index - 1;
            }
            System.out.println("Invalid selection. Please enter again, or enter 0 to go back.");
        }
    }

    private static LocalDate parseFlexibleDate(String input) {
        try {
            return LocalDate.parse(input.trim(), DateTimeFormatter.ofPattern("yyyy-M-d"));
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private static LocalTime parseFlexibleTime(String input) {
        try {
            return LocalTime.parse(input.trim(), DateTimeFormatter.ofPattern("H:m"));
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private static String generateAppointmentId() {
        String appointmentId;
        do {
            appointmentId = String.format("APT%03d", appointmentCounter);
            appointmentCounter++;
        } while (findAppointmentById(appointmentId) != null);
        return appointmentId;
    }

    private static String generateFitnessEventActivityId() {
        String activityId = String.format("FEA%03d", fitnessEventActivityCounter);
        fitnessEventActivityCounter++;
        return activityId;
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("====================================");
        System.out.println(title);
        System.out.println("====================================");
    }



}
