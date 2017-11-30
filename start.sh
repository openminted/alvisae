#!/bin/sh
/etc/init.d/postgresql start

/usr/local/glassfish4/bin/asadmin start-domain
/usr/local/glassfish4/bin/asadmin -u admin deploy /$AAE_NAME.war
/usr/local/glassfish4/bin/asadmin set-web-context-param --name configFilePath --value '/glassfish.localhost.props' $AAE_NAME
/usr/local/glassfish4/bin/asadmin stop-domain
/usr/local/glassfish4/bin/asadmin start-domain --verbose
