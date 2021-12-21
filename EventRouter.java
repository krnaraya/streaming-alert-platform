// camel-k: language=java

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class EventRouter extends RouteBuilder {
    private String consumerMaxPollRecords = "50000";
    private String consumerGroup = "routerEventGroup";
    private  String kafkaBootstrap = "my-cluster-kafka-brokers:9092";

    @Override
    public void configure() throws Exception {
       
        from("kafka:" + "input-stream" + "?brokers=" + kafkaBootstrap + "&maxPollRecords="
                + consumerMaxPollRecords + "&seekTo=" + "beginning"
                + "&groupId=" + consumerGroup)
                .log("${body}")
                .to("kafka:"+"alerts-stream"+ "?brokers=" + kafkaBootstrap);
    }
}