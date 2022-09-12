package BusinessLayer.SupplierBusiness;

import java.time.Duration;
import java.time.Instant;

public class Contact {
    private final int supplierBN;
    private final int contactId;
    private String name;
    private String phoneNumber;
    private String email;
    private Instant timeStamp;

    public Contact(int contactId, int supplierBN, String name, String phoneNumber, String email) {
        this.supplierBN = supplierBN;
        this.contactId = contactId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        timeStamp = Instant.now();
    }

    public int getContactId() {
        updateTimeStamp();
        return contactId;
    }

    public int getSupplierBN() {
        updateTimeStamp();
        return supplierBN;
    }

    public String getName() {
        updateTimeStamp();
        return name;
    }

    public void setName(String name) {
        updateTimeStamp();
        this.name = name;
    }

    public String getPhoneNumber() {
        updateTimeStamp();
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        updateTimeStamp();
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        updateTimeStamp();
        return email;
    }

    public void setEmail(String email) {
        updateTimeStamp();
        this.email = email;
    }

    public boolean equals(Contact other) {
        updateTimeStamp();
        return supplierBN == other.getSupplierBN() && name == other.getName() && phoneNumber == other.getPhoneNumber() && email == other.getEmail();
    }

    private void updateTimeStamp() { timeStamp = Instant.now();}

    public boolean shouldCleanCache() {
        Duration duration = Duration.between( timeStamp , Instant.now());
        Duration limit = Duration.ofMinutes(5);
        return (duration.compareTo( limit ) > 0);
    }
}
