FROM openjdk:11

WORKDIR /app

COPY ./src ./src
COPY ./postgresql-42.2.18.jar .

WORKDIR /app

RUN javac src/Main.java

EXPOSE 80

CMD ["java", "-classpath", "postgresql-42.2.18.jar:.", "src.Main"]
