# AlvisAE

AlvisAE is an editor that facilitates the annotation of text documents with the goal of extracting knowledge.

This project containsAlvisAE two main components: an Annotation Editor (alvisae-ui)  and an Web Service (alvisae-ws). 

## Try it...

> Note that, you must have [docker](https://www.docker.com/) installed on your computer

```
sudo docker run -d --rm --name alvisae.ws -p 8080:8080 -p 5432:5432  bibliome/alvisae:1.0.0
``` 
* Go to [http://localhost:8080/alvisae/alvisae-ws/AlvisAE/](http://192.168.56.101:8080/alvisae/alvisae-ws/AlvisAE)
* Sing-In with login *annotator1* and password *annotator1*

