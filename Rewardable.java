public interface Rewardable {
    int getRequiredPoints();
    boolean issueReward(Resident resident);
}
