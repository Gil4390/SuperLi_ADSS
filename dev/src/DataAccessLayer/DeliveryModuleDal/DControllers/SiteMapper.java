package DataAccessLayer.DeliveryModuleDal.DControllers;

        import BusinessLayer.DeliveryModule.Objects.Site;
        import BusinessLayer.EmployeeModule.Objects.Branch;
        import BusinessLayer.EmployeeModule.Objects.Employee;
        import BusinessLayer.SupplierBusiness.Supplier;
        import DataAccessLayer.EmployeesModuleDal.Mappers.EmployeeMapper;
        import DataAccessLayer.SupplierDAL.SupplierMapper;
        import DataAccessLayer.EmployeesModuleDal.Mappers.BranchMapper;

        import java.util.*;

public class SiteMapper  {

    Map<String, Site> sites= new HashMap<>();

    private SupplierMapper supplierMapper = SupplierMapper.getInstance();
    private BranchMapper branchMapper= BranchMapper.getInstance();
    private EmployeeMapper employeeMapper= EmployeeMapper.getInstance();

    public SiteMapper()  { }

    public void checkSiteExist(String address) throws Exception {
        if (sites.containsKey(address)) return;
        if (branchMapper.branchExist(address)) return;
        if (supplierMapper.SupplierExists(address)) return;
        throw new Exception("site with address "+ address+" doesnt exists!");
    }

    public Site getSite(String address) throws Exception {
        if (sites.containsKey(address)) return sites.get(address);
        try{ Branch b= branchMapper.getBranch(address);
            Employee employee = employeeMapper.getEmployee(b.getBranchManagerId());
            sites.put(address, new Site(b.getBranchAddress(), employee.getNumber(), employee.getName(), b.getShippingArea()));
            return sites.get(address);}
        catch (Exception e){
            try {Supplier supplier= supplierMapper.getSupplierByAddress(address);
                sites.put(address, new Site(supplier.getAddress(), supplier.getAllContacts().get(0).getPhoneNumber(), supplier.getAllContacts().get(0).getName(), supplier.getArea()));
                return sites.get(address);}
            catch (Exception exception){ throw new Exception("coudnt fins site addressed "+ address); }
        }
    }

    public List<Site> selectAllSites() throws Exception {
        List<Site> sitesList = new LinkedList<>();
        for (Branch b : branchMapper.selectAllBranches()) {
            Employee employee = employeeMapper.getEmployee(b.getBranchManagerId());
            sitesList.add(new Site(b.getBranchAddress(), employee.getNumber(), employee.getName(), b.getShippingArea())); }
        for (Supplier supplier : supplierMapper.selectAllSuppliers())
            sitesList.add(new Site(supplier.getAddress(), supplier.getAllContacts().get(0).getPhoneNumber(), supplier.getAllContacts().get(0).getName(), supplier.getArea()));
        for (Site site: sitesList)
            if (!sites.containsKey(site.getAddress())) sites.put(site.getAddress(), site);
        return sitesList;
    }

}
