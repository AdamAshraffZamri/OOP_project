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
            if(!validateVoucher()){
                if(LocalDate.now().isAfter(expiryDate)){
                    throw new InvalidVoucherException("Voucher has expired.");
                }
                throw new InvalidVoucherException("Voucher is inactive, revoked, or already used.");
            }

            if(resident.getPointAccount().getTotalPoints() < requiredPoints){
                System.out.println("Redemption failed: Insufficient points.");
                return false;
            }

            resident.getPointAccount().deductPoints(requiredPoints);

            return true;
        } catch (InvalidVoucherException e) {
            System.out.println("Invalid voucher. " + e.getMessage());
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
    }

    @Override
    public int getRequiredPoints(){
        return requiredPoints;
    }

    public String getVoucherCode(){
        return voucherCode;
    }

    public String getDescription(){
        return description;
    }

    public LocalDate getExpiryDate(){
        return expiryDate;
    }

    public String getStatus(){
        if (LocalDate.now().isAfter(expiryDate)) {
            return "Expired";
        }
        if (isActive) {
            return "Active";
        }
        return "Revoked";
    }

    @Override
    public String toString(){
        return voucherId+ ","+ voucherCode+ ","+ description+ ","+ requiredPoints+ ","+ expiryDate+ ","+ isActive;
    }
}
