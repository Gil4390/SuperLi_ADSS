package DeliveryTests;

import BusinessLayer.DeliveryModule.Controllers.StockShortnessController;
import BusinessLayer.DeliveryModule.Objects.StockShortness;
import DataAccessLayer.DeliveryModuleDal.DControllers.StockShortnessMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


class StockShortnessControllerTest {

    StockShortnessController stockShortnessController;

    @BeforeEach
    void setUp() {
        stockShortnessController = new StockShortnessController(new StockShortnessMapper());
        stockShortnessController.resetData();
    }

    @AfterEach
    void tearDown() {
        stockShortnessController.resetData();
    }

    @Test
    void getAllRelevantStockShortnesses() {
        try {
            List<StockShortness> stockShortnesses = stockShortnessController.getAllRelevantStockShortnesses();
            for (int i = 0; i < stockShortnesses.size(); i++) {
                assertEquals(i, stockShortnesses.get(i).getId());
            }
            assertEquals(16, stockShortnesses.size());

            List<StockShortness> l1 = new LinkedList<>();
            l1.add(new StockShortness(3, "asd", "fasdf", 234, 123, "afa",1));
            l1.add(new StockShortness(6, "asd", "fasdf", 234, 123, "afa",1));
            List<StockShortness> l2 = new LinkedList<>();
            l2.add(new StockShortness(8, "asd", "fasdf", 234, 123, "afa",1));
            l2.add(new StockShortness(1, "asd", "fasdf", 234, 123, "afa",1));

            List<Integer> l3 = new LinkedList<>();
            for (StockShortness s: l1) {
                l3.add(s.getId());
            }
            List<Integer> l4 = new LinkedList<>();
            for (StockShortness s: l2) {
                l4.add(s.getId());
            }
            stockShortnessController.boundToDelivery(1, l3);
            stockShortnessController.boundToDelivery(2, l4);
            stockShortnesses = stockShortnessController.getAllRelevantStockShortnesses();
            assertEquals(12, stockShortnesses.size());
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void bound() {
        try {
            List<StockShortness> stockShortnesses = stockShortnessController.getAllRelevantStockShortnesses();
            for (StockShortness stockShortness : stockShortnesses) {
                assertEquals(-1, stockShortness.getDeliveryBound());
            }

            List<StockShortness> l1 = new LinkedList<>();
            l1.add(new StockShortness(3, "asd", "fasdf", 234, 123, "afa",2));
            l1.add(new StockShortness(6, "asd", "fasdf", 234, 123, "afa",2));
            List<StockShortness> l2 = new LinkedList<>();
            l2.add(new StockShortness(8, "asd", "fasdf", 234, 123, "afa",2));
            l2.add(new StockShortness(1, "asd", "fasdf", 234, 123, "afa",2));

            List<Integer> l3 = new LinkedList<>();
            for (StockShortness s: l1) {
                l3.add(s.getId());
            }
            List<Integer> l4 = new LinkedList<>();
            for (StockShortness s: l2) {
                l4.add(s.getId());
            }
            stockShortnessController.boundToDelivery(1, l3);
            stockShortnessController.boundToDelivery(2, l4);

            List<StockShortness> stockShortnesses1 = stockShortnessController.getStockShortnessOfDelivery(1);
            for (StockShortness stockShortness : stockShortnesses1) {
                assertEquals(1, stockShortness.getDeliveryBound());
            }
            List<StockShortness> stockShortnesses2 = stockShortnessController.getStockShortnessOfDelivery(2);
            for (StockShortness stockShortness : stockShortnesses2) {
                assertEquals(2, stockShortness.getDeliveryBound());
            }
        }
        catch (Exception e){
            fail();
        }
    }
}