package DataAccessLayer.DeliveryModuleDal.DControllers;


import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DalController {
    private final String tableName;
    private String path;
    private String connectionString;
    public DalController(String tableName)
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
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    protected String getTableName() {
        return tableName;
    }
    protected abstract Object ConvertReaderToObject(ResultSet reader) throws Exception;
    public abstract void cleanCache();
    // update table row when value is string
    protected boolean update(int id, String attributeName, String attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+columnName+" = ? "
                + "WHERE "+attributeName+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    protected boolean update(int value1, int value2, String attributeName, Double attributeValue, String columnName1, String columnName2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setDouble(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setInt(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    protected boolean update(int id1, int id2, String attributeName, int attributeValue, String column1, String column2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE "+column1+" = ? AND "+column2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, attributeValue);
            pstmt.setInt(2, id1);
            pstmt.setInt(3, id2);
            // update
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean update(int id1, int id2, String attributeName, String attributeValue, String column1, String column2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE "+column1+" = ? AND "+column2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, id1);
            pstmt.setInt(3, id2);
            // update
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    // update table row when value is int
    public boolean update(int id, String attributeName, int attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+columnName+" = ? "
                + "WHERE "+attributeName+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, attributeValue);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean update(int id, String attributeName, boolean attributeValue, String columnName) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE "+columnName+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setBoolean(1, attributeValue);
            pstmt.setInt(2, id);
            // update
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean update(int value1, String value2, String attributeName, String attributeValue, String columnName1, String columnName2) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE "+columnName1+" = ? AND "+columnName2+" = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, attributeValue);
            pstmt.setInt(2, value1);
            pstmt.setString(3, value2);
            // update
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean update(int value1, int value2, int value3, String attributeName, double attributeValue, String columnName1, String columnName2, String columnName3) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE "+columnName1+" = ? AND "+columnName2+" = ? AND "+columnName3+" = ?";

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
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    public boolean update(String tableName, int value1, int value2, int value3, String attributeName, double attributeValue, String columnName1, String columnName2, String columnName3) {
        String sql = "UPDATE "+tableName+" SET "+attributeName+" = ? "
                + "WHERE "+columnName1+" = ? AND "+columnName2+" = ? AND "+columnName3+" = ?";

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
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    // get all objects of specific table - each row is a object
    public List<Object> select() {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(rs));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return results;
    }
    public List<Object> selectList(int id, String columnName) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName+" = "+id;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(rs));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        return results;
    }
    public List<Object> selectList(String id, String columnName) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName+" = "+id;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(rs));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        return results;
    }
    public List<Object> selectList(int id1, String columnName1, int id2, String columnName2) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName1+" = "+id1+" AND "+columnName2+" = "+id2;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(rs));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        return results;
    }
    public List<Object> selectList(int id1, String columnName1, String id2, String columnName2) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName1+" = "+id1+" AND "+columnName2+" = "+id2;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(rs));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        return results;
    }
    public List<Object> selectList(String id1, String columnName1, String id2, String columnName2) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName1+" = "+"'"+id1+"'"+" AND "+columnName2+" = "+"'"+id2+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(rs));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        return results;
    }
    public List<Object> selectList(String id1, String columnName1, String id2, String columnName2,String id3, String columnName3) {
        List<Object> results = new ArrayList<>();
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName1+" = '"+id1+"' AND "+columnName2+" = '"+id2+"' AND "+columnName3+" = '"+id3+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                results.add(ConvertReaderToObject(rs));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
        return results;
    }
    public Object select(String id, String columnName)
    {
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName+" = '"+id+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(rs);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Object select(int id, String columnName)
    {
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName+" = "+id;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(rs);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Object select(int id1, int id2, String columnName1, String columnName2)
    {
        String sql = "SELECT * FROM "+tableName+" WHERE "+columnName1+" = "+id1+" AND "+columnName2+" = "+id2;
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(rs);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Object selectLastId(String columnName)
    {
        String sql = "SELECT * FROM "+tableName+" ORDER BY "+columnName+" DESC LIMIT 1";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next())
                return ConvertReaderToObject(rs);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean delete(int id, String columnName) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName+" = "+id;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }
    public boolean delete(String id, String columnName) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName+" = "+id;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }
    public boolean delete(int value1, int value2, int value3, String columnName1, String columnName2, String columnName3) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+value2+" AND "+columnName3+" = "+value3;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }
    public boolean delete(int value1, String value2, String value3, String columnName1, String columnName2, String columnName3) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName1+" = "+value1+" AND "+ columnName2+ " = '"+value2+"' AND "+columnName3+""+" = '"+value3+"'";
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }

    public boolean delete(String tableName, int value1, int value2, int value3, String columnName1, String columnName2, String columnName3) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+value2+" AND "+columnName3+" = "+value3;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }

    public boolean delete(int value1, String value2, String columnName1, String columnName2) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+value2;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }
    public boolean delete(int value1, int value2, String columnName1, String columnName2) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName1+" = "+value1+" AND "+ columnName2+ " = "+value2;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean delete(String value1, String value2, String columnName1, String columnName2) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName1+" = '"+value1+"' AND "+ columnName2+ " = '"+value2+"'";
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }

    public boolean delete(String value1, String value2, String value3, String columnName1, String columnName2, String columnName3) {
        String sql = "DELETE FROM "+tableName+" WHERE "+columnName1+" = '"+value1+"' AND "+ columnName2+ " = '"+value2+"' AND "+ columnName3+ " = '"+value3+"'";
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            return true;
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
            return  false;
        }
    }

    public void deleteAllData(){
        String sql = "DELETE FROM "+tableName;
        try(Connection conn = this.connect()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }

    }

}
