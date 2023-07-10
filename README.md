# TDD

An order managemment system

This project, will create a simple Order Management System using Spring Boot while following the principles of Test Driven Development (TDD). The Order Management System is designed as a simple web application that allows its users to handle the basic functionalities related to order handling. It is a practical example of a real-world system businesses might use to manage their order processes.

 

Key Functionalities:

Creating a new order: The application allows users to create a new order. The user must input details like customer name, order date, shipping address, and total order amount. Once the order is created, it gets saved in the database.

Updating an existing order: If a mistake is made while creating an order, or if there's a need to update any current order details, the application offers the functionality to modify those details. The user will select the order to be updated, make the necessary changes, and save them. The order in the database will be updated with the new details.

Fetching the details of an order: Users can view the details of any order by fetching them from the database. By inputting the unique order ID, the application will display the complete details of that order.

Deleting an order: If an order is canceled or the user decides to remove some old orders, the application provides a delete functionality. The user selects the order to be deleted, and the application removes it from the database.

 

By incorporating all these features, the Order Management System serves as a basic platform for managing and maintaining business orders. The main purpose of this application is to demonstrate the working of a Spring Boot application developed following the principles of Test-Driven Development (TDD). The application showcases how to write tests first and then write the application code, ensuring that every line of code written is of value and works as expected.

The good stuff:


How to-  Open project in IDE and run build via /controller
The tests are located in /repository
Run the mvn package via CLI to execute the requsite JAR file.




