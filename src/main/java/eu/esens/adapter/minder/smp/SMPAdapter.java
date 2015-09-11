package eu.esens.adapter.minder.smp;

import minderengine.Slot;
import minderengine.Wrapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by melis on 17/08/15.
 */
public abstract class SMPAdapter extends Wrapper {
    static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SMPAdapter.class.getName());
    private final String sutName;

    /**
     * A dummy status flag
     */
    private boolean isRunning = false;

    private RestTemplate restTemplate;

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

            restTemplate = new RestTemplate();

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
     * @param identifierScheme
     * @param id
     * @param documentType
     */
    @Slot
    public HashMap<String, List<String>> getSignedServiceMetadata(String serverAddress,String identifierScheme, String id, String documentType) throws IOException {
        LOGGER.debug("getSignedServiceMetadata called");
        HashMap<String, List<String>> response;

        LOGGER.debug("getServiceGroup called");
        String theGetCommand = serverAddress + "/" + identifierScheme + "::"+id+"/services/"+documentType;
        System.out.println("The prepared GET Command:"+theGetCommand);

        ClientHttpRequest clientHttpRequest = restTemplate.getRequestFactory().createRequest(URI.create(theGetCommand), HttpMethod.GET);
        ClientHttpResponse clientHttpResponse = clientHttpRequest.execute();

        response = getResponseMessageAsHashMap(clientHttpResponse);


        return response;

    }

    /**
     * This method will be called in tests by Minder for receiving the service group
     * for a given business identifier.
     * Returns the participant identifier of the recipient, and a list of references to individual ServiceMetadata
     * resources that are associated with that participant identifier.
     *
     * @param identifierScheme
     * @param id
     */
    @Slot
    public HashMap<String, List<String>> getServiceGroup(String serverAddress,String identifierScheme, String id) throws IOException {
        HashMap<String, List<String>> response;

        LOGGER.debug("getServiceGroup called");
        String theGetCommand = serverAddress + "/" + identifierScheme + "::"+id;
        System.out.println("The prepared GET Command:"+theGetCommand);

        ClientHttpRequest clientHttpRequest = restTemplate.getRequestFactory().createRequest(URI.create(theGetCommand), HttpMethod.GET);
        ClientHttpResponse clientHttpResponse = clientHttpRequest.execute();

        response = getResponseMessageAsHashMap(clientHttpResponse);


        return response;

    }

    protected HashMap<String, List<String>> getResponseMessageAsHashMap(ClientHttpResponse clientHttpResponse) throws IOException {
        HashMap<String, List<String>> response = new HashMap<>();

        //Response Code
        System.out.println(clientHttpResponse.getRawStatusCode());
        List<String> statusCode = new ArrayList<>();
        statusCode.add(String.valueOf(clientHttpResponse.getRawStatusCode()));
        response.put(Constants.HTTP_MESSAGE_STATUS, statusCode);

        //Response Body
        List<String> body = new ArrayList<>();
        InputStream inputStream = clientHttpResponse.getBody();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        String xmlBodyString = "";
        while (null != (line = bufferedReader.readLine())) {
            xmlBodyString += line + "\n";
        }
        body.add(xmlBodyString);
        response.put(Constants.HTTP_MESSAGE_BODY, body);

        //Response Header
        for (String key : clientHttpResponse.getHeaders().keySet()) {
            List<String> values = clientHttpResponse.getHeaders().get(key);
            response.put(key, values);
            for (String value : values) {
                System.out.println("Key=" + key + " Value=" + value);
            }
        }
        return response;
    }


}
