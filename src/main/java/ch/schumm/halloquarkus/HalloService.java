package ch.schumm.halloquarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.reactive.messaging.annotations.Emitter;
import io.smallrye.reactive.messaging.annotations.Stream;

@ApplicationScoped
public class HalloService {

    @ConfigProperty(name = "kafka.bootstrap.servers")
    String bootstrapServers;

    @Inject
    @Stream("nachricht-bus") // Emit on the channel 'nachricht-bus'
    Emitter<String> nachrichtEmitter;




    //@Outgoing("nachricht")
    public String sagHallo(String name) {
        nachrichtEmitter.send(name); 
        return "neuste Nachricht: " + name;
    }

}