package ServiceLayer.Objects.EmployeeObjects;

import BusinessLayer.EmployeeModule.Objects.Job;

import java.util.Vector;

public class FJob {
    private String title;
    private Vector<String> Certifications;

    public FJob(Job job) {
        this.title = job.getTitle();
        Certifications = job.getCertifications();
    }

    public String getTitle() {
        return title;
    }

    public Vector<String> getCertifications() {
        return Certifications;
    }
}
