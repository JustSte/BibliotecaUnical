import { HttpClient } from '@angular/common/http';
import { Injectable , inject} from '@angular/core';
import { ReservationRequest } from '../../models/reservation-request.model';
import { Observable, of, switchMap, throwError } from 'rxjs';
import { Reservation } from '../../models/reservation.model';
import { AuthService } from '../auth/auth.service';
import { User } from '../../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private apiUrl = 'http://localhost:8080/api/reservation';
  private http = inject(HttpClient);
  private readonly authService = inject(AuthService);

  createReservation(request:ReservationRequest): Observable<Reservation>
  {
    if(request.resourceType.toLowerCase() === "chair")
    {
      const seatsReserved = this.authService.getUserSeatReserved();
      if(seatsReserved !== undefined && seatsReserved > 1)
      {
        throw new Error("Already reserved a chair. Cancel your current chair if you want to reserve another one.");
      }
    }

    if(request.resourceType.toLowerCase() === "locker")
    {
      const lockerReserved = this.authService.getUserLockerReserved() ;
      if(lockerReserved !== null && lockerReserved! > 1)
      {
        throw new Error("Already reserved a locker. Cancel your current locker if you want to reserve another one.");
      }

    }
    return this.http.post<Reservation>(this.apiUrl, request); 
  }

  getReservationById(id:number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/${id}`);
  }

  getReservationByUserId(id:string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.apiUrl}/userId/${id}`);
  }

  getActiveReservationForUser(resource : string): Observable<Reservation>
  {
    return this.http.get<Reservation>(`${this.apiUrl}/active/resource/${resource}`)
  }

  cancelReservation(reservationId:number): Observable<string>
  {
    if(reservationId === null)
    {
      throw new Error("No such reservation!");
    }

    return this.getReservationById(reservationId).pipe(
      switchMap(reservation => {
        if (this.authService.getUserId() !== reservation.userId) {
          return throwError(() => new Error("You are not authorized to cancel this reservation!"));
        }

        return this.http.put<void>(`${this.apiUrl}/cancel/${reservation.id}`, null).pipe(
          switchMap(() => {
            return of("Reservation cancelled successfully")})
        );
      })
    );

  }


}
