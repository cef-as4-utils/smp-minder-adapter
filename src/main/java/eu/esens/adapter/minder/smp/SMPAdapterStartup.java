package eu.esens.adapter.minder.smp;

import gov.tubitak.minder.client.MinderClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;

/**
 * Created by melis on 17/08/15.
 */
public class SMPAdapterStartup {
    static final Logger LOGGER = Logger.getLogger(SMPAdapterStartup.class.getName());

    public static SMPAdapter wrapper;

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure(SMPAdapterStartup.class.getClassLoader().getResource("logging.properties"));
        LOGGER.info("Initializing the wrapper");

        //initialize the wrapper.
        System.setProperty("propertyFile", "wrapper.properties");

        //Connects to MinderClient
        MinderClient minderClient = new MinderClient();
        wrapper = (SMPAdapter) minderClient.wrapper();

        //An easy trick to hang the main thread.
        JFrame frame = new JFrame("SMPAdapter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setSize(300,200);
        frame.setVisible(true);
    }


}
