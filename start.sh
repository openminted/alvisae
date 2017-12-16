#!/bin/sh
/etc/init.d/postgresql start

/usr/local/glassfish4/bin/asadmin start-domain

/usr/local/glassfish4/bin/asadmin -u admin deploy --contextroot /alvisae/$AAE_NAME/AlvisAE --name AlvisAEGenericUI /AlvisAEGenericUI.war

/usr/local/glassfish4/bin/asadmin -u admin deploy --contextroot /alvisae/$AAE_NAME --name cdxws-lift-1.0-SNAPSHOT /cdxws-lift-1.0-SNAPSHOT.war

/usr/local/glassfish4/bin/asadmin set-web-context-param --name configFilePath --value '/glassfish.localhost.props' 'cdxws-lift-1.0-SNAPSHOT'

/usr/local/glassfish4/bin/asadmin stop-domain

/usr/local/glassfish4/bin/asadmin start-domain --verbose
