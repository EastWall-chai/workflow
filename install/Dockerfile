FROM openjdk:8-jre

ARG jarname
ENV warname ${jarname}

RUN mkdir -p /opt/application/   &&   chmod -R 777  /opt/application/   

#拷贝包文件
COPY ${jarname}  /usr/local/
       
#添加权限
RUN chmod -R 777  /usr/local/

COPY simsun.ttf /usr/share/fonts/truetype/dejavu/

ENTRYPOINT java -jar -Dspring.config.location=/opt/application/application.yml /usr/local/${warname}
