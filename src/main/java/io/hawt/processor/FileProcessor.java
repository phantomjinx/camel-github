package io.hawt.processor;

import java.io.File;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.hawt.Constants;

public class FileProcessor implements Processor, Constants {

    private static Object locked = new Object();
    
    private Gson gson;
    private String baseDir;

    private int delay;

    public FileProcessor(String baseDir, String delay) {
        System.out.println("File Processor Base Directory: " + baseDir);
        this.baseDir = baseDir;
        System.out.println("File Processor Delay: " + delay);
        this.delay = Integer.parseInt(delay);

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        String commitMsg = message.getBody(String.class);

        JsonObject json = new JsonObject();
        json.addProperty("message", commitMsg);

        String sha = "";
        Map<String, Object> headers = message.getHeaders();
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            if (entry.getKey().equals("CamelGitHubCommitSha")) {
                if (entry.getValue() != null)
                    sha = entry.getValue().toString();
                else
                    // Should not happen but just in case
                    sha = Long.toString(System.currentTimeMillis());
            }

            String id = entry.getKey();
            String value = entry.getValue() == null ? "" : entry.getValue().toString();
            
            json.addProperty(id, value);
        }

        /*
         * Active delay to slow down the collecting of commits / hour
         */
        synchronized(locked) {
            Thread.sleep(delay);
        }

        String filename = baseDir + File.separator + sha + ".json";
        exchange.getMessage().setHeader(Exchange.FILE_NAME, filename);
        exchange.getMessage().setBody(gson.toJson(json));
    }
}
