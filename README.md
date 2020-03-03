# SpringGraalVM-ManageIT

SpringBoot Server with GraalVM Engine integrated (ManageIT-Platform-POC)

~~~
Readme for SpringGraalVM-ManageIT Application  
  
 
Created by Arthur Stocker, 01.01.20.  
Copyright (c) 2020 Arthur Stocker. All rights reserved.  
~~~


&nbsp;  
&nbsp;  
&nbsp;  
# Table of Content
[1    Installation](#1-installation)  
[1.1  Dependencies](#11-dependency)  
[1.2  GrallVM Community Edidtion configuration](#12-java-configuration)  
[1.3  SpringBoot configuration](#13-ringojs-configuration)   
[1.4  BaseX configuration](#14-basex-configuration)  
[2    Java Compile source and create JAR package](#2-java-compile-source-and-create-jar-package)  
[3    Start BRjx environment](#3-start-brjx-environment)  
[A    Open topics](#a-open-topics)  
[A.1  Import XML into BaseX DB](#a1-import-xml-into-basex-db)  
[B    Examples](#b-examples)  
[B.1  JS code to define protocol handler](#b1-js-code-to-define-protocol-handler)  
[B.2  JS/XQuery code Test snippets](#b2-js-code-testsnipets)  


&nbsp;  
&nbsp;  
&nbsp;  
# 1. Installation  
## 1.1 Dependency  

### § 1 GrallVM Community Edidtion  
First we need to install GrallVM Community Edidtion. As of the time when I installed GrallVM Community Edidtion, I decided to install the newest one.  

### § 2 SpringBoot  
If JAVA is installed, download RingoJS, unpack it to a Folder of your choice.  

### § 3 BaseX  
And now, the application backend I use, is BaseX. Download BaseX GUI and Web.  
Unpack it. Once done, proceed with the section about your OS.  


### § 11 macOS  
At the time when I made my notes I run macOS Sierra 10.12.3.  
Your first decision is where to store the Xquery modules. I came to the conclusion that the folder should be at the location where BaseX  
GUI stores thier data.  

> /Users/.../Library/Application Support/BaseX/Repo


## 1.2  GrallVM Community Edidtion configuration 
....... TODO ...... 

## 1.3 SpringBoot configuration 
....... TODO ...... 

## 1.4  BaseX configuration 
....... TODO ...... 

## 2 JAVA Compile source and create JAR package 
Compile JAVA source: 
````
javac -classpath src/??? -d build/classes src/main/java/org/manageit/???.java
````
Create JAR package: 
````
jar cvf jars/???.jar -C build/classes src/main/java/org/manageit/???.class
````

## 3 Start SpringBoot environment
```` 
....... TODO ...... 
````


&nbsp;  
&nbsp;  
&nbsp;  
# A. Open topics  

## A.1 Import XML into BaseX DB  

when importing XML in to BaseX set index as follows:
> Text index
> ```
> path, name, type, *:path, *:name, *:type, Q{uri}path, Q{uri}name, Q{uri}type, Q{uri}*, *
> ```
> Attribute index
> ```
> id, create, read, update, delete, *:id, *:create, *:read, *:update, *:delete, Q{uri}id, Q{uri}create, Q{uri}read, Q{uri}update, Q{uri}delete, Q{uri}*, *
> ```


&nbsp;  
&nbsp;  
&nbsp;  
# B. Examples  

## B.1 JS code to define protocol handler  

>```
> addToClasspath('./packages/brjx/jars/URLConnectionFactory.jar');
>
> var URLConnection;
> 
> var URLConnectionMethods = {
>     urlconnectionfactory: {
>         connect: function() {
>             console.dir(arguments);
>             console.log('Connected!');
>         },
>         getInputStream: function() {
>             console.dir(arguments);
>             console.log('new InputStream');
>         }
>     }
> };
> 
> java.net.URL.setURLStreamHandlerFactory(
>     new JavaAdapter(java.net.URLStreamHandlerFactory, {
>         createURLStreamHandler: function(protocol) {
>             function getURLStreamHandler(protocol) {
>                 return Object.keys(URLConnectionMethods).indexOf(protocol) != -1 ?
>                     new JavaAdapter(java.net.URLStreamHandler, {
>                         openConnection: function(url) {
>                             return URLConnection = new org.manageit.urlconnection.URLConnectionFactory(url) {
>                                 connect: URLConnectionMethods[protocol].connect,
>                                 getInputStream: URLConnectionMethods[protocol].getInputStream
>                             };
>                         }
>                     }) :
>                     null;
>             }
>             return getURLStreamHandler(protocol);
>         }
>     })
> );
> 
> var u = new java.net.URL('urlconnectionfactory:/test.url');
> var c = u.openConnection();
> c.connect();
> ```
<sub>Registering and using a custom java.net.URL protocol: https://stackoverflow.com/questions/26363573/registering-and-using-a-custom-java-net-url-protocol</sub>


## B.2 JS/XQuery code Test snippets 

> ```
> 
> query.variable = {key: '$table', value: '175c9043-0cc4-4165-bdb1-eb66fc13457f'};
> query.string = 'declare variable $table as xs:string external;\n  for $db in db:list()\n    (:where expression - database - beginn:)(:where expression - database - end:)\n      for $doc in collection($db)\n        (:where expression - document - beginn:)(:where expression - document - end:)\n          for $t in $doc/*\n            (:where expression - table - beginn:) where $t/@id = $table (:where expression - table - end:)\n              (:for $r in $t/*:)\n                (:where expression - record - beginn:)(:where expression - record - end:)\n                  return $t (:string, number, boolean, null, object, array:)\n';
> 
> db:optimize(
>   'mydb',
>   true(),
>   map { 'autooptimize': true(), 'updindex':true(), 'textindex': true(), 'textinclude': 'id', 'attrindex': '', 'attrinclude': '' }
> )
> 
> ```
