import { Component, effect, inject, OnInit, signal} from '@angular/core';
import { User } from '../../models/user.model';
import { AuthService } from '../../services/auth/auth.service';
import { AsyncPipe, CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent implements OnInit {
  private readonly authService = inject(AuthService);
  isLoading = signal<boolean>(false);
  user = signal<User | null>(null);

  accountManagement() {
    this.authService.accountManagement();
  }

  ngOnInit() {
    this.authService.initUser().subscribe((user) => {
      this.isLoading.set(true);
      setTimeout(() => {
        this.user.set(user);
        this.isLoading.set(false);
        }, 500);
      });
  }

}
