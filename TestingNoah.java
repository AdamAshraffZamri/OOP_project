import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class TestingNoah {
    public static void main(String[] args){
        Resident noah= new Resident("R001", "Noah Heng", "noah@gmail.com", "noah1234", "011-11856068", "Skudai1");
        Resident adam= new Resident("R002", "Adam Ashraff", "adam@gmail.com", "adam1234", "019-8043775", "Skudai2");
        Resident jason= new Resident("R003", "Jason", "jason@gmail.com", "jason1234", "011-37064616", "Skudai3");
        Resident faatihah= new Resident("R004", "Faatihah", "faatihah@gmail.com", "faatihah1234", "017-7947327", "Skudai4");

        //! Testing Voucher
        noah.getPointAccount().addPoints(200);
        adam.getPointAccount().addPoints(130);

        Voucher voucher1= new Voucher("V001", "GROC-2026", "RM10 off at Aeon",100, LocalDate.of(2026, 12, 31));
        Voucher voucher2= new Voucher("V002", "OLD-2025", "RM5 off",50, LocalDate.of(2025, 1, 1)); // expired

        System.out.println("Noah redeems Aeon voucher:");
        noah.redeemReward(voucher1);
        System.out.println("Noah's remaining points: "+noah.viewPoints()+ "\n");

        System.out.println("Noah tries same voucher again:");
        noah.redeemReward(voucher1);
        System.out.println();

        System.out.println("Adam tries expired voucher:");
        adam.redeemReward(voucher2);
        System.out.println();

        Voucher revokedVoucher= new Voucher("V003", "REVK-0001", "Free Coffee Voucher",50, LocalDate.of(2027, 6, 30));
        revokedVoucher.revoke();
        System.out.println("Adam tries revoked voucher:");
        adam.redeemReward(revokedVoucher);
        System.out.println();

        //! Testing ClinicPriorityReward
        jason.getPointAccount().addPoints(300);
        ClinicPriorityReward priorityReward = new ClinicPriorityReward("CPR-001", 150, 5);

        System.out.println("Jason redeems Clinic Priority Reward:");
        jason.redeemReward(priorityReward);
        System.out.println("Jason's remaining points: "+ jason.viewPoints()+"\n");

        Appointment appointment= new Appointment("APT-001", LocalDate.of(2026, 7, 1), LocalTime.of(9, 0), jason);
        System.out.println("Appointment BEFORE priority: "+ appointment);
        priorityReward.applyToAppointment(appointment);
        System.out.println("Appointment AFTER priority:  "+ appointment+"\n");

        ClinicPriorityReward anotherReward = new ClinicPriorityReward("CPR-002", 200, 3);
        System.out.println("Jason tries another reward:");
        boolean result= jason.redeemReward(anotherReward);
        System.out.println("Redemption result: "+(result ? "Success" : "Failed due to Insufficient points")+ "\n");

        //! Testing CommunityCenter & FitnessEvent
        CommunityCenter center = new CommunityCenter("CC-001", "Skudai Community Center", "Skudai, Johor");

        FitnessEvent morningRun = new FitnessEvent("FE-001", "Morning Run 5K", LocalDate.of(2026, 7, 10), "Skudai Park", 3);
        FitnessEvent yogaClass  = new FitnessEvent("FE-002", "Outdoor Yoga Class", LocalDate.of(2026, 7, 12), "Community Hall", 10);

        center.addEvent(morningRun);
        center.addEvent(yogaClass);
        System.out.println();

        System.out.println("Event details for Morning Run:");
        System.out.println(morningRun);
        System.out.println();

        System.out.println("Noah registers:   " + morningRun.registerParticipant(noah));
        System.out.println("Adam registers:   " + morningRun.registerParticipant(adam));
        System.out.println("Jason registers:  " + morningRun.registerParticipant(jason));
        System.out.println("Faatihah tries full event: " + morningRun.registerParticipant(faatihah));
        System.out.println("Noah tries duplicate: " + morningRun.registerParticipant(noah) + "\n");

        System.out.println("Updated Morning Run details:");
        System.out.println(morningRun);
        System.out.println();

        FitnessEvent updatedYoga= new FitnessEvent("FE-002", "Indoor Yoga Class (Updated)", LocalDate.of(2026, 7, 14), "Main Hall", 10);
        center.updateEvent(updatedYoga);
        center.removeEvent("FE-002");
        System.out.println();

        //! Testing FileManager
        ArrayList<Resident> allResidents = new ArrayList<>();
        allResidents.add(noah);
        allResidents.add(adam);
        allResidents.add(jason);
        allResidents.add(faatihah);

        FileManager fileManager = new FileManager("users.txt", "medicine_inventory.txt");

        System.out.println(">> Saving residents to users.txt:");
        fileManager.saveUsers(new ArrayList<>(allResidents));
        System.out.println();
    }
    
    
}
