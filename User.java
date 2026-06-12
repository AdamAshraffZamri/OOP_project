public abstract class User {
    // Protected means child classes can access these directly
    protected String userId;
    protected String fullName;
    protected String email;
    protected String password;
    protected String phoneNo;

    // Constructor to initialize the basic user data
    public User(String userId, String fullName, String email, String password, String phoneNo) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
    }

    // Basic authentication logic
    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            System.out.println(fullName + " logged in successfully.");
            return true;
        }
        System.out.println("Login failed. Check credentials.");
        return false;
    }

    public void logout() {
        System.out.println(fullName + " has logged out.");
    }

    public void updateProfile(String fullName, String email, String phoneNo) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        System.out.println("Profile updated successfully.");
    }

    // Getters and Setters can be generated here via your IDE
    public String getFullName() { return fullName; }
    public String getUserId() { return userId; }
}