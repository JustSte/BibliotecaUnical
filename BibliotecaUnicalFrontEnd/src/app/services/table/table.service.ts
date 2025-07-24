import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ReservationService } from '../reservation/reservation.service';
import { map, Observable, of, switchMap, throwError } from 'rxjs';
import { Table } from '../../models/table.model';
import { Chair } from '../../models/chair.model';
import { ReservationRequest } from '../../models/reservation-request.model';
import { ChairService } from '../chair/chair.service';

@Injectable({
  providedIn: 'root',
})
export class TableService {
  private apiUrl = 'http://localhost:8080/api/libraryTable';
  private http = inject(HttpClient);
  private readonly reservationService = inject(ReservationService);
  private readonly chairService = inject(ChairService);

  getTableById(id: number): Observable<Table> {
    return this.http.get<Table>(`${this.apiUrl}/${id}`);
  }

  getAllTables(): Observable<Table[]> {
    return this.http.get<Table[]>(this.apiUrl);
  }


  reserveChair(id: number, type: string): Observable<Chair | null> {
    const request: ReservationRequest = {
      resourceId: id,
      resourceType: type,
      userId: '',
    };
    console.warn("REQUEST", request.resourceId, request.resourceType)
    return this.checkIfUserReservedChair().pipe(
      switchMap((chairAlreadyReserved) => {
        if (chairAlreadyReserved && chairAlreadyReserved.id) {
          console.warn('Chair already reserved by user!');
          return throwError(
            () => new Error('You cannot reserve another chair!')
          );
        }

        return this.chairService.getChairById(request.resourceId).pipe(
          switchMap((chair) => {
            console.warn("CCCC", chair)
            if (chair.reserved) {
              console.warn('Chair already reserved by someone else.', chair);
              return of(null);
            } else {

              return this.reservationService
                .createReservation(request)
                .pipe(map(() => chair));
            }
          })
        );
      })
    );
  }

  checkIfUserReservedChair(): Observable<Chair | null> {
    return this.reservationService.getActiveReservationForUser('chair').pipe(
      switchMap((reservation) => {
        if (reservation && reservation.resourceId) {
          return this.chairService.getChairById(reservation.resourceId);
        }
        return of(null);
      })
    );
  }

  freeChair(chair: Chair): Observable<String> {
    return this.reservationService.cancelReservation(chair.reservationId!);
  }
  
  constructor() {}
}
