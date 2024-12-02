package com.example.ticketingSystem.Service;

import com.example.ticketingSystem.modules.Ticket;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class CustomerService implements Runnable{

    private TicketPoolService ticketPoolService;
    private final int customerID;
    private int customerRetrievalRate;
    private boolean running = true;
    private static Logger logger= (Logger) LoggerFactory.getLogger(CustomerService.class);
    public CustomerService(TicketPoolService ticketPoolService, int customerID, int customerRetrievalRate) {
        this.ticketPoolService = ticketPoolService;
        this.customerID = customerID;
        this.customerRetrievalRate = customerRetrievalRate;

        if(!ticketPoolService.registerCustomer(customerID)){
            throw new IllegalArgumentException("Customer ID " + customerID + " already exists");
        }
    }

    public void setTicketPoolService(TicketPoolService ticketPoolService) {
        this.ticketPoolService = ticketPoolService;
    }
    @Override
    public void run() {
        while(running){
            try{
               Thread.sleep(customerRetrievalRate);
               if(ticketPoolService !=null){
                   Ticket ticket= ticketPoolService.removeTicket();
                   //ticket pool service la irunthu return aahum ticket object than inga varuthuu
                   if(ticket != null){
                       logger.info("Customer"+customerID+"purchased ticket"+ticket.getTicketID());
                   }else{
                       logger.warn("Customer"+customerID+"found no tickets available.");
                   }
               }
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                logger.warn("Customer"+customerID+"interrupted");
                break;
            }
        }
    }
}



