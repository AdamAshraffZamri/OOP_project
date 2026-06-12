import java.time.LocalDate;

public class TestingAdam {
    public static void main(String[] args) {
        System.out.println("=== Starting System Test for Module 1 ===\n");

        // 1. Create a Resident object to test the constructor and inheritance
        Resident adam = new Resident("RES-0119", "Muhammad Adam Ashraff", "adam@ecohealth.com", "securePass123", "012-3456789", "Alor Gajah, Melaka");

        // 2. Test Login Authentication
        System.out.println("--- Testing Login ---");
        adam.login("adam@ecohealth.com", "wrongpassword"); // Should fail
        adam.login("adam@ecohealth.com", "securePass123"); // Should succeed

        // 3. Test Point Account Initialization (Encapsulation check)
        System.out.println("\n--- Testing Point Account ---");
        PointAccount myAccount = adam.getPointAccount();
        System.out.println("Initial Points Balance: " + myAccount.getTotalPoints()); // Should be 0

        // 4. Test Activity Logging (Using the Mock Activity below)
        System.out.println("\n--- Testing Activity Logging ---");
        MockActivity morningRecycling = new MockActivity("ACT-01", "Recycled 5kg of plastic", 50);
        adam.logActivity(morningRecycling); // Should add 50 points

        // 5. Test Reward Redemption (Using the Mock Reward below)
        System.out.println("\n--- Testing Reward Redemption ---");
        MockReward freeCoffeeVoucher = new MockReward(30);
        System.out.println("Attempting to buy coffee voucher (Cost: 30)...");
        adam.redeemReward(freeCoffeeVoucher); // Should succeed (50 - 30 = 20 points left)

        MockReward clinicPriority = new MockReward(100);
        System.out.println("Attempting to buy clinic priority (Cost: 100)...");
        adam.redeemReward(clinicPriority); // Should fail (Insufficient points)
        
        // 6. Test Profile Update & Logout
        System.out.println("\n--- Testing Profile Update & Logout ---");
        adam.updateProfile("Muhammad Adam Ashraff Bin Zamri", "adam.zamri@ecohealth.com", "019-8765432");
        adam.logout();
    }
}

// =====================================================================
// MOCK CLASSES FOR TESTING 
// (These act as temporary stand-ins until your teammates build the real ones)
// =====================================================================

class MockActivity extends Activity {
    private int mockPoints;

    public MockActivity(String id, String desc, int points) {
        super(id, LocalDate.now(), desc);
        this.mockPoints = points;
    }

    @Override
    public int calculatePoints() {
        return mockPoints; // Simply returns the hardcoded points for testing
    }
}

class MockReward implements Rewardable {
    private int cost;

    public MockReward(int cost) {
        this.cost = cost;
    }

    @Override
    public int getRequiredPoints() {
        return cost;
    }

    @Override
    public boolean issueReward(Resident resident) {
        System.out.println(">> SUCCESS: Mock Reward successfully issued to " + resident.getFullName() + "!");
        return true;
    }
}
