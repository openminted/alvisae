FROM glassfish:4.0-jdk7

MAINTAINER Ba "mandiayba@gmail.com"

ENV PG_VERSION 9.4
ENV AAE_NAME alvisae-ws
ENV DB_NAME annotation
ENV DB_USER annotation_admin
ENV DB_PASS annotroot

# Setup gosu for easier command execution
RUN gpg --keyserver pool.sks-keyservers.net --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4 \
    && curl -o /usr/local/bin/gosu -SL "https://github.com/tianon/gosu/releases/download/1.2/gosu-amd64" \
    && curl -o /usr/local/bin/gosu.asc -SL "https://github.com/tianon/gosu/releases/download/1.2/gosu-amd64.asc" \
    && gpg --verify /usr/local/bin/gosu.asc \
    && rm /usr/local/bin/gosu.asc \
    && rm -r /root/.gnupg/ \
    && chmod +x /usr/local/bin/gosu

RUN apt-get update && \
    apt-get install -y postgresql-$PG_VERSION libpq-dev && \
    rm -rf /var/lib/apt/lists/* 


RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/$PG_VERSION/main/pg_hba.conf && \ 
    echo "listen_addresses='*'" >> /etc/postgresql/$PG_VERSION/main/postgresql.conf


COPY user.sql /
COPY dumpdb.sql /
COPY glassfish.localhost.props /


# Sets postgres password

RUN gosu postgres /etc/init.d/postgresql start && \
    gosu postgres psql -v DB_NAME=${DB_NAME} -v DB_PASS=${DB_PASS} -v DB_USER=${DB_USER} -f  /user.sql && \
    gosu postgres psql $DB_NAME < /dumpdb.sql




COPY alvisae-ws/target/cdxws-lift-1.0-SNAPSHOT.war /

COPY alvisae-ui/AlvisAE.GenericUI/AlvisAEGenericUI.war /

COPY start.sh /

RUN ["chmod", "+x", "/start.sh"]

EXPOSE 8080
EXPOSE 4848
EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

ENTRYPOINT ["/start.sh"]

