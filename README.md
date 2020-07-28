## IBM Call for Code 2020

## Call for Code Document

[CFC_Cognizant__TeamSpartans.docx](https://github.com/rvenkateshkumar86-cts/CFC-COVID19-BOST/files/4931521/CFC_Cognizant__TeamSpartans.docx)

## Project Team Name - Team Spartan

## Project description
BOST (Be on Safe Track) is a mobile app used for COVID-19 patient, Public servant officer and normal user. It helps user to take self-assessment test based on age, temperature etc. In addition, it helps public servant officer to track travel coordinates, check temperature and notify them based on temperature exceeds threshold values. It helps BOST app user to know latest news on COVID 19 update and to chat with assistance in order to get help based on nearby hospital details, quarantine activities for COVID 19 patients etc.

## Current Problems

In day-to-day process, identify the COVID-19 positive patient is very difficult by verifying the user from multiple channels like airport, trains, buses etc. We need to maintain separate security team to manual check user using temperature check machine and updating on the software manually. Self-quarantine patients has to check with doctor frequently over phone calls/messaging manually about daily activities. In addition, patient need someone help to get daily foods and others. In addition, for normal user their do not know how to check self-assessment in order to know chances of affection.

## Solutions

An Mobile app will be maintains based on IBM cloud services in order to support the following features.
* To take self-assessment test using (Blood Pressure, Pulse Rate, Respiratory Rate) to know about chance of affection. 
* Temperature check feature and able to send temperature details to Watson IOT services for monitoring if any users temperature exceeds more than 98 degree temperature. This data will recorded separately for each person. This temperature records will be helpful for Public servant officer, so that, at which time or place a person got temperature variation can be identified and they can easy track the COVID -19 contact cases.

* Chatbot assistant to provide the following features. 
	* To provide information about the number of covid patients in an area. This area should be as small as 3-5 km range. Based on this others can decide on whether to 	      enter these areas or bypass their routes/shopping destinations/visits to these areas based on the statistics
	* To provides facility for the neediest people especially those infected with COVID and under treatment in residences and those people who cannot perform their daily 		needs like cooking, travel to hospital for testing due to infection etc. They should be able to register and put requests for food, medicine, vehicle/ambulance
	  needs etc. and their requests should be accepted with high priority
	* To provide statistics of nearby hospitals with COVID treatment facilities. It should provide data about provision for ventilators, doctors, free beds/rooms, ICU 	     vacancies etc. so that patients need not go in search of vacancies from one hospital to other and thus help in reducing queuing up in hospital
	* In addition, helps self-quarantine person to engage with chat-bot about daily activities, exercise, food etc. based on age and weight categories.
* Notify users and doctors about frequent update on COVID related news feed based on location.
* Track user travels based on Google API with firebase database.


## High Level Architecture Diagram

![architectural_diagram_bost_app](https://user-images.githubusercontent.com/67637361/86685668-50c89d80-c021-11ea-90d1-fc882520cfad.jpg)

## Step Flow

### Flow 1 (Normal User)

* Register your device with BOST Application.
* Receive notification about covid-19 latest updates every 30 minutes.
* Check whether a chance of covid-19 affected by providing vital information.
* Use chatbot facility to get information about no. of covid-19 patients in an area, safe zones to travel, covid-19 treatment hospitals.
* Verify your temperature any time using your smart phone.
* Track user location to store history of visits in offline and notify him on crossing 50km.

### Flow 2 (Covid-19 affected User)

* Register your device with BOST Application.
* Receive notification about covid-19 latest updates every 30 minutes.
* Use chatbot facility to put requests for food, medicine, vehicle/ambulance needs and treated with high priority.
* Track user location to store history of visits in offline and notify him on crossing 50km.
* Verify your temperature any time using your smart phone.

### Flow 3 (Public Servant)

* Register your device with BOST Application.
* Receive notification about covid-19 latest updates every 30 minutes.
* Use chatbot facility to get information about no. of covid patients in an area, safe zones to travel, covid treatment hospitals.
* Track both normal and covid-19 affected user location and get notification as device Id and mobile number on crossing 50km from initial location.
* Get notification as device Id and mobile number of a normal user or covid-1 affected user when he is crossing a temperature of 98˚F.

## Technical Diagram

![TechnicalDiagram](https://user-images.githubusercontent.com/67637361/86258121-1487de00-bbd8-11ea-94d9-b0f298d2352e.jpg)


## Included components

* [IBM Cloud Storage](https://www.ibm.com/in-en/cloud/object-storage): Cloud Object Storage makes it possible to store practically limitless amounts of data, simply and cost effectively. It is commonly used for data archiving and backup, web and mobile applications, and as scalable, persistent storage for analytics.

* [IBM Cloud Functions](https://cloud.ibm.com/openwhisk): Functions-as-a-Service (FaaS) platform used to run your application code without servers, scale it automatically, and pay nothing when it is not in use.It offers easy access to IBM Watson APIs within the event-trigger-action workflow, makes cognitive analysis of application data inherent to your workflows.

* [IBM Watson AutoAI Cloud Services](https://dataplatform.cloud.ibm.com/docs/content/wsj/analyze-data/autoai-build.html): The AutoAI graphical tool automatically analyzes your data and generates candidate model pipelines customized for your predictive modeling problem.  These model pipelines are created iteratively as AutoAI analyzes your dataset and discovers data transformations, algorithms, and parameter settings that work best for your problem setting. 

* [IBM Watson Discovery](https://cloud.ibm.com/catalog/services/discovery): Watson Discovery is an AI search technology that rapidly build cognitive, cloud-based exploration applications that unlock actionable insights hidden in unstructured data — including your own proprietary data, as well as public and third-party data. It brings together a functionally rich set of integrated, automated Watson APIs.

* [IBM Push Notifications](https://cloud.ibm.com/catalog/services/push-notifications): Push Notifications service provides a unified push capability to send personalized and segmented real-time notifications to mobile and web applications using an intuitive UI, client SDK's and simple REST API's. It also monitor the push performance by generating graphs from your user data. 

* [IBM Watson Assistance](https://cloud.ibm.com/docs/assistant?topic=assistant-getting-started): Watson Assistant is a conversation AI platform that helps you provide customers fast, straightforward and accurate answers to their questions, across any application, device or channel. By addressing common customer inquiries, Watson Assistant reduces the cost of customer interactions, helping your agents focus on complex use cases – not repetitive responses.

* [IBM Watson IOT](https://www.ibm.com/cloud/watson-iot-platform): IBM Watson IoT Platform is a managed, cloud-hosted service designed to make it simple to derive value from your IoT devices. It reduce operational expense by understanding IoT devices to operate them more effectively and efficiently.


## Featured technologies

* [Artificial Intelligence](https://developer.ibm.com/technologies/artificial-intelligence/): Artificial intelligence can be applied to disparate solution spaces to deliver disruptive technologies.

* [Cloud](https://developer.ibm.com/depmodels/cloud/): Accessing computer and information technology resources through the Internet.

* [Data Science](https://developer.ibm.com/technologies/data-science/): Systems and scientific methods to analyze structured and unstructured data in order to extract knowledge and insights.


* [Andriod SDK](https://developer.android.com/studio): The Android software development kit (SDK) includes a comprehensive set of development tools.These include a debugger, libraries, a handset emulator based on QEMU. Android apps can be written  mainly in Kotlin and Java.

* [JDK 1.8](https://www.oracle.com/in/java/technologies/javase/javase-jdk8-downloads.html): The JDK is a development environment for building applications, applets, and components using the Java programming language. The JDK includes tools useful for developing and testing programs written in the Java programming language and running on the Java platform.

* [Gradle 6.1.1](https://gradle.org/install/): Gradle is an open-source build automation tool focused on flexibility and performance. Gradle build scripts are written using a Groovy or Kotlin DSL. 

* [Firebase Database](https://firebase.google.com/products/realtime-database): The Firebase Realtime Database is a cloud-hosted database. Data is stored as JSON and synchronized in realtime to every connected client. When you build cross-platform apps with our iOS, Android, and JavaScript SDKs, all of your clients share one Realtime Database instance and automatically receive updates with the newest data.

* [Firebase console Management(FCM)](https://firebase.google.com/docs/cloud-messaging):Firebase Cloud Messaging (FCM) is a cross-platform messaging solution that lets you reliably send messages at no cost.

* [Google Map API](https://cloud.google.com/maps-platform/): Google Maps API lets you customize maps with your own content and imagery for display on web pages and mobile devices. 



