# Trains Of Sheffield - Team 35
## Installing and setup database
1. Create your database in MySQL Workbench on your server
2. Import database.sql file from code bundle directory into MySQL Workbench:
  - login to your database server
  - choose Server -> Data Import from the toolbar
  - select option "Import from self-contained file" and select database.sql file into your database

## Installing and setup code
1. Enter app directory and run this command to build the project. We are expecting to have gradle installed.
```/gradlew build```
2. We are expecting to have Java 17+ installed. To run the app type
```cd build/libs```
```java -jar team035-1.0.jar```

## Manager credentials
1. The database has been initialised with one manager user and couple of products
2. Manager credentials
login: ``manager@trains.com``
password: ``Manager1!``
