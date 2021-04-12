package lapr.project.ui;

import lapr.project.controller.NotifyCourierController;
import java.util.Scanner;
import lapr.project.controller.ManageDeliveryRunController;
import lapr.project.utils.Utils;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

            startFolderWatch();
            startDeliveryTimer();
            if (!checkVPNConnection()) {
                System.out.println("No VPN Connection");
            } else {
                int option;
                Scanner scan = new Scanner(System.in);
                do {
                    System.out.println("");
                    System.out.println("Pharma Deliveries App");
                    System.out.println("1 - Install Database");
                    System.out.println("2 - Console Output");
                    System.out.println("3 - Graphic Interface");
                    System.out.println("4 - Quit");
                    System.out.println("Select an option:");
                    option = Utils.convertStringToInt(scan.nextLine());

                    switch (option) {
                        case 1:
                            new InstallDatabase().start();
                            break;
                        case 2:
                            new ConsoleUI().start();
                            break;
                        case 3:
                            App.main(args);
                            break;
                        case 4:
                            break;
                    }
                } while (option!=4);
                
                scan.close();
                System.exit(0);

            }
        

    }

    /**
     * Checks if the user is using ISEP VPN. This VPN is required to access the
     * remote database.
     */
    public static boolean checkVPNConnection() {

        return true;
//        String iface = "";
//        try {
//            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
//                if (networkInterface.isUp()) {
//                    iface = networkInterface.getName();
//                }
//                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
//                    return true;
//                }
//            }
//            return false;
//        } catch (SocketException e1) {
//            e1.printStackTrace();
//            return false;
//        }
    }

    /**
     * Starts a thread that waits for inputFiles in the specified system
     * Directory (see Utils.Constants for Directory path)
     */
    public static void startFolderWatch() {
        NotifyCourierController notifyC = new NotifyCourierController();
        notifyC.startWatch();
    }
    
    public static void startDeliveryTimer(){
        try{
             ManageDeliveryRunController manageDeliveryRunController = new ManageDeliveryRunController();
        manageDeliveryRunController.startDeliveryRunTimer();
        }catch(Exception ex){
            
        }
       
    }

}
