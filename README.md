# Importing the Project
Intellij has native support for `sbt` projects. Please, see details [here](https://blog.jetbrains.com/scala/2013/11/18/built-in-sbt-support-in-intellij-idea-13/).

# Running the Project Locally
You may launch the app from your ide for debugging or running. Please, see details [here](https://www.playframework.com/documentation/2.5.x/IDE).

# Running Cassandra Locally
For local development you may run cassandra locally from docker with the following command:
`docker run --name cassandra-akka -p 7000:7000 -p 7199:7199 -p 9042:9042 -p 9160:9160 -d cassandra`.

The keyspaces and tables required by the application are created automatically for you if the application is started and the `akka` and `akka_snapshot` keyspaces are missing.

# Importing Campaign Data
You may import our `sample.csv` file to create sample campaigns.
To import the file, first run the backend and cassandra, then execute:
`http -f POST localhost:9000/campaign file@src/test/resources/sample.csv` (See [httpie](https://httpie.org/)).

If the import is successful, you should get a response like:
```http
HTTP/1.1 200 OK
Content-Length: 39
Content-Type: application/json
Date: ...

{
    "err": 3,
    "ok": 9,
    "timeout": 0,
    "total": 12
}
```

To test a campaign, execute: `http GET localhost:9000/customer/1`. Here is the output:
```http
HTTP/1.1 200 OK
Content-Length: 251
Content-Type: application/json
Date: ...

{
    "actionButtonLabel": "Click Here",
    "actionButtonUrl": "http://mycampaign.de/after-click.html",
    "adUrl": "http://mycampaign.de/sample.html",
    "campaign": "2",
    "customer": "1",
    "displayCount": 1,
    "endDate": 64597215600000,
    "startDate": 1483225200000,
    "status": "active"
}
```
