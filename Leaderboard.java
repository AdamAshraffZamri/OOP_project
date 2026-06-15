//! temporary from AI cuz no pic for this
import java.util.ArrayList;
import java.util.Comparator;

public class Leaderboard {
    private String leaderboardId;
    private ArrayList<Resident> rankedResidents;

    public Leaderboard(String leaderboardId) {
        this.leaderboardId = leaderboardId;
        this.rankedResidents = new ArrayList<>();
    }

    public void generateRanking(ArrayList<Resident> residents) {
        rankedResidents = new ArrayList<>(residents);
        rankedResidents.sort(
            Comparator.comparingInt((Resident r) -> r.getPointAccount().getTotalPoints()).reversed()
        );
    }

    public void displayRanking() {
        System.out.println("===== Community Leaderboard =====");
        if (rankedResidents.isEmpty()) {
            System.out.println("No residents to display.");
            return;
        }
        for (int i = 0; i < rankedResidents.size(); i++) {
            Resident r = rankedResidents.get(i);
            System.out.println((i + 1) + ". " + r.getFullName()
                    + " - " + r.getPointAccount().getTotalPoints() + " pts");
        }
        System.out.println("=================================");
    }

    public ArrayList<Resident> getTopResidents(int limit) {
        int end = Math.min(limit, rankedResidents.size());
        return new ArrayList<>(rankedResidents.subList(0, end));
    }

    public String getLeaderboardId() { return leaderboardId; }
}