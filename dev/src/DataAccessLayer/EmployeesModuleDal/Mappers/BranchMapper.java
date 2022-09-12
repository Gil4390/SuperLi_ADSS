package DataAccessLayer.EmployeesModuleDal.Mappers;

import BusinessLayer.EmployeeModule.Objects.Branch;
import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BranchMapper extends DalController {

    private Map<String, Branch> BranchesMapper;

    private static class DBranchesMapper {
        private static BranchMapper instance = new BranchMapper();
    }

    public static BranchMapper getInstance() {
        return BranchMapper.DBranchesMapper.instance;
    }

    private BranchMapper() {
        super("Branches");
        BranchesMapper = new ConcurrentHashMap<>();

    }

    public void load() throws Exception {
        for (Branch dBranch : selectAllBranches())
            BranchesMapper.put(dBranch.getBranchAddress(), dBranch);
    }

    public Branch getBranch(String address) throws Exception {
        if (BranchesMapper.get(address) == null) {
            if( select(address, "address") == null)
                throw new Exception("the site doesnt exists");
            BranchesMapper.put(address, (Branch) select(address, "address"));
        }
        return BranchesMapper.get(address);
    }


    public boolean branchExist(String address) {
        try {
            getBranch(address);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Branch> selectAllBranches() throws Exception {
        return (List<Branch>) (List<?>) select();
    }

    public boolean insert(String address, String shippingArea, int managerID) {
        Branch dBranch = new Branch(address, managerID, shippingArea);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(), "Address", "ShippingArea", "ManagerID");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dBranch.getBranchAddress());
            pstmt.setString(2, dBranch.getShippingArea());
            pstmt.setInt(3, dBranch.getBranchManagerId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        BranchesMapper.put(address, dBranch);
        return true;

    }

    public void deleteBranch(String address) {
        checkDTOExists(address);
        Branch dBranch = BranchesMapper.get(address);
        delete(dBranch.getBranchAddress(), "Address");
        BranchesMapper.remove(address);
    }

    private void checkDTOExists(String address) {
        if (!BranchesMapper.containsKey(address))
            throw new IllegalArgumentException("Destination is not exists in the database!");
    }


    protected Branch ConvertReaderToObject(ResultSet reader) {
        Branch result = null;
        try {
            result = new Branch(reader.getString(1), reader.getInt(3), reader.getString(2));
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<String, Branch>> iter = BranchesMapper.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Branch> entry = iter.next();
            if (entry.getValue().shouldCleanCache()) {
                System.out.println("Cleaning branch " + entry.getValue().getBranchAddress() + " from cache!");
                iter.remove();
            }
        }
    }

    @Override
    public void deleteAllData() {
        super.deleteAllData();
        BranchesMapper = new HashMap<>();
    }
}
