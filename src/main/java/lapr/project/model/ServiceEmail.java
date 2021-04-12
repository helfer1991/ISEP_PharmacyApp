package lapr.project.model;
/**
 * Interface designed as an API for an ServiceEmail
 */
public interface ServiceEmail {
    /**
     * Method that receives an email address and email content and sends the respective email.
     * @param destinationEmail String containing the destination email address
     * @param emailSubjectLine String containing the subject line of the email
     * @param emailContent String content of the email
     * @return boolean true if sent correctly
     *          false if the email was not sent correctly
     */
    public boolean sendEmail(String destinationEmail,String emailSubjectLine,  String emailContent);
}
