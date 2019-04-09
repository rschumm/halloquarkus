Minimales Beispiel für eine [Quarkus](https://quarkus.io/) - Applikation in OpenShift:
(läuft auf jedem OpenShift Cluster)

# WIP (Work in Progress)

based on the example in [Quarkus Getting Started Guide](https://quarkus.io/guides/getting-started-guide).   
featured in [Rémy blod ingénieur](https://www.schumm.ch/blog.html)   

planned features: 
- proper templating 
- peristence with [Hibernate](https://quarkus.io/guides/hibernate-orm-guide) or [Hibernate light panaché](https://quarkus.io/guides/hibernate-orm-panache-guide)
- Database migrations with [flyway](https://quarkus.io/guides/flyway-guide)  
- some reactive stuff





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


# build a native application

    mvn verify -Pnative

this will build a native executable based on GraalVM in `target` for the local machine. (needs GraalVM installed [properly](https://quarkus.io/guides/building-native-image-guide.html))  
It will also run the native tests with `@SubstrateTest` derived from the "normal" tests.  

> this is the exicting part about Quarkus: the resulting executable is just 19 MB - with everthing it needs to run. And starts up in 6 ms:  

    2019-04-08 14:18:58,833 INFO  [io.quarkus] (main) Quarkus 0.12.0 started in 0.006s. Listening on: http://0.0.0.0:8080





# build a docker image 

running 

    mvn package -Pnative -Dnative-image.docker-build=true

will build the native executable within a docker container and also produce it for that platform.   

then 

    docker build -f src/main/docker/Dockerfile.native -t schumm/halloquarkus .

will build the image, which you can run: 

    docker run -i --rm -p 8080:8080 schumm/halloquarkus


# local build on openshift 

this is a "local binary build" which will push the local workspace to OpenShift and build the docker there.    

    oc new-build --binary --name=halloquarkus -l app=halloquarkus
    oc patch bc/halloquarkus -p '{"spec":{"strategy":{"dockerStrategy":{"dockerfilePath":"src/main/docker/Dockerfile.native"}}}}'
    oc start-build halloquarkus --from-dir=. --follow

this will build the docker image and push it to the image-stream on OpenShift. Then: 

    oc new-app --image-stream=halloquarkus:latest
    oc expose service halloquarkus

...et voilà - fertig. The resulting image is ca. 44 MB. 


# s2i build on openshift 

this is a ["s2i source build"](https://quarkus.io/guides/openshift-s2i-guide) where everything is build inside a builder pod on openshift.  
to keep things clean, first create a new project, then trigger the builder image etc... 

    oc new-project quark-s2i
    oc new-app quay.io/quarkus/centos-quarkus-native-s2i~https://github.com/rschumm/halloquarkus.git --name=halloquarkus

then, again, to expose to a route: 

    oc expose service halloquarkus

...et voilà - fertig, noch schneller. Strangely, this image is ca. 480 MB.... no idea why. 





