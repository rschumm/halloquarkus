package ch.schumm.halloquarkus.rx;

import io.smallrye.reactive.messaging.annotations.Stream;
import org.reactivestreams.Publisher;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A simple resource retrieving the in-memory "my-data-stream" and sending the items as server-sent events.
 */
@Path("/prices")
@RequestScoped
public class PriceResource {

    @Inject
    @Stream("converted-price") Publisher<Double> prices; 

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello price resource";
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)             
    public Publisher<Double> stream() {                 
        return prices;
    }
}