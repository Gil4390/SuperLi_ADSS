package DataAccessLayer;

import BusinessLayer.ApplicationFacade;
import DataAccessLayer.InventoryDAL.DALFacade;
import DataAccessLayer.SupplierDAL.*;
import Presentation.Model.BackendController;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class DataBaseCreator {
    private String path;
    private String connectionString;

    public DataBaseCreator() {
        path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
        connectionString = "jdbc:sqlite:".concat(path);
    }

    public void CreateAllTables() throws SQLException, IOException {
        File dbFile = new File(path);
        dbFile.createNewFile(); // if file already exists will do nothing
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
        try(Statement s = conn.createStatement()) {
            //Supplier Tables
            s.addBatch(createSuppliersTableCommand());
            s.addBatch(createContactsTableCommand());
            s.addBatch(createDemandOrdersTableCommand());
            s.addBatch(createFixedOrdersTableCommand());
            s.addBatch(createOrderProductsTableCommand());
            s.addBatch(createBillsOFQuantitiesTableCommand());
            s.addBatch(createSupplierItemsDiscountsTableCommand());

            //Employee Tables
            s.addBatch(createEmployeesTableCommand());
            s.addBatch(createdDriverIsAvailableTableCommand());
            s.addBatch(createBranchesTableCommand());
            s.addBatch(createJobCertificationsTableCommand());
            s.addBatch(createEmployeeJobConstraintTableCommand());
            s.addBatch(createShiftsTableCommand());
            s.addBatch(createShiftsEmployeesTableCommand());
            s.addBatch(createMessagesTableCommand());
            //Delivery Tables
            s.addBatch(createTrucksTableCommand());
            s.addBatch(createTruckIsAvailableTableCommand());
            s.addBatch(createDeliveryTableCommand());
            s.addBatch(createDeliveryDestTableCommand());
            s.addBatch(createStockShortnessTableCommand());
            s.addBatch(createSiteDocTableCommand());
            s.addBatch(createSiteDocLoadTableCommand());
            s.addBatch(createSiteDocUnloadTableCommand());
            s.addBatch(createSiteDocLogTableCommand());
            //Inventory Tables
            s.addBatch(createCategoriesTableCommand());
            s.addBatch(createDamagedItemsTableCommand());
            s.addBatch(createDiscountsTableCommand());
            s.addBatch(createInShortageItemsTableCommand());
            s.addBatch(createItemLocationTableCommand());
            s.addBatch(createItemPriceTableCommand());
            s.addBatch(createItemQuantityTableCommand());
            s.addBatch(createItemsTableCommand());
            s.addBatch(createItemsByCategoryTableCommand());
            s.addBatch(createPurchasedItemsTableCommand());
            s.addBatch(createShelvesHandlerTableCommand());
            //Test Table
            s.addBatch(createLoadDataTestTableCommand());
            s.executeBatch();
            conn.close();
        }
        catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    private String createMessagesTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Messages(" +
                "BranchAddress TEXT NOT NULL," +
                "Job Text NOT NULL," +
                "Message Text NOT NULL," +
                "PRIMARY KEY(BranchAddress,Job,Message))";
        return command;
    }
    private String createCategoriesTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Categories (" +
                "BranchAddress TEXT NOT NULL," +
                "CategoryID TEXT NOT NULL," +
                "CategoryName TEXT NOT NULL," +
                "CategoryFather TEXT," +
                "SubCounter	INTEGER NOT NULL," +
                "PRIMARY KEY(BranchAddress, CategoryID)," +
                "FOREIGN KEY(CategoryFather) REFERENCES Categories(CategoryID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createDamagedItemsTableCommand() {
        String command ="CREATE TABLE IF NOT EXISTS DamagedItems (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "TimeFound TEXT NOT NULL," +
                "QuantityFound INTEGER NOT NULL," +
                "ExpiredOrDamaged TEXT NOT NULL," +
                "Back0Front1 INTEGER NOT NULL," +
                "PRIMARY KEY(BranchAddress,ItemID,TimeFound)," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createDiscountsTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Discounts (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "DiscountName TEXT NOT NULL," +
                "FromDate TEXT NOT NULL," +
                "EndDate TEXT NOT NULL," +
                "DiscountFare REAL NOT NULL," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID)," +
                "PRIMARY KEY(BranchAddress,ItemID,FromDate),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createInShortageItemsTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS InShortageItems (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID)," +
                "PRIMARY KEY(BranchAddress,ItemID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createItemLocationTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS ItemLocation (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "ShelfID INTEGER NOT NULL," +
                "FOREIGN KEY(ShelfID) REFERENCES ShelvesHandler(ShelfID)," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID)," +
                "PRIMARY KEY(BranchAddress,ItemID,ShelfID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createItemPriceTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS ItemPrice (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "CurrPrice REAL NOT NULL," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID)," +
                "PRIMARY KEY(BranchAddress,ItemID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createItemQuantityTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS ItemQuantity (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "AmountInBackRoom INTEGER NOT NULL," +
                "AmountInFrontRoom INTEGER NOT NULL," +
                "TotalAmount INTEGER NOT NULL," +
                "MinimalQuantity INTEGER NOT NULL," +
                "FullQuantity INTEGER NOT NULL," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID)," +
                "PRIMARY KEY(BranchAddress,ItemID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createItemsTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Items (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "ItemName TEXT NOT NULL," +
                "CategoryID TEXT NOT NULL," +
                "Manufacture TEXT NOT NULL," +
                "FOREIGN KEY(CategoryID) REFERENCES Categories(CategoryID)," +
                "PRIMARY KEY(BranchAddress,ItemID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createItemsByCategoryTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS ItemsByCategory (" +
                "BranchAddress TEXT NOT NULL," +
                "CategoryID TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "PRIMARY KEY(BranchAddress,CategoryID,ItemID)," +
                "FOREIGN KEY(CategoryID) REFERENCES Categories(CategoryID)," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createPurchasedItemsTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS PurchasedItems (" +
                "BranchAddress TEXT NOT NULL," +
                "ItemID INTEGER NOT NULL," +
                "OrderID INTEGER NOT NULL," +
                "TimePurchased TEXT NOT NULL," +
                "SupplierBusinessNumber INTEGER NOT NULL," +
                "AmountPurchased INTEGER NOT NULL," +
                "UnitPriceFromSupplier REAL NOT NULL," +
                "DiscountFromSupplier REAL NOT NULL," +
                "PRIMARY KEY(BranchAddress,ItemID,OrderID)," +
                "FOREIGN KEY(ItemID) REFERENCES Items(ItemID)," +
                "FOREIGN KEY(OrderID) REFERENCES DemandOrders(OrderID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    private String createShelvesHandlerTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS ShelvesHandler(" +
                "BranchAddress TEXT NOT NULL," +
                "ShelfID INTEGER NOT NULL," +
                "Back0Front1 INTEGER NOT NULL," +
                "PRIMARY KEY(BranchAddress,ShelfID),"+
                "FOREIGN KEY(BranchAddress) REFERENCES Branches(Address))";
        return command;
    }

    
    private String createSuppliersTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Suppliers (" +
                "BusinessNumber INTEGER NOT NULL UNIQUE," +
                "Name	TEXT NOT NULL,"+
                "BankAccount INTEGER," +
                "ShouldDeliver INTEGER NOT NULL," +
                "PaymentMethod	TEXT NOT NULL," +
                "Day TEXT NOT NULL," +
                "Address TEXT NOT NULL," +
                "Area TEXT NOT NULL," +
                "PRIMARY KEY (BusinessNumber));";

        return command;
    }

    private String createContactsTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Contacts (" +
                "ContactID INTEGER,"+
                "SupplierBN INTEGER,"+
                "Name TEXT,"+
                "PhoneNumber	TEXT NOT NULL UNIQUE," +
                "Email	TEXT NOT NULL UNIQUE," +
                "PRIMARY KEY(ContactID,SupplierBN)," +
                "FOREIGN KEY(SupplierBN) REFERENCES Suppliers(BusinessNumber));";

        return command;
    }

    private String createDemandOrdersTableCommand() {
        String command =  "CREATE TABLE IF NOT EXISTS DemandOrders (" +
                "BranchAddress TEXT,"+
                "OrderID 	INTEGER,"+
                "SupplierBN 	INTEGER,"+
                "Price 	REAL NOT NULL,"+
                "IsSupplied 	INTEGER NOT NULL,"+
                "OrderDate 	TEXT NOT NULL,"+
                "SupplyDate 	TEXT,"+
                "PRIMARY KEY(BranchAddress, OrderID, SupplierBN)," +
                "FOREIGN KEY(SupplierBN) REFERENCES Suppliers(BusinessNumber));";

        return command;
    }

    private String createFixedOrdersTableCommand() {
        String command =  "CREATE TABLE IF NOT EXISTS FixedOrders  ("+
                "BranchAddress TEXT,"+
                "OrderID 	INTEGER,"+
                "SupplierBN 	INTEGER,"+
                "Price 	REAL NOT NULL,"+
                "IsActive 	INTEGER NOT NULL,"+
                "OrderDate 	TEXT NOT NULL,"+
                "CancelDate 	TEXT,"+
                "Days 	TEXT NOT NULL,"+
                "PRIMARY KEY(BranchAddress, OrderID, SupplierBN)," +
                "FOREIGN KEY(SupplierBN) REFERENCES Suppliers(BusinessNumber));" ;

        return command;
    }

    private String createOrderProductsTableCommand() {
        String command =  "CREATE TABLE IF NOT EXISTS OrderProducts  ("+
                "OrderID 	               INTEGER,"+
                "ProductID 	               INTEGER,"+
                "Name 	             TEXT NOT NULL,"+
                "PriceAfterDiscount  REAL NOT NULL,"+
                "Amount 	      INTEGER NOT NULL,"+
                "Discount 	         REAL NOT NULL,"+
                "SingleItemPrice   	 REAL NOT NULL,"+
                "UnSuppliedAmount INTEGER NOT NULL,"+
                "PRIMARY KEY(ProductID , OrderID));";

        return command;
    }

    private String createBillsOFQuantitiesTableCommand() {
        String command =  "CREATE TABLE IF NOT EXISTS BillsOFQuantities  ("+
                "BranchAddress TEXT,"+
                "SupplierBN 	INTEGER,"+
                "CatalogNumber 	INTEGER,"+
                "Price 	REAL NOT NULL,"+
                "Name 	TEXT NOT NULL,"+
                "SupplierCatalog 	INTEGER NOT NULL,"+
                "PRIMARY KEY(BranchAddress, SupplierBN, CatalogNumber)," +
                "FOREIGN KEY(SupplierBN) REFERENCES Suppliers(BusinessNumber));";

        return command;
    }

    private String createSupplierItemsDiscountsTableCommand() {
        String command =  "CREATE TABLE IF NOT EXISTS SupplierItemsDiscounts  ("+
                "SupplierBN 	INTEGER,"+
                "CatalogNumber 	INTEGER,"+
                "Amount 	INTEGER NOT NULL,"+
                "Discounts 	REAL,"+
                "PRIMARY KEY(SupplierBN ,CatalogNumber, Amount)," +
                "FOREIGN KEY(SupplierBN) REFERENCES BillsOFQuantities(SupplierBN)," +
                "FOREIGN KEY(CatalogNumber) REFERENCES BillsOFQuantities(CatalogNumber));";

        return command;
    }

    private String createdDriverIsAvailableTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS DriverIsAvailable (" +
                "driverId TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "PRIMARY KEY (driverId, date),"+
                "FOREIGN KEY(driverId) REFERENCES Employees(ID));";

        return command;
    }

    private String createTrucksTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Trucks (" +
                "LicenseNumber TEXT NOT NULL UNIQUE," +
                "Model TEXT NOT NULL," +
                "BaseWeight INTEGER NOT NULL," +
                "MaxWeight INTEGER NOT NULL," +
                "PRIMARY KEY (LicenseNumber));" ;
        return command;
    }

    private String createTruckIsAvailableTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'Trucks Available' (" +
                "LicenseNumber TEXT NOT NULL," +
                "Date TEXT NOT NULL," +
                "PRIMARY KEY (LicenseNumber, Date)," +
                "FOREIGN KEY(LicenseNumber) REFERENCES Trucks(LicenseNumber));";
        return command;
    }

    private String createDeliveryTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Deliveries (" +
                "Id INTEGER NOT NULL UNIQUE," +
                "Date TEXT NOT NULL," +
                "Time TEXT NOT NULL," +
                "TruckLicense TEXT NOT NULL," +
                "DriverId INTEGER NOT NULL," +
                "PRIMARY KEY (Id)," +
                "FOREIGN KEY(TruckLicense) REFERENCES Trucks(LicenseNumber)," +
                "FOREIGN KEY(DriverId) REFERENCES Employees(Id));";
        return command;
    }

    private String createDeliveryDestTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Destinations (" +
                "DeliveryId INTEGER NOT NULL," +
                "DestinationAddress TEXT NOT NULL," +
                "NumberList INTEGER NOT NULL," +
                "PRIMARY KEY (DeliveryId, NumberList)," +
                "FOREIGN KEY(DeliveryId) REFERENCES Deliveries(Id)," +
                "FOREIGN KEY(DestinationAddress) REFERENCES Sites(Address));";
        return command;
    }

    private String createSiteDocTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'Site Documents' (" +
                "Id INTEGER NOT NULL UNIQUE," +
                "DeliveryId INTEGER NOT NULL," +
                "SiteAddress TEXT NOT NULL," +
                "TruckWeight INTEGER NOT NULL," +
                "ContactNumber TEXT NOT NULL," +
                "ContactName TEXT NOT NULL," +
                "locationInAddressList INTEGER NOT NULL," +
                "PRIMARY KEY (Id)," +
                "FOREIGN KEY(DeliveryId) REFERENCES Delivery(Id)," +
                "FOREIGN KEY(SiteAddress) REFERENCES Sites(Address));";
        return command;
    }

    private String createSiteDocLoadTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'Load Items' (" +
                "SiteDocumentId INTEGER NOT NULL," +
                "ItemNumber INTEGER NOT NULL," +
                "Amount INTEGER NOT NULL," +
                "PRIMARY KEY (SiteDocumentId,ItemNumber, Amount)," +
                "FOREIGN KEY(SiteDocumentId) REFERENCES SiteDocuments(Id));";
        return command;
    }

    private String createSiteDocUnloadTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'Unload Items' (" +
                "SiteDocumentId INTEGER NOT NULL," +
                "ItemNumber INTEGER NOT NULL," +
                "Amount INTEGER NOT NULL," +
                "PRIMARY KEY (SiteDocumentId,ItemNumber, Amount)," +
                "FOREIGN KEY(SiteDocumentId) REFERENCES SiteDocuments(Id));";
        return command;
    }

    private String createSiteDocLogTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'Site Document Log' (" +
                "SiteDocumentId INTEGER NOT NULL," +
                "Log TEXT NOT NULL," +
                "LocationInList INTEGER NOT NULL," +
                "PRIMARY KEY (SiteDocumentId, LocationInList)," +
                "FOREIGN KEY(SiteDocumentId) REFERENCES SiteDocuments(Id));";
        return command;
    }

    private String createStockShortnessTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'Stock Shortnesses' (" +
                "Id INTEGER NOT NULL UNIQUE," +
                "BranchAddress TEXT NOT NULL," +
                "ItemName TEXT NOT NULL," +
                "ItemCatalogNumber INTEGER NOT NULL," +
                "Amount INTEGER NOT NULL," +
                "SupplierAddress TEXT NOT NULL," +
                "BoundedToDelivery INTEGER NOT NULL," +
                "BoundedToLoadDocument INTEGER NOT NULL," +
                "BoundedToUnloadDocument INTEGER NOT NULL," +
                "BoundToOrderId INTEGER NOT NULL," +
                "PRIMARY KEY (Id)," +
                "FOREIGN KEY(BranchAddress) REFERENCES Sites(Address), "+
                "FOREIGN KEY(SupplierAddress) REFERENCES Sites(Address)); ";
        return command;
    }
    private String createBranchesTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Branches (" +
                "Address TEXT," +
                "ShippingArea TEXT," +
                "ManagerID INTEGER," +
                "PRIMARY KEY(Address));";
        return command;
    }
    private String createEmployeesTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Employees (" +
                "ID INTEGER NOT NULL," +
                "Name TEXT NOT NULL," +
                "BankDetails TEXT NOT NULL," +
                "SALARY INTEGER NOT NULL," +
                "EmployeeStartDate TEXT NOT NULL," +
                "EmployeeDetails TEXT NOT NULL,"+
                "Number TEXT NOT NULL,"+
                "PRIMARY KEY(ID));";
        return command;
    }
    private String createJobCertificationsTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS JobCertifications (" +
                "EmployeeID INTEGER NOT NULL," +
                "JobTitle TEXT NOT NULL," +
                "CertificationName TEXT NOT NULL," +
                "PRIMARY KEY(EmployeeID,JobTitle,CertificationName));";
        return command;
    }

    private String createShiftsTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS Shifts (" +
                "ShiftID TEXT NOT NULL," +
                "BranchAddress TEXT NOT NULL," +
                "Date TEXT NOT NULL," +
                "ShiftType TEXT NOT NULL," +
                "ManagerID INTEGER NOT NULL," +
                "PRIMARY KEY(ShiftID));";
        return command;
    }

    private String createEmployeeJobConstraintTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'EmployeeJobConstraint' (" +
                "EmployeeID INTEGER NOT NULL," +
                "Date TEXT NOT NULL," +
                "ShiftType TEXT NOT NULL," +
                "PRIMARY KEY(EmployeeID,Date,ShiftType));";
        return command;
    }

    private String createShiftsEmployeesTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'ShiftsEmployees' (" +
                "ShiftID INTEGER NOT NULL," +
                "EmployeeID INTEGER NOT NULL," +
                "JobTitle TEXT NOT NULL," +
                "PRIMARY KEY(ShiftID,EmployeeID));";
        return command;
    }

    private String createLoadDataTestTableCommand() {
        String command = "CREATE TABLE IF NOT EXISTS 'LoadDataTest' (" +
                "Load INTEGER NOT NULL);";
        return command;
    }

    public void deleteAllTables() {
        BackendController.getInstance().deleteDataForTests(); //
        DALFacade.getInstance().cleanCache();
        BackendController.clearBackendControllerForTests();
        CacheCleaner cacheCleaner = new CacheCleaner(Arrays.asList(SupplierMapper.getInstance(), DemandOrderMapper.getInstance(), FixedOrderMapper.getInstance(), BillOfQuantityMapper.getInstance(), ContactMapper.getInstance(), OrderProductMapper.getInstance()));
        cacheCleaner.cleanForTests();
        try(Connection conn = DriverManager.getConnection(connectionString);
            Statement s = conn.createStatement()){
            //Supplier Deletes
            s.addBatch( "DROP TABLE IF EXISTS 'Suppliers'");
            s.addBatch("DROP TABLE IF EXISTS 'Contacts'");
            s.addBatch("DROP TABLE IF EXISTS 'DemandOrders'");
            s.addBatch("DROP TABLE IF EXISTS 'FixedOrders'");
            s.addBatch("DROP TABLE IF EXISTS 'OrderProducts'");
            s.addBatch("DROP TABLE IF EXISTS 'BillsOFQuantities'");
            s.addBatch("DROP TABLE IF EXISTS 'BOQDiscounts'");
            s.addBatch("DROP TABLE IF EXISTS 'SupplierItemsDiscounts'");
            //Inventory Deletes
            s.addBatch( "DROP TABLE IF EXISTS 'Categories'");
            s.addBatch("DROP TABLE IF EXISTS 'DamagedItems'");
            s.addBatch("DROP TABLE IF EXISTS 'Discounts'");
            s.addBatch("DROP TABLE IF EXISTS 'InShortageItems'");
            s.addBatch("DROP TABLE IF EXISTS 'ItemLocation'");
            s.addBatch("DROP TABLE IF EXISTS 'ItemPrice'");
            s.addBatch("DROP TABLE IF EXISTS 'ItemQuantity'");
            s.addBatch("DROP TABLE IF EXISTS 'Items'");
            s.addBatch("DROP TABLE IF EXISTS 'ItemsByCategory'");
            s.addBatch("DROP TABLE IF EXISTS 'PurchasedItems'");
            s.addBatch("DROP TABLE IF EXISTS 'ShelvesHandler'");
            s.addBatch("DROP TABLE IF EXISTS 'Site Document Log'");
            s.addBatch("DROP TABLE IF EXISTS 'Unload Items'");
            s.addBatch("DROP TABLE IF EXISTS 'Load Items'");
            s.addBatch("DROP TABLE IF EXISTS 'Site Documents'");
            s.addBatch("DROP TABLE IF EXISTS 'Stock Shortnesses'");
            s.addBatch("DROP TABLE IF EXISTS 'Destinations'");
            s.addBatch("DROP TABLE IF EXISTS 'Deliveries'");
            s.addBatch("DROP TABLE IF EXISTS 'Trucks Available'");
            s.addBatch("DROP TABLE IF EXISTS 'Trucks'");
            s.addBatch("DROP TABLE IF EXISTS 'DriverIsAvailable'");
            s.addBatch("DROP TABLE IF EXISTS 'Branches'");
            s.addBatch("DROP TABLE IF EXISTS 'ShiftsEmployees'");
            s.addBatch("DROP TABLE IF EXISTS 'EmployeeJobConstraint'");
            s.addBatch("DROP TABLE IF EXISTS 'Shifts'");
            s.addBatch("DROP TABLE IF EXISTS 'Messages'");
            s.addBatch("DROP TABLE IF EXISTS 'JobCertifications'");
            s.addBatch("DROP TABLE IF EXISTS 'Employees'");
            s.addBatch("DROP TABLE IF EXISTS 'LoadDataTest'");
            s.executeBatch();
        }
        catch (SQLException e){
            System.out.println("Failed to Delete all tables");
        }
    }
}
