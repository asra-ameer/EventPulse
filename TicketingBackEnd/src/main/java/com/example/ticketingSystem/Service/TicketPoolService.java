package com.example.ticketingSystem.Service;
import com.example.ticketingSystem.modules.Ticket;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


@Service
public class TicketPoolService {
    public final Queue<Ticket> ticketPool = new ConcurrentLinkedQueue<>();
    private  int maxPoolTickets=200;
    private int  maxEventTickets=1000;
    private int ticketSold=0;
    private static final Logger logger = LoggerFactory.getLogger(TicketPoolService.class);
    private Set<Integer> registeredVendors = new HashSet<>();
    private Set<Integer> registeredCustomers = new HashSet<>();

    public synchronized void addTicket(int vendorID, Ticket ticket) {
        if ((ticketPool.size() +ticketSold)  >= maxEventTickets) {
         logger.warn("Vendor "+ vendorID + "cannot add ticket.Maximum event tickets reached: "+maxEventTickets);
          return;
        }
        if (ticketPool.size()  >= maxPoolTickets){
          logger.warn("Vendor "+ vendorID + " cannot add ticket.Maximum event tickets reached : {} "+maxPoolTickets);
          return;
        }

        ticketPool.add(ticket);
            logger.info("Vendor " +vendorID+ " Added ticket "+ticket.getTicketID() );

    }
    public synchronized void resetTicketPool() {
        ticketPool.clear();
        ticketSold=0;
        registeredVendors.clear();
        registeredCustomers.clear();
        logger.info("Ticket pool has been reset");
    }
    //remove means a selling a ticket
    public synchronized Ticket removeTicket() {
        Ticket ticket = ticketPool.poll();
        if (ticket !=null) {
            ticketSold++;
            logger.info("Ticket Purchased: "+ticket.getTicketID());
        }else{
            logger.warn("No ticket available for purchasing  ");
        }
        return ticket;
    }
    public int  getAvailableTickets() {
        return ticketPool.size();
    }

    public int getTicketsSold(){
        return ticketSold;
    }

// register vendor
    public synchronized boolean registerVendor(int vendorID){
        if (registeredVendors.contains(vendorID)) {
            logger.warn("Vendor "+vendorID + " already registered");
            return false;
        }
        registeredVendors.add(vendorID);
        logger.info("Vendor registered with ID"+vendorID);
        return true;
    }
    //Register customer and check if the ID is unique
    public synchronized boolean registerCustomer(int customerID){
        if (registeredCustomers.contains(customerID)) {
            logger.warn("Customer "+customerID + " already registered");
            return false;
        }
        registeredCustomers.add(customerID);
        logger.info("Customer registered with ID"+customerID);
        return true;
    }
    public int getMaxEventTickets() {
        return maxEventTickets;
    }

    public void setMaxEventTickets(int maxEventTickets) {
        this.maxEventTickets = maxEventTickets;
    }

    public int getMaxPoolTickets() {
        return maxPoolTickets;
    }

    public void setMaxPoolTickets(int maxPoolTickets) {
        this.maxPoolTickets = maxPoolTickets;
    }
}
