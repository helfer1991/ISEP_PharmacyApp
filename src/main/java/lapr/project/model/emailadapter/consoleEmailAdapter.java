package lapr.project.model.emailAdapter;

import lapr.project.model.ServiceEmail;

public class consoleEmailAdapter implements ServiceEmail {


    @Override
    public boolean sendEmail(String destinationEmail, String emailSubjectLine, String emailContent) {
        System.out.println("Email to: "+destinationEmail);
        System.out.println("Subject: "+emailSubjectLine);
        System.out.println("Content: \n"+emailContent);
        System.out.println();
        return true;
    }
}
