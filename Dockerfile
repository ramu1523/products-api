FROM frolvlad/alpine-oraclejdk8:slim
ADD build/libs/products-api-0.0.1.jar  products-api.jar
RUN sh -c 'touch /products-api.jar'
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/products-api.jar"]
