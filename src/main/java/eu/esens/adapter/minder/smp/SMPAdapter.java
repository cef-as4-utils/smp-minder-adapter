package eu.esens.adapter.minder.smp;

import minderengine.Slot;
import minderengine.Wrapper;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * Created by melis on 17/08/15.
 */
public abstract class SMPAdapter extends Wrapper{
    static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SMPAdapter.class.getName());
    private final String sutName;
    private final String smpServerAddress;

    /**
     * A dummy status flag
     */
    private boolean isRunning = false;

    //Read wrapper specific properties from wrappper.properties file here
    public SMPAdapter() {
        Properties properties = new Properties();
        try {
            String res = System.getProperty("propertyResource", "wrapper.properties");
            URL url = this.getClass().getResource(res);

            if (url == null) {
                url = Thread.currentThread().getContextClassLoader().getResource(res);
            }
            if (url == null) {
                throw new IllegalArgumentException("Couldn't load wrapper properties resource [" + res + "]");
            }

            InputStream is = url.openStream();
            properties.load(is);
            is.close();

            sutName = properties.getProperty("SUT_NAME");
            smpServerAddress = properties.getProperty("SMP_SERVER_URL");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Called by the server when a test case that contains this wrapper is about
     * to be run. Perform any initialization here
     */
    @Override
    public void startTest() {
        finishTest();
        isRunning = true;
    }

    /**
     * Called by the server when a test case that conatins this wrapper is
     * finished. Perform your own resource deallocation here.
     */
    @Override
    public void finishTest() {
        isRunning = false;
    }

    @Override
    public String getSUTName() {
        return sutName;
    }

    //Define your signal and slots in here.
    /**
     * This method will be called in tests by Minder for receiving the service metadata
     * for a given business identifier, a service and docType
     * Returns all of the metadata about a Service, or a redirection URL to another Service Metadata Publisher holding
     * this information.
     *
     * @param businessIdentifier
     */
    @Slot
    public String getSignedServiceMetadata(String businessIdentifier, String serviceId, String docType) {
        //Please check and change the input parameters. With which parameter do you query ?
        //Determine the return variable. I just put and ordinary variable right now.
        String foundMetadata = null;
        LOGGER.debug("getSignedServiceMetadata called");
        //TODO Fill here

        //CALL The related service of SMP

        //Wait for the response from SMP

        //Get the response from SMP

        return foundMetadata;

    }

    /**
     * This method will be called in tests by Minder for receiving the service group
     * for a given business identifier.
     * Returns the participant identifier of the recipient, and a list of references to individual ServiceMetadata
     * resources that are associated with that participant identifier.
     * @param businessIdentifier
     */
    @Slot
    public List<String> getServiceGroup(String businessIdentifier) {
        //Please check and change the input parameters. With which parameter do you query ?
        //Determine the return variable. I just put and ordinary variable right now.
        List<String> foundServices = null;

        LOGGER.debug("getServiceGroup called");
        //TODO Fill Here

        //CALL The related service of SMP

        //Wait for the response from SMP

        //Get the response from SMP

        return foundServices;

    }


}
