package DataAccessLayer.DeliveryModuleDal.DControllers;

import java.sql.*;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class TruckAvailableMapper extends DalController {

    private final String LicenseNumberColumnName = "LicenseNumber";
    private final String DateColumnName = "Date";

    public TruckAvailableMapper() {
        super("'Trucks Available'");
    }

    public List<String> getAllDatesUnAvailable(String truckLicense) throws Exception {
        List<String> dates = new LinkedList<>();
        String sql = "SELECT * FROM "+getTableName()+" WHERE "+LicenseNumberColumnName+" = '"+truckLicense+"'";
        try(Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next())
                dates.add(convertReaderToDateString(rs));
            return dates;
        }
        catch (SQLException e) {
            throw new Exception("could not get all available dates for truck " + truckLicense);
        }
    }

    public List<String> getAllTrucksAvailableByDate(String date) throws Exception {
            List<String> licenses = new LinkedList<>();
        String sql = "SELECT * FROM "+getTableName()+" WHERE "+DateColumnName+" = '"+date+"'";
            try(Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
                while (rs.next())
                    licenses.add(convertReaderToLicenseString(rs));
                return licenses;
            }
            catch (SQLException e) {
                throw new Exception("could not get all available trucks per date " + date);
            }

    }

    public void insert(String truckLicense, String date) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?,?)",
                getTableName(),LicenseNumberColumnName,DateColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, truckLicense);
            pstmt.setString(2, date);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new Exception("could not insert truck date");
        }
    }

    public void remove(String truckLicense, String date) throws Exception {
        if(!delete(truckLicense, date, LicenseNumberColumnName, DateColumnName))
            throw new Exception("could not remove date");
    }

    @Override
    public void cleanCache() {
    }

    @Override
    protected ResultSet ConvertReaderToObject(ResultSet reader) throws SQLException { return reader;}

    private String convertReaderToDateString(ResultSet reader) throws SQLException {
        return reader.getString(2);
    }
    private String convertReaderToLicenseString(ResultSet reader) throws SQLException {
        return reader.getString(1);
    }
}
