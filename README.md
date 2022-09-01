# TOP 10 BUSLINES INFORMATION IN SL BUS NETWORK
	 
## Build
To build the this application you must have jdk8 and maven


Commands to build:
```
mvn clean [-> clean up]
mvn clean compile [-> compile source code into java classes]
mvn clean package [-> package all java classes and depednences into fat jar file(executable)]
mvn clean compile exec:java  [-> compile source code and execute in current JVM]
mvn clean test [-> execute tests locally]

```

### Start the service
NOTE: Please make sure that you have update application.properties file with your SL api key

```
 java -jar sl-buslines-0.0.1.war --spring.config.location=<path to application.properties file>
```

## Configurations:

```code
apiUrl: https://api.sl.se/api2/LineData.json?key={key}&model={model}
transportationMode: BUS
apiKey: <sl api key>
server.port: 8080
server.url: http://localhost
``


## Implemented Endpoints:

#### GET /stops
This will return the top 10 bus lines with maximum stops in its route

#### GET /

- This will redirect to a html page where top 10 bus lines displayed, by expanding each line you can see the list of stops in that line.
- Stops sorted based on stopAreaNumber.
- Origin and destination are two random stops in the line, which are not actual origin and destination in line.
  Since there is no direct relation between stops to find out the actual values,
  I considered first and last stops in the sorted list as origin and destination

