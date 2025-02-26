package com.example.ticketingSystem.Service;

import com.example.ticketingSystem.modules.Ticket;
import java.util.logging.Logger;

public class VendorService implements Runnable {
    private static final Logger log = Logger.getLogger(TicketPoolService.class.getName());
    private TicketPoolService ticketPoolService;
    private int vendorID;
    private int releaseRate;
    private boolean running=true;

    public VendorService(TicketPoolService ticketPoolService, int vendorID, int releaseRate) {
        if (!ticketPoolService.registerVendor(vendorID)){
            throw new IllegalArgumentException(
                    "Vendor ID "+vendorID+" already exists"
            );
        }
        this.ticketPoolService = ticketPoolService;
        this.vendorID = vendorID;
        this.releaseRate = releaseRate;
    }

    public void setTicketPoolService(TicketPoolService ticketPoolService) {
        this.ticketPoolService = ticketPoolService;
    }

    @Override
    public void run() {
        int ticketID=1;
        while (running){
            try{
                Thread.sleep(releaseRate);
                if (ticketPoolService !=null){
                    if (ticketPoolService.getAvailableTickets()+ ticketPoolService.getTicketsSold() >=ticketPoolService.getEventTicketLimit()){
                        log.warning("Vendor"+ vendorID + " cannot add ticket. Maximum event ticket limit reached.");
                        return;
                    }
                    if (ticketPoolService.getAvailableTickets() >= ticketPoolService.getpoolTicketLimit()){
                        log.info("Vendor "+ vendorID + " waiting,max pool limit reached ");
                        continue;
                    }
                    Ticket ticket = new Ticket(ticketID++);
                    ticketPoolService.addTicket(vendorID, ticket);
                }

            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warning("Vendor"+ vendorID + " interrupted");
                break;
            }
        }
    }
}
