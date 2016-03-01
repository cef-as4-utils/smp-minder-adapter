package minder.smp;

import gov.tubitak.minder.client.MinderClient;
import gov.tubitak.minder.client.MinderClientApp;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by melis on 17/08/15.
 */
public class SMPAdapterStartup {
    static final Logger LOGGER = Logger.getLogger(SMPAdapterStartup.class.getName());

    public static SMPAdapter wrapper;

    public static void main(String[] args) throws Exception {
        LOGGER.info("Initializing the wrapper");
        MinderClientApp.main(args);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println("Input command " + line);
            if ("quit".equalsIgnoreCase(line)) {
                System.exit(0);
            }
        }
    }


}
