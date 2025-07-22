import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenuComponent } from './components/menu/menu.component';
import { AuthService } from './services/auth/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, MenuComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'BibliotecaUnical';
  constructor(private authService: AuthService)
  {
  }

  async ngOnInit()
  {
    this.authService.initUser().subscribe((user) => console.log(user , " user dopo init"));
  }
}
