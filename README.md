# Clinic
A sample project to show some skills with the team at uni

## Prerequisites
- [Java JDK](https://www.oracle.com/pl/java/technologies/javase-downloads.html) version 11+
- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle) version 3.0.0+
- [PostgreSQL](https://www.postgresql.org/download) version 13

## Libraries
| Library name                                                                                                     | Description                                                                                                                          |
|------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|
| [Spring Boot 3](https://spring.io/projects/spring-boot)                                                          | Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".  v3.0.5               |
| [Spring Web](https://spring.io/guides/gs/serving-web-content/)                                                   | Allows you to create endpoint on Web sites and lot more                                                                              |
| [Lombok](https://projectlombok.org/)                                                                             | Project Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java.                    |
| [Maven](https://maven.apache.org/download.cgi)                                                                   | Maven is a tool that can now be used for building and managing any Java-based project.                    |

## Instructions

1. Clone this repository to your computer.

2. Set up the PostgreSQL database:
   - Open the PostgreSQL database management tool or terminal.
   - Create a new database.

3. Configure the `application.properties` file:
   - Go to the root directory of the application.
   - Find the `application.properties` file located at `src/main/resources`.
   - Open the `application.properties` file in a text editor.
   - Change the values of the following properties to your database credentials:
     ```
     user=your_username
     password=your_password
     ```
   - Save the `application.properties` file.

4. Import the database schema:
   - Locate the `create.sql` file in the `src/main/resources/db` directory.
   - Use your PostgreSQL database management tool to import the `create.sql` file into the created database. This will set up the necessary schema for the application.

5. Start the Java server:
   - Open the terminal and go to the root directory of the application in the command line.
   - Run the command `mvn clean install`
   - Run the command to start the Java application (e.g., `java -jar WebApp.jar`).

6. Start the React application:
   - Open the terminal and go to the `src/main/frontend` directory.
   - Run `npm install` to install the project dependencies.
   - Run `npm install chart.js react-chartjs-2` and `npm install randomcolor` to install extensions for charts.
   - After the installation is complete, run `npm start` to start the React application.

7. Accessing the application:
   - The Java server will be available at `http://localhost:8080`.
   - The React application will be available at `http://localhost:3000`.
