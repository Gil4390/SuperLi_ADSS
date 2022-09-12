package BusinessLayer.SupplierBusiness;

import DataAccessLayer.SupplierDAL.BillOfQuantityMapper;
import DataAccessLayer.SupplierDAL.ContactMapper;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Supplier {
    protected enum PaymentMethod {
        CreditCard,
        Cash,
        BankTransfer,
        Check
    }

    private String name;
    private final int businessNumber;
    private int bankAccount;
    private boolean shouldDeliver;
    private PaymentMethod paymentMethod;
    private final Set<DayOfWeek> possibleSupplyDays;
    private final String address;
    private final String area;
    private int contactIdGenerator;
    private Instant timeStamp;
    private final ContactMapper contactMapper;
    private final BillOfQuantityMapper billOfQuantityMapper;

    public Supplier(int businessNumber, String name, int bankAccount, boolean shouldDeliver, String paymentMethod, Set<DayOfWeek> possibleSupplyDays, String address, String area) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.bankAccount = bankAccount;
        this.shouldDeliver = shouldDeliver;
        this.paymentMethod = PaymentMethod.valueOf(paymentMethod);
        this.possibleSupplyDays = possibleSupplyDays;
        this.address = address;
        this.area = area;
        this.contactMapper = ContactMapper.getInstance();
        this.billOfQuantityMapper = BillOfQuantityMapper.getInstance();
        this.contactIdGenerator = contactMapper.getLastContactId()+1;
        this.timeStamp = Instant.now();
    }

    public void setName(String name) {
        updateTimeStamp();
        if(name.equals(""))
            throw new IllegalArgumentException("Invalid input!");
        this.name = name;
    }

    public int getBusinessNumber() {
        updateTimeStamp();
        return businessNumber;
    }

    public int getBankAccount() {
        updateTimeStamp();
        return bankAccount;
    }

    public void setBankAccount(int bankAccount) {
        updateTimeStamp();
        this.bankAccount = bankAccount;
    }

    public boolean getShouldDeliver() {
        updateTimeStamp();
        return shouldDeliver;
    }

    public String getArea() {
        return area;
    }

    public void setShouldDeliver(boolean shouldDeliver) {
        updateTimeStamp();
        this.shouldDeliver = shouldDeliver;
    }

    public String getPaymentMethod() {
        updateTimeStamp();
        return paymentMethod.toString();
    }

    public void setPaymentMethod(String paymentMethod) {
        updateTimeStamp();
        this.paymentMethod = PaymentMethod.valueOf(paymentMethod);
    }

    public Set<DayOfWeek> getPossibleSupplyDays() {
        updateTimeStamp();
        return possibleSupplyDays;
    }

    public String getAddress() {
        updateTimeStamp();
        return address;
    }

    public void addContact(String contactName, String phoneNumber, String email) {
        updateTimeStamp();
        contactMapper.insert(contactIdGenerator++, businessNumber, contactName, phoneNumber, email);
    }

    public void removeContact(int contactId) {
        updateTimeStamp();
        contactMapper.delete(businessNumber, contactId);
    }

    public void updateContactName(int contactId, String newContactName) {
        updateTimeStamp();
        contactMapper.updateContactName(businessNumber, contactId, newContactName);
    }

    public void updateContactEmail(int contactId, String newEmail) {
        updateTimeStamp();
        contactMapper.updateContactEmail(businessNumber, contactId, newEmail);
    }

    public void updateContactPhone(int contactId, String newPhoneNumber) {
        updateTimeStamp();
        contactMapper.updateContactPhoneNumber(businessNumber, contactId, newPhoneNumber);
    }

    public List<Contact> getAllContacts() {
        updateTimeStamp();
        return contactMapper.getSupplierContacts(businessNumber);
    }

    public double getItemPrice(String branchAddress, int catalogNumber) {
        updateTimeStamp();
        return billOfQuantityMapper.getSupplierItem(branchAddress, businessNumber, catalogNumber).getPrice();
    }

    public BillOfQuantity getBillOfQuantity() {
        updateTimeStamp();
        return billOfQuantityMapper.getBillOfQuantity(businessNumber);
    }

    public void addItem(String branchAddress, int catalogNumber, int supplierCatalogNum, double price, String name) {
        updateTimeStamp();
        billOfQuantityMapper.insertSupplierItem(branchAddress, businessNumber, catalogNumber, supplierCatalogNum, price, name);
    }

    public void removeItem(String branchAddress, int catalogNumber) {
        updateTimeStamp();
        billOfQuantityMapper.delete(branchAddress, businessNumber, catalogNumber);
    }

    public void updateItemPrice(String branchAddress, int supplierBN, int catalogNumber, double newPrice) {
        updateTimeStamp();
        billOfQuantityMapper.updateItemPrice(branchAddress, supplierBN, catalogNumber, newPrice);
    }

    public Map<Integer, Double> getItemDiscounts(String branchAddress, int catalogNumber) {
        updateTimeStamp();
        return billOfQuantityMapper.getItemDiscounts(branchAddress, businessNumber, catalogNumber);
    }

    public void updateItemName(String branchAddress, int catalogNumber, String newName) {
        updateTimeStamp();
        billOfQuantityMapper.updateItemName(branchAddress, businessNumber, catalogNumber, newName);
    }

    public String getItemName(String branchAddress, int catalogNumber) {
        updateTimeStamp();
        return billOfQuantityMapper.getItemName(branchAddress, businessNumber, catalogNumber);
    }

    public void updateSupplierCatalog(String branchAddress, int catalogNumber, int newSupplierCatalog) {
        updateTimeStamp();
        billOfQuantityMapper.updateSupplierCatalog(branchAddress, businessNumber, catalogNumber, newSupplierCatalog);
    }

    public void removeItemDiscountAccordingToAmount(String branchAddress, int catalogNumber, int amount) {
        updateTimeStamp();
        billOfQuantityMapper.deleteItemDiscountAccordingToAmount(branchAddress, businessNumber, catalogNumber, amount);
    }

    public void updateItemDiscountAccordingToAmount(String branchAddress, int catalogNumber, int amount, double newDiscount) {
        updateTimeStamp();
        billOfQuantityMapper.updateItemDiscountAccordingToAmount(branchAddress, businessNumber, catalogNumber, amount, newDiscount);
    }

    public void addItemDiscountAccordingToAmount(String branchAddress, int catalogNumber, int amount, double discount) {
        updateTimeStamp();
        billOfQuantityMapper.insertItemDiscountAccordingToAmount(branchAddress, businessNumber, catalogNumber, amount, discount);
    }

    public void canSupply(DayOfWeek dayOfWeek) {
        if(!possibleSupplyDays.contains(dayOfWeek))
            throw new IllegalArgumentException("Supplier cant supply in the day: "+dayOfWeek);
    }

    private void updateTimeStamp() { timeStamp = Instant.now(); }

    public String getName() {
        updateTimeStamp();
        return name;
    }

    public boolean shouldSupplyOrder(Set<DayOfWeek> supplyDays) {
        for(DayOfWeek dayOfWeek : supplyDays)
            if(!possibleSupplyDays.contains(dayOfWeek))
                return false;
        return true;
    }

    public boolean shouldCleanCache(){
        Duration duration = Duration.between( timeStamp , Instant.now());
        Duration limit = Duration.ofMinutes(5);
        return (duration.compareTo( limit ) > 0);
    }
}
