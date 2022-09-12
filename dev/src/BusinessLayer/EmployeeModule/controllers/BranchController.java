package BusinessLayer.EmployeeModule.controllers;

import BusinessLayer.EmployeeModule.Objects.Branch;
import DataAccessLayer.EmployeesModuleDal.Mappers.BranchMapper;

import java.util.LinkedList;
import java.util.List;

public class BranchController {

    private final BranchMapper mapper;

    public BranchController(BranchMapper mapper){
        this.mapper = mapper;
    }

    public void addBranch(String address, int managerID, String shippingArea) throws Exception {
        mapper.insert(address, shippingArea, managerID);
    }

    private Branch getBranch(String address) throws Exception{
        Branch branch = mapper.getBranch(address);
        if (branch==null){
            throw new Exception("Branch does not exist");
        }
        return branch;
    }

    public String getShippingArea(String address) throws Exception {
        return getBranch(address).getShippingArea();
    }

    public int getBranchManagerId(String address) throws Exception {
        return getBranch(address).getBranchManagerId();
    }

    public List<String> getAllBranchesByAddress() throws Exception {
        List<Branch> branches =  mapper.selectAllBranches();
        List<String> branchesAdd = new LinkedList<>();
        for (Branch branch : branches){
            branchesAdd.add(branch.getBranchAddress());
        }
        return branchesAdd;
    }

    public List<String> getAddressOfHQ() throws Exception{
        List<String> HQ = new LinkedList<>();
        HQ.add(getAllBranchesByAddress().get(0));
        return HQ;
    }

    public String getBranchOfManage(int managerID) throws Exception {
        for (Branch b: mapper.selectAllBranches())
        {
            if (b.getBranchManagerId()==managerID)
                return b.getBranchAddress();
        }
        return "Not found";
    }

    public void removeBranch(String addressBranch) {
        mapper.deleteBranch(addressBranch);
    }

    public void deleteAllData() {
        mapper.deleteAllData();
    }
}
