FROM openjdk:11

WORKDIR /app/src

COPY ./src .

WORKDIR /app

RUN javac src/Main.java

EXPOSE 80

CMD ["java", "-classpath postgresql-42.2.18.jar", "src.Main"]
