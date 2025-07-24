import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { TableService } from '../../services/table/table.service';
import { Table } from '../../models/table.model';
import { Chair } from '../../models/chair.model';
import { ChairService } from '../../services/chair/chair.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-table',
  imports: [CommonModule, ConfirmDialog, ToastModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './table.component.html',
  styleUrl: './table.component.css',
})
export class TableComponent implements OnInit {
  private readonly tableService = inject(TableService);
  private readonly chairService = inject(ChairService);
  private confirmationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  tables = signal<Table[] | null>(null);
  isReserving = signal<boolean>(false);
  chairReserved = signal<Chair | null>(null);

  ngOnInit() {
    this.getAllTables();
    this.tableService.checkIfUserReservedChair().subscribe((value) => {
      if(value)
      {
        this.chairReserved.set(value);
      }
    });
  }
  /* getTableByTableId(id:number){
    this.tableService.getTableById(id).subscribe((table) => {
      this.table.set(table);
    });
  } */

  getAllTables() {
    this.tableService.getAllTables().subscribe((tables) => {
      this.tables.set(tables);
    });
  }

  reserveChair(chair: Chair) {
    console.log("AAAA", chair);
    if (this.isReserving()) return;
    this.tableService.reserveChair(chair.id, 'CHAIR').subscribe({
      next: (chair) => {
        console.warn("Chair reserve component:", chair);
        if (chair !== null && chair.id) {
          this.isReserving.set(true);
          this.chairReserved.set(chair);
          setTimeout(() => {
            this.ngOnInit();
            this.isReserving.set(false);
          }, 1000);
        }
      },
      error: (err) => {
        this.isReserving.set(false);
      },
    });
  }

  freeChair(chair: Chair) {
    if (!chair || this.isReserving()) {
      return;
    }

    this.tableService.freeChair(chair).subscribe({
      next: () => {
        this.isReserving.set(true);
        this.chairReserved.set(null);
        setTimeout(() => {
          this.ngOnInit();
          this.isReserving.set(false);
        }, 1000);
      },
    });
  }

  confirmReservation(event: Event, chair: Chair) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure that you want to reserve this chair?',
      header: 'Confirmation',
      closable: true,
      closeOnEscape: true,
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
        label: 'Cancel',
        severity: 'secondary',
        outlined: true,
      },
      acceptButtonProps: {
        label: 'Confirm',
      },
      accept: () => {
        this.reserveChair(chair);
        this.messageService.add({
          severity: 'info',
          summary: 'Confirmed',
          detail: 'You have accepted',
        });
      },
      reject: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Rejected',
          detail: 'You have rejected',
          life: 3000,
        });
      },
    });
  }

  cancelReservation(event: Event, chair: Chair) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure that you want to free this chair?',
      header: 'Cancellation',
      closable: true,
      closeOnEscape: true,
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
        label: 'Cancel',
        severity: 'secondary',
        outlined: true,
      },
      acceptButtonProps: {
        label: 'Confirm',
      },
      accept: () => {
        this.freeChair(chair);
        console.warn('Chair cancel:', chair);
        this.messageService.add({
          severity: 'info',
          summary: 'Cancel reservation',
          detail: 'You have accepted to cancel the reservation',
        });
      },
      reject: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Rejected',
          detail: 'You have rejected',
          life: 3000,
        });
      },
    });
  }
}
