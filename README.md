Minimales Beispiel für eine [Quarkus](https://quarkus.io/) - Applikation in OpenShift:
(läuft auf jedem OpenShift Cluster)

# WIP

based on the example in [Quarkus Getting Started Guide](https://quarkus.io/guides/getting-started-guide)


# setup 


    mvn io.quarkus:quarkus-maven-plugin:0.12.0:create \
    -DprojectGroupId=ch.schumm \
    -DprojectArtifactId=halloquarkus\
    -DclassName="ch.schumm.halloquarkus.GreetingResource" \
    -Dpath="/hallo"




# run

test (will run the Application and test it with [RestAssured](http://rest-assured.io/)) 

    mvn test

dev mode with hot reload: 

     mvn compile quarkus:dev
    

see [localhost](http://localhost:8080/hallo)   

