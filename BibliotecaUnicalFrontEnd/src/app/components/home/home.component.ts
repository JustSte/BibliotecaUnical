import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AnimateOnScroll } from 'primeng/animateonscroll';
import { PrimeIcons } from 'primeng/api';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'app-home',
  imports: [CommonModule, AnimateOnScroll, AvatarGroupModule, AvatarModule, CardModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
