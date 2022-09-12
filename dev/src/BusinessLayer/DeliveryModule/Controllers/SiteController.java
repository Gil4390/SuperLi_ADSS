package BusinessLayer.DeliveryModule.Controllers;

import BusinessLayer.DeliveryModule.Objects.Site;
import DataAccessLayer.DeliveryModuleDal.DControllers.SiteMapper;

import java.util.LinkedList;
import java.util.List;

public class SiteController {

    private final SiteMapper mapper;

    public SiteController(SiteMapper mapper){
        this.mapper = mapper;
        //loadData();
    }

    private Site getSite(String address) throws Exception {
        mapper.checkSiteExist(address);
        return mapper.getSite(address);
    }

    public void checkShippingAreaValidity(List<String> siteAddresses) throws Exception {
        List<String> ShippingAreas = new LinkedList<>();
        for (String siteAddress: siteAddresses) {
            String ShippingArea = getSite(siteAddress).getShippingArea();
            if (!ShippingAreas.contains(ShippingArea)) ShippingAreas.add(ShippingArea);
        }
        if(ShippingAreas.size()>1) {
            throw new Exception("the delivery contains multiple shipping areas");
        }
    }

    public String getContactNumber(String address) throws Exception {
        return getSite(address).getContactNumber();
    }

    public String getContactName(String address) throws Exception {
        return getSite(address).getContactName();
    }



}
