public class ClinicPriorityReward implements Rewardable{
    private String priorityId;
    private int requiredPoints;
    private int priorityLevel;
    private boolean isIssued;
 
    public ClinicPriorityReward(String priorityId, int requiredPoints, int priorityLevel){
        this.priorityId= priorityId;
        this.requiredPoints= requiredPoints;
        this.priorityLevel= priorityLevel;
        this.isIssued= false;
    }

    @Override
    public boolean issueReward(Resident resident){
        if(resident.getPointAccount().getTotalPoints() < requiredPoints){
            System.out.println("Redemption failed: Insufficient points.");
            return false;
        }

        if(isIssued){
            System.out.println("Reward has already been issued.");
            return false;
        }
        this.isIssued= true;
        resident.getPointAccount().deductPoints(requiredPoints);
        return true;
    }

    public void applyToAppointment(Appointment appointment){
        appointment.setPriorityLevel(this.priorityLevel);
        System.out.println("Clinic priority reward applied to appointment.");
    }

    @Override
    public int getRequiredPoints(){
        return requiredPoints;
    }

    public int getPriorityLevel(){
        return priorityLevel;
    }

    public boolean getIsIssued(){
        return isIssued;
    }

}
