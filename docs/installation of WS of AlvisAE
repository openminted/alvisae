# Installation of a new instance of AlvisAE web services

{{toc}}

## Install Maven

The easiest way is to get it through your own package manager. Our current development version is 2.2.1, and can be obtained "here":http://www.apache.org/dyn/closer.cgi/maven/binaries/apache-maven-2.2.1-bin.tar.gz (manual installation).

## Get the source code

<pre>
svn co https://genome.jouy.inra.fr/svn_cadixe/cdxws/trunk/lift-prototype cdxws
</pre>

## Set-up database parameters

The Configuration of the database connection can be either done via parameters of the web container (allowing to deploy the same generic package for any AlvisAE instance) or by a property file embedded within the war package (hence requiring to generate a new package for each new AlvisAE instance).

* Check [[WSConfig|how to configure the WebService]] using web container params

or alternatively 

* use a property file
** create a file in the @cdxws/src/main/resources/props/@ directory
the file name should respect the following format @<user>.<hostname>.props@ (e.g. on the host bibweb, the Glassfish server is executed under the glassfish user, hence the property file name @glassfish.bibweb.props@)
** add the correct database parameters in the property file:
<pre>
db.type=postgresql
db.server=bddev
db.port=5432
db.dbname=annotation
db.username=annotation_admin
db.password=*****
db.schema=aae_newinstance
</pre>
** save the file.

## Prepare deployable package


(!) Since this project includes an in-project repository for classes shared with AlvisAE UI, it might be necessary _before compilation_ to reset cached version of the corresponding artefact (AlvisAE.shared-xx.jar) living in your local Maven repository (because during development phase, the artefact version may not be systematically increased).
Usually, Maven repository lives in your home directory, so you can use the following command (Maven repository will be refreshed with newer version comming from svn)
<pre>
rm -fr ~/.m2/repository/fr/inra/
</pre>

Then, use following commands to generated deployable package: 
<pre>
cd cdxws
mvn package
</pre>

The exact name of the package built by Maven is indicated within the 10 last lines of Maven output :
<pre>
...
[INFO] Building war: /home/dev/cdxws/target/cdxws-lift-1.0-SNAPSHOT.war
...
</pre>

## Deploy

copy the generated package to a directory accessible from the GlassFish server

<pre>
cp target/cdxws-lift-1.0-SNAPSHOT.war /tmp/
</pre>

login as a user that is authorized to deploy packages on the Glassfish server
<pre>
su glassfish
cd
glassfishv3/bin/asadmin  -p 5848 deploy --force  --contextroot <context root of the instance> --name <name of the instance> /tmp/cdxws-lift-1.0-SNAPSHOT.war
</pre>


## Exemples of deployment on bibDEV/bibWEB :

<pre>

ssh glassfish@bibdev

/usr/local/glassfish/current/bin/asadmin  deploy --port 5848 --force  --contextroot '/alvisae/NEWINSTANCE' --name 'cdxws-alvisae-NEWINSTANCE' /tmp/cdxws-lift-1.0-SNAPSHOT.war
</pre>


