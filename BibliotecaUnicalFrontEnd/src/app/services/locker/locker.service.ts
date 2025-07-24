import { inject, Injectable } from '@angular/core';
import { Locker } from '../../models/locker.model';
import { map, Observable, of, switchMap, tap, throwError } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Page } from '../../models/page.models';
import { ReservationRequest } from '../../models/reservation-request.model';
import { ReservationService } from '../reservation/reservation.service';

@Injectable({
  providedIn: 'root',
})
export class LockerService {
  private apiUrl = 'http://localhost:8080/api/lockers';
  private http = inject(HttpClient);
  private readonly reservationService = inject(ReservationService);

  getListLockers(page: number = 0, size: number = 5): Observable<Page<Locker>> {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Locker>>(this.apiUrl, { params });
  }

  getLocker(id: number): Observable<Locker> {
    return this.http.get<Locker>(`${this.apiUrl}/${id}`);
  }

  getSideLockers(side: string): Observable<Locker[]> {
    return this.http.get<Locker[]>(`${this.apiUrl}/side/${side}`);
  }

  reserveLocker(id: number, type: string): Observable<Locker | null> {
    const request: ReservationRequest = {
      resourceId: id,
      resourceType: type,
      userId: '',
    };

    return this.checkIfUserReservedLocker().pipe(
      switchMap((lockerAlreadyReserved) => {
        if (lockerAlreadyReserved && lockerAlreadyReserved.id) {
          console.warn('Locker already reserved by user!');
          return throwError(
            () => new Error('You cannot reserve another locker!')
          );
        }

        return this.getLocker(request.resourceId).pipe(
          switchMap((locker) => {
            if (locker.reserved) {
              console.warn('Locker already reserved by someone else.');
              return of(null);
            } else {
              return this.reservationService
                .createReservation(request)
                .pipe(map(() => locker));
            }
          })
        );
      })
    );
  }

  freeLocker(locker: Locker): Observable<String> {
    return this.reservationService.cancelReservation(locker.reservationId!);
  }

  checkIfUserReservedLocker(): Observable<Locker | null> {
    return this.reservationService.getActiveReservationForUser('locker').pipe(
      switchMap((reservation) => {
        if (reservation && reservation.resourceId) {
          return this.getLocker(reservation.resourceId);
        }
        return of(null);
      })
    );
  }
}
