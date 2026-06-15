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
        if(isIssued){
            System.out.println("ERROR: Reward " + priorityId + " has already been issued to " + resident.getFullName() + ".");
            return false;
        }
        this.isIssued= true;
        System.out.println("SUCCESS: Clinic Priority Reward issued to "+ resident.getFullName()+"!");
        System.out.println("\tPriority Level "+ priorityLevel+ " granted! You have been prioritised in the appointment queue...");
        return true;
    }

    public void applyToAppointment(Appointment appointment){
        appointment.setPriorityLevel(this.priorityLevel);
        System.out.println("Priority Level "+ priorityLevel+ " applied to the appointment made by "+appointment.getResident().getFullName()+ " on "+ appointment.getAppointmentDate()+ "(higher number = higher priority)");
    }

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
