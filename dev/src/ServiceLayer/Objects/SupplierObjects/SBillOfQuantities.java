package ServiceLayer.Objects.SupplierObjects;

import BusinessLayer.SupplierBusiness.BillOfQuantity;
import BusinessLayer.SupplierBusiness.SupplierItem;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SBillOfQuantities {
    private final Map<Integer, SSupplierItem> items;

    public SBillOfQuantities(String branchAddress, BillOfQuantity billOfQuantity) {
        items = new ConcurrentHashMap<>();
        for(SupplierItem supplierItem : billOfQuantity.getItems(branchAddress).values())
            items.put(supplierItem.getCatalogNumber(), new SSupplierItem(supplierItem));
    }

    public Map<Integer, SSupplierItem> getItems() {
        return items;
    }
}
