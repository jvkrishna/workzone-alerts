# Workzone Alerts
Institute of Transportation:  Workzone Congestion Alerts Module

## Setup
1. Download and install [Maven](https://maven.apache.org/install.html) 
2. Download and install [MongoDB](https://docs.mongodb.com/manual/installation/)
3. Configure application.properties file in resources.
	* Update mongodb settings
	* Provide Twilio account credentials
4. Modify *recipients.json* file to provide the recipients mobile numbers.
5. Update *workzonesensors.csv* file to provide the work zone sensor locations and the work zone projects. Also update the *sensor_camera_2017.csv* to provide the work zone sensors and the camera mappings.

## Installation
1. Navigate to project root folder and run the following command 

  		mvn clean install
 2. Go to the *target* folder and extract the **ROOT.war** file.
 3. Either deploy the *ROOT.war* file in a *Java Servlet Container* or manually run the application using the following command.
 
 		java -jar ROOT.war
4. Once the application successfully started, post the work zone status information to this application. Below explains the REST call structure for posting the work zone statuses.

    Type |Value
    ---|---
    HTTP Method:| POST 
    Request URL: |http://\<context-url>/workzone/feeds/consume
	Form Data:|<code>{<br/>"workzone": "$workzoneName",<br/>"device": "$deviceName",<br/>"avgSpeed": "$avgSpeed",<br/>"alert": "Must be one of {SLOW,STOP,BLANK}"<br/>}</code>
    
<br/>
5. To access the current bottlenecks (Slow or Stop conditions) in the work zones, use the follwoing feed.

		http://<context-url>/feeds/alertfeed

