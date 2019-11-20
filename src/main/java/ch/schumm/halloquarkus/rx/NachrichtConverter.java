package ch.schumm.halloquarkus.rx;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

/**
 * A bean consuming data from the "prices" Kafka topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class NachrichtConverter {


    @Incoming("nachricht")                                 
    @Outgoing("nachricht-bearbeitet")                         
    @Broadcast                                          
    public String process(String nachricht) {
        return "k: " + nachricht;
    }

}