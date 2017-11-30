FROM glassfish:4.0-jdk7

MAINTAINER Ba "mandiayba@gmail.com"

ENV PG_VERSION 9.4
ENV AAE_NAME alvisae-ws
ENV DB_NAME annotation
ENV DB_USER annotation_admin
ENV DB_PASS annotroot;84


COPY w.service/target/cdxws-lift-1.0-SNAPSHOT.war /
COPY start.sh /
COPY dumpdb.sql /
COPY glassfish.localhost.props /

RUN ["chmod", "+x", "/start.sh"]

RUN mv /cdxws-lift-1.0-SNAPSHOT.war /$AAE_NAME.war

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


# Sets postgres password
RUN gosu postgres /etc/init.d/postgresql start && \
    gosu postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres'";

RUN gosu postgres /etc/init.d/postgresql start && \
    gosu postgres psql -c "CREATE ROLE $DB_USER WITH LOGIN ENCRYPTED PASSWORD '${DB_PASS}' CREATEDB"; 

RUN gosu postgres /etc/init.d/postgresql start && \
    gosu postgres psql -c "CREATE DATABASE $DB_NAME WITH OWNER $DB_USER TEMPLATE template0 ENCODING 'UTF8'";

RUN gosu postgres /etc/init.d/postgresql start && \
    gosu postgres psql -c "GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER";

RUN gosu postgres /etc/init.d/postgresql start && \
    gosu postgres psql $DB_NAME < /dumpdb.sql;


EXPOSE 8080
EXPOSE 4848
EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

ENTRYPOINT ["/start.sh"]

