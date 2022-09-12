package ServiceLayer.Objects.InventoryObjects;

import BusinessLayer.InventoryBusiness.ReportItems.InShortageItem;

public class FInShortageItem extends FBasicItem{
    private final int currentQuantity;
    private final int minimalQuantity;
    private final int fullQuantity;

    public FInShortageItem(InShortageItem item){
        super(item.getItemID() , item.getName(), item.getCategoryName(), item.getManufacture());
        this.currentQuantity = item.getCurrentQuantity();
        this.minimalQuantity = item.getMinimalQuantity();
        this.fullQuantity = item.getFullQuantity();
    }

    public int getCurrentQuantity() {return currentQuantity;}
    public int getMinimalQuantity() {return minimalQuantity;}
    public int getFullQuantity() {return fullQuantity;}
}
