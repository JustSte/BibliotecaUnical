import { CommonModule, DatePipe } from '@angular/common';
import { Component, computed, effect, inject, signal } from '@angular/core';
import { Locker } from '../../models/locker.model';
import { LockerService } from '../../services/locker/locker.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { AuthService } from '../../services/auth/auth.service';
import { switchMap, tap } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';



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
  isReserving = signal<Boolean>(false);
  lockerReserved = signal<Locker | null>(null);
  private readonly lockerService = inject(LockerService);
  private authService = inject(AuthService);

  count = signal(0);

  constructor(
    private confirmationService: ConfirmationService,
    private messageService: MessageService
  ) {}

  private loggingEffect = effect(() => {
    console.log(`The count is: ${this.count()}`);
  });

/*   private changeSideEffect = effect(() => {
    console.log(`Side changed: ${this.lockerSide()}`);
    this.loadLockers();
    
  }); */

/*   private checkIfUserReservedLocker = effect(() => {
    if(!this.authService.isAuthenticated())
    {
      console.log("in");
      return;
    }
    console.log("out");
    let lockerId = this.authService.getUserLockerReserved();
    console.log("aaa", lockerId);
    if (lockerId !== null && lockerId !== 0) {
      this.lockerService.getLocker(lockerId).subscribe((locker) => {
      this.lockerReserved.set(locker);
      console.log(this.lockerReserved());
      });
    }
  }); */

  incrementButton() {
    this.authService.refreshUser();
    this.count.update((value) => value + 1);
    this.loadLockers();
  }

  chooseLocker(lockerSide: 'left' | 'right') {
    this.lockerSide.set(lockerSide);
    this.loadLockers();
  }

  unChooseLocker() {
    this.lockers.set([]);
    this.lockerSide.set(null);
  }

  loadLockers() {
    console.log('Loading lockers');
    const side = this.lockerSide();
    if (!side) return;

    this.lockerService.getSideLockers(side).subscribe({
      next: (lockers) => this.lockers.set(lockers),
      error: (err) => console.error('Error on loading lockers', err),
    });
    this.lockerService.checkIfUserReservedLocker().subscribe((value) => 
    {
      if(value !== null)
      {
        console.log("CheckUserLocker!:: ", value)
        this.lockerReserved.set(value);
      }
      });
      this.authService.refreshUser();
    console.log(this.lockerReserved(), 'locker reserved to check');
    //this.authService.refreshUser();
  }

  manual(){
    this.lockerService.getLocker(255).pipe(tap((value) => console.log(value, " ciao"))).subscribe();
  }

  freeLockerFromReservation(locker: Locker) {
    if (locker === null || this.isReserving()) {
      console.error('Errore: locker not defined!');
      return;
    }

    this.lockerService.freeLocker(locker).pipe(
        tap(() => {
        })
      )
      .subscribe({
        next: () => {
          this.isReserving.set(true);
          this.lockerReserved.set(null);
          this.authService.refreshUser();
          setTimeout(() => {
          this.loadLockers();
          this.isReserving.set(false);
        }, 1000);
          console.log('Locker free.')
        },
        error: (err) => alert(err.message),
      });
  }

  onLockerClick(locker: Locker) {
    if (this.isReserving()) return;


    this.lockerService.reserveLocker(locker.id, 'LOCKER').pipe(
    ).subscribe({
      next: (res) => {
        this.isReserving.set(true);
        this.lockerReserved.set(res);
        this.authService.refreshUser();
        setTimeout(() => {
          this.loadLockers();
          this.isReserving.set(false);
        }, 1000);
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
        console.log(locker, ' Locker confirm');
        this.onLockerClick(locker);
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



