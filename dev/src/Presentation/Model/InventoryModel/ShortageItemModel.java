package Presentation.Model.InventoryModel;

import Presentation.Model.BackendController;
import ServiceLayer.Objects.InventoryObjects.FInShortageItem;

import java.util.List;

public class ShortageItemModel extends BasicItemModel{
    private final int currentQuantity;
    private final int minimalQuantity;
    private final int fullQuantity;

//    public ShortageItemModel(int itemID, String name, String categoryName, String manufacture, int currentQuantity,int minimalQuantity) {
//        super(itemID, name, categoryName, manufacture);
//        this.currentQuantity = currentQuantity;
//        this.minimalQuantity = minimalQuantity;
//    }

    public ShortageItemModel(FInShortageItem shortageItem) {
        super(shortageItem.getItemID(),shortageItem.getName(), shortageItem.getCategoryName(), shortageItem.getManufacture());
        currentQuantity = shortageItem.getCurrentQuantity();
        minimalQuantity = shortageItem.getMinimalQuantity();
        fullQuantity = shortageItem.getFullQuantity();
    }

    public static List<ShortageItemModel> getReport() {
        return BackendController.getInstance().getShortageReport();
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public int getMinimalQuantity() {
        return minimalQuantity;
    }

    public int getFullQuantity() {
        return fullQuantity;
    }
}
