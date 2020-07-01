## IBM Call for Code 2020

## Call for Code Document


## Project Team Name - Team Spartan

## Project description
BOST (Be on Safe Track) is a mobile app used for both COVID-19 patient and doctors. It helps doctor to priortize the patient based on age, temperature etc. Also, it helps user to track travel coordinates, check temperature and notify the doctors based on temperature exceeds threshold values.

## Current Problems

In day-to-day process, identify the COVID-19 positive patient is very difficult by verifying the user from multiple channels like airport, trains, buses etc. We need to maintain separate security team to manual check user using temperature check machine and updating on the software manually.  Self-quarantine patients has to check with doctor frequently over phone calls/messaging manually about daily activities. Also, patient need someone help to get daily foods and others.
In addition, for doctors who are attending the COVID-19 patients on the hospital there are facing difficult to treat patients based on priorities since cases are increasing drastically all over the world. Doctor has to find critical patient manually.

## Solutions

An Mobile app will be maintains based on IBM cloud services in order to support the following features in which two different way of services will be availed based on users and doctors.
*	To monitor patient’s details (Blood Pressure, Pulse Rate, Respiratory Rate) and to decide which patients should get more attention. A video Feed also help to know the status of the patients. With the help of 3-4 volunteers, a single doctor can handle nearly 100 patients at a time.
*	Temperature check is integrated with bar code like aadhar bar codes, in which temperature will be recorded at each time if a person is moving in and out in office or hotels etc.
This data will recorded separately for each person. This temperature records will be helpful for 
hospitals ,so that , at which time or place a person got temperature variation can be identified and they can easy track the covid -19 contact cases.
*	Chatbot assistant to provide the following features. 
	  * To provide information about the number of covid patients in an area. This area should be as small as 3-5 km range. Based on this others can decide on whether to enter       these areas or bypass their routes/shopping destinations/visits to these areas based on the statistics
    * To provides facility for the most needy people especially those infected with covid and under treatment in residences and those people who cannot perform their daily
      needs like cooking, travel to hospital for testing due to infection etc. They should be able to register and put requests for food, medicine, vehicle/ambulance needs
      etc and their requests should be accepted with high priority
    * To provide statistics of nearby hospitals with covid treatment facilities. It should provide data about provision for ventilators, doctors, free beds/rooms, ICU
      vacancies etc so that patients need not go in search of vacancies from one hospital to other and thus help in reducing queuing up in hospital
    * Also, helps self-quarantine person to engage with chatbot about daily activities, exercise, food etc based on age and weight categories.
* Notify users and doctors about frequent update on VIRUS related news feed based on location.
* Track user travels based on Geospatial analytics details.


## Included components

* [IBM Cloud Storage](https://www.ibm.com/in-en/cloud/object-storage): Cloud Object Storage makes it possible to store practically limitless amounts of data, simply and cost effectively. It is commonly used for data archiving and backup, web and mobile applications, and as scalable, persistent storage for analytics.

* [IBM Cloudant](https://cloud.ibm.com/catalog/services/cloudant) : IBM Cloudant is a fully managed JSON document database that offers independent serverless scaling of provisioned throughput capacity and storage. Cloudant is compatible with Apache CouchDB and accessible through a simple to use HTTPS API for web, mobile, serverless, and IoT applications. Cloudant is SOC2 and ISO 27001 compliant with HIPAA readiness optional for Dedicated Hardware environments. 

* [IBM Cloud Functions](https://cloud.ibm.com/openwhisk): Functions-as-a-Service (FaaS) platform used to run your application code without servers, scale it automatically, and pay nothing when it is not in use.It offers easy access to IBM Watson APIs within the event-trigger-action workflow, makes cognitive analysis of application data inherent to your workflows.

* [IBM Bluemix Geospatial Location](https://ibm-watson-data-lab.github.io/location-tracker): The Location Tracker demo app uses geolocation to track and map user movements. The app uses an Offline First approach, storing the geolocation data locally on the device using Cloudant Sync or PouchDB, and then syncing this data with IBM Cloudant. The Location Tracker demo app is available as a native mobile app or as a web app. Both versions share a mobile backend built with Node.js.

* [IBM Watson AutoAI Cloud Services](https://dataplatform.cloud.ibm.com/docs/content/wsj/analyze-data/autoai-build.html): The AutoAI graphical tool automatically analyzes your data and generates candidate model pipelines customized for your predictive modeling problem.  These model pipelines are created iteratively as AutoAI analyzes your dataset and discovers data transformations, algorithms, and parameter settings that work best for your problem setting. 

* [IBM Watson Discovery](https://cloud.ibm.com/catalog/services/discovery): Watson Discovery is an AI search technology that rapidly build cognitive, cloud-based exploration applications that unlock actionable insights hidden in unstructured data — including your own proprietary data, as well as public and third-party data. It brings together a functionally rich set of integrated, automated Watson APIs.

* [IBM Push Notifications](https://cloud.ibm.com/catalog/services/push-notifications): Push Notifications service provides a unified push capability to send personalized and segmented real-time notifications to mobile and web applications using an intuitive UI, client SDK's and simple REST API's. It also monitor the push performance by generating graphs from your user data. 

* [IBM Watson Assistance](https://cloud.ibm.com/docs/assistant?topic=assistant-getting-started): Watson Assistant is a conversation AI platform that helps you provide customers fast, straightforward and accurate answers to their questions, across any application, device or channel. By addressing common customer inquiries, Watson Assistant reduces the cost of customer interactions, helping your agents focus on complex use cases – not repetitive responses.

* [IBM Watson IOT](https://www.ibm.com/cloud/watson-iot-platform): IBM Watson IoT Platform is a managed, cloud-hosted service designed to make it simple to derive value from your IoT devices. It reduce operational expense by understanding IoT devices to operate them more effectively and efficiently.


## Featured technologies

* [Artificial Intelligence](https://developer.ibm.com/technologies/artificial-intelligence/): Artificial intelligence can be applied to disparate solution spaces to deliver disruptive technologies.

* [Cloud](https://developer.ibm.com/depmodels/cloud/): Accessing computer and information technology resources through the Internet.

* [Data Science](https://developer.ibm.com/technologies/data-science/): Systems and scientific methods to analyze structured and unstructured data in order to extract knowledge and insights.

* [Cloundant Sync](https://www.ibm.com/cloud/learn/offline-first): Cloudant, built on open source Apache® CouchDB™, is designed so that CRUD and query requests go to the local replica, regardless of network state. Upon request, Cloudant’s Mobile Sync feature will keep the local replica and remote instance in sync, enabling offline functionality.

* [Andriod SDK](https://developer.android.com/studio): The Android software development kit (SDK) includes a comprehensive set of development tools.These include a debugger, libraries, a handset emulator based on QEMU. Android apps can be written  mainly in Kotlin and Java.

* [JDK 1.8](https://www.oracle.com/in/java/technologies/javase/javase-jdk8-downloads.html): The JDK is a development environment for building applications, applets, and components using the Java programming language. The JDK includes tools useful for developing and testing programs written in the Java programming language and running on the Java platform.

* [Gradle 6.1.1](https://gradle.org/install/): Gradle is an open-source build automation tool focused on flexibility and performance. Gradle build scripts are written using a Groovy or Kotlin DSL. 

* [Firebase Database](https://firebase.google.com/products/realtime-database): The Firebase Realtime Database is a cloud-hosted database. Data is stored as JSON and synchronized in realtime to every connected client. When you build cross-platform apps with our iOS, Android, and JavaScript SDKs, all of your clients share one Realtime Database instance and automatically receive updates with the newest data.

* [Firebase console Management(FCM)](https://firebase.google.com/docs/cloud-messaging):Firebase Cloud Messaging (FCM) is a cross-platform messaging solution that lets you reliably send messages at no cost.

* [Google Map API](https://cloud.google.com/maps-platform/): Google Maps API lets you customize maps with your own content and imagery for display on web pages and mobile devices. 


## Technical Diagram

![TechnicalDiagram](https://user-images.githubusercontent.com/67637361/86258121-1487de00-bbd8-11ea-94d9-b0f298d2352e.jpg)


