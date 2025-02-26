package com.example.ticketingSystem.Service;
import com.example.ticketingSystem.modules.Ticket;
import java.util.logging.Logger;

public class CustomerService implements Runnable{

    private TicketPoolService ticketPoolService;
    private final int customerID;
    private int customerRetrievalRate;
    private boolean running = true;
    private static final Logger log = Logger.getLogger(CustomerService.class.getName());

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
                       log.info("Customer"+customerID+"purchased ticket"+ticket.getTicketID());
                   }else{
                       log.warning("Customer"+customerID+"found no tickets available.");
                   }
               }
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                log.warning("Customer"+customerID+"interrupted");
                break;
            }
        }
    }
}



