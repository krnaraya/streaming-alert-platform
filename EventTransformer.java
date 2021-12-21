// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EventTransformer extends RouteBuilder {
    private String consumerMaxPollRecords = "50000";
    private String consumerGroup = "weathearEventGroup";
    private  String kafkaBootstrap = "my-cluster-kafka-brokers:9092";

    @Override
    public void configure() throws Exception {
       
        from("kafka:" + "weatherevents" + "?brokers=" + kafkaBootstrap + "&maxPollRecords="
                + consumerMaxPollRecords + "&seekTo=" + "beginning"
                + "&groupId=" + consumerGroup)
                .process(new weatherProcessor())
                .log("${body}")
                .to("kafka:"+"input-stream"+ "?brokers=" + kafkaBootstrap);

    }

    private final class weatherProcessor implements Processor {


        @Override
        public void process(Exchange exchange) throws Exception {
            System.out.println(exchange.getIn().getBody().toString());
            java.util.Map valueMap = new com.fasterxml.jackson.databind.ObjectMapper().readValue(exchange.getIn().getBody().toString(), java.util.HashMap.class);
            java.util.Map weatherDataMap = (java.util.HashMap) valueMap.get("data");

            String eventID = "100";
            String eventName = "Wind";
            String eventDate = (String) weatherDataMap.get("Date");
            String eventLocation = (String) weatherDataMap.get("Location");
            String windSpeed = (String) weatherDataMap.get("WindGustSpeed");
            String parameterName = "Wind Speed";

            System.out.println(eventDate+ " " + eventLocation + " "+ windSpeed+ " " + exchange.getIn().getHeader("kafka.KEY"));
            
            String responseString = "{ \"data\":{\"event_id\":"+eventID + ", \"event_name\":\""+eventName+"\",\"event_date\":\""+eventDate+"\","
            + "\"location\":\""+eventLocation+"\",\"parameter_name\":\""+ parameterName+"\", \"parameter_value\":\""+
            windSpeed+"\"}}";

            System.out.println(responseString);
            exchange.getIn().setBody(responseString);

        }
    }
}
