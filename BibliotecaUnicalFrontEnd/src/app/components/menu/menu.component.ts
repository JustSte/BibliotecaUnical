import { Component, effect, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import Keycloak from 'keycloak-js';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';
import { MenubarModule } from 'primeng/menubar';
import { ImageModule } from 'primeng/image';
import {
  KEYCLOAK_EVENT_SIGNAL,
  KeycloakEventType,
  typeEventArgs,
  ReadyArgs
} from 'keycloak-angular';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-menu',
  imports: [RouterModule, ButtonModule, MenubarModule, CommonModule, ImageModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})


export class MenuComponent {

toggleDarkMode() {
    const element = document.querySelector('html');
    if(element)
    {
      element.classList.toggle('my-app-dark');
      if(this.darkMode)
      {
        this.darkMode=false;
      }
      else{
        this.darkMode = true;
      }
    }
}
  items: MenuItem[];
  authenticated = false;
  keycloakStatus: string | undefined;
  darkMode:boolean | undefined;

  private readonly keycloak = inject(Keycloak);
  private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

  constructor()
  {
    effect(() => {
      const keycloakEvent = this.keycloakSignal();
      this.darkMode= true;
      this.keycloakStatus = keycloakEvent.type;
      if(keycloakEvent.type === KeycloakEventType.Ready)
      {
        this.authenticated = typeEventArgs<ReadyArgs>(keycloakEvent.args);
      }

      if(keycloakEvent.type === KeycloakEventType.AuthLogout)
      {
        this.authenticated = false;
      }
    });

    this.items = [
      { label: 'Home',  iconName: 'home', routerLink: '/' },
      { label: 'Books', iconName: 'book', routerLink: '/books' },
      { label: 'Lockers', iconName: 'key', routerLink: '/lockers' },
      { label: 'Seats', iconName: 'chair', routerLink: '/seats' },
      { label: 'Profile', iconName:'account_box', routerLink:'/profile'}
    ];

  }

  login()
  {
    this.keycloak.login();
  }

  logout()
  {
    this.keycloak.logout();
  }

  


}
