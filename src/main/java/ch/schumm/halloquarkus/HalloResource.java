package ch.schumm.halloquarkus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public String halloWelt() {
        return "Hoi zäme uf dä ganze Wält!";
    }





}