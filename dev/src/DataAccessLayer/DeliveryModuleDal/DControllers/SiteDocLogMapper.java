package DataAccessLayer.DeliveryModuleDal.DControllers;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class SiteDocLogMapper extends DalController {

    private final String SiteDocIdColumnName = "SiteDocumentId";
    private final String LogColumnName = "Log";
    private final String IndexColumnName = "LocationInList";

    public SiteDocLogMapper() {
        super("'Site Document Log'");
    }

    public List<String> selectAllLogs(int siteDocId) throws Exception {
        Map<Integer, String> logPerIndex = new HashMap<>();
         String sql = "SELECT * FROM "+getTableName()+" WHERE "+SiteDocIdColumnName+" = "+ siteDocId;
         try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                logPerIndex.put(ConvertReaderToInt(rs), ConvertReaderToObject(rs));
            }
            return compare(logPerIndex);
         }
        catch (Exception e){
            throw new Exception("could not select list of logs " + getTableName());
        }
    }

    private List<String> compare(Map<Integer, String> map) {
        List<String> result = new LinkedList<>();
        while (!map.isEmpty())
            result.add(extractMinValue(map));
        return result;
    }

    private String extractMinValue(Map<Integer, String> map) {
        int min = Integer.MAX_VALUE;
        for (Integer i: map.keySet()) {
            if (i < min)
                min = i;
        }
        String result = map.get(min);
        map.remove(min);
        return result;
    }

    public void insert(int siteDocId, String log, int index) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(), SiteDocIdColumnName, LogColumnName, IndexColumnName);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, siteDocId);
            pstmt.setString(2, log);
            pstmt.setInt(3, index);
            pstmt.executeUpdate();
        }
        catch (Exception e){
            throw new Exception("could not insert to " + getTableName());
        }
    }

    public void delete(int siteDocId, int index) throws Exception {
        if(!delete(siteDocId, index, SiteDocIdColumnName, IndexColumnName))
            throw new Exception("no log, siteDoc id:" + siteDocId + ", index:" + index);
    }


    @Override
    protected String ConvertReaderToObject(ResultSet reader) throws Exception {
        String result;
        result = reader.getString(2);
        return result;
    }

    protected Integer ConvertReaderToInt(ResultSet reader) throws Exception {
        int result;
        result = reader.getInt(3);
        return result;
    }

    public void addLogToSiteDoc(int documentId, String log) throws Exception {
        int index = selectAllLogs(documentId).size();
        insert(documentId,log,index);
    }

    public void deleteAllLogsOfSiteDoc(int siteDocid) throws Exception {
        if(!delete(siteDocid, SiteDocIdColumnName))
            throw new Exception("couldnt delete logs of site doc "+ siteDocid);
    }

    @Override
    public void cleanCache() {
    }
}
