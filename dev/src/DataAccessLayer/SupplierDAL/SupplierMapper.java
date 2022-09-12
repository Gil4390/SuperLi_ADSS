package DataAccessLayer.SupplierDAL;

import BusinessLayer.SupplierBusiness.Supplier;

import java.sql.*;
import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class SupplierMapper extends DalController {


    private static class DSupplierControllerHolder{
        private static SupplierMapper instance = new SupplierMapper();
    }

    public static SupplierMapper getInstance() {
        return DSupplierControllerHolder.instance;
    }

    private final String BusinessNumberColumnName="BusinessNumber";
    private final String NameColumnName="Name";
    private final String BankAccountColumnName="BankAccount";
    private final String ShouldDeliverColumnName="ShouldDeliver";
    private final String PaymentMethodColumnName="PaymentMethod";
    private final String DayColumnName="Day";
    private final String AddressColumnName="Address";
    private final String AreaColumnName="Area";

    private Map<Integer, Supplier> suppliers;

    private SupplierMapper() {
        super("Suppliers");
        suppliers = new HashMap<>();
    }
    // get all suppliers from database
    public List<Supplier> selectAllSuppliers(){
        return (List<Supplier>)(List<?>)select();
    }
    // insert new supplier to database
    public boolean insert(int businessNumber, String name, int bankAccount, boolean shouldDeliver, String paymentMethod, Set<DayOfWeek> possibleSupplyDays, String address, String area) {
        checkSupplierExistsInDataBase(businessNumber);
        Supplier supplier = new Supplier(businessNumber, name, bankAccount, shouldDeliver, paymentMethod, possibleSupplyDays, address, area);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}) VALUES(?,?,?,?,?,?,?,?)",
                getTableName(), BusinessNumberColumnName, NameColumnName,
                BankAccountColumnName, ShouldDeliverColumnName, PaymentMethodColumnName,
                DayColumnName, AddressColumnName, AreaColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplier.getBusinessNumber());
            pstmt.setString(2, supplier.getName());
            pstmt.setInt(3, supplier.getBankAccount());
            pstmt.setBoolean(4, supplier.getShouldDeliver());
            pstmt.setString(5, supplier.getPaymentMethod());
            pstmt.setString(6, supplier.getPossibleSupplyDays().toString());
            pstmt.setString(7, supplier.getAddress());
            pstmt.setString(8, supplier.getArea());

            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        suppliers.put(businessNumber, supplier);
        return true;
    }

    public void delete(int supplierBN) {
        checkSupplierNotExistsInDataBase(supplierBN);
        delete(supplierBN, BusinessNumberColumnName);
        suppliers.remove(supplierBN);
    }

    public void checkSupplierExistsInDataBase(int supplierBN) {
        if(suppliers.containsKey(supplierBN))
            throw new IllegalArgumentException("supplier already exists!");
        Supplier supplier = (Supplier) select(supplierBN, BusinessNumberColumnName);
        if(supplier != null)
            throw new IllegalArgumentException("supplier already exists!");
    }

    public void checkSupplierNotExistsInDataBase(int supplierBN) {
        if(!suppliers.containsKey(supplierBN)) {
            Supplier supplier = (Supplier) select(supplierBN, BusinessNumberColumnName);
            if(supplier==null)
                throw new IllegalArgumentException("Supplier is not found!");
            suppliers.put(supplierBN, supplier);
        }
    }

    public void updateShouldDeliver(int supplierBN, boolean shouldDeliver) {
        checkSupplierNotExistsInDataBase(supplierBN);
        update(supplierBN, ShouldDeliverColumnName, shouldDeliver, BusinessNumberColumnName);
        suppliers.get(supplierBN).setShouldDeliver(shouldDeliver);
    }

    public void updatePaymentMethod(int supplierBN, String paymentMethod) {
        checkSupplierNotExistsInDataBase(supplierBN);
        update(supplierBN, PaymentMethodColumnName, paymentMethod, BusinessNumberColumnName);
        suppliers.get(supplierBN).setPaymentMethod(paymentMethod);
    }

    public void updateSupplierBankAccount(int supplierBN, int newBankAccount) {
        checkSupplierNotExistsInDataBase(supplierBN);
        update(supplierBN, BankAccountColumnName, newBankAccount, BusinessNumberColumnName);
        suppliers.get(supplierBN).setBankAccount(newBankAccount);
    }

    public void updateSupplierName(int supplierBN, String newName) {
        checkSupplierNotExistsInDataBase(supplierBN);
        update(supplierBN, NameColumnName, newName, BusinessNumberColumnName);
        suppliers.get(supplierBN).setName(newName);
    }

    public Supplier getSupplier(int supplierBN) {
        checkSupplierNotExistsInDataBase(supplierBN);
        return suppliers.get(supplierBN);
    }

    public Supplier getSupplierByAddress(String address) {
        Supplier supplier = (Supplier) select(address, AddressColumnName);
        if(supplier == null)
            throw new IllegalArgumentException("supplier doesnt exists!");
            suppliers.putIfAbsent(supplier.getBusinessNumber(), supplier);
        return supplier;
    }

    public boolean SupplierExists(String address) {
        try{getSupplierByAddress(address); return true;}
        catch (Exception e) {return false;}
    }

    public List<Supplier> getAllSuppliers() {
        for(Supplier supplier : selectAllSuppliers())
            suppliers.putIfAbsent(supplier.getBusinessNumber(), supplier);
        return suppliers.values().stream().collect(Collectors.toList());
    }
    @Override
    protected Object ConvertReaderToObject(ResultSet reader) {
        Supplier result = null;
        try {
            Set<DayOfWeek> daysOfSupply = new HashSet<>();
            String[] daysOfSupplyStr = reader.getString(6).substring(1, reader.getString(6).length()-1).split(", ");
            for(String day : daysOfSupplyStr)
                daysOfSupply.add(DayOfWeek.valueOf(day));
            result = new Supplier(reader.getInt(1), reader.getString(2),reader.getInt(3),reader.getBoolean(4), reader.getString(5), daysOfSupply, reader.getString(7), reader.getString(8));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public synchronized void cleanCache() {
        Iterator<Map.Entry<Integer,Supplier>> iter = suppliers.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,Supplier> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning supplier "+entry.getValue().getBusinessNumber() +" from cache!");
                iter.remove();
            }
        }
    }

    @Override
    protected void cleanCacheForTests() {
        suppliers = new HashMap<>();
    }
}
