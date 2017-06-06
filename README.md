# Importing the Project
Intellij has native support for `sbt` projects. Please, see details [here](https://blog.jetbrains.com/scala/2013/11/18/built-in-sbt-support-in-intellij-idea-13/).

# Running the Project Locally
You may launch the app from your ide for debugging or running. Please, see details [here](https://www.playframework.com/documentation/2.5.x/IDE).

# Running Cassandra Locally
For local development you may run cassandra locally from docker with the following command:
`docker run --name cassandra-akka -p 7000:7000 -p 7199:7199 -p 9042:9042 -p 9160:9160 -d cassandra`.

The keyspaces and tables required by the application are created automatically for you if the application is started and the `akka_example` and `akka_example_snapshot` keyspaces are missing.
