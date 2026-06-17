import java.util.ArrayList ;

public class CommunityClinic {
    private String clinicId ;
    private String clinicName ;
    private String address ;
    private MedicineInventory inventory ;
    private ArrayList <Appointment> appointments ;
    
    public CommunityClinic(String clinicId, String clinicName, String address){
        this.clinicId = clinicId ;
        this.clinicName = clinicName ;
        this.address = address ;
        this.inventory = new MedicineInventory(clinicId) ;
        this.appointments = new ArrayList <> ();
    }

    public void addAppointment (Appointment appointment){
        appointments.add(appointment) ;
    }  

    public MedicineInventory getMedicineInventory(){
        return inventory ;
    }

    public ArrayList <Appointment> viewAppointments(){
        return appointments ;
    } 

    public String getClinicId (){
        return clinicId ;
    }

    public String getClinicName (){
        return clinicName ;
    }

    public String getAddress (){
        return address ;
    }
}
