public class Admin extends User {
    private String adminLevel;

    public Admin(String userId, String fullName, String email, String password, String phoneNo, String adminLevel) {
        super(userId, fullName, email, password, phoneNo);
        this.adminLevel = adminLevel;
    }

    // Stubs for administrative actions. 
    // Your teammates building the Event and Activity modules will call these.
    public void createActivity(String activityType) {
        System.out.println("Admin " + fullName + " created a new activity: " + activityType);
    }

    public void issueVoucher(Resident resident, Voucher voucher) {
        System.out.println("Admin issued a voucher to " + resident.getFullName());
    }
}
