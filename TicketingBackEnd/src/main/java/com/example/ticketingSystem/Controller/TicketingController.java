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
        @PostMapping("/set-max-event-tickets")
        public ResponseEntity<String> setMaxEventTickets(@RequestBody int maxEventTickets)
        {
            ticketPoolService.setEventTicketLimit(maxEventTickets);
            return ResponseEntity.ok("Max event tickets set to " + maxEventTickets);
        }

        @PostMapping("/set-max-pool-tickets")
        public ResponseEntity<String> setMaxPoolTickets(@RequestBody int maxPoolTickets){
            ticketPoolService.setpoolTicketLimit(maxPoolTickets);
            return ResponseEntity.ok("Max pool tickets set to "+maxPoolTickets);
        }

        @PostMapping("/stop")
        public ResponseEntity<String> stopAll() {
            isStopped = true;
            executorService.shutdownNow();
            ticketPoolService.resetTicketPool();
            isStopped= false;

            this.executorService=Executors.newFixedThreadPool(10);
            return ResponseEntity.ok("System stopped and reset.");

        }
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

        @GetMapping("/status")
        public ResponseEntity<Map<String, Object>> status() {
            Map<String, Object> response = new HashMap<>();
            response.put("availableTickets", ticketPoolService.getAvailableTickets());
            response.put("ticketsSold", ticketPoolService.getTicketsSold());
            return ResponseEntity.ok(response); }





}
