package com.example.ticketingSystem.config;
import com.example.ticketingSystem.Service.TicketPoolService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean(name="customTicketPoolService")
    public TicketPoolService ticketPoolService() {
        return new TicketPoolService();
    }
}
