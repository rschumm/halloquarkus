Minimales Beispiel für eine [Quarkus](https://quarkus.io/) - Applikation in OpenShift mit PostgreSQL. 
(läuft auf jedem OpenShift Cluster)

# WIP (Work in Progress)

based on the example in [Quarkus Getting Started Guide](https://quarkus.io/guides/getting-started-guide).   

features: 
- Quarkus Application based on maven, CDI, resteasy, jsonb
- peristence with [Hibernate light panaché](https://quarkus.io/guides/hibernate-orm-panache-guide) backed by PostgreSQL 
- GUI based on twitter's Bootstrap



planned features: 
- proper templating 
- Database migrations with [flyway](https://quarkus.io/guides/flyway-guide)  
- Integrate the [Kafka Example](https://quarkus.io/guides/kafka-guide), if possible with [Strimzi](https://strimzi.io/). 
- some reactive stuff





# scaffolding: how the initial app was generated


    mvn io.quarkus:quarkus-maven-plugin:0.12.0:create \
    -DprojectGroupId=ch.schumm.demo \
    -DprojectArtifactId=halloquarkus\
    -DclassName="ch.schumm.demo.halloquarkus.GreetingResource" \
    -Dpath="/hallo"

# Configuration

on [Quarkus](https://quarkus.io/guides/application-configuration-guide), Config is done by [Microprofile Config](https://microprofile.io/project/eclipse/microprofile-config) and thus can be overwritten by System Variables. 


# local run commands

test (will run the Application and test it with [RestAssured](http://rest-assured.io/)) 

    mvn test

dev mode with hot reload: 

     mvn compile quarkus:dev


see [localhost](http://localhost:8080/hallo)   


# build a native application

    mvn verify -Pnative

this will build a native executable based on GraalVM in `target` for the local machine. (needs GraalVM installed [properly](https://quarkus.io/guides/building-native-image-guide.html))  
It will also run the native tests with `@SubstrateTest` derived from the "normal" tests.  

> this is the exicting part about Quarkus: the resulting executable is just 19 MB - with everthing it needs to run. And starts up in 6 ms:  

    2019-04-08 14:18:58,833 INFO  [io.quarkus] (main) Quarkus 0.12.0 started in 0.006s. Listening on: http://0.0.0.0:8080










# build a docker image 

running 

    mvn package -Pnative -Dnative-image.docker-build=true

will build the native executable within a docker container and also produce it for that platform (of the docker image.) 

then 

    docker build -f src/main/docker/Dockerfile.native -t schumm/halloquarkus .

will build the image, which you can run: 

    docker run -i --rm -p 8080:8080 schumm/halloquarkus


# local build on openshift 

this is a "local binary build" which will push the local built cloud native runner to OpenShift and build the docker there....  

## build cloud native runner locally  

    mvn verify -Pnative

## set up the build 

    oc new-build --binary --name=halloquarkus -l app=halloquarkus
    oc patch bc/halloquarkus -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.native"}}}'
    
start the first build... 

    oc start-build halloquarkus --from-dir=. --follow

... and push it to the image-stream on OpenShift. Then: 

    oc new-app --image-stream=halloquarkus:latest
    oc expose service halloquarkus

... will do the deployment and expose the app - et voilà - fertig. The resulting image is ca. 44 MB. 

## rebuild 

    mvn verify -Pnative
    oc start-build halloquarkus --from-dir=. --follow


# s2i build on openshift 

this is a ["s2i source build"](https://quarkus.io/guides/openshift-s2i-guide) where everything is build inside a builder pod on openshift.  
to keep things clean, first create a new project, then trigger the [builder image](https://quay.io/repository/quarkus/centos-quarkus-native-s2i?tab=tags) n.b.: the image must match the GraalVM Version that matches the Quarkus Version etc... 

    oc new-project quark-s2i
    oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:19.0.2~https://github.com/rschumm/halloquarkus.git --name=halloquarkus 
    optional: --source-secret='rs-gitlab'

then, again, to expose to a route: 

    oc expose service halloquarkus

...et voilà - fertig, noch schneller. This image is ca. 480 MB, because it contains the build tools. To fix that, use a chained build as stated in the ["tutorial"](https://quarkus.io/guides/openshift-s2i-guide)

nb: to override the config values in `application.properties` just set a Environment Value in the Deployment Config, e.g.: 

    quarkus.datasource.url -> jdbc:postgresql://postgresql:5432/holz?sslmode=disable




# Build and deploy to a VM on Jenkins using Ansible

This app can also be built and deployed in Jenkins, using Quarkus' builder image, and then deployed via ansible to a host where ansible has access to.  
For this, see the `Jenkinsfile` and my Ansible Playbook called (`deploy_webapp`) 

nb: to override the config values in `application.properties` just set a Environment Value in the host-system, e.g.: 

    quarkus.datasource.url -> jdbc:postgresql://postgresql:5432/holz?sslmode=disable

if a Java System Property is needed, use -Dquarkus.datasource.url..., if a Unix Env is needed, use export QUARKUS_DATASOURCE_URL=... respectively.


## build native application with s2i locally

run the builder image, mount the local source directory and run maven: 

    docker run -it  --entrypoint /bin/bash --rm -v /home/rschumm/git/halloquarkus:/project quay.io/quarkus/centos-quarkus-maven:19.0.2
    mvn clean verify -Pnative -DskipTests 

Documentation for the different builder images can be found on [GitHub here](https://github.com/quarkusio/quarkus-images).  

