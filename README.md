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
•	To monitor patient’s details (Blood Pressure, Pulse Rate, Respiratory Rate) and to decide which patients should get more attention. A video Feed also help to know the status of the patients. With the help of 3-4 volunteers, a single doctor can handle nearly 100 patients at a time.
•	Temperature check is integrated with bar code like aadhar bar codes, in which temperature will be recorded at each time if a person is moving in and out in office or hotels etc.
This data will recorded separately for each person. This temperature records will be helpful for 
hospitals ,so that , at which time or place a person got temperature variation can be identified and they can easy track the covid -19 contact cases.
•	Chatbot assistant to provide the following features. 
o	To provide information about the number of covid patients in an area. This area should be as small as 3-5 km range. Based on this others can decide on whether to enter these areas or bypass their routes/shopping destinations/visits to these areas based on the statistics
o	To provides facility for the most needy people especially those infected with covid and under treatment in residences and those people who cannot perform their daily needs like cooking, travel to hospital for testing due to infection etc. They should be able to register and put requests for food, medicine, vehicle/ambulance needs etc and their requests should be accepted with high priority
o	To provide statistics of nearby hospitals with covid treatment facilities. It should provide data about provision for ventilators, doctors, free beds/rooms, ICU vacancies etc so that patients need not go in search of vacancies from one hospital to other and thus help in reducing queuing up in hospitals
o	Also, helps self-quarantine person to engage with chatbot about daily activities, exercise, food etc based on age and weight categories.
•	Notify users and doctors about frequent update on VIRUS related news feed based on location.
•	Track user travels based on Geospatial analytics details.


## Included components

* [IBM Cloud Storage](https://www.ibm.com/in-en/cloud/object-storage): Cloud Object Storage makes it possible to store practically limitless amounts of data, simply and cost effectively. It is commonly used for data archiving and backup, web and mobile applications, and as scalable, persistent storage for analytics.

* [IBM Cloudant](https://cloud.ibm.com/catalog/services/cloudant) : IBM Cloudant is a fully managed JSON document database that offers independent serverless scaling of provisioned throughput capacity and storage. Cloudant is compatible with Apache CouchDB and accessible through a simple to use HTTPS API for web, mobile, serverless, and IoT applications. Cloudant is SOC2 and ISO 27001 compliant with HIPAA readiness optional for Dedicated Hardware environments. 

* [IBM Cloud Functions](https://cloud.ibm.com/openwhisk): Functions-as-a-Service (FaaS) platform used to run your application code without servers, scale it automatically, and pay nothing when it is not in use.

* [IBM Bluemix Geospatial Location](https://ibm-watson-data-lab.github.io/location-tracker): The Location Tracker demo app uses geolocation to track and map user movements. The app uses an Offline First approach, storing the geolocation data locally on the device using Cloudant Sync or PouchDB, and then syncing this data with IBM Cloudant. The Location Tracker demo app is available as a native mobile app or as a web app. Both versions share a mobile backend built with Node.js. 

## Featured technologies

* [Artificial Intelligence](https://developer.ibm.com/technologies/artificial-intelligence/): Artificial intelligence can be applied to disparate solution spaces to deliver disruptive technologies.
* [Cloud](https://developer.ibm.com/depmodels/cloud/): Accessing computer and information technology resources through the Internet.
* [Data Science](https://developer.ibm.com/technologies/data-science/): Systems and scientific methods to analyze structured and unstructured data in order to extract knowledge and insights.
* [Cloundant Sync](https://www.ibm.com/cloud/learn/offline-first): Cloudant, built on open source Apache® CouchDB™, is designed so that CRUD and query requests go to the local replica, regardless of network state. Upon request, Cloudant’s Mobile Sync feature will keep the local replica and remote instance in sync, enabling offline functionality.

