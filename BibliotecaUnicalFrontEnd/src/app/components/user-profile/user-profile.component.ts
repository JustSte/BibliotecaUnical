import { Component, effect, inject, OnInit, signal } from '@angular/core';
import { User } from '../../models/user.model';
import { AuthService } from '../../services/auth/auth.service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-user-profile',
  imports: [],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent implements OnInit {
  private readonly authService = inject(AuthService);

  user = signal<User | null>(null);

  accountManagement() {
    this.authService.accountManagement();
  }

  ngOnInit() {
    this.authService.getUser().subscribe((user) => this.user.set(user));
  }

  detectUpdate = effect(() => {
    const user = this.user();
    if (user) {
      console.log(user, 'changed');
    }
  });

  /*   async ngOnInit()
  {
      if(this.keycloak?.authenticated)
      {
        const profile = await this.keycloak.loadUserProfile();

        this.user = {
          id: profile.id as string,
          name: `${profile?.firstName} ${profile.lastName}`,
          email: profile.email,
          username: profile.username,
          lockersReserved: profile.attributes?.["lockersReserved"] as number,
          seatsReserved: profile.attributes?.["seatsReserved"] as number
        };
      }
  } */
}
