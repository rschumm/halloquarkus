package ch.schumm.halloquarkus;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ch.schumm.halloquarkus.model.Message;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import org.reactivestreams.Publisher;
import io.smallrye.reactive.messaging.annotations.Stream;


@Path("/hallo")
public class HalloResource {

    @Inject
    HalloService service;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/sali/{name}")
    public String sali(@PathParam("name") String name) {
        return service.sagHallo(name);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/sali")
    public String saliParam(@QueryParam("textinput") String name) {
        return service.sagHallo(name);
    }

    @POST
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/sali")
    public String saliPost(@FormParam("textinput") String name) {
        Message message = new Message();
        message.text = name;
        message.date = LocalDate.now();
        message.persist();
        return service.sagHallo(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/logbuch")
    public List<Message> logbuch() {
        PanacheQuery<Message> messages = Message.findAll(Sort.by("id").descending()); 
        // make it use pages of n entries at a time
        messages.page(Page.ofSize(6));

        // get the first page
        return messages.list();

        //return Message.listAll(Sort.by("id").descending()); 
    }


    @Inject
    @Stream("nachricht-bearbeitet") Publisher<String> nachricht; 


    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)             
    public Publisher<String> stream() {                 
        return nachricht;
    }




    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String halloWelt() {
        return "Hoi zäme uf dä ganze Wält!";
    }





}