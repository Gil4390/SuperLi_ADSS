package DataAccessLayer.DeliveryModuleDal.DControllers;

import BusinessLayer.DeliveryModule.Objects.SiteDoc;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SiteDocMapper extends DalController {

    private final String IdColumnName = "Id";
    private final String DeliveryIdColumnName = "DeliveryId";
    private final String SiteAddressColumnName = "SiteAddress";
    private final String TruckWeightColumnName = "TruckWeight";
    private final String ContactNumberColumnName = "ContactNumber";
    private final String ContactNameColumnName = "ContactName";
    private final String LocationInAddress= "locationInAddressList";

    private Map<Integer, SiteDoc> siteDocs;
    private final SiteDocLogMapper logMapper;
    private final LoadItemsMapper loadMapper;
    private final UnloadItemsMapper unloadMapper;

    public SiteDocMapper(SiteDocLogMapper siteDocLogMapper, LoadItemsMapper loadItemsMapper, UnloadItemsMapper unloadItemsMapper) {
        super("'Site Documents'");
        siteDocs = new ConcurrentHashMap<>();
        this.logMapper = siteDocLogMapper;
        this.loadMapper = loadItemsMapper;
        this.unloadMapper = unloadItemsMapper;
    }

    public void load() throws Exception {
        for(SiteDoc siteDoc : selectAllSiteDocs())
            siteDocs.put(siteDoc.getDocumentId(), siteDoc);
    }

    public void checkSiteDocExist(int documentId)throws Exception{
        if(getSiteDoc(documentId) == null)
            throw new NoSuchElementException();
    }

    public SiteDoc getSiteDoc(int documentId) throws Exception{
        if (siteDocs.containsKey(documentId))
            return siteDocs.get(documentId);
        try {
            SiteDoc rs= (SiteDoc) select(documentId, IdColumnName);
            List<String> logs = logMapper.selectAllLogs(documentId);
            Map<Integer, Integer> load = loadMapper.selectAllLoad(documentId);
            Map<Integer, Integer> unload = unloadMapper.selectAllUnload(documentId);
            SiteDoc result = createSiteDocObject(rs, logs, load, unload);
            if(!siteDocs.containsKey(documentId))siteDocs.put(documentId, result);
            return result;
        }
        catch (Exception e) {
            throw new Exception("couldnt fetch site document number "+documentId);
        }

    }

    public List<SiteDoc> getFutureSiteDocs(int deliveryId) throws Exception {
        List<SiteDoc> rsList = (List<SiteDoc>)(List<?>)selectList(deliveryId, DeliveryIdColumnName, 0, TruckWeightColumnName);
        List<SiteDoc> results = new ArrayList<>();
        for (SiteDoc rs: rsList){
            int documentId = rs.getDocumentId();
            List<String> logs = logMapper.selectAllLogs(documentId);
            Map<Integer, Integer> load = loadMapper.selectAllLoad(documentId);
            Map<Integer, Integer> unload = unloadMapper.selectAllUnload(documentId);
            SiteDoc result = createSiteDocObject(rs, logs, load, unload);
            results.add(result);
            if(!siteDocs.containsKey(documentId)) siteDocs.put(documentId, result);
        }
        return results;
    }

    public void addLogToSiteDoc(int documentId, String  log) throws Exception {
        if (siteDocs.containsKey(documentId))
            siteDocs.get(documentId).addLog(log);
        logMapper.addLogToSiteDoc(documentId, log);
    }

    public List<SiteDoc> getAllDocumentsForDelivery(int deliveryId) throws Exception {
        List<SiteDoc> rsList = (List<SiteDoc>)(List<?>)selectList(deliveryId, DeliveryIdColumnName);
        List<SiteDoc> results = new ArrayList<>();
        for (SiteDoc rs: rsList){
            int documentId = rs.getDocumentId();
            List<String> logs = logMapper.selectAllLogs(documentId);
            Map<Integer, Integer> load = loadMapper.selectAllLoad(documentId);
            Map<Integer, Integer> unload = unloadMapper.selectAllUnload(documentId);
            SiteDoc result = createSiteDocObject(rs, logs, load, unload);
            results.add(result);
            if(!siteDocs.containsKey(documentId)) siteDocs.put(documentId, result);
        }
        return results;
    }

    public void changeWeightOnDocument(int documentId, int newTruckWeight) throws Exception {
        if (siteDocs.containsKey(documentId))
            siteDocs.get(documentId).changeWeight(newTruckWeight);
        if (!update(documentId,IdColumnName , newTruckWeight,TruckWeightColumnName ))
            throw new Exception("cannot change truck weight");
    }

    public void removeItemFromUnLoad(int documentId, int itemCatalogNumber, int amount) throws Exception {
        if (siteDocs.containsKey(documentId))
            siteDocs.get(documentId).removeFromUnload(itemCatalogNumber, amount);
        unloadMapper.removeItemFromUnload(documentId, itemCatalogNumber, amount);
    }

    public List<SiteDoc> getDocumentForDeliveryAndSite(int deliveryId, String address) throws Exception {
        List<SiteDoc> rsList = (List<SiteDoc>)(List<?>)selectList(deliveryId, DeliveryIdColumnName, "'"+address+"'", SiteAddressColumnName);
        List<SiteDoc> results = new ArrayList<>();
        for (SiteDoc rs: rsList){
            int documentId = rs.getDocumentId();
            List<String> logs = logMapper.selectAllLogs(documentId);
            Map<Integer, Integer> load = loadMapper.selectAllLoad(documentId);
            Map<Integer, Integer> unload = unloadMapper.selectAllUnload(documentId);
            SiteDoc result = createSiteDocObject(rs, logs, load, unload);
            results.add(result);
            if(!siteDocs.containsKey(documentId)) siteDocs.put(documentId, result);
        }
        return results;
    }

    public void removeItemFromLoad(int documentId, int itemCatalogNumber, int amount) throws Exception {
        if (siteDocs.containsKey(documentId))
            siteDocs.get(documentId).removeFromLoad(itemCatalogNumber, amount);
        loadMapper.removeItemFromload(documentId, itemCatalogNumber, amount);
    }


    private List<SiteDoc> selectAllSiteDocs() throws Exception {
        List<SiteDoc> rsList = (List<SiteDoc>)(List<?>)select();
        List<SiteDoc> results = new ArrayList<>();
        for (SiteDoc rs: rsList){
            int documentId = rs.getDocumentId();
            List<String> logs = logMapper.selectAllLogs(documentId);
            Map<Integer, Integer> load = loadMapper.selectAllLoad(documentId);
            Map<Integer, Integer> unload = unloadMapper.selectAllUnload(documentId);
            SiteDoc result = createSiteDocObject(rs, logs, load, unload);
            results.add(result);
            if(!siteDocs.containsKey(documentId)) siteDocs.put(documentId, result);
        }
        return results;
    }

    public void insert(SiteDoc siteDoc) throws Exception {
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}) VALUES(?,?,?,?,?,?,?)",
                getTableName(), IdColumnName, SiteAddressColumnName, DeliveryIdColumnName, TruckWeightColumnName, ContactNumberColumnName, ContactNameColumnName, LocationInAddress);
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, siteDoc.getDocumentId());
            pstmt.setString(2, siteDoc.getSiteAddress());
            pstmt.setInt(3, siteDoc.getDeliveryId());
            pstmt.setInt(4, siteDoc.getWeight());
            pstmt.setString(5, siteDoc.getContactNumber());
            pstmt.setString(6, siteDoc.getContactName());
            pstmt.setInt(7, siteDoc.getLocationInAddressList());
            pstmt.executeUpdate();
            siteDocs.put(siteDoc.getDocumentId(), siteDoc);
            for(Map.Entry<Integer, Integer> loaditem :siteDoc.getLoadItems().entrySet())
                loadMapper.insert(siteDoc.getDocumentId(), loaditem.getKey(), loaditem.getValue());
            for(Map.Entry<Integer, Integer> unloaditem :siteDoc.getUnloadItems().entrySet())
                unloadMapper.insert(siteDoc.getDocumentId(), unloaditem.getKey(), unloaditem.getValue());
        }
        catch (Exception e) {
            throw new Exception("couldnt insert new Site document to the system: "+ siteDoc.getDocumentId() );
        }
    }

    public void addToUnload(int documentId, int itemCatalogNumber, int amount) throws Exception {
        if (siteDocs.containsKey(documentId))
            siteDocs.get(documentId).addToUnload(itemCatalogNumber, amount);
        unloadMapper.AddItemToUnload(documentId, itemCatalogNumber, amount);
    }

    public void addToLoad(int documentId, int itemCatalogNumber, int amount) throws Exception {
        if (siteDocs.containsKey(documentId))
            siteDocs.get(documentId).addToLoad(itemCatalogNumber, amount);
        loadMapper.AddItemToLoad(documentId, itemCatalogNumber, amount);
    }


    private void delete(int siteDocid) throws Exception {
        if(siteDocs.containsKey(siteDocid))
            siteDocs.remove(siteDocid);
        if (!delete(siteDocid, IdColumnName))
            throw new Exception("couldnt delete SiteDoc");
        logMapper.deleteAllLogsOfSiteDoc(siteDocid);
        loadMapper.deleteAllLoadItemsOfSiteDoc(siteDocid);
        unloadMapper.deleteAllUnloadItemsOfSiteDoc(siteDocid);
    }


    private int getIdFromRes(ResultSet rs)throws Exception{
        int result = -1;
        try {
            result = rs.getInt(1);
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    protected SiteDoc createSiteDocObject(SiteDoc reader, List<String> logs, Map<Integer, Integer> load, Map<Integer, Integer> unload) throws Exception {
        SiteDoc result;
        result = new SiteDoc(reader.getDeliveryId(), reader.getSiteAddress(), reader.getDocumentId(), load, unload, logs, reader.getWeight(), reader.getContactNumber(), reader.getContactName(), reader.getLocationInAddressList());
        return result;
    }

    @Override
    protected SiteDoc ConvertReaderToObject(ResultSet reader) throws Exception {
        return new SiteDoc(reader.getInt(2), reader.getString(3), reader.getInt(1), new HashMap<>(), new HashMap<>(), new ArrayList<>(), reader.getInt(4), reader.getString(5), reader.getString(6), reader.getInt(7));
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, SiteDoc>> iter = siteDocs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,SiteDoc> entry = iter.next();
            if(entry.getValue().shouldCleanCache()){
                System.out.println("Cleaning site doc "+entry.getValue().getDocumentId() +" from cache!");
                iter.remove();
            }
        }
    }

    public int removeSiteDocFromDelivery(int SiteDocId) throws Exception {
        SiteDoc siteDoc = getSiteDoc(SiteDocId);
        int deliveryId = siteDoc.getDeliveryId();
        int Location = siteDoc.getLocationInAddressList();
        siteDoc.changeLocation(-1);
        update(deliveryId, Location, LocationInAddress, -1, DeliveryIdColumnName, LocationInAddress);
        List<SiteDoc> allSiteDocs =selectAllSiteDocs();
        for(SiteDoc sd: allSiteDocs){
            if (sd.getDeliveryId()==deliveryId && sd.getLocationInAddressList()>Location){
                if (siteDocs.containsKey(sd.getDocumentId()))
                    sd.changeLocation(sd.getLocationInAddressList()-1);
                update(deliveryId, sd.getLocationInAddressList()+1, LocationInAddress, sd.getLocationInAddressList(), DeliveryIdColumnName, LocationInAddress);
            }
        }
        return Location;
    }

    public void deleteAllDataFromDbs() {
        logMapper.deleteAllData();
        loadMapper.deleteAllData();
        unloadMapper.deleteAllData();
        deleteAllData();
        siteDocs = new HashMap<>();
    }

    public SiteDoc getSiteDocByDeliveryIdAndLocation(int deliveryId, int locationInAddressList) throws Exception {
        SiteDoc initial = (SiteDoc) select(deliveryId, locationInAddressList, DeliveryIdColumnName, LocationInAddress);
        int documentId = initial.getDocumentId();
        List<String> logs = logMapper.selectAllLogs(documentId);
        Map<Integer, Integer> load = loadMapper.selectAllLoad(documentId);
        Map<Integer, Integer> unload = unloadMapper.selectAllUnload(documentId);
        SiteDoc result = createSiteDocObject(initial, logs, load, unload);
        if(!siteDocs.containsKey(documentId)) siteDocs.put(documentId, result);
        return result;

    }
}
