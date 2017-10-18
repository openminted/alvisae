# Create and initialize a Database

## Prerequisite : Creation of the Database schema

with PostgreSQL's client:
```
psql -h bddev -U annotation_admin -d annotation
***** (enter password)
psql=> 
create schema aae_example;
```
to replace a previously existing schema, you must drop the schema using this command (Warning : can not be undone!!)
```
psql=> 

drop schema aae_example cascade;
```


## Working with Scala console


### startup the Scala console

* go to the Web Service's source base directory
* compile the source

```
mvn compile
```

* start the console 
```
mvn scala:console

scala> 
```

### Connect to the database
There is two alternative methods to connect to the database 

#### Method 1
Note : in this method, the properties file must be correctly configured to point to the right database engine.
> see main/resources/props/*.props

```scala 

import fr.inra.mig.cdxws.db._
CadixeDB.createSession().bindToCurrentThread;
```

#### Method 2
Note : in this method, you must provide the parameters to point to the right database engine.

```scala 

import fr.inra.mig.cdxws.db._
val schema = "aae_example"
CadixeDB.createPGSession("bddev", 5432, "annotation", schema, "annotation_admin", "****").bindToCurrentThread;
```


### Creation of the database objects within the schema

start the Scala console and connect to the database
```scala 

CadixeDB.create;
```


### Init of application data : Users

start the Scala console and connect to the database
```scala 

import fr.inra.mig.cdxws.db._

CadixeDB.createSession().bindToCurrentThread;

val user = User("claire","demo",false,true,"{}");

CadixeDB.users.insert(user)
```
