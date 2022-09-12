package DataAccessLayer.EmployeesModuleDal.Mappers;

import DataAccessLayer.DeliveryModuleDal.DControllers.DalController;
import BusinessLayer.EmployeeModule.Objects.JobCertifications;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JobsCertificationsMapper extends DalController {

    private Map<Integer, List<JobCertifications>> dJobCertificationsMap;

    public JobsCertificationsMapper() {
        super("JobCertifications");
        dJobCertificationsMap = new ConcurrentHashMap<>();
    }

    public void load() throws Exception {
        for (JobCertifications dJobCertifications: selectAllJobCertifications()) {
            if (dJobCertificationsMap.get(dJobCertifications.getEmployeeID())== null)
            {
                dJobCertificationsMap.put(dJobCertifications.getEmployeeID(), new ArrayList<>());
            }
            dJobCertificationsMap.get(dJobCertifications.getEmployeeID()).add(dJobCertifications);
        }
    }

    public List<JobCertifications> selectAllJobCertifications() throws Exception {
        return (List<JobCertifications>)(List<?>)select();
    }
    public List<JobCertifications> getJobCertification(int employeeID)throws Exception {
        return ((List<JobCertifications>)(List<?>)selectList(employeeID,"EmployeeID"));
    }

    public Vector<String> getCertification(int employeeID , String job)throws Exception {
        Vector<String> temp = new Vector<>();
        for (JobCertifications jc : getJobCertification(employeeID))
        {
            if (jc.getJobTitle().equals(job))
                temp.add(jc.getCertificationName());
        }
        return temp;
    }

    public List<Integer> getEmployeeWorksSpecificJob(String job) throws Exception {
        List<Integer> employees = new LinkedList<>();
        for (JobCertifications djc : selectAllJobCertifications())
        {
            if (djc.getJobTitle().equals(job))
                employees.add(djc.getEmployeeID());
        }
        return employees;
    }


    public boolean insert (int id, String JobTitle ,String CertificationName) throws Exception {
        if (dJobCertificationsMap.size()==0) {
            dJobCertificationsMap.put(id, new LinkedList<>());
            for (JobCertifications jc : getJobCertification(id)) {
                dJobCertificationsMap.get(id).add(jc);
            }
        }
        JobCertifications dJobCertifications = new JobCertifications(id , JobTitle,CertificationName);
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?,?,?)",
                getTableName(),"EmployeeID","JobTitle", "CertificationName");
        try (Connection conn = super.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dJobCertifications.getEmployeeID());
            pstmt.setString(2, dJobCertifications.getJobTitle());
            pstmt.setString(3, dJobCertifications.getCertificationName());
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        if(!dJobCertificationsMap.containsKey(dJobCertifications.getEmployeeID())){
            dJobCertificationsMap.put(dJobCertifications.getEmployeeID(), new LinkedList<>());
            dJobCertificationsMap.get(dJobCertifications.getEmployeeID()).add(dJobCertifications);
        }
        dJobCertificationsMap.get(dJobCertifications.getEmployeeID()).add(dJobCertifications);
        return true;

    }
    public void deleteJobCertification(int id , String JobTitle , String CertificationName ) {
        checkDTOExists(id);
        int count=0;
        JobCertifications d=null;
        for ( JobCertifications dJobCertifications :dJobCertificationsMap.get(id) )
        {
            if (dJobCertifications.getJobTitle().equals(JobTitle) && dJobCertifications.getCertificationName().equals(CertificationName)) {
                delete(id, JobTitle, CertificationName, "EmployeeID", "JobTitle", "CertificationName");
                d= dJobCertifications;
            }
        }
        if (d!=null)
        {
            dJobCertificationsMap.get(id).remove(d);
        }
    }
    public void deleteJobCertification(int id) throws  Exception {
        if (dJobCertificationsMap.size()==0)
            dJobCertificationsMap.put(id,getJobCertification(id));
        checkDTOExists(id);
        for ( JobCertifications dJobCertifications :dJobCertificationsMap.get(id) )
        {
            delete(id,dJobCertifications.getJobTitle(),dJobCertifications.getCertificationName(), "EmployeeID","JobTitle","CertificationName");
        }
        dJobCertificationsMap.remove(id);
    }

    private void checkDTOExists(int id){
        if(!dJobCertificationsMap.containsKey(id))
            throw new IllegalArgumentException("Destination is not exists in the database!");
    }

    protected Object ConvertReaderToObject(ResultSet reader) {
        JobCertifications result = null;
        try {
            result = new JobCertifications(reader.getInt(1), reader.getString(2), reader.getString(3));
        }
        catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    @Override
    public void cleanCache() {
        Iterator<Map.Entry<Integer, List<JobCertifications>>> iter = dJobCertificationsMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer,List<JobCertifications>> entry = iter.next();
            for (JobCertifications e: entry.getValue()) {
                if(e.shouldCleanCache()){
                    System.out.println("Cleaning job certification of employee "+e.getEmployeeID() +" from cache!");
                    iter.remove();
                }
            }
        }
//        if (d!=null)
//        {
//            dJobCertificationsMap.get(id).remove(d);
//        }
    }
}
