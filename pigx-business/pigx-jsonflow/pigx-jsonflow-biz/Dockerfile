FROM pig4cloud/java:8-jre

MAINTAINER 766488893@qq.com

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /pigx-jsonflow

WORKDIR /pigx-jsonflow

EXPOSE 7010

ADD ./target/pigx-jsonflow-biz.jar ./

CMD sleep 180;java $JAVA_OPTS -jar pigx-jsonflow-biz.jar
