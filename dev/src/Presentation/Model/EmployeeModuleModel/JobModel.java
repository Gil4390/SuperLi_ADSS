package Presentation.Model.EmployeeModuleModel;

import ServiceLayer.Objects.EmployeeObjects.FJob;

import java.util.Vector;

public class JobModel {
    private String title;
    private Vector<String> Certifications;

    public JobModel(FJob job) {
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
