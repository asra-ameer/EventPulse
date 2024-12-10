Real-Time Event Ticketing System
Introduction
The Real-Time Event Ticketing System is a high-performance application designed to manage dynamic ticket releases and purchases in real-time. It supports vendors releasing tickets and customers purchasing them while maintaining data integrity through multi-threading and synchronization.

Key features include:

Setting maximum ticket limits for events and ticket pools.
Adding vendors and customers with configurable rates.
Monitoring system status (tickets available and sold).
Stopping and resetting the system dynamically.
Folder Structure
The project follows a modular structure for better maintainability:


src/main/java/com/example/ticketingSystem
│
├── Controller
│   └── TicketingController.java   # Handles HTTP requests and defines API endpoints
│
├── Modules
│   └── Vendor.java                # Defines the Vendor model
│   └── Customer.java              # Defines the Customer model
│   └── TicketPool.java            # Manages ticket pool data and logic
│
├── Services
│   └── VendorService.java         # Implements logic for vendor ticket release
│   └── CustomerService.java       # Implements logic for customer ticket purchase
│   └── TicketPoolService.java     # Centralized service for managing ticket operations
│
└── Application.java               # Main entry point for the Spring Boot application
Setup Instructions
Prerequisites
Java Development Kit (JDK)
Ensure you have JDK 17 (or above) installed. Download here.

Maven
Install Maven for building the application. Download here.

Node.js and Angular CLI
For the frontend (if integrated), install Node.js (v16 or later) and Angular CLI. Download Node.js, Install Angular CLI.

IDE
Use an IDE like IntelliJ IDEA, Eclipse, or VS Code for better development experience.

How to Build and Run the Application
Clone the repository:

git clone https://github.com/your-repository/ticketing-system.git
cd ticketing-system
Build the backend:

mvn clean install
Run the backend:

mvn spring-boot:run
Set up the frontend (if applicable):

cd frontend
npm install
ng serve
Access the application:

Backend: http://localhost:8080
Frontend (if applicable): http://localhost:4200
Usage Instructions
Configuring and Starting the System
Set Ticket Limits
Use the following API endpoints to configure ticket limits:

Set event ticket limit: POST /api/eventticket/set-max-event-tickets
Body: { "maxEventTickets": 100 }
Set pool ticket limit: POST /api/eventticket/set-max-pool-tickets
Body: { "maxPoolTickets": 50 }
Start Vendors and Customers
Start vendors and customers to simulate ticket transactions:

Start a vendor: POST /api/eventticket/start-vendor?vendorId=1&ticketReleaseRate=5
Start a customer: POST /api/eventticket/start-customer?customerId=1&customerRetrievalRate=3
Monitor Status
Use GET /api/eventticket/status to view:

Available tickets
Tickets sold
Stop and Reset the System
Use POST /api/eventticket/stop to stop all processes and reset the ticket pool.

UI Controls (Frontend) (if applicable)
Dashboard
Displays ticket statistics (available and sold).
Configuration Panel
Allows admins to set ticket limits and manage vendors/customers.
Controls
Buttons to start/stop the system and add vendors/customers dynamically.
Future Improvements
Add authentication and authorization for system access.
Integrate a persistent database for ticket records.
Enhance the frontend with real-time updates and dashboards.
