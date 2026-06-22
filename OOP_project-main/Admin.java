import java.util.ArrayList;

public class Admin extends User {
    private String adminLevel;

    public Admin(String userId, String fullName, String email, String password, String phoneNo, String adminLevel) {
        super(userId, fullName, email, password, phoneNo);
        this.adminLevel = adminLevel;
    }

    public void createEvent(FitnessEvent event) {
        System.out.println("Admin " + fullName + " created event: " + event.getEventName());
    }

    public void editEvent(FitnessEvent event) {
        System.out.println("Admin " + fullName + " edited event: " + event.getEventName());
    }

    public void deleteEvent(String eventId) {
        System.out.println("Admin " + fullName + " deleted event: " + eventId);
    }

    public void issueVoucher(Voucher voucher, Resident resident) {
        resident.getRewardHistory().add(voucher);
    }

    public void revokeVoucher(Voucher voucher) {
        voucher.revoke();
    }

    public Leaderboard generateLeaderboard(ArrayList<Resident> residents) {
        Leaderboard leaderboard = new Leaderboard("ADMIN-LB");
        leaderboard.generateRanking(residents);
        return leaderboard;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    @Override
    public void displayProfile() {
        System.out.println("=== Admin Profile ===");
        super.displayProfile();
        System.out.println("Admin Level: " + adminLevel);
    }
}
