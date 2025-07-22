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
    this.authService.refreshUser();
  }

}
