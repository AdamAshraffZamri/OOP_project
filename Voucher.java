import java.time.LocalDate;

public class Voucher implements Rewardable {
    private String voucherId;
    private String voucherCode;
    private String description;
    private int requiredPoints;
    private LocalDate expiryDate;
    private boolean isActive;

    public Voucher(String voucherId, String voucherCode, String description, int requiredPoints, LocalDate expiryDate){
        this.voucherId= voucherId;
        this.voucherCode= voucherCode;
        this.description= description;
        this.requiredPoints= requiredPoints;
        this.expiryDate= expiryDate;
        this.isActive= true;
    }

    @Override
    public boolean issueReward(Resident resident){
        try{
            if(LocalDate.now().isAfter(expiryDate)){
                throw new InvalidVoucherException("Voucher ["+ voucherCode+ "] has expired on "+ expiryDate+ "!");
            }
            if(!isActive){
                throw new InvalidVoucherException("Voucher ["+ voucherCode+ "] is no longer active or has already been used!");
            }

            this.isActive= false;

            System.out.println("SUCCESS: Voucher ["+ voucherCode+ "] - \""+ description+ "\" has been issued to "+ resident.getFullName()+".");
            System.out.println("\tValid until: "+ expiryDate);

            return true;
        } catch (InvalidVoucherException e) {
            System.out.println("VOUCHER ERROR: "+e.getMessage());
            return false;
        }
    }

    public boolean validateVoucher(){
        if(LocalDate.now().isAfter(expiryDate)){
            return false;
        }
        if(!isActive){
            return false;
        }
        return true;
    }

    public void revoke(){
        this.isActive=false;
        System.out.println("Voucher ["+ voucherCode+ "] has been revoked.");
    }

    @Override
    public int getRequiredPoints(){
        return requiredPoints;
    }

    public String getVoucherCode(){
        return voucherCode;
    }

    public LocalDate getExpiryDate(){
        return expiryDate;
    }

    @Override
    public String toString(){
        return voucherId+ ","+ voucherCode+ ","+ description+ ","+ requiredPoints+ ","+ expiryDate+ ","+ isActive;
    }
}
