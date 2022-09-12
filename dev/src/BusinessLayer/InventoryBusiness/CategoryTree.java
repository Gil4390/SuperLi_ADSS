package BusinessLayer.InventoryBusiness;

import DataAccessLayer.InventoryDAL.DALFacade;

import java.util.List;

public class CategoryTree {
    private Category root;

    public CategoryTree(String branchAddress){
        try {
            DALFacade.getInstance().addCategory(branchAddress, "0", "Product", null);
        }catch(Exception e) {//root = null;
            root = DALFacade.getInstance().getCategory(branchAddress, "0");
        }
        root = DALFacade.getInstance().getCategory(branchAddress, "0");
    }

    public Category getCategory(String branchAddress, String categoryID)
    {
        if(categoryID == null)
            throw new NullPointerException("categoryID was null");
        if(categoryID.contains("#"))
            return root.getCategory(categoryID.substring(2));
        else{
            if(categoryID.equals("0"))
                return root;
            if(!root.getSubCatList().contains(categoryID))
                throw new IllegalArgumentException("the category: "+categoryID+", not in the system");
            return findSpecificCategory(root.getSubCatList(), categoryID);
        }
    }

    private Category findSpecificCategory(List<Category> categories, String CategoryID){
        for(Category category : categories){
            if(category.getCatID().equals(CategoryID))
                return category;
        }
        throw new IllegalArgumentException("categoryID provided isn't part of categories provided");
    }

    public void moveCategory(Category newFatherCat, Category deliveredCat){
        if(newFatherCat==null || deliveredCat==null)
            throw new NullPointerException("category was null");
        try{
            deliveredCat.getFatherCategory().deleteSubCat(deliveredCat.getCatID());
            newFatherCat.addSubCat(deliveredCat);
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }
    }

    public void addCategory(Category newFatherCat, String catName){
        if(newFatherCat==null)
            throw new NullPointerException("category was null");
        if(catName==null)
            throw new NullPointerException("catName was null");
        newFatherCat.addNewSubCat(catName);
    }

    public void removeCategory(String branchAddress, String categoryID) {
        Category cat = getCategory(branchAddress, categoryID);
        if(!cat.getSubCatList().isEmpty())
            throw new IllegalArgumentException("cant remove category with sub cats");
        if(!cat.getItemIDList().isEmpty())
            throw new IllegalArgumentException("cant remove category with sub items");
        if(cat.getCatID().equals("0"))
            throw new IllegalArgumentException("cant remove root category");
        cat.getFatherCategory().deleteSubCat(cat.getCatID());
        DALFacade.getInstance().removeCategory(branchAddress, cat.getCatID());
    }
}
