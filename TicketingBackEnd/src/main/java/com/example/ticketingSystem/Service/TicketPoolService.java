package com.example.ticketingSystem.Service;
import com.example.ticketingSystem.modules.Ticket;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Service
public class TicketPoolService {
    public final Queue<Ticket> ticketPool = new ConcurrentLinkedQueue<>();
    private  int poolTicketLimit=500;
    private int  EventTicketLimit=10000;
    private int ticketSold=0;
    private static final Logger log = Logger.getLogger(TicketPoolService.class.getName());
    private Set<Integer> signedupSeller = new HashSet<>();
    private Set<Integer> signedUpCustomer = new HashSet<>();

    public synchronized void addTicket(int vendorID, Ticket ticket) {
        if ((ticketPool.size() +ticketSold)  >= EventTicketLimit) {
         log.warning("Maximum event tickets reached: "+EventTicketLimit+", Vendor "+ vendorID + " cannot add ticket.");
          return;
        }
        if (ticketPool.size()  >= poolTicketLimit){
          log.warning("Maximum event tickets reached :  "+poolTicketLimit+", Vendor "+ vendorID + " cannot add ticket.");
          return;
        }

        ticketPool.add(ticket);
            log.info("Vendor " +vendorID+ " Added ticket "+ticket.getTicketID() );

    }
    public synchronized void resetTicketPool() {
        ticketPool.clear();
        ticketSold=0;
        signedupSeller.clear();
        signedUpCustomer.clear();
        log.info("Ticket pool cleared");
    }
    //remove means a selling a ticket
    public synchronized Ticket removeTicket() {
        Ticket ticket = ticketPool.poll();
        if (ticket !=null) {
            ticketSold++;
            log.info("Ticket Purchased: "+ticket.getTicketID());
        }else{
            log.warning("No ticket available for purchase   ");
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
        if (signedupSeller.contains(vendorID)) {
            log.warning("Vendor "+vendorID + " already registered");
            return false;
        }
        signedupSeller.add(vendorID);
        log.info("Vendor registered with ID"+vendorID);
        return true;
    }
    //Register customer and check if the ID is unique
    public synchronized boolean registerCustomer(int customerID){
        if (signedUpCustomer.contains(customerID)) {
            log.warning("Customer "+customerID + " already registered");
            return false;
        }
        signedUpCustomer.add(customerID);
        log.info("Customer registered with ID"+customerID);
        return true;
    }
    public int getEventTicketLimit() {
        return EventTicketLimit;
    }

    public void setEventTicketLimit(int EventTicketLimit) {
        this.EventTicketLimit = EventTicketLimit;
    }

    public int getpoolTicketLimit() {
        return poolTicketLimit;
    }

    public void setpoolTicketLimit(int poolTicketLimit) {
        this.poolTicketLimit = poolTicketLimit;
    }
}
