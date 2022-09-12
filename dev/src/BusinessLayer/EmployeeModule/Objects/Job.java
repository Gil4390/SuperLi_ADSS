package BusinessLayer.EmployeeModule.Objects;

import BusinessLayer.DeliveryModule.Objects.TimeStampChecker;

import java.util.Vector;

public class Job extends TimeStampChecker {
    private String title;
    private Vector<String> Certifications;

    //we added deliveryManager, CEO
    //need to make sure there is only one CEO and he is the manager of HQ.
    enum Jobs{
        warehouse, cashier, shiftmanager, usher , driver, branchmanager, hr, acquisition, deliveryManager, CEO
    }
    public Job (String title)
    {
        validateTitle(title);
        this.title = title;
        this.Certifications = new Vector<>();
        if (title.equals("shift manager"))
            addShiftManagerCertifications();
    }

    public Job (String title, Vector<String> certifications)
    {
        validateTitle(title);
        this.title = title;
        this.Certifications = certifications;
        if (title.equals("shift manager"))
            addShiftManagerCertifications();
    }



    public String getTitle() {
        updateTimeStamp();
        return title;
    }

    protected void addCertificate(String cert){
        updateTimeStamp();
        Certifications.add(cert);
    }
    private void addShiftManagerCertifications()
    {
        updateTimeStamp();
        addCertificate("team leader");
        addCertificate("cancellation card");
    }

    private void validateTitle(String title){
        updateTimeStamp();
        title=title.toLowerCase();
        boolean found = false;
        if (title.contains(" "))
        {
            title = title.split(" ")[0]+title.split(" ")[1];
        }
        for (Jobs eJob : Jobs.values()){
            if(eJob.toString().equals(title)) found = true;
        }
        if(!found){
            throw new IllegalArgumentException("Job title not valid");
        }
    }
    @Override
    public String toString() {
        updateTimeStamp();
        return title;
    }

    public Vector<String> getCertifications() {
        updateTimeStamp();
        return Certifications;
    }
}
