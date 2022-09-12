package ServiceLayer.Objects.InventoryObjects;

import BusinessLayer.InventoryBusiness.Category;

public class FCategory {
    private final String name;
    private final String catID;
    private final String fatherID;

    public FCategory(Category cat) {
        this.name= cat.getName();
        this.catID =cat.getCatID();
        if(cat.getCatID().equals("0"))
            fatherID=null;
        else
            this.fatherID = cat.getFatherCategory().getCatID();
    }

    public FCategory(String catName, String catID, String fatherID) {
        this.name= catName;
        this.catID =catID;
        this.fatherID = fatherID;
    }

    public String getFatherID() {
        return fatherID;
    }
    public String getName() {return name;}
    public String getCatID() {return catID;}
}
