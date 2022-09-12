package DeliveryTests;

import BusinessLayer.DeliveryModule.Controllers.SiteDocController;
import BusinessLayer.DeliveryModule.Objects.SiteDoc;
import BusinessLayer.DeliveryModule.Objects.StockShortness;
import BusinessLayer.DeliveryModule.Objects.problemSiteVisit;
import DataAccessLayer.DeliveryModuleDal.DControllers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SiteDocControllerTest {

    SiteDocController siteDocController;
    List<StockShortness> stockShortnessList;
    Map<StockShortness, Boolean> stockShortnessBooleanMap;


    @BeforeEach
    void setUp() {
        try {
            siteDocController = new SiteDocController(new SiteDocMapper(new SiteDocLogMapper(), new LoadItemsMapper(), new UnloadItemsMapper()));
            stockShortnessList = new StockShortnessMapper().getAllUnboundedStockShortnesses();
            stockShortnessBooleanMap = new HashMap<>();
            for (StockShortness stockShortness: this.stockShortnessList) {
                stockShortnessBooleanMap.put(stockShortness, false);
            }
            siteDocController.deleteAllData();
        }
        catch (Exception ignored){}
    }

    @AfterEach
    void tearDown() {
        siteDocController.deleteAllData();
    }

    @Test
    void createNewSiteDoc() {
        try {
            Map<Integer, Integer> loadItems1 = new HashMap<>();
            Map<Integer, Integer> unloadItems1 = new HashMap<>();
            unloadItems1.put(0, 30); unloadItems1.put(9, 38);
            loadItems1.put(0, 30); loadItems1.put(3, 30); loadItems1.put(4, 7); loadItems1.put(5, 55); loadItems1.put(7, 88);
            SiteDoc siteDoc1 = siteDocController.createNewSiteDoc("Tel-Aviv", loadItems1, unloadItems1, 0, "idan", "mor", 0);

            assertEquals(30, siteDoc1.getLoadItems().get(0));
            assertEquals(30, siteDoc1.getLoadItems().get(3));
            assertEquals(7, siteDoc1.getLoadItems().get(4));
            assertEquals(55, siteDoc1.getLoadItems().get(5));
            assertEquals(88, siteDoc1.getLoadItems().get(7));

            for (Map.Entry<StockShortness, Boolean> e: stockShortnessBooleanMap.entrySet()) {
                stockShortnessBooleanMap.replace(e.getKey(), true);
            }
            Map<Integer, Integer> loadItems2 = new HashMap<>();
            Map<Integer, Integer> unloadItems2 = new HashMap<>();
            unloadItems2.put(0, 30); unloadItems2.put(9, 38);
            loadItems2.put(0, 30); loadItems2.put(3, 30); loadItems2.put(4, 7); loadItems2.put(5, 55); loadItems2.put(7, 88);
            SiteDoc siteDoc2 = siteDocController.createNewSiteDoc("Tel-Aviv", loadItems2, unloadItems2, 1, "idan", "mor", 0);

            assertEquals(30, siteDoc2.getUnloadItems().get(0));
            assertEquals(38, siteDoc2.getUnloadItems().get(9));
        }
        catch (Exception ignored){
        }
    }

    @Test
    void addLogToFutureSites() {
        try {
            siteDocController.createSiteDoc("Tel-Aviv", stockShortnessBooleanMap, 0, "idan", "mor", 0);
            siteDocController.createSiteDoc("Ramat-Gan", stockShortnessBooleanMap, 0, "idan", "mor", 1);
            siteDocController.createSiteDoc("Haifa", stockShortnessBooleanMap, 0, "idan", "mor", 2);

            siteDocController.changeWeight(0, 3000);
            siteDocController.addLogToFutureSites(0, problemSiteVisit.overweightTruck);

            for (SiteDoc siteDoc : siteDocController.getAllDocumentsForDelivery(0)) {
                if (siteDoc.getSiteAddress().equals("Tel-Aviv"))
                    assertTrue(siteDoc.getLogList().isEmpty());
                else
                    assertFalse(siteDoc.getLogList().isEmpty());
            }
        }
        catch (Exception e){
            fail();
        }
    }

}
