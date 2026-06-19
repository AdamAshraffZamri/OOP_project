import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
    private String userFileName;
    private String medicineFileName;

    public FileManager(String userFileName, String medicineFileName) {
        this.userFileName = userFileName;
        this.medicineFileName = medicineFileName;
    }

    // Current format:
    // RESIDENT,userId,fullName,email,password,phoneNo,address,totalPoints
    // ADMIN,userId,fullName,email,password,phoneNo,adminLevel
    // STAFF,userId,fullName,email,password,phoneNo,staffId,position
    // Older resident-only rows are still supported by loadUsers().
    public void saveUsers(ArrayList<User> users){
        try{ 
            FileWriter output= new FileWriter(userFileName);

            for (User user:users){
                if (user instanceof Resident){
                    Resident r= (Resident)user;
                    String userInfo= "RESIDENT,"+ r.getUserId()+ ","+ r.getFullName()+ ","+ r.getEmail()+ ","+ r.getPassword()+ ","+ r.getPhoneNo()+ ","+ r.getAddress()+ ","+ r.getPointAccount().getTotalPoints();
                    output.write(userInfo+ System.lineSeparator());
                } else if (user instanceof Admin) {
                    Admin a = (Admin) user;
                    String userInfo= "ADMIN,"+ a.getUserId()+ ","+ a.getFullName()+ ","+ a.getEmail()+ ","+ a.getPassword()+ ","+ a.getPhoneNo()+ ","+ a.getAdminLevel();
                    output.write(userInfo+ System.lineSeparator());
                } else if (user instanceof ClinicStaff) {
                    ClinicStaff s = (ClinicStaff) user;
                    String userInfo= "STAFF,"+ s.getUserId()+ ","+ s.getFullName()+ ","+ s.getEmail()+ ","+ s.getPassword()+ ","+ s.getPhoneNo()+ ","+ s.getStaffId()+ ","+ s.getPosition();
                    output.write(userInfo+ System.lineSeparator());
                }
            }
            output.close();

        } catch (IOException e){
            System.out.println("ERROR saving users: " + e.getMessage());
        }
    }

    // Loads resident data and returns them as a list of User
    //! Returns an empty list if file does not exist yet
    public ArrayList<User> loadUsers(){
        ArrayList<User> users= new ArrayList<>();
        File file= new File(userFileName);

        if (!file.exists()){
            System.out.println("No existing user file found...");
            return users;
        }

        try{
            Scanner input= new Scanner(file);

            while (input.hasNextLine()) {
                String content= input.nextLine().trim();

                if (content.isEmpty()){
                    continue;
                }

                String[] parts = content.split(",");

                if (parts[0].equals("RESIDENT")) {
                    if (parts.length < 8) {
                        System.out.println("ERROR: incomplete resident data -> " + content);
                        continue;
                    }

                    int totalPoints = Integer.parseInt(parts[parts.length - 1]);
                    String address = joinParts(parts, 6, parts.length - 1);
                    Resident resident = new Resident(parts[1], parts[2], parts[3], parts[4], parts[5], address);
                    restorePoints(resident, totalPoints);
                    users.add(resident);
                } else if (parts[0].equals("ADMIN")) {
                    if (parts.length < 7) {
                        System.out.println("ERROR: incomplete admin data -> " + content);
                        continue;
                    }

                    users.add(new Admin(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]));
                } else if (parts[0].equals("STAFF")) {
                    if (parts.length < 8) {
                        System.out.println("ERROR: incomplete staff data -> " + content);
                        continue;
                    }

                    users.add(new ClinicStaff(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]));
                } else {
                    if (parts.length < 7) {
                        System.out.println("ERROR: incomplete user data -> " + content);
                        continue;
                    }

                    int totalPoints = Integer.parseInt(parts[parts.length - 1]);
                    String address = joinParts(parts, 5, parts.length - 1);
                    Resident resident = new Resident(parts[0], parts[1], parts[2], parts[3], parts[4], address);
                    restorePoints(resident, totalPoints);
                    users.add(resident);
                }
            }
            input.close();
            System.out.println("Loaded "+ users.size()+" user(s) from "+ userFileName+ "...");
        } catch (IOException e){
            System.out.println("ERROR loading users: "+ e.getMessage());
        } catch (NumberFormatException e){
            System.out.println("ERROR parsing user data: "+ e.getMessage());
        }

        return users;
    }

    private String joinParts(String[] parts, int start, int endExclusive) {
        String result = "";
        for (int i = start; i < endExclusive; i++) {
            if (i > start) {
                result += ",";
            }
            result += parts[i];
        }
        return result;
    }

    private void restorePoints(Resident resident, int totalPoints) {
        if (totalPoints > 0){
            resident.getPointAccount().addPoints(totalPoints);
        }
    }

    // Format: medicineId,medicineName,stockQuantity,expiryDate

    public void saveMedicineInventory(MedicineInventory inventory) {
        try{ 
            FileWriter output= new FileWriter(medicineFileName);

            for (Medicine medicine:inventory.getMedicineList()) {
                String content= medicine.getMedicineId()+ ","+ medicine.getMedicineName()+ ","+ medicine.getStockQuantity()+ ","+ medicine.getExpiryDate();
                output.write(content+ System.lineSeparator());
            }

            output.close();
        } catch (IOException e) {
            System.out.println("ERROR saving medicine inventory: " + e.getMessage());
        }
    }

    public MedicineInventory loadMedicineInventory(){
        MedicineInventory inventory= new MedicineInventory("INV001");
        File file= new File(medicineFileName);

        if(!file.exists()){
            System.out.println("No existing medicine inventory file found. Starting fresh.");
            return inventory;
        }

        try{
            Scanner input= new Scanner(file);
            int count= 0;
            while(input.hasNextLine()){
                String content= input.nextLine().trim();
                if (content.isEmpty()) continue;

                String[] parts= content.split(",", 4); // 4 fields
                if (parts.length<4) {
                    System.out.println("ERROR: incomplete medicine data -> "+ content);
                    continue;
                }

                String medicineId   = parts[0];
                String medicineName = parts[1];
                int stockQuantity   = Integer.parseInt(parts[2]);
                LocalDate expiryDate= LocalDate.parse(parts[3]);

                Medicine medicine= new Medicine(medicineId, medicineName, stockQuantity, expiryDate);
                inventory.addMedicine(medicine);
                count++;
            }
            System.out.println("Loaded "+ count+ " medicine(s) from "+ medicineFileName+ "...");

            input.close();
        }catch(IOException e){
            System.out.println("ERROR loading medicine inventory: "  + e.getMessage());
        }catch(Exception e){
            System.out.println("ERROR parsing medicine data: "+ e.getMessage());
        }

        return inventory;
    }
}
