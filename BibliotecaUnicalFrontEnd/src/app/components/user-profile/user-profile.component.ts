import { Component, inject, OnInit } from '@angular/core';
import Keycloak from 'keycloak-js';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-profile',
  imports: [],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})

export class UserProfileComponent implements OnInit{
  private readonly keycloak = inject(Keycloak);
  
  user: User | undefined;

  async ngOnInit()
  {
      if(this.keycloak?.authenticated)
      {
        const profile = await this.keycloak.loadUserProfile();

        this.user = {
          name: `${profile?.firstName} ${profile.lastName}`,
          email: profile.email,
          username: profile.username
        };
      }
  }
}
