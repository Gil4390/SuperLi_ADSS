package BusinessLayer.EmployeeModule.controllers;

import BusinessLayer.EmployeeModule.Objects.*;
import DataAccessLayer.EmployeesModuleDal.Mappers.DriversConstraintsMapper;
import DataAccessLayer.EmployeesModuleDal.Mappers.JobsCertificationsMapper;
import DataAccessLayer.EmployeesModuleDal.Mappers.MessageMapper;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JobController {
    private JobsCertificationsMapper jobsCertificationsMapper;
    private DriversConstraintsMapper driversConstraintsMapper;

    private MessageMapper messagesMapper;


    public JobController(JobsCertificationsMapper jobsCertificationsMapper, DriversConstraintsMapper driversConstraintsMapper , MessageMapper messageMapper){
        //jobs=new HashMap<>();
        this.jobsCertificationsMapper = jobsCertificationsMapper;
        this.driversConstraintsMapper = driversConstraintsMapper;
        this.messagesMapper = messageMapper;
    }


    public boolean hasJob(int EmployeeId,String jobTitle)throws Exception {
        List<JobCertifications> temp = jobsCertificationsMapper.getJobCertification(EmployeeId);
        for (JobCertifications djc : temp)
        {
            if (djc.getJobTitle().equals(jobTitle))
                return true;
        }
        return false;
    }

    public void addJobToEmployee(int employeeID, String newJob) throws Exception {
        if (hasJob(employeeID, newJob))
            throw new Exception("employee Id: "+employeeID+"already has this job");
        if (newJob.equals("shift manager"))
        {
            jobsCertificationsMapper.insert(employeeID, newJob,"team leader");
            jobsCertificationsMapper.insert(employeeID, newJob,"cancellation card");
        }
        else
            jobsCertificationsMapper.insert(employeeID, newJob,"not require certification");

    }

    public void addJobToEmployee(int employeeID, String newJob, Vector<String> certifications) throws Exception {
        if (hasJob(employeeID, newJob))
            throw new Exception("employee Id: "+employeeID+"already has this job");
        for (String cer : certifications)
            jobsCertificationsMapper.insert(employeeID, newJob,cer);


    }

    public void removeJobFromEmployee(int employeeID, String jobTitle) throws Exception {
        if (!hasJob(employeeID, jobTitle))
            throw new Exception("employee Id: "+employeeID+"doest have this job");
        else{
            jobsCertificationsMapper.deleteJobCertification(employeeID,jobTitle,"not require certification");
        }
    }
    public void removeEmployeeJob(int eID)throws Exception{
        for (JobCertifications djc:jobsCertificationsMapper.getJobCertification(eID))
            jobsCertificationsMapper.deleteJobCertification(djc.getEmployeeID());
    }

    public List<Integer> getAllDriversId() throws Exception {
        return jobsCertificationsMapper.getEmployeeWorksSpecificJob("driver");
    }
    public List<Integer> getDriversRelevant(List<Integer> driversId, int maxWeight, String date) throws Exception {
        List<Integer> ans= new LinkedList<>();
        for (Integer dId: getAllDriversId()){
            if (hasJob(dId, "driver")){
                DriverJob d = new DriverJob(dId , jobsCertificationsMapper.getCertification(dId,"driver"));
                d.reloadsAvailable(driversConstraintsMapper.getMustsOfDriver(dId));
                if(d.checkMaxTruckWeight(maxWeight) && d.isAvailable(date)){
                    ans.add(dId);
                }
            }
        }
        return ans;
    }

    public void validateDriver(String date, int driverId, int maxTruckWeight) throws Exception {
        if(!hasJob(driverId, "driver")) throw new Exception("the employee is not a driver!");
        DriverJob d = new DriverJob(driverId , jobsCertificationsMapper.getCertification(driverId,"driver"));
        d.reloadsAvailable(driversConstraintsMapper.getMustsOfDriver(driverId));
        if (!d.checkMaxTruckWeight(maxTruckWeight)) throw new Exception("the truck is too heavy for the driver");
        if (!d.isAvailable(date)) throw new Exception("the driver is not available at this date");
    }

    public boolean checkTruckForDriver(int driverId, int maxWeight) throws Exception {
        if(!hasJob(driverId, "driver")) throw new Exception("the employee is not a driver!");
        DriverJob d = new DriverJob(driverId , jobsCertificationsMapper.getCertification(driverId,"driver"));
        return d.checkMaxTruckWeight(maxWeight);

    }

    public void setDriverUnAvailable(int driverId, String date) throws Exception {
        if(!hasJob(driverId, "driver")) throw new Exception("the employee is not a driver!");
        DriverJob d = new DriverJob(driverId , jobsCertificationsMapper.getCertification(driverId,"driver"));
        d.setUnavailable(date);
        driversConstraintsMapper.insert(driverId,date);
    }

    public List<String> getDriverLicencesForDriver(int driverId) throws Exception {
        if (!hasJob(driverId, "driver")) throw new Exception("the employee "+driverId+" is not a driver");
        DriverJob d = new DriverJob(driverId , jobsCertificationsMapper.getCertification(driverId,"driver"));
        return d.getDriverLicencesForDriver();
    }

    public List<Integer> filterDriversByMaxWeight(List<Integer> availableDriversIds, int maxWeight) throws Exception {
        List<Integer> result = new LinkedList<>();
        DriverJob d;
        Map<String , List<String>> jobsCert = new ConcurrentHashMap<>();
        for (Integer id : availableDriversIds){
            for (JobCertifications j : jobsCertificationsMapper.getJobCertification(id)){
                if (!jobsCert.containsKey(j.getJobTitle()))
                    jobsCert.put(j.getJobTitle(),new LinkedList<>());
                jobsCert.get(j.getJobTitle()).add(j.getCertificationName());
            }
            for (String job : jobsCert.keySet()) {
                if (job.equals("driver")) {
                    d = new DriverJob(id, jobsCertificationsMapper.getCertification(id, "driver"));
                    d.reloadsAvailable(driversConstraintsMapper.getMustsOfDriver(id));
                    if ((d.checkMaxTruckWeight(maxWeight)))
                        result.add(id);
                }
            }

        }
        return result;
    }

    public List<Integer> getDriversToSpecificShift(String date) throws Exception {
        List<Integer> integers = new LinkedList<>();
        for (DriverMustWorkDate date1t:driversConstraintsMapper.selectAllDriversDates())
            if (date1t.getDate().equals(date))
                integers.add(date1t.getId());
        return integers;
    }

    private void validateEmployeeHasJob(int empID, String jobTitle) throws Exception {
        if(!hasJob(empID, jobTitle)){
            throw new Exception("employee doesnt have this job");
        }
    }

    public void addCertToEmployee(int empID, String jobTitle, String certName) throws Exception {
        validateEmployeeHasJob(empID, jobTitle);
        if(employeeHasJobCert(empID, jobTitle, certName)){
            throw new Exception("Employee already has this job Certification");
        }
        jobsCertificationsMapper.insert(empID, jobTitle, certName);
    }

    private boolean employeeHasJobCert(int empID, String jobTitle, String certName) throws Exception {
        for (JobCertifications certs : jobsCertificationsMapper.getJobCertification(empID)){
            if (certs.getCertificationName().equals(certName)){
                return true;
            }
        }
        return false;
    }

    public void deleteAllData() {
        jobsCertificationsMapper.deleteAllData();
        driversConstraintsMapper.deleteAllData();
    }

    public List<String> pullMessages(String branchAddress,String job) throws Exception {
        List<String> messages = new LinkedList<>();
        for (JobMessages m : messagesMapper.selectAllMessages())
            if (m.getJob().equals(job) && m.getBranch().equals(branchAddress))
                messages.add(m.getMessage());
        for (String mes : messages)
            messagesMapper.deleteMessage(branchAddress,job,mes);
        return messages;
    }

    public List<String> pullMessagesForJob(String job) throws Exception {
        List<String> messages = new LinkedList<>();
        for (JobMessages m : messagesMapper.selectAllMessages())
            if (m.getJob().equals(job) && !messages.contains(m.getMessage()))
                messages.add(m.getMessage());
        for (String mes : messages)
            messagesMapper.deleteMessageForJob(job,mes);
        return messages;
    }

    public void pushMessage(String branch, String job, String msg) {
        messagesMapper.insert(job,msg,branch);
    }
}
