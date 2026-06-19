import java.util.ArrayList;

public class Resident extends User {
    private String address;
    private PointAccount pointAccount;
    
    // Arrays to hold the history of what the resident has done
    // Note: 'Activity' and 'Rewardable' will show errors until your teammates build them
    private ArrayList<Activity> activityList;
    private ArrayList<Rewardable> rewardHistory;

    public Resident(String userId, String fullName, String email, String password, String phoneNo, String address) {
        // 'super' calls the parent (User) constructor to handle the basic info
        super(userId, fullName, email, password, phoneNo);
        
        this.address = address;
        // Automatically create a new point account when a resident registers
        registerAccount();
        this.activityList = new ArrayList<>();
        this.rewardHistory = new ArrayList<>();
    }

    public void registerAccount() {
        this.pointAccount = new PointAccount(userId + "-ACC");
    }

    // Logs an activity and adds the points to their account
    public void logActivity(Activity activity) {
        activityList.add(activity);
        int pointsEarned = activity.calculatePoints(); 
        pointAccount.addPoints(pointsEarned);
        System.out.println("Activity logged successfully.");
        System.out.println("Points earned: " + pointsEarned);
        System.out.println("Current total points: " + pointAccount.getTotalPoints());
    }

    // Attempts to redeem a reward using the resident's points
    public boolean redeemReward(Rewardable reward) {
        boolean success= reward.issueReward(this); // Tell the reward to issue itself

        if (success) {
            rewardHistory.add(reward);
            return true;
        }
        return false;
    }

    public PointAccount getPointAccount() {
        return pointAccount;
    }

    public String getAddress(){
        return address;
    }

    public int viewPoints() {
        return pointAccount.getTotalPoints();
    }

    @Override
    public void displayProfile() {
        System.out.println("=== Resident Profile ===");
        super.displayProfile();
        System.out.println("Address: " + address);
        System.out.println("Community Points: " + pointAccount.getTotalPoints());
    }

    public void viewActivityHistory() {
        System.out.println("=== Activity History ===");
        if (activityList.isEmpty()) {
            System.out.println("No activities recorded yet.");
            return;
        }

        for (Activity activity : activityList) {
            activity.displayActivityDetails();
        }
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

    public ArrayList<Rewardable> getRewardHistory() {
        return rewardHistory;
    }
}
