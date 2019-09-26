FROM java:8-jdk-alpine
RUN apk add --no-cache bash
EXPOSE 8081
COPY ./Cold_Path-1.0.jar /usr/app/
WORKDIR /usr/app
ENV table = ${table}
RUN sh -c 'touch Cold_Path-1.0.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=default","-jar","Cold_Path-1.0.jar"]