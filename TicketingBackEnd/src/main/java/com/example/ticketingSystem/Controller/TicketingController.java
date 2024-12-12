package com.example.ticketingSystem.Controller;

import com.example.ticketingSystem.Service.CustomerService;
import com.example.ticketingSystem.Service.TicketPoolService;
import com.example.ticketingSystem.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


    @RestController
    @RequestMapping("api/eventticket")
    @CrossOrigin("http://localhost:4200/")
    public class TicketingController{

        private ExecutorService executorService = Executors.newFixedThreadPool(10);
        private boolean isStopped = false;
        private TicketPoolService ticketPoolService;

        @Autowired
        public TicketingController(TicketPoolService ticketPoolService) {
            this.ticketPoolService = ticketPoolService;
        }

        /**
         * Sets the maximum number of event tickets allowed.
         *
         * @param maxEventTickets The maximum number of tickets for an event (sent in the request body).
         * @return ResponseEntity containing a confirmation message.
         */

        @PostMapping("/set-max-event-tickets")
        public ResponseEntity<String> setMaxEventTickets(@RequestParam int maxEventTickets)
        {
            ticketPoolService.setEventTicketLimit(maxEventTickets);
            return ResponseEntity.ok("Max event tickets set to " + maxEventTickets);
        }

        /**
         * Sets the maximum number of tickets in the pool.
         *
         * @param maxPoolTickets The maximum number of tickets in the pool (sent in the request body).
         * @return ResponseEntity containing a confirmation message.
         */

        @PostMapping("/set-max-pool-tickets")
        public ResponseEntity<String> setMaxPoolTickets(@RequestParam int maxPoolTickets){
            ticketPoolService.setpoolTicketLimit(maxPoolTickets);
            return ResponseEntity.ok("Max pool tickets set to "+maxPoolTickets);
        }

        /**
         * Stops the system by shutting down the thread pool, resetting the ticket pool, and reinitializing the thread pool.
         *
         * @return ResponseEntity containing a confirmation message.
         */
        @PostMapping("/stop")
        public ResponseEntity<String> stopAll() {
            isStopped = true;
            executorService.shutdownNow();
            ticketPoolService.resetTicketPool();
            isStopped= false;

            this.executorService=Executors.newFixedThreadPool(10);
            return ResponseEntity.ok("System stopped and reset.");

        }

        /**
         * Starts a vendor task that releases tickets at a specified rate.
         *
         * @param vendorId          The unique ID of the vendor.
         * @param ticketReleaseRate The rate at which the vendor releases tickets.
         * @return ResponseEntity containing a confirmation message.
         */
        @PostMapping("/start-vendor")
        public ResponseEntity<String> startVendor(@RequestParam int vendorId, @RequestParam int ticketReleaseRate) {
            if (vendorId <= 0 || ticketReleaseRate <= 0) {
                return ResponseEntity.badRequest().body("Vendor ID and release rate must be positive integers.");
            }
            VendorService vendor = new VendorService(ticketPoolService,vendorId, ticketReleaseRate);
            vendor.setTicketPoolService(ticketPoolService);
            executorService.submit(vendor);
            return ResponseEntity.ok("Vendor " + vendorId + " started.");
        }

        /**
         * Starts a customer task that retrieves tickets at a specified rate.
         *
         * @param customerId            The unique ID of the customer.
         * @param customerRetrievalRate The rate at which the customer retrieves tickets.
         * @return ResponseEntity containing a confirmation message.
         */
        @PostMapping("/start-customer")
        public ResponseEntity<String> startCustomer(@RequestParam int customerId, @RequestParam int customerRetrievalRate) {
            if (customerId <= 0 || customerRetrievalRate <= 0) {
                return ResponseEntity.badRequest().body("Customer ID and retrieval rate must be positive integers.");
            }
            CustomerService customer = new CustomerService(ticketPoolService,customerId, customerRetrievalRate);
            customer.setTicketPoolService(ticketPoolService);
            executorService.submit(customer);
            return ResponseEntity.ok("Customer " + customerId + " started.");
        }

        /**
         * Provides the current status of the ticket pool.
         *
         * @return ResponseEntity containing a map with the available tickets and tickets sold.
         */
        @GetMapping("/status")
        public ResponseEntity<Map<String, Object>> status() {
            Map<String, Object> response = new HashMap<>();
            response.put("availableTickets", ticketPoolService.getAvailableTickets());
            response.put("ticketsSold", ticketPoolService.getTicketsSold());
            return ResponseEntity.ok(response); }





}
