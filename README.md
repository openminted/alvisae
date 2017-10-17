# AlvisAE

AlvisAE is an editor that facilitates the annotation of text documents with the goal of extracting knowledge.

This project is the server side of alvisae. It is a web service that implements all the operations to manage campaigns, documents, annotations, curations, tasks, workflows and users. It assures the storage of document annotations as well as linguistic processing via [AlvisNLP](https://github.com/Bibliome/alvisnlp).

## How to install

### get the source code and package

```sh
git clone https://github.com/mandiayba/alvisae
cd alvisae
mvn compile package
```

### install the war on glassfish
We suppose that glassfish and postgresql are already installed


### deploy the war
copy the generated package to a directory accessible from the GlassFish server

```
cp target/cdxws-lift-1.0-SNAPSHOT.war /tmp/
```

login as a user that is authorized to deploy packages on the Glassfish server

```
su glassfish
cd
glassfishv3/bin/asadmin  -p 5848 deploy --force  --contextroot <context root of the instance> --name <name of the instance> /tmp/cdxws-lift-1.0-SNAPSHOT.war
```

### set-up database parameters
The content of property file look like this. Save the file with name of the following format <user>.<hostname>.props

```
db.type=postgresql
db.server=bddev
db.port=5432
db.dbname=annotation
db.username=annotation_admin
db.password=*****
db.schema=aae_newinstance
```

One simplest way to setup the parameters is by using these two glassfish commands :

```
GLASSFISH_HOME/bin/asadmin set-web-context-param --name configFilePath --value 'path/to/the/config/file' 'the_application_name'

GLASSFISH_HOME/bin/asadmin restart-domain
```


## How to use

// todo




/usr/local/glassfish/current/bin/asadmin set-web-context-param --name configFilePath --value '/usr/local/glassfish/WebAppConfig/AlvisAE-demo.props' 'AlvisAE-ws-demo'
