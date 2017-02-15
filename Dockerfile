FROM anapsix/alpine-java:8
ADD classy-*-all.jar classy.jar
ENTRYPOINT ["java", "-jar", "classy.jar", "opt/jdk/jre/lib/rt.jar"]