import java.util.ArrayList;

public class Resident extends User {
    private String address;
    private PointAccount account;
    
    // Arrays to hold the history of what the resident has done
    // Note: 'Activity' and 'Rewardable' will show errors until your teammates build them
    private ArrayList<Activity> activityList;
    private ArrayList<Rewardable> issuedRewards;

    public Resident(String userId, String fullName, String email, String password, String phoneNo, String address) {
        // 'super' calls the parent (User) constructor to handle the basic info
        super(userId, fullName, email, password, phoneNo);
        
        this.address = address;
        // Automatically create a new point account when a resident registers
        this.account = new PointAccount(userId + "-ACC"); 
        this.activityList = new ArrayList<>();
        this.issuedRewards = new ArrayList<>();
    }

    // Logs an activity and adds the points to their account
    public void logActivity(Activity activity) {
        activityList.add(activity);
        int pointsEarned = activity.calculatePoints(); 
        account.addPoints(pointsEarned);
        System.out.println("Activity logged successfully.");
    }

    // Attempts to redeem a reward using the resident's points
    public boolean redeemReward(Rewardable reward) {
        int cost = reward.getRequiredPoints();

        if(account.getTotalPoints()<cost){
            System.out.println("Redemption failed: Insufficient points.");
            return false;
        }

        boolean success= reward.issueReward(this); // Tell the reward to issue itself

        if (success) {
            account.deductPoints(cost);
            issuedRewards.add(reward);
            return true;
        }
        return false;
    }

    public PointAccount getPointAccount() {
        return account;
    }

    public String getAddress(){
        return address;
    }

    public int viewPoints() {
        return account.getTotalPoints();
    }
}