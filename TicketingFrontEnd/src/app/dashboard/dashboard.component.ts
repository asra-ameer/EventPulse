import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { interval, Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  imports: [FormsModule, CommonModule],
})
export class DashboardComponent implements OnInit, OnDestroy {
  availableTickets: number = 0;
  ticketsSold: number = 0;
  vendorId?: number;
  ticketReleaseRate?: number;
  customerId?: number;
  customerRetrievalRate?: number;
  maxEventTickets:number=  0;
  maxPoolTickets: number = 0;
  message?: string;
  private pollingSubscription?: Subscription;
  private isStopped: boolean = false;
  logs: string[] = [];

  constructor(private http: HttpClient) {
    
  }

  ngOnInit(): void {
    this.startPolling();
    this.pollingSubscription = interval(10).subscribe(() => {
      this.getTicketCount();
    });
  }

  startPolling() {
    this.pollingSubscription = interval(500).subscribe(() => {
      this.getTicketCount();
    
    });
  }

  getTicketCount() {
    this.http
      .get<{ availableTickets: number; ticketsSold: number }>(
        'http://localhost:8080/api/eventticket/status'
      )
      .subscribe(
        (response) => {
          this.availableTickets = response.availableTickets;
          this.ticketsSold = response.ticketsSold;
          this.isStopped = false;
    
        },
        (error) => {
          console.error('Error fetching ticket count', error);

          if (!this.isStopped) {
            this.message =
              'Application is stopped. Ticket count not available.';
            this.isStopped = true;
          }
        }
      );
  }
  
  startVendor() {
    if (
      !this.vendorId ||
      !this.ticketReleaseRate ||
      this.vendorId <= 0 ||
      this.ticketReleaseRate <= 0
    ) {
      this.message =
        'Please enter valid positive values for Vendor ID and Ticket  Release Rate.';
      return;
    }
    this.http
      .post('http://localhost:8080/api/eventticket/start-vendor', null, {
        params: {
          vendorId: this.vendorId,
          ticketReleaseRate: this.ticketReleaseRate,
        },
        responseType: 'text',
      })
      .subscribe(
        (response) => {
          this.message = response;
          this.getTicketCount();
        },
        (error) => {
          console.error('Error starting vendor', error);
          this.message = 'Failed to start vendor';
        }
      );
  }

  startCustomer() {
    if (
      !this.customerId ||
      !this.customerRetrievalRate ||
      this.customerId <= 0 ||
      this.customerRetrievalRate <= 0
    ) {
      this.message =
        'Please enter valid positive values for Customer ID and Customer Retrieval Rate.';

      return;
    }
    this.http
      .post('http://localhost:8080/api/eventticket/start-customer', null, {
        params: {
          customerId: this.customerId.toString(),
          customerRetrievalRate: this.customerRetrievalRate.toString(),
        },
        responseType: 'text',
      })
      .subscribe(
        (response) => {
          this.message = response;
          this.getTicketCount();
        },
        (error) => {
          console.error('Error starting customer', error);
          this.message = 'Failed to start customer';
        }
      );
  }

  setMaxEventTickets() {
    this.http
    .post(
      `http://localhost:8080/api/eventticket/set-max-event-tickets`, 
      null,  // No body
      {
        params: { maxEventTickets: this.maxEventTickets.toString() },  // Send correct query param
        responseType: 'text'
      }
    )
    .subscribe((response) => {
      this.message = response;
      console.log(this.maxPoolTickets);
    }, (error) => {
      console.error('Error occurred:', error);
      this.message = 'An error occurred while setting the max event tickets.';
    });
  }

  setMaxPoolTickets() {
    this.http
    .post(
      `http://localhost:8080/api/eventticket/set-max-pool-tickets`, 
      null,  // No body
      {
        params: { maxPoolTickets: this.maxPoolTickets.toString() },  // Send correct query param
        responseType: 'text'
      }
    )
    .subscribe((response) => {
      this.message = response;
      console.log(this.maxPoolTickets);
    }, (error) => {
      console.error('Error occurred:', error);
      this.message = 'An error occurred while setting the max pool tickets.';
    });
  }

  clearLogs(): void {
    this.logs = [];
  }

  stopAll() {
    this.http
      .post('http://localhost:8080/api/eventticket/stop', null, {
        responseType: 'text',
      })
      .subscribe(
        (response) => {
          this.message = 'System stopped and reset.';
          this.isStopped = true;

          // Reset frontend values
          this.availableTickets = 0;
          this.ticketsSold = 0;
          this.vendorId = 0;
          this.ticketReleaseRate = 0;
          this.customerId = 0;
          this.customerRetrievalRate = 0;
          this.maxEventTickets = 1000;
          this.maxPoolTickets = 200;

          // Unsubscribe from polling
          if (this.pollingSubscription) {
            this.pollingSubscription.unsubscribe();
          }
        },
        (error) => {
          console.error('Error stopping and resetting system', error);
          this.message = 'Failed to stop and reset system.';
        }
      );
  }
  ngOnDestroy(): void {
    if (this.pollingSubscription) {
      this.pollingSubscription.unsubscribe();
    }
  }
}
