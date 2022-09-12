package DataAccessLayer.InventoryDAL;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO {
    private final String tableName;
    private String path;
    private String connectionString;
    public AbstractDAO(String tableName)
    {
        this.tableName = tableName;
    }

    protected Connection connect() {
        path = (new File("").getAbsolutePath()).concat("\\SuperLiDB.db");
        connectionString = "jdbc:sqlite:".concat(path);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionString);
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",couldn't connect to DB");
        }
        return conn;
    }

    public abstract void cleanCache();
    public String getTableName() {
        return tableName;
    }

    protected abstract Object ConvertReaderToObject(String branchAddress, ResultSet reader);

    // update table row when value is string
    public boolean update(String branchAddress, int id, String attributeName, String attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", DB couldn't update in: "+ tableName +", "+ columnName);
        }
        return true;
    }
    public boolean update(String branchAddress, String id, String attributeName, int attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, attributeValue);
            pstmt.setString(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", DB couldn't update in: "+ tableName +", "+ columnName);
        }
        return true;
    }
    public boolean update(String branchAddress, int value1, int value2, String attributeName, Double attributeValue, String columnName1, String columnName2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setInt(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update in: "+ tableName);
        }
        return true;
    }
    public boolean update(String branchAddress, int id1, int id2, String attributeName, int attributeValue, String column1, String column2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+column1+" = ? AND "+column2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, attributeValue);
            pstmt.setInt(2, id1);
            pstmt.setInt(3, id2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update in: "+ tableName);
        }
        return true;
    }
    public boolean update(String branchAddress, int id1, int id2, String attributeName, String attributeValue, String column1, String column2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+column1+" = ? AND "+column2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, id1);
            pstmt.setInt(3, id2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update in: "+ tableName);
        }
        return true;
    }
    //update table row when ID is string
    public boolean update(String branchAddress, String id, String attributeName, String attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setString(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+", DB couldn't update in:"+ tableName +", "+ columnName);
        }
        return true;
    }

    // update table row when value is int
    public boolean update(String branchAddress, int id, String attributeName, int attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, attributeValue);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update: "+ tableName);
        }
        return true;
    }
    public boolean update(String branchAddress, int id, String attributeName, boolean attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setBoolean(1, attributeValue);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update in: "+ tableName);
        }
        return true;
    }
    public boolean update(String branchAddress, int value1, String value2, String attributeName, String attributeValue, String columnName1, String columnName2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setString(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update in: "+ tableName);
        }
        return true;
    }
//for changes of double value
    public boolean update(String branchAddress, int value1, String value2, String attributeName, double attributeValue, String columnName1, String columnName2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setString(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update in: "+ tableName);
        }
        return true;
    }

    public boolean update(String branchAddress, int value1, int value2, int value3, String attributeName, double attributeValue, String columnName1, String columnName2, String columnName3) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = ? AND "+columnName2+" = ? AND "+columnName3+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setInt(3, value2);
            pstmt.setInt(4, value3);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't update in tableName");
        }
        return true;
    }

    public void update(String branchAddress, int id, String attributeName, double attributeValue, String columnName) {
        String sql = "UPDATE " + tableName + " SET " + attributeName + " = ? "
                + "WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND " + columnName + " = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage() + ",DB couldn't update in: " + tableName);
        }
    }

    // get all objects of specific table - each row is an object
    public List<Object> selectAllByBranch(String branchAddress) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                Object o = ConvertReaderToObject(branchAddress, rs);
                results.add(o);
            }
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't selectAll in: "+ tableName);
        }
        return results;
    }

    public List<Object> selectListByBranch(String branchAddress, int id, String columnName) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = "+id;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                Object o = ConvertReaderToObject(branchAddress, rs);
                results.add(o);
            }
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't selectList in: "+ tableName);
        }
        return results;
    }

    public List<Object> selectListByBranch(String branchAddress, int id1, String columnName1, int id2, String columnName2) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+id1+" AND "+columnName2+" = "+id2;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(branchAddress, rs));
            }
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't selectAll in: "+ tableName);
        }
        return results;
    }



    public Object select(String branchAddress, int id, String columnName)
    {
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = "+id;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(branchAddress, rs);
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't select by"+columnName+" in: "+ tableName);
        }
        return null;
    }


    public Object select(String branchAddress, int id1, int id2, String columnName1, String columnName2)
    {
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+id1+" AND "+columnName2+" = "+id2;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(branchAddress, rs);
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't select by: "+columnName1+", and by: "+columnName2+" in: "+ tableName);
        }
        return null;
    }

    //those 4 select methods when the id is string
    public List<Object> selectListByBranch(String branchAddress, String id, String columnName) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = "+"'"+id+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(branchAddress, rs));
            }
        }
        catch (SQLException e) {
            //throw new IllegalArgumentException(e.getMessage()+",DB couldn't selectList in: "+ tableName);
            return results;
        }
        return results;
    }

    public List<Object> selectListByBranch(String branchAddress, String id1, String columnName1, int id2, String columnName2) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+"'"+id1+"'"+" AND "+columnName2+" = "+id2;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(branchAddress, rs));
            }
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't selectAll in: "+ tableName);
        }
        return results;
    }
    public Object select(String branchAddress, String id, String columnName)
    {
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = "+"'"+id+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(branchAddress, rs);
            else
                throw new IllegalArgumentException();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't select by "+columnName+" in: "+ tableName);
        }
    }

    public Object select(String branchAddress, String id1, String id2, String columnName1, String columnName2)
    {
        String sql = "SELECT * FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+"'"+id1+"'"+" AND "+columnName2+" = "+"'"+id2+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(branchAddress, rs);
            else
                throw new IllegalArgumentException();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't select by: "+columnName1+", and by: "+columnName2+" in: "+ tableName);
        }
    }

    public boolean delete(String branchAddress, String id, String columnName) {
        String sql = "DELETE FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = "+"'"+id+"'";
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't delete by: "+columnName+" in: "+ tableName);
        }
    }

    public boolean delete(String branchAddress, int id, String columnName) {
        String sql = "DELETE FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName+" = "+id;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage()+",DB couldn't delete by: "+columnName+" in: "+ tableName);
        }
    }

    public boolean delete(String branchAddress, int value1, int value2, int value3, String columnName1, String columnName2, String columnName3) {
        String sql = "DELETE FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+value2+" AND "+columnName3+" = "+value3;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }

    public boolean delete(String branchAddress, String tableName, int value1, int value2, int value3, String columnName1, String columnName2, String columnName3) {
        String sql = "DELETE FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+value2+" AND "+columnName3+" = "+value3;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }

    public boolean delete(String branchAddress, int value1, String value2, String columnName1, String columnName2) {
        String sql = "DELETE FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+"'"+value2+"'";
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }
    public boolean delete(String branchAddress, int value1, int value2, String columnName1, String columnName2) {
        String sql = "DELETE FROM "+tableName+" WHERE BranchAddress"+" = "+"'"+branchAddress+"'"+" AND "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+value2;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }

}
