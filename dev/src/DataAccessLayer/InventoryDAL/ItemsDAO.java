package DataAccessLayer.InventoryDAL;
import BusinessLayer.InventoryBusiness.Item;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ItemsDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String ItemIDColumnName = "ItemID";
    private final String ItemNameColumnName = "ItemName";
    private final String CategoryIDColumnName = "CategoryID";
    private final String ManufactureColumnName = "Manufacture";

    private Map<String, Map<Integer, Item>> itemsIM;

    public ItemsDAO() {
        super("Items");
        itemsIM = new HashMap<>();
    }
    @Override
    public void cleanCache() {
        itemsIM= new HashMap<>();
    }
    // get all items from database
    public List<Item> selectAllItems(String branchAddress) {
        return (List<Item>) (List<?>) selectAllByBranch(branchAddress);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // insert a new item to the database
    public boolean insert(String branchAddress, int itemID, String name, String categoryID, String manufacture) {
        //checkItemExistsInDataBase(itemID);
        Item item = new Item(branchAddress, itemID, name, categoryID, manufacture);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}) VALUES(?,?,?,?,?)",
                getTableName(), BranchAddressColumnName, ItemIDColumnName, ItemNameColumnName,
                CategoryIDColumnName, ManufactureColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setInt(2, item.getItemID());
            pstmt.setString(3, item.getName());
            pstmt.setString(4, item.getCategory().getCatID());
            pstmt.setString(5, item.getManufacture());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+" ,problem at inserting a new item to dal");
        }
        itemsIM.putIfAbsent(branchAddress, new HashMap<>());
        itemsIM.get(branchAddress).put(item.getItemID(), item);
        return true;
    }

    public void updateItemName(String branchAddress, int itemID, String newName) {
        update(branchAddress, itemID, ItemNameColumnName, newName, ItemIDColumnName);
    }

    public void updateItemCategoryID(String branchAddress, int itemID, String categoryID) {
        update(branchAddress, itemID, CategoryIDColumnName, categoryID, ItemIDColumnName);
    }

    public void updateManufacture(String branchAddress, int itemID, String manufacture) {
        update(branchAddress, itemID, ManufactureColumnName, manufacture, ItemIDColumnName);
    }

    public Item getItem(String branchAddress, int itemID) {
        if (itemsIM.containsKey(branchAddress))
            if (itemsIM.get(branchAddress).containsKey(itemID))
                return itemsIM.get(branchAddress).get(itemID);
        Item item = (Item) select(branchAddress, itemID, ItemIDColumnName);
        itemsIM.putIfAbsent(branchAddress, new HashMap<>());
        itemsIM.get(branchAddress).put(itemID,item);
        return item;
    }

    public List<Item> getAllItems(String branchAddress) {
        for (Item item : selectAllItems(branchAddress)){
            itemsIM.putIfAbsent(branchAddress, new HashMap<>());
            itemsIM.get(branchAddress).putIfAbsent(item.getItemID(), item);
        }
        return itemsIM.get(branchAddress).values().stream().collect(Collectors.toList());
    }

    @Override
    protected Item ConvertReaderToObject(String branchAddress, ResultSet reader) {
        Item result = null;
        try {
            result = new Item(branchAddress, reader.getInt(2), reader.getString(3),
                    reader.getString(4), reader.getString(5));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        } catch (Exception e){
            throw new IllegalArgumentException(e.getMessage());
        }
        return result;
    }

    public void deleteRecord(String branchAddress, int itemID){
        delete(branchAddress, itemID, ItemIDColumnName);
    }

    public int selectItemsCounter(String branchAddress) {
//        List <Item> itemList = (List<Item>)(List<?>) selectAllByBranch(branchAddress);
//        int counter = 0;
//        for(Item item : itemList){
//            if(item.getItemID()>= counter)
//                counter = item.getItemID()+1;
//        }
        return ((List<Item>)(List<?>) selectAllByBranch(branchAddress)).size();
    }
}



























//    public void checkItemExistsInDataBase(int itemID) {
//        if(itemsIM.containsKey(itemID))
//            throw new IllegalArgumentException("item already exists in cache");
//        Item item = (Item) select(itemID);
//        if(item != null)
//            throw new IllegalArgumentException("item already exists in database");
//    }
//
//    public void checkItemNotExistsInDataBase(int itemBN) {
//        if(!items.containsKey(itemBN)) {
//            Item item = (Item) select(itemBN, BusinessNumberColumnName);
//            if(item==null)
//                throw new IllegalArgumentException("item not exists!");
//            items.put(itemBN, item);
//        }
//    }

//    @Override
//    public void cleanCache() {
//        Iterator<Map.Entry<Integer,Item>> iter = itemsIM.entrySet().iterator();
//        while (iter.hasNext()) {
//            Map.Entry<Integer,Item> entry = iter.next();
//            if(entry.getValue().shouldCleanCache()){
//                System.out.println("Cleaning item "+entry.getValue().getItemID() +" from cache!");
//                iter.remove();
//            }
//        }
//    }
