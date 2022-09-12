package Presentation.View.EmployeeModuleView;

import Presentation.Model.BackendController;
import Presentation.View.View;

import java.util.List;

public class MessagesView implements View {
    private final String job;
    private final String branch;
    private final View returnView;

    public MessagesView(String job, String branch, View returnView) {
        this.job = job;
        this.branch = branch;
        this.returnView = returnView;
    }

    public MessagesView(String job, View returnView) {
        this.job = job;
        this.branch = null;
        this.returnView = returnView;
    }

    @Override
    public void printMenu() {
        if(branch == null) {
            List<String> messages = BackendController.getInstance().pullMessagesForJob(job);
            if (messages.size() !=0) {
                System.out.println();
                System.out.println("Here is your latest messages:");
                for (String str :messages) {
                    System.out.println(str);
                }
            }
            else
                System.out.println("There are no messages for you");
        }
        else {
            List<String> messages = BackendController.getInstance().pullMessages(branch, job);
            if (messages.size() !=0) {
                System.out.println();
                System.out.println("Here is your latest messages:");
                for (String str :messages) {
                    System.out.println(str);
                }
            }
            else
                System.out.println("There are no messages for you");
        }
        System.out.println("Press any key to go back.");
    }

    @Override
    public View nextInput(String input) {
        return returnView;
    }
}
