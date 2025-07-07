import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BooksComponent } from './components/books/books.component';
import { canActivateAuthRole } from './guards/auth-role.guard';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import { NotFoundComponent } from './components/not-found/not-found.component';

export const routes: Routes = [
  { path: '',
    component : HomeComponent
  },
  { 
    path: 'books',
    component: BooksComponent,
/*     canActivate: [canActivateAuthRole],
    data:{role : 'USER'} */
  },
  {
    path: 'profile',
    component: UserProfileComponent,
/*     canActivate: [canActivateAuthRole],
    data: {role : 'USER'} */
  },
  { path: 'forbidden', component: ForbiddenComponent},
  { path: '**', component: NotFoundComponent}
];