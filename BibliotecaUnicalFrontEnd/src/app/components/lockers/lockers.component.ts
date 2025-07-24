import { CommonModule, DatePipe } from '@angular/common';
import { Component, computed, effect, inject, signal } from '@angular/core';
import { Locker } from '../../models/locker.model';
import { LockerService } from '../../services/locker/locker.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { tap } from 'rxjs';

@Component({
  selector: 'app-lockers',
  imports: [CommonModule, ConfirmDialog, ToastModule],
  providers: [ConfirmationService, MessageService],
  templateUrl: './lockers.component.html',
  styleUrl: './lockers.component.css',
})
export class LockersComponent {
  lockerChosen = computed(() => this.lockerSide() !== null);
  lockerSide = signal<'left' | 'right' | null>(null);
  lockers = signal<Locker[]>([]);
  errorMessage: string | null = null;
  isReserving = signal<boolean>(false);
  lockerReserved = signal<Locker | null>(null);
  private readonly lockerService = inject(LockerService);


  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  chooseLocker(lockerSide: 'left' | 'right') {
    this.lockerSide.set(lockerSide);
    this.loadLockers();
  }

  unChooseLocker() {
    this.lockers.set([]);
    this.lockerSide.set(null);
  }

  loadLockers() 
  {
    console.log('Loading lockers');
    const side = this.lockerSide();
    if (!side) return;

    this.lockerService.getSideLockers(side).subscribe({
      next: (lockers) => this.lockers.set(lockers),
      error: (err) => console.error('Error on loading lockers', err),
    });

    this.lockerService.checkIfUserReservedLocker().subscribe((value) => {
      if (value !== null) {
        this.lockerReserved.set(value);
      }
    });

  }

  freeLockerFromReservation(locker: Locker) {
    if (locker === null || this.isReserving()) {
      console.error('Errore: locker not defined!');
      return;
    }

    this.lockerService.freeLocker(locker).subscribe({
        next: () => {
          this.isReserving.set(true);
          this.lockerReserved.set(null);
          setTimeout(() => {
          this.loadLockers();
          this.isReserving.set(false)
        }, 1000);
        },
        error: (err) => {alert(err.message)},
      });
  }

  reserveLocker(locker: Locker) {
    if (this.isReserving()) return;
    this.lockerService.reserveLocker(locker.id, 'LOCKER').subscribe({
      next: (res) => {
        if(res !== null && res.id)
        {
          console.log("Res in reserve: " , res);
          this.isReserving.set(true);
          this.lockerReserved.set(res);
          setTimeout(() => 
          {
            this.loadLockers();
            this.isReserving.set(false);
          }, 1000);
        }
        else
        {
          console.log("Res in reserve ELSE: " , res);
          this.messageService.add({
            severity: 'error',
            summary: 'This locker is already reserved. Refresh the page!',
            life: 3000,
          });
        }
    
      },
      error: (err) => {
        this.isReserving.set(false);
        if (err.status === 409) {
          this.messageService.add({
            severity: 'error',
            summary: 'This locker is already reserved. Refresh the page!',
            life: 3000,
          });
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'Error during reservation.',
            life: 3000,
          });
        }
      },
    });
  }

  confirmReservation(event: Event, locker: Locker) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure that you want to reserve this locker?',
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
        this.reserveLocker(locker);
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

  cancelReservation(event: Event, locker: Locker) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Are you sure that you want to cancel this locker?',
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
        this.freeLockerFromReservation(locker);
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



