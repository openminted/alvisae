h1. Web Services Specification and usage


BASEURL=@http://bibweb:8580/alvisae/demo/api@

* (!) Every web service requires authentication. the credentials provided are checked at the application level (vs at Container), hence the couple user/password must be one stored in user table.
* (!) since credentials are passed within http header, these web service should always be used over SSL on non secure networks 

h2. User related services

h3. Check User/password (and get list of associated campaigns)

* url: BASEURL *@/user/me@*
* method : *GET*
* url parameters: _none_
* return: user id and list of Campaign associated to the current user (the one used for authentication)
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)


<pre>
curl -u foo:bar -w "\n%{http_code}\n" http://bibweb:8580/alvisae/demo/api/user/me

{
  "id":1,
  "login":"aae_root",
  "campaigns":[]
}
200

</pre>


h3. Get any user's authorizations

* url: BASEURL *@/user/@*  %{color:blue}user_id% *@/authorizations@*
* method : *GET*
* restriction: can be performed by application admin only
* url parameters: 
** %{color:blue}user_id% : numeric id of the user to be checked
* return: list of authorizations 
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)

<pre>
curl -u aae_root:Tadmin -w "\n%{http_code}\n"  http://bibweb:8580/alvisae/demo/api/user/4/authorizations

{
  "global":[1],
  "bycampaign":{
    
  }
}
200

</pre>


h3. Get current user's authorizations (and list of associated campaigns)

* url: BASEURL *@/user/me@* 
* method : *GET*
* url parameters: _none_
* form parameters:
** @wzauths@ : set to true to retrieve authorizations
* return: user id, list of authorizations and list of Campaign associated to the current user
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)

<pre>

curl -u foo:bar -w "\n%{http_code}\n" http://bibweb:8580/alvisae/demo/api/user/me?wzauths=true

{
  "id":5,
  "login":"foo",
  "is_admin":false,
  "is_active":true,
  "campaigns":[],
  "authorizations":{
    "global":[1],
    "bycampaign":{
      
    }
  }
}
200

</pre>


h3. Get list of all possible authorizations

* url: BASEURL *@/authorizations@* 
* method : *GET*
* restriction: can be performed by application admin only
* url parameters: _none_
* form parameters:
** [  @scopes@ : comma separated list of scopes for which the authorizations will be retrieved. No limit if not specified ]
* return: list of known authorizations with their id and human readable description
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)

<pre>

curl -u aae_root:Tadmin -w "\n%{http_code}\n" http://bibweb:8580/alvisae/demo/api/authorizations?scopes=AlvisAE

{
  "authorizations":[{
    "auth_id":1,
    "scope":"AlvisAE",
    "description":"Connect",
    "campaignrelated":false
  },{
    "auth_id":2,
    "scope":"AlvisAE",
    "description":"Create campaign",
    "campaignrelated":false
  }
  ]
}
200

</pre>


h3. Add a new user

* url: BASEURL *@/user@* 
* method : *POST*
* restriction: can be performed by application admin only
* url parameters: _none_
* form parameters:
** @login@ : the desired login name for new user
** @is_admin@ : boolean set to true is new user will be application admin
** @passwd@ : the password for new user
** [  @is_active@ : whether the new user is active or not, default=true  ]
* return: user id, list of authorizations and list of Campaign associated to the current user
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)
** 409 Conflict: if the desired user name is not available

<pre>

curl -u aae_root:Tadmin -w "\n%{http_code}\n"  -X POST -d 'login=foo&is_admin=false&passwd=bar'  http://bibweb:8580/alvisae/demo/api/user

{
  "id":4,
  "login":"foo",
  "is_admin":false,
  "is_active":true,
  "campaigns":[],
  "authorizations":{
    "global":[],
    "bycampaign":{
      
    }
  }
}
200

</pre>


h3. Change password

* url: BASEURL *@/user/@*  %{color:blue}user_id% *@/chpasswd@*
* method : *POST*
* restriction: can be performed by current user for himself, or by application admin for any user
* url parameters: 
** %{color:blue}user_id% : numeric id of the user to be modified
* form parameters:
** @passwd@ : the new password
* return: user id and list of Campaign associated to the current user
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)


<pre>

curl -u foo:bar -w "\n%{http_code}\n"  -X POST -d 'passwd=foo' http://bibweb:8580/alvisae/demo/api/user/4/chpasswd

{
  "id":4,
  "login":"foo",
  "campaigns":[]
}
200

</pre>


h3. Update user's info (except password and properties)

* url: BASEURL *@/user/@*  %{color:blue}user_id% 
* method : *PUT*
* restriction: can be performed by application admin only
* url parameters: 
** %{color:blue}user_id% : numeric id of the user to be modified
* form parameters: 
** @login@ : the new value for login name
** @is_admin@ : boolean set to true is user will be application admin
** [  @is_active@ : whether the user is active or not ]
* return: user id, list of authorizations and list of Campaign associated to the current user
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)
** 409 Conflict: if the desired user name is not available

<pre>

curl -u aae_root:Tadmin -w "\n%{http_code}\n"  -X PUT -d 'login=toto&is_admin=true'  http://bibweb:8580/alvisae/demo/api/user/4

{
  "id":4,
  "login":"toto",
  "is_admin":true,
  "campaigns":[],
  "authorizations":{
    "global":[],
    "bycampaign":{
      
    }
  }
}
200

</pre>


h3. Retrieve user's properties

* url: BASEURL *@/user/@*  %{color:blue}user_id% *@/props@*
* method : *GET*
* restriction: can be performed by current user for himself, or by application admin for any user
* url parameters: 
** %{color:blue}user_id% : numeric id of the user to be modified
* return: properties map associated to specified user 
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)

<pre>
 curl -u aae_root:Tadmin -w "\n%{http_code}\n"   http://bibweb:8580/alvisae/demo/api/user/4/props

{
  "properties":{
     "key":"value",
   }
}
200

</pre>


h3. Replace user's properties

* url: BASEURL *@/user/@*  %{color:blue}user_id% *@/props@*
* method : *PUT*
* restriction: can be performed by current user for himself, or by application admin for any user
* url parameters: 
** %{color:blue}user_id% : numeric id of the user to be modified
* payload: 
the json serialization of the Map, e.g. @{"key1":"value1", "key2":"value2"}@

* return: properties map associated to specified user 
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)

<pre>
 curl -u aae_root:Tadmin -w "\n%{http_code}\n" -T payload.json  http://bibweb:8580/alvisae/demo/api/user/4/props

{
  "properties":{
    "key1":"value1",
    "key2":"value2"
  }
}
200

</pre>



h2. Other services

...
...
...


h2. Import/Export services

h3. Export all Annotations of a campaign as a zip archive of CSV files


* url: BASEURL *@/campaigns/@*  %{color:blue}campaign_id% *@/annotations/CSV@*
* method : *GET*
* restriction: can be performed by application admin only
* url parameters: 
** %{color:blue}campaign_id% : numeric id of the campaign to be exported

* return: a zip archive of CSV files containing annotations, and text files of the raw documents.
* status code:
** 401 Unauthorized: if the current user is not authenticated (unknown login or wrong password)


<pre>

curl -u aae_root:Tadmin -w "\n%{http_code}\n" http://bibdev:8580/alvisae/tv05/api/campaigns/3/annotations/CSV > annotations.zip

</pre>
