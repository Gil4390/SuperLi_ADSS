package DataAccessLayer.InventoryDAL;

import BusinessLayer.InventoryBusiness.Category;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CategoriesDAO extends AbstractDAO {
    private final String BranchAddressColumnName = "BranchAddress";
    private final String CategoryIDColumnName = "CategoryID";
    private final String CategoryNameColumnName = "CategoryName";
    private final String CategoryFatherColumnName = "CategoryFather";
    private final String SubCounterColumnName = "SubCounter";

    private Map<String, Map<String, Category>> categoriesByBranchIM;

    public CategoriesDAO() {
        super("Categories");
        categoriesByBranchIM = new HashMap<>();
    }
    @Override
    public void cleanCache() {
        categoriesByBranchIM = new HashMap<>();
    }
    // get all categories from database
    public List<Category> selectAllItems(String branchAddress) {
        return (List<Category>) (List<?>) selectAllByBranch(branchAddress);
    }

    // insert a new category to the database
    public boolean insert(String branchAddress, String categoryID, String categoryName, String categoryFatherID) {
        Category category = new Category(branchAddress, categoryID, categoryName);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}) VALUES(?,?,?,?,?)",
                getTableName(), BranchAddressColumnName, CategoryIDColumnName, CategoryNameColumnName, CategoryFatherColumnName, SubCounterColumnName);
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, branchAddress);
            pstmt.setString(2, category.getCatID());
            pstmt.setString(3, category.getName());
            pstmt.setString(4, categoryFatherID);
            pstmt.setInt(5, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", problem at inserting a new category to dal");
        }
        categoriesByBranchIM.putIfAbsent(branchAddress, new HashMap<>());
        categoriesByBranchIM.get(branchAddress).put(category.getCatID(), category);
        return true;
    }

    public void updateCategoryID(String branchAddress, String CategoryID, String newID) {
        update(branchAddress, CategoryID, CategoryIDColumnName, newID, CategoryIDColumnName);
    }

    public void updateCategoryName(String branchAddress, String CategoryID, String newName) {
        update(branchAddress, CategoryID, CategoryNameColumnName, newName, CategoryIDColumnName);
    }

    public void updateCategoryFather(String branchAddress, String CategoryID, String newFather) {
        update(branchAddress, CategoryID, CategoryFatherColumnName, newFather, CategoryIDColumnName);
    }

    public Category getCategory(String branchAddress, String categoryID) {
        if (categoriesByBranchIM.containsKey(branchAddress))
            if (categoriesByBranchIM.get(branchAddress).containsKey(categoryID))
                return categoriesByBranchIM.get(branchAddress).get(categoryID);
        Category cat = (Category) select(branchAddress, categoryID, CategoryIDColumnName);
        categoriesByBranchIM.putIfAbsent(branchAddress, new HashMap<>());
        categoriesByBranchIM.get(branchAddress).put(cat.getCatID(),cat);
        return cat;
    }

    public List<Category> getAllCategories(String branchAddress) {
        for (Category category : selectAllItems(branchAddress)) {
            categoriesByBranchIM.putIfAbsent(branchAddress, new HashMap<>());
            categoriesByBranchIM.get(branchAddress).putIfAbsent(category.getCatID(), category);
        }
        return categoriesByBranchIM.get(branchAddress).values().stream().collect(Collectors.toList());
    }

    public List<Category> getSubCat(String branchAddress, String categoryID) {
        List<Category> categories = (List<Category>) (List<?>) selectListByBranch(branchAddress, categoryID, CategoryFatherColumnName);
        for (Category category : categories) {
            categoriesByBranchIM.putIfAbsent(branchAddress, new HashMap<>());
            categoriesByBranchIM.get(branchAddress).putIfAbsent(category.getCatID(), category);
        }
        return categories;
    }

    public List<String> getIDSubCat(String branchAddress, String categoryID) {
        List<Category> categories = (List<Category>) (List<?>) selectListByBranch(branchAddress, categoryID, CategoryFatherColumnName);
        List<String> catIDList = new LinkedList<>();
        for (Category category : categories)
            catIDList.add(category.getCatID());
        return catIDList;
    }

    public void removeFromMap(String branchAddress, String catID){// todo, needs to be by branchAddress
        if(categoriesByBranchIM.containsKey(catID))
            categoriesByBranchIM.remove(catID);
    }

    public List<Integer> getItemIDList(String branchAddress, String categoryID) {
        return (List<Integer>) (List<?>) selectItemsByCatList(branchAddress, categoryID, CategoryIDColumnName);
    }

    private List<Object> selectItemsByCatList(String branchAddress, String catId, String columnName) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+"ItemsByCategory"+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = "+"'"+ catId + "'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(rs.getInt(3));
            }
        }
        catch (SQLException e) {
            //throw new IllegalArgumentException(e.getMessage()+",DB couldn't selectList in: "+ "ItemsByCategory");
            return results;
        }
        return results;
    }

    public String getFatherID (String branchAddress, String subCategoryID) {
        String sql = "SELECT * FROM "+super.getTableName()+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+CategoryIDColumnName+" = "+"'"+subCategoryID+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return rs.getString(4);
            else
                throw new IllegalArgumentException();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't select by "+CategoryFatherColumnName+" in: "+ super.getTableName());
        }
    }

    public Integer getCounter(String branchAddress, String catID) {
        String sql = "SELECT * FROM "+super.getTableName()+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+CategoryIDColumnName+" = '"+catID+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return rs.getInt(5);
            else
                throw new IllegalArgumentException();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't select by "+CategoryFatherColumnName+" in: "+ super.getTableName());
        }
    }


    @Override
    protected Category ConvertReaderToObject(String branchAddress, ResultSet reader) {
        Category cat = null;
        try {
            cat = new Category(branchAddress, reader.getString(2), reader.getString(3));
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return cat;
    }

    public void updateName(String branchAddress, String catID, String newName) {
        //update(catID,CategoryIDColumnName,newName,CategoryNameColumnName);
        update(branchAddress, catID,CategoryNameColumnName,newName,CategoryIDColumnName);
    }

    public void updateFather(String branchAddress, String catID, String fatherID) {
        update(branchAddress, catID,CategoryFatherColumnName, fatherID,CategoryIDColumnName);
    }

    public void updateCounter(String branchAddress, String catID, int counter){
        update(branchAddress, catID, SubCounterColumnName, counter, CategoryIDColumnName);
    }

    public void removeSubCategory(String branchAddress, String subCatID) {
        update(branchAddress, subCatID, CategoryFatherColumnName, null,CategoryIDColumnName);
    }

    public void deleteRecord(String branchAddress, String categoryID){
        delete(branchAddress, categoryID, CategoryIDColumnName);
    }
}