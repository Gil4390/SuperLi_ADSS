package Presentation.Model.InventoryModel;

import Presentation.Model.BackendController;
import ServiceLayer.Objects.InventoryObjects.FInventoryItem;

import java.util.List;

public class InventoryItemModel extends BasicItemModel {
    private final int frontAmount;
    private final int backAmount;
    private final int totalAmount;
    private final double priceInStore;
    private final double priceAfterDiscounts;

    public InventoryItemModel(FInventoryItem item){
        super(item.getItemID() , item.getName(), item.getCategoryName(), item.getManufacture());
        this.frontAmount = item.getFrontAmount();
        this.backAmount = item.getBackAmount();
        this.totalAmount = item.getTotalAmount();
        this.priceInStore = item.getPriceInStore();
        this.priceAfterDiscounts = item.getPriceAfterDiscounts();
    }

    public static List<InventoryItemModel> getReport() {
        return BackendController.getInstance().getInventoryReport();
    }
    public static List<InventoryItemModel> getReportByCategory(CategoryModel categoryModel) {
        return BackendController.getInstance().getInventoryByCategoryReport(categoryModel);
    }

    public int getFrontAmount() {return frontAmount;}
    public int getBackAmount() {return backAmount;}
    public int getTotalAmount() {return totalAmount;}
    public double getPriceInStore() {return priceInStore;}
    public double getPriceAfterDiscounts() {return priceAfterDiscounts;}
}
