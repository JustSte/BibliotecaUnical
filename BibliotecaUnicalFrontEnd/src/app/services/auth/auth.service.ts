import { inject, Injectable, signal } from '@angular/core';
import Keycloak from 'keycloak-js';
import { User } from '../../models/user.model';
import { BehaviorSubject, from, map, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly keycloak = inject(Keycloak);
  user = signal<User | null>(null);
  isAuthenticated = signal<boolean>(false);
  
  initUser(): Observable<User>
  {
      if(!this.keycloak?.authenticated)
      {
        return of(null as any);
      }
      return from(this.keycloak.loadUserProfile()).pipe(
        map((profile) => {
          const user: User = {
          id: profile.id as string,
          name: `${profile?.firstName} ${profile.lastName}`,
          email: profile.email,
          username: profile.username,
          lockerReserved: Number(profile.attributes?.['lockerReserved'] || 0),
          seatReserved: Number(profile.attributes?.['seatReserved']|| 0)
          };
          console.warn("attributes: ", profile.attributes);
          this.isAuthenticated.set(true);
          this.user.set(user);
          return user;
        })
      )
      
  }



  accountManagement() {
    this.keycloak.accountManagement();
  }

  refreshUser(): void {
    this.keycloak.updateToken(100000000);
    this.keycloak.onAuthRefreshSuccess = () => ("Token refreshed");
    this.initUser().subscribe((user) => this.user.set(user));
  }

  getIsAuthenticated(): boolean
  {
    return this.isAuthenticated();
  }

  getUser() : Observable<User | null>{
    return of(this.user());
  }

  getUserId() : string | undefined {
    return this.user()?.id;
  }
  
  getUserName() : string| undefined {
    return this.user()?.name;
  }

  getUserEmail() : string | undefined {
    return this.user()?.email;
  }

  getUserUsername() : string | undefined
  {
    return this.user()?.username;
  }

  getUserLockerReserved(): number | null
  {
    return this.user()!.lockerReserved;
  }
  
   getUserSeatReserved(): number | undefined
  {
    return this.user()?.seatReserved;
  }
}
