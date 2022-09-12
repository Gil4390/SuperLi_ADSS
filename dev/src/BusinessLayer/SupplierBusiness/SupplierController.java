package BusinessLayer.SupplierBusiness;

import DataAccessLayer.SupplierDAL.SupplierMapper;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SupplierController {
    private final SupplierMapper supplierMapper;

    public SupplierController() {
        this.supplierMapper = SupplierMapper.getInstance();
    }

    public Supplier getSupplier(int supplierBN) {
        return supplierMapper.getSupplier(supplierBN);
    }

    protected List<Supplier> getAllSuppliers()
    {
        return supplierMapper.getAllSuppliers();
    }

    public void addSupplier(String name, int businessNumber, int bankAccount, boolean shouldDeliver ,String paymentMethod, Set<DayOfWeek> daysOfSupply, String address, String area) {
        if(businessNumber<=0 || bankAccount<=0 || name == null || name.equals(""))
            throw new IllegalArgumentException("Invalid input!");
        supplierMapper.insert(businessNumber, name, bankAccount, shouldDeliver, paymentMethod, daysOfSupply, address, area);
    }

    public void removeSupplier(int supplierBN) {
        supplierMapper.delete(supplierBN);
    }

    public BillOfQuantity getBillOfQuantity(int supplierBN) {
        return supplierMapper.getSupplier(supplierBN).getBillOfQuantity();
    }

    protected void updateSupplierName(int supplierBN, String newName) {
        if(newName == null)
            throw new IllegalArgumentException("Invalid input!");
        supplierMapper.updateSupplierName(supplierBN, newName);
    }

    protected void updateSupplierBankAccount(int supplierBN, int newBankAccount) {
        if(newBankAccount<=0)
            throw new IllegalArgumentException("Invalid input!");
        supplierMapper.updateSupplierBankAccount(supplierBN, newBankAccount);
    }

    protected void updateSupplierDelivery(int supplierBN, boolean newDeliveryMethod) {
        supplierMapper.updateShouldDeliver(supplierBN, newDeliveryMethod);
    }

    protected void updateSupplierPaymentMethod(int supplierBN, String newPaymentMethod) {
        if(newPaymentMethod == null)
            throw new IllegalArgumentException("Invalid input!");
        supplierMapper.updatePaymentMethod(supplierBN, newPaymentMethod);
    }

    public void addContact(int supplierBN, String contactName, String phoneNumber, String email) {
        isLegalEmail(email);
        isLegalPhoneNumber(phoneNumber);
        supplierMapper.getSupplier(supplierBN).addContact(contactName, phoneNumber,email);
    }

    protected void removeContact(int supplierBN, int contactId) {
        supplierMapper.getSupplier(supplierBN).removeContact(contactId);
    }

    protected void updateContactName(int supplierBN, int contactId, String newContactName) {
        supplierMapper.getSupplier(supplierBN).updateContactName(contactId, newContactName);
    }

    protected void updateContactEmail(int supplierBN, int contactId, String newEmail) {
        isLegalEmail(newEmail);
        supplierMapper.getSupplier(supplierBN).updateContactEmail(contactId, newEmail);
    }

    protected void updateContactPhone(int supplierBN, int contactId, String newPhoneNumber) {
        isLegalPhoneNumber(newPhoneNumber);
        supplierMapper.getSupplier(supplierBN).updateContactPhone(contactId, newPhoneNumber);
    }

    public double getItemPrice(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierMapper.getSupplier(supplierBN).getItemPrice(branchAddress, catalogNumber);
    }

    public void addItem(String branchAddress, int supplierBN, int catalogNumber, int supplierCatalogNum, double price, String name) {
        if(price<=0 || catalogNumber <=0 || supplierCatalogNum <=0 || name==null)
            throw new IllegalArgumentException("Invalid input!");
        supplierMapper.getSupplier(supplierBN).addItem(branchAddress, catalogNumber, supplierCatalogNum, price, name);
    }

    protected void removeItem(String branchAddress, int supplierBN, int catalogNumber) {
        supplierMapper.getSupplier(supplierBN).removeItem(branchAddress, catalogNumber);
    }

    protected void updateItemPrice(String branchAddress, int supplierBN, int catalogNumber, double newPrice) {
        supplierMapper.getSupplier(supplierBN).updateItemPrice(branchAddress, supplierBN, catalogNumber, newPrice);
    }

    public Map<Integer, Double> getItemDiscounts(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierMapper.getSupplier(supplierBN).getItemDiscounts(branchAddress, catalogNumber);
    }

    public void addItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double discount) {
        supplierMapper.getSupplier(supplierBN).addItemDiscountAccordingToAmount(branchAddress, catalogNumber, amount, discount);
    }

    protected void updateItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount, double newDiscount) {
        supplierMapper.getSupplier(supplierBN).updateItemDiscountAccordingToAmount(branchAddress, catalogNumber, amount, newDiscount);
    }

    protected void removeItemDiscountAccordingToAmount(String branchAddress, int supplierBN, int catalogNumber, int amount) {
        supplierMapper.getSupplier(supplierBN).removeItemDiscountAccordingToAmount(branchAddress, catalogNumber, amount);
    }

    protected void updateSupplierCatalog(String branchAddress, int supplierBN, int catalogNumber, int newSupplierCatalog) {
        supplierMapper.getSupplier(supplierBN).updateSupplierCatalog(branchAddress, catalogNumber, newSupplierCatalog);
    }

    public String getItemName(String branchAddress, int supplierBN, int catalogNumber) {
        return supplierMapper.getSupplier(supplierBN).getItemName(branchAddress, catalogNumber);
    }

    protected void updateItemName(String branchAddress, int supplierBN, int catalogNumber, String newName) {
        supplierMapper.getSupplier(supplierBN).updateItemName(branchAddress, catalogNumber, newName);
    }

    protected List<Contact> getAllContacts(int supplierBN) {
        return supplierMapper.getSupplier(supplierBN).getAllContacts();
    }

    private void isLegalEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        if(!m.matches())
            throw new IllegalArgumentException("Illegal email address");
    }

    private void isLegalPhoneNumber(String phoneNumber) {
        if(phoneNumber.length() != 10 || phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '5')
            throw new IllegalArgumentException("Illegal phone number!") ;
    }

    protected Map<Supplier, HashMap<Integer, Integer>> itemsToSuppliersForFixedOrder(String branchAddress, Map<Integer, Integer> itemIdAndAmount, Set<DayOfWeek> supplyDays) {
        Map<Supplier, HashMap<Integer, Integer>> supplierToItems = new HashMap<>();
        for(int itemId : itemIdAndAmount.keySet()) {
            Supplier supplierToOrderFrom = getMinSupplierPriceForFixedOrder(branchAddress, itemId, itemIdAndAmount.get(itemId), supplyDays);
            if(supplierToOrderFrom != null) {
                supplierToItems.putIfAbsent(supplierToOrderFrom, new HashMap<>());
                supplierToItems.get(supplierToOrderFrom).put(itemId, itemIdAndAmount.get(itemId));
            }
            else
                throw new IllegalArgumentException("the item with itemId: "+itemId+" cant be supplied in the supply days you have selected");
        }
        return supplierToItems;
    }

    protected Map<Supplier, HashMap<Integer, Integer>> itemsToSuppliersForDemandOrder(String branchAddress, Map<Integer, Integer> itemIdAndAmount) {
        Map<Supplier, HashMap<Integer, Integer>> supplierToItems = new HashMap<>();
        for(int itemId : itemIdAndAmount.keySet()) {
            Supplier supplierToOrderFrom = getMinSupplierPriceForDemandOrder(branchAddress, itemId, itemIdAndAmount.get(itemId));
            if(supplierToOrderFrom != null) {
                supplierToItems.putIfAbsent(supplierToOrderFrom, new HashMap<>());
                supplierToItems.get(supplierToOrderFrom).put(itemId, itemIdAndAmount.get(itemId));
            }
            else
                throw new IllegalArgumentException("the item with itemId: "+itemId+" cant be supplied in the supply days you have selected");
        }
        return supplierToItems;
    }

    protected Supplier getMinSupplierPriceForFixedOrder(String branchAddress, int itemId, int amount, Set<DayOfWeek> supplyDays){
        double price = Double.MAX_VALUE;
        Supplier supplierToOrderFrom = null;
        for(Supplier supplier : supplierMapper.getAllSuppliers()) {
            if(!supplier.shouldSupplyOrder(supplyDays))
                continue;
            BillOfQuantity billOfQuantity = getBillOfQuantity(supplier.getBusinessNumber());
            if(!billOfQuantity.checkItemExists(itemId, branchAddress))
                continue;
            double supplierPrice = billOfQuantity.getItemPriceAccordingToAmount(branchAddress, itemId, amount);
            if(supplierPrice < price) {
                price = supplierPrice;
                supplierToOrderFrom = supplier;
            }
        }
        return supplierToOrderFrom;
    }

    protected Supplier getMinSupplierPriceForDemandOrder(String branchAddress, int itemId, int amount){
        double price = Double.MAX_VALUE;
        Supplier supplierToOrderFrom = null;
        for(Supplier supplier : supplierMapper.getAllSuppliers()) {
            BillOfQuantity billOfQuantity = getBillOfQuantity(supplier.getBusinessNumber());
            if(!billOfQuantity.checkItemExists(itemId, branchAddress))
                continue;
            double supplierPrice = billOfQuantity.getItemPriceAccordingToAmount(branchAddress, itemId, amount);
            if(supplierPrice < price) {
                price = supplierPrice;
                supplierToOrderFrom = supplier;
            }
        }
        return supplierToOrderFrom;
    }

    public void isSupplierCanSupply(int supplierBN, DayOfWeek day) {
        supplierMapper.getSupplier(supplierBN).canSupply(day);
    }
}