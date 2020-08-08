FROM openjdk:13

ENV USER liquibase
ENV USER_HOME /home/liquibase
ENV USER_UID 109
ENV USER_GID 114

ENV TINI_VERSION v0.16.1

USER root

RUN mkdir -p ${USER_HOME} && \
 chown ${USER_UID}:${USER_GID} ${USER_HOME} && \
 groupadd -g ${USER_GID} --system ${USER} && \
 useradd --system --uid ${USER_UID} --home ${USER_HOME} --gid ${USER_GID} ${USER} && \
 curl -Lv https://github.com/krallin/tini/releases/download/${TINI_VERSION}/tini -o /tini && \
 chmod +x /tini

# Set timezone to CST
ENV TZ=America/Lima
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ARG LIQUIBASE_VERSION=4.0.0

ENV LIQUIBASE_HOME=/opt/liquibase
RUN mkdir -p ${LIQUIBASE_HOME}
WORKDIR ${LIQUIBASE_HOME}


# Download, install, clean up
RUN set -x \
 && curl -SkL https://github.com/liquibase/liquibase/releases/download/v${LIQUIBASE_VERSION}/liquibase-${LIQUIBASE_VERSION}.tar.gz | tar -xzf -

# RUN curl -o /liquibase/lib/<db platform>.jar <url to maven jar driver>
RUN curl -o ${LIQUIBASE_HOME}/lib/postgresql.jar https://repo1.maven.org/maven2/org/postgresql/postgresql/42.2.12/postgresql-42.2.12.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/mssql.jar https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/8.2.0.jre13/mssql-jdbc-8.2.0.jre13.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/mysql.jar https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.21/mysql-connector-java-8.0.21.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/mariadb.jar https://repo1.maven.org/maven2/org/mariadb/jdbc/mariadb-java-client/2.6.0/mariadb-java-client-2.6.0.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/h2.jar https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
# DB2 version 11.1
RUN curl -o ${LIQUIBASE_HOME}/lib/db2.jar https://repo1.maven.org/maven2/com/ibm/db2/jcc/11.1.4.4/jcc-11.1.4.4.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/snowflake.jar https://repo1.maven.org/maven2/net/snowflake/snowflake-jdbc/3.12.3/snowflake-jdbc-3.12.3.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/sybase.jar https://repo1.maven.org/maven2/net/sf/squirrel-sql/plugins/sybase/3.5.0/sybase-3.5.0.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/firebird.jar https://repo1.maven.org/maven2/net/sf/squirrel-sql/plugins/firebird/3.5.0/firebird-3.5.0.jar
RUN curl -o ${LIQUIBASE_HOME}/lib/sqlite.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.31.1/sqlite-jdbc-3.31.1.jar


RUN chown -R ${USER_UID}:${USER_GID} ${LIQUIBASE_HOME}
ENV PATH $PATH:${LIQUIBASE_HOME}

USER ${USER}
WORKDIR ${USER_HOME}

ENTRYPOINT ["/tini", "--"]
