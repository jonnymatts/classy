FROM anapsix/alpine-java:8
ADD classy-*-all.jar classy.jar
CMD ["java", "-jar", "classy.jar"]