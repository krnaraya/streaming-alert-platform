// camel-k: language=java dependency=camel-aws2-sns

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.aws2.sns.*;

public class EventNotifier extends RouteBuilder {

    private String consumerMaxPollRecords = "50000";
    private String consumerGroup = "weathearEventGroup";
    private  String kafkaBootstrap = "my-cluster-kafka-brokers:9092";
    private String awsAccessKey = "";
    private String awsSecretKey = "";

    @Override
    public void configure() throws Exception {

       
        from("kafka:" + "alerts-stream" + "?brokers=" + kafkaBootstrap + "&maxPollRecords="
                + consumerMaxPollRecords + "&seekTo=" + "beginning"
                + "&groupId=" + consumerGroup)
                .process(new notificationProcessor())
                .log("${body}")
                .to("aws2-sns://arn:aws:sns:ap-southeast-2:239048922879:notifyuser?accessKey=" + awsAccessKey + "&secretKey=" + awsSecretKey);

    }

    private final class notificationProcessor implements Processor {


        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println(exchange.getIn().getBody().toString());
            java.util.Map valueMap = new com.fasterxml.jackson.databind.ObjectMapper().readValue(exchange.getIn().getBody().toString(), java.util.HashMap.class);
            //java.util.Map eventDataMap = (java.util.HashMap) valueMap.get("data");

            String eventName = (String) valueMap.get("event_name");;
            //String eventID = (String) eventDataMap.get("event_id");
            String eventDate = (String) valueMap.get("event_date");
            String eventLocation = (String) valueMap.get("location");
            Double paramValue = (Double) valueMap.get("parameter_value");
            String parameterValue = Double.toString(paramValue);
            String parameterName = (String) valueMap.get("parameter_name");;

            System.out.println(eventDate+ " " + eventLocation + " "+ parameterValue + " " + exchange.getIn().getHeader("kafka.KEY"));
            
            String responseString = "Hello, The " + parameterName + " is expected to be " + parameterValue + " on " + eventDate + " at " + eventLocation;

            System.out.println(responseString);
            exchange.getIn().setBody(responseString);

        }
    }
    
}
