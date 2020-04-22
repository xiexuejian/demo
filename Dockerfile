FROM ansible/centos7-ansible
WORKDIR /root
COPY ./target/sample.jar /root
RUN cd /root
EXPOSE 8080
CMD ["java","-jar","sample.jar","&"]