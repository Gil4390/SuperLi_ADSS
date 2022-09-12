package ServiceLayer.Objects.SupplierObjects;

import BusinessLayer.SupplierBusiness.Supplier;

import java.time.DayOfWeek;
import java.util.Set;

public class SSupplier {
    public enum PaymentMethod {
        CreditCard,
        Cash,
        BankTransfer,
        Check
    }
    private String name;
    private int businessNumber;
    private int bankAccount;
    private boolean shouldDeliver;
    private PaymentMethod paymentMethod;
    private Set<DayOfWeek> possibleSupplyDays;
    private String address;

    public SSupplier(Supplier supplier) {
        this.name = supplier.getName();
        this.businessNumber = supplier.getBusinessNumber();
        this.bankAccount = supplier.getBankAccount();
        this.shouldDeliver = supplier.getShouldDeliver();
        this.paymentMethod = PaymentMethod.valueOf(supplier.getPaymentMethod());
        this.possibleSupplyDays = supplier.getPossibleSupplyDays();
        this.address = supplier.getAddress();
    }
    public String getName() {
        return name;
    }

    public int getBusinessNumber() {
        return businessNumber;
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public boolean isShouldDeliver() {
        return shouldDeliver;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Set<DayOfWeek> getPossibleSupplyDays() {
        return possibleSupplyDays;
    }
    public String getAddress() {
        return address;
    }
}
