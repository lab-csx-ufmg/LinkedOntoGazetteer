# LinkedOntoGazetteerWeb (LOG)

LinkedOntoGazetteerWeb is the web application with the search engine implementation and its API web services.

## REXSTER/RESTClient

Summary of files:

 * RESTClient.java (Abstract)
 * GremlinRESTClient.java
 * VertexClient.java

There are three classes responsible to fetch data from the Rexster server: RESTClient, GremlinRESTClient and VertexClient. The RESTClient is an abstract class that contain which contains the basic methods that are common to the request methods, like the request method itself, and configuration methods like the class constructor and header constructor.

The GremlinRESTClient contains the functions to request Rexster using Gremlin, so it contains the main functions to perform searching within the graph. Other then these specific functions, the class also sets the particular server API endpoint to execute its requests.

The VertexClient contains functions responsible to retrieve more details about specific vertexes that the users visualize at the search results.

## Importing project

The project can be imported to eclispe using git directly by following the steps:

1. First it's needed to review the git panel using: **Window** / **Show View** / **Other...** / **Git** / **Git Repository**
2. In the new panel select **Clone a Git Repository**
3. In the new window fill the **URI** with the repository one: [https://github.com/lab-csx-ufmg/LinkedOntoGazetteer](https://github.com/lab-csx-ufmg/LinkedOntoGazetteer), and click on **Next** button
4. Select **master** branch (or any other with working in progress) and click on **Next** button again
5. Select **directory destination** and option **Import all existing Eclipse project after clone finishes**
6. If the project is not loaded as a web project left click the project (Package Explore) and select **Properties** / **Project Facets** menu on the new window / **convert to faceted form…** link / check **Dynamic Web Module** and **Java** boxes and click on **Apply** button

## Running Environment Variable
In order to use the application inside tomcat its required to edit/create a file on bin's catalina folder called setenv.sh (or setenv.bat for windows) which will be read by catalina.sh script on tomcat startup.

```[shell]
#!/bin/bash

# setenv.sh

export REXSTER_HOST=http://localhost:8182
export REXSTER_USER=username
export REXSTER_PASS=password
```