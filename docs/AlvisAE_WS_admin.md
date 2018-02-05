{{toc}}

# Creating Campaign, adding Documents and importing Annotations with the Command Line Interface

## Prerequisite

the 3 first steps of [[Installation]] must be completed
* Install Maven
* Get the source code
* Set-up database parameters in a property file (named @dbparam.props@ hereafter)

## Build the CLI program

### go to svn local working copy

<pre>
cd cdxws/
</pre>

h3. generate the CLI jar

<pre>
mvn clean compile assembly:single
</pre>

the jar @AlvisAE-cli.jar@ will be generated in the @target@ subdirectory 

## modus operandi

the command line interface program provides several distinct commands:
* *@create-user@* 
 create a new user in the AlvisAE DataBase
* *@create-campaign@* 
 create a new campaign in the AlvisAE DataBase, with its Annotation schema and Workflow definition
* *@add-documents@* 
 add documents to the AlvisAE DataBase, including their optional HTML formattings
* *@import-annotations@*
 import annotations related to an already added documents and associate them to a (user, task, campaign)
* *@assign-document@* 
 assign a document to a specific campaign (required when no annotation has been imported for this doc)
* *@export-campaign@* 
 export the documents and annotations of the specified campaign as a zip archive

All of these commands require a mandatory parameter (@dbparam.props@) which specifies the AlvisAE Database to work with. 

### Creating a new user

Will create a new user with the specified name and password

+command:+
* *@--create-user@*

+parameters:+
* *@-p@* properties file containing connection parameters
* *@--userName@* user name
* *@--password@* user's password

+standard ouput:+
* @userInternalId@ \t @"userName"@

+example:+
<pre>
 java -jar target/AlvisAE-cli.jar --create-user -p dbparam.props --userName foo --password bar

...
4	"foo"
</pre>

### Creating a new campaign

Will create a new campaign with the specified name, Annotation schema and Workflow definition

+command:+
* *@--create-campaign@*

+parameters:+
* *@-p@* properties file containing connection parameters
* *@-c@* campaign name
* *@-s@* JSON schema definition file
* *@-w@*  %{background:lightgreen}[optional]% XML workflow definition file
* *@--guidelines@*  %{background:lightgreen}[optional]% URL to the annotation guidelines of the campaign

+standard ouput:+
* @campaignInternalId@ \t @"campaignName"@

+example:+
<pre>
 java -jar target/AlvisAE-cli.jar --create-campaign -p dbparam.props -c "New Campaign #1" -s embedded_schema.json -w workflow.xml

...
14	"New Campaign #1"
</pre>


### Adding new document(s) to AlvisAE DB

Will add all documents present in the specified directory (files with .json extension).

*Note*: An optional unique document identifier may be set at the property key @DocumentID@ in the JSON file (makes the following import-annotations step easier).

*Note*: If present, @--add-documents@ will store the first annotation set with type @HtmlAnnotation@ as the HTML layout of the document.

+command:+
* *@--add-documents@*

+parameters:+
* *@-p@* properties file containing connection parameters
* *@-d@* documents input directory

+standard ouput (one line per added doc):+
* @documentInternalId@ \t @"importedDocumentPath"@ \t @"optionalDocumentExternalId"@

+example:+
<pre>
 java -jar target/AlvisAE-cli.jar --add-documents  -p dbparam.props -d /tmp/json

...
2014	"/tmp/json/BTID-20052.json"	"BTID20052"
2015	"/tmp/json/BTID-60597.json"
</pre>

### Importing Annotations into AlvisAE DB (one AnnotationSet at a time)

Will import the Annotation Set present in the specified JSON file, and associate it to the specified document, user, task and campaign
(as side effects, the document and the user will be associated to the campaign, the document will be assigned to the user)

+command:+
* *@--import-annotations@*

+parameters:+
* *@-p@* properties file containing connection parameters
* *@--annotationSetFile@* JSON file containing the Annotation Set to be imported
* *@--docInternalId@* internal Id of Document corresponding to the imported Annotation Set
or,  *@--docExternalId@*  external id of the Document (as specified during document import)
* *@-c@* campaign name
or,  *@--campaignId@*  internal id of the Campaign where to import the Annotation Set
* *@--taskName@* name of the Task where to import the Annotation Set
* *@--userName@* name of the User to be associated with the imported Annotation Set
or, *@--userId@* internal id of the User to be associated with the imported Annotation Set
* *@--annotationSetId@* %{background:lightgreen}[optional]% id of the AnnotationSet (in the JSON file) to import, by default the first of type UserAnnotationSet will be imported

+standard ouput:+
_na_

+example:+
<pre>
 java -jar target/AlvisAE-cli.jar --import-annotations  -p dbparam.props --annotationSetFile /tmp/json/BTID-20052.json --docInternalId 2014 -c "New Campaign #1" --userName foo --taskName "default-task"

</pre>






### Importing Annotations into AlvisAE DB (bulk import)

Will import, in the specified campaign, all user's Annotation Sets contained in the json files present in the specified directory.
The corresponding documents (identified by their external ids) must have already been imported in the database.
Only Annotation sets corresponding to actual user & task (identified by their internal ids, or translated thanks to @taskList@ and @userList@) will be imported
(as side effects, the document and the user will be associated to the campaign, the document will be assigned to the user)

+command:+
* *@--import-annotations@*

+parameters:+
* *@-p@* properties file containing connection parameters
* *@-c@* campaign name
or,  *@--campaignId@*  internal id of the Campaign where to import the Annotation Set
* *@-d@* input directory

* *@--taskList@* %{background:lightgreen}[optional]% CSV file containing the map of task ids and name (if none specified, the internal id are reused)
* *@--userList@* %{background:lightgreen}[optional]% CSV file containing the map of users ids and name (if none specified, the internal id are reused)

*Notes* about @taskList@ and @userList@
* In the JSON format, task and user are specified by numeric internal id. @taskList@ (and @userList@) is used to convert the numeric id found in the Json file to the task name (respectively user name) found in the destination campaign.  
* Since these files are produced during Json [[CLICreateCampaign#Export-a-campaign|export of a campaign]] , this allows seamless import (in the same database) to another campaign sharing the same workflow; Just reuse the @taskList@ file unchanged during import. If the task names (or user names) are different between campaigns, you'll need to explicitly make the mapping by editing the second column of @taskList@ (resp. @userList@)


+standard ouput:+
_na_

+example:+
<pre>
 java -jar target/AlvisAE-cli.jar --import-annotations  -p dbparam.props -d /tmp/json/ -c "New Campaign #1""

</pre>








### Assigning a document to a specific campaign

Will associate the document to the specified campaign, so it can be later on assigned to any user participating in the campaign (via the web UI)

+command:+
* *@--assign-document@*

+parameters:+
* *@-p@* properties file containing connection parameters
* *@--docInternalId@* internal Id of Document corresponding to the imported Annotation Set
or,  *@--docExternalId@*  external id of the Document (as specified during document import)
* *@-c@* campaign name
or,  *@--campaignId@*  internal id of the Campaign where to import the Annotation Set

+standard ouput:+
_na_

+example:+
<pre>
 java -jar target/AlvisAE-cli.jar --assign-document  -p dbparam.props --docInternalId 2014 -c "New Campaign #1" 

</pre>

### Export a campaign

Will create a zip archive containing the documents and the annotations of the specified campaign

+command:+
* *@--export-campaign@*

+parameters:+
* *@-p@* properties file containing connection parameters
* *@-c@* campaign name
or,  *@--campaignId@*  internal id of the Campaign to be exported
* *@-o@* %{background:lightgreen}[optional]% output directory where the export archive file will be created
* *@--format@* %{background:lightgreen}[optional]% format of the exported files ( +CSV+ | Json )

+standard ouput:+
* @zipArchiveFileName@

+example:+
<pre>
 java -jar target/AlvisAE-cli.jar --export-campaign -p dbparam.props -c "New Campaign #1" -f json -o /tmp

...
/tmp/aae_14.zip
</pre>
