package BusinessLayer.SupplierBusiness;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SupplierItem {
    private final String branchAddress;
    private final int supplierBN;
    private final int catalogNumber;
    private int supplierCatalog;
    private double price;
    private String itemName;
    private final Map<Integer, Double> discountAccordingToAmount;

    public SupplierItem(String branchAddress, int supplierBN, int catalogNumber, double price, String itemName, int supplierCatalog) {
        this.branchAddress = branchAddress;
        this.supplierBN = supplierBN;
        this.catalogNumber = catalogNumber;
        this.supplierCatalog = supplierCatalog;
        this.price = price;
        this.itemName = itemName;
        this.discountAccordingToAmount = new HashMap<>();
        discountAccordingToAmount.put(0, 0.0);
    }
    
    public String getBranchAddress() { return branchAddress; }

    public int getSupplierBN() {
        return supplierBN;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public int getSupplierCatalog() {
        return supplierCatalog;
    }

    public void setSupplierCatalog(int supplierCatalog) {
        this.supplierCatalog = supplierCatalog;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    protected Double getDiscountForOrder(int amount) {
        return discountAccordingToAmount.get(discountAccordingToAmount.keySet().stream().filter(k -> k <= amount).max(Comparator.naturalOrder()).orElse(null));
    }

    public void addItemDiscountAccordingToAmount(int amount, double discount) {
        checkAmountNotExists(amount);
        checkIfDiscountIsLegal(amount, discount);
        discountAccordingToAmount.put(amount, discount);
    }

    public void checkIfDiscountIsLegal(int amount, double discount) {
        Double currentMaxDiscount = discountAccordingToAmount.get(discountAccordingToAmount.keySet().stream().filter(k -> k <= amount).max(Comparator.naturalOrder()).orElse(null));
        if(currentMaxDiscount > discount)
            throw new IllegalArgumentException("illegal discount!");
        Integer amountAbove = discountAccordingToAmount.keySet().stream().filter(k -> k > amount).min(Comparator.naturalOrder()).orElse(null);
        if(amountAbove != null && discountAccordingToAmount.get(amountAbove) < discount)
            throw new IllegalArgumentException("illegal discount!");
    }

    protected void updateItemDiscountAccordingToAmount(int amount, double newDiscount) {
        checkAmountExists(amount);
        checkIfDiscountIsLegal(amount, newDiscount);
        discountAccordingToAmount.replace(amount, newDiscount);
    }

    protected void removeItemDiscountAccordingToAmount(int amount) {
        checkAmountExists(amount);
        discountAccordingToAmount.remove(amount);
    }

    public void checkAmountExists(int amount){
        if(!discountAccordingToAmount.containsKey(amount))
            throw new IllegalArgumentException("Amount does not exists!");
    }

    public boolean isAmountExists(int amount) {
        return discountAccordingToAmount.containsKey(amount);
    }

    public void checkAmountNotExists(int amount){
        if(discountAccordingToAmount.containsKey(amount))
            throw new IllegalArgumentException("Amount already exists!");
    }

    public Map<Integer, Double> getItemDiscounts() {
        return discountAccordingToAmount;
    }
}
