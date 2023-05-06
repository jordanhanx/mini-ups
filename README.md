# Mini UPS
This GitHub repository contains the source code for a mini delivery system, developed as a course project. The system is designed to work in tandem with a mini Amazon project and facilitates seamless communication between the two using the lightweight Protocol Buffers. The robust backend was developed using SpringBoot, ensuring a high-performance, scalable, and easy-to-maintain architecture. And the user-friendly frontend was created with Thymeleaf, a modern server-side Java template engine that enables rapid development of web applications.

## Getting started
```
sudo docker-compose up --build
```
*UPS website port = **8080**

## Configure Ups(local)/Amazon/WorldSimulator/ host&ports
```
cd ./mini-ups/src/main/resources/
nano application.properties
```
