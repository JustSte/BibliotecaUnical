import { inject, Injectable } from '@angular/core';
import { Locker } from '../../models/locker.model';
import { Observable, of, switchMap, tap } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Page } from '../../models/page.models';
import { ReservationRequest } from '../../models/reservation-request.model';
import { ReservationService } from '../reservation/reservation.service';
import { Reservation } from '../../models/reservation.model';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LockerService {

  private apiUrl = 'http://localhost:8080/api/lockers';
  private http = inject(HttpClient);
  private lockers: Locker[] = [];
/*   private request!: ReservationRequest; */
  private readonly reservationService = inject(ReservationService);
  private readonly authService = inject(AuthService);

  private mockLockers: Locker[] = 
  [
  { id: 1, occupied: false, reserved: false, side: 'left' },
  { id: 2, occupied: false, reserved: false, side: 'left' },
  { id: 3, occupied: false, reserved: false, side: 'left' },
  { id: 4, occupied: false, reserved: false, side: 'left' },
  { id: 5, occupied: false, reserved: false, side: 'left' },
  { id: 6, occupied: false, reserved: false, side: 'left' },
  { id: 7, occupied: false, reserved: false, side: 'left' },
  { id: 8, occupied: false, reserved: false, side: 'left' },
  { id: 9, occupied: false, reserved: false, side: 'left' },
  { id: 10, occupied: false, reserved: false, side: 'left' },
  { id: 11, occupied: false, reserved: false, side: 'left' },
  { id: 12, occupied: false, reserved: false, side: 'left' },
  { id: 13, occupied: false, reserved: false, side: 'left' },
  { id: 14, occupied: false, reserved: false, side: 'left' },
  { id: 15, occupied: false, reserved: false, side: 'left' }
  ];

  getListLockers(page: number = 0, size: number = 5):Observable<Page<Locker>>
  {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Locker>>(this.apiUrl, {params});
  };

  getLocker(id:number):Observable<Locker>{
    return this.http.get<Locker>(`${this.apiUrl}/${id}`)
  };

  getSideLockers(side:string):Observable<Locker[]>
  {
    return this.http.get<Locker[]>(`${this.apiUrl}/side/${side}`)
  }

  getLockerMock(side:string):Observable<Locker[]>
  {
    const filteredLockers = this.mockLockers.filter(l => l.side === side);
    return of(filteredLockers);
  };

  reserveLocker(id:number, type:string): Observable<Locker>
  {
    const request: ReservationRequest = {
      resourceId: id,
      resourceType: type,
      userId: ''
    }
    request.resourceId = id;
    request.resourceType= type;
    this.reservationService.createReservation(request).subscribe();
    return this.getLocker(id);
  }

  freeLocker(locker: Locker): Observable<String>
  {
    console.log("Reservation id in locekrService: ", locker.reservationId)
    return this.reservationService.cancelReservation(locker.reservationId!);
  }

  checkIfUserReservedLocker(): Observable<Locker | null>
  {
    return this.reservationService.getActiveReservationForUser("locker").pipe(
      switchMap(reservation => {
        if(reservation && reservation.resourceId)
        {
          return this.getLocker(reservation.resourceId);
        }
        return of(null);
      })
    );
  }

}
