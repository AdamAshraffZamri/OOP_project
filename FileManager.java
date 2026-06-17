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

    // Format: userId,fullName,email,password,phoneNo,address,totalPoints
    public void saveUsers(ArrayList<User> users){
        try{ 
            FileWriter output= new FileWriter(userFileName);

            for (User user:users){
                if (user instanceof Resident){
                    Resident r= (Resident)user;
                    String userInfo= r.getUserId()+ ","+ r.getFullName()+ ","+ r.getEmail()+ ","+ r.getPassword()+ ","+ r.getPhoneNo()+ ","+ r.getAddress()+ ","+ r.getPointAccount().getTotalPoints();
                    output.write(userInfo+ System.lineSeparator());
                }
            }
            output.close();
            System.out.println("Users saved to "+ userFileName+ " successfully...");

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

                String[] parts = content.split(",", 7); // 7 fields
                if (parts.length < 7) {
                    System.out.println("ERROR: incomplete user data -> " + content);
                    continue;
                }

                String userId   = parts[0];
                String fullName = parts[1];
                String email    = parts[2];
                String password = parts[3];
                String phoneNo  = parts[4];
                String address  = parts[5];
                int totalPoints = Integer.parseInt(parts[6]);

                Resident resident = new Resident(userId, fullName, email, password, phoneNo, address);
                //! Restore the points
                if (totalPoints > 0){
                    resident.getPointAccount().addPoints(totalPoints);
                }
                users.add(resident);
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

    // Format: medicineId,medicineName,stockQuantity,expiryDate

    public void saveMedicineInventory(MedicineInventory inventory) {
        try{ 
            FileWriter output= new FileWriter(medicineFileName);

            for (Medicine medicine:inventory.getMedicineList()) {
                String content= medicine.getMedicineId()+ ","+ medicine.getMedicineName()+ ","+ medicine.getStockQuantity()+ ","+ medicine.getExpiryDate();
                output.write(content+ System.lineSeparator());
            }
            System.out.println("Medicine inventory saved to " + medicineFileName + " successfully.");

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