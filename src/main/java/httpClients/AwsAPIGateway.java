package httpClients;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


public class AwsAPIGateway extends Service<String> {

    private static final String TARGET_URL = "https://kvyi4lwa5b.execute-api.eu-west-1.amazonaws.com/GenAI/micUML";

    private String payload, apiKey, accessKey, secretAccessKey;

    public void setSourceCodeRequest(String payload, String apiKey, String accessKey, String secretAccessKey) {

    	this.apiKey = apiKey;

    	this.accessKey = accessKey;

    	this.secretAccessKey = secretAccessKey;

    	this.payload = payload;
    }


    @Override
    protected Task<String> createTask() {

        return new Task<String>() {

            @Override
            protected String call() throws Exception { return fetchData(0); }

            private String fetchData(int attempt) throws Exception {

                if(isCancelled()) return null;

                if(attempt > 2) throw new Exception("Tried twice without success to get a response from this URL: " + TARGET_URL);

                System.out.println("payload = " + payload);

                System.out.println("apiKey = " + apiKey);

                System.out.println("accessKey = " + accessKey);

                System.out.println("secretAccessKey = " + secretAccessKey);

                return AwsHttpPost.execute(TARGET_URL, payload, apiKey, accessKey, secretAccessKey);
            }
        };
    }
}