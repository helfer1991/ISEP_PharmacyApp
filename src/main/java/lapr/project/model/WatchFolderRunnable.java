package lapr.project.model;

import lapr.project.controller.NotifyCourierController;

import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import static lapr.project.utils.Constants.INPUT_FILES_DIRECTORY;


public class WatchFolderRunnable implements Runnable{

    /**
     * Instance of the controller for this use case
     */
    private NotifyCourierController notifyController;

    /**
     * Constructor
     * @param notifyController
     */
    public WatchFolderRunnable(NotifyCourierController notifyController) {
        this.notifyController = notifyController;
    }

    /**
     * Watches the INPUT_FILES_DIRECTORY for new files and retrieves the name of the new file.
     */
    @Override
    public void run() {


        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(INPUT_FILES_DIRECTORY);

            path.register( watchService, StandardWatchEventKinds.ENTRY_CREATE); //just on create or add no modify?

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {

                    String fileName = event.context().toString();
                    notifyController.processDetectedFile(fileName);

                }
                key.reset();
            }

        } catch (Exception e) {
            //Logger.getGlobal().setUseParentHandlers(false);
            //Logger.getGlobal().log(Level.SEVERE, e.toString(), e);

        }
    }


}
