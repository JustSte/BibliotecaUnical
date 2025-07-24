import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BooksComponent } from './components/books/books.component';
import { canActivateAuthRole } from './guards/auth-role.guard';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { LockersComponent } from './components/lockers/lockers.component';
import { TableComponent } from './components/table/table.component';

export const routes: Routes = [
  { path: '',
    component : HomeComponent
  },
  { 
    path: 'books',
    component: BooksComponent,
    canActivate: [canActivateAuthRole],
    data:{role: ['USER']}
  },
  {
    path: 'lockers',
    component: LockersComponent,
    canActivate: [canActivateAuthRole],
    data: {role : 'USER'}
  },
  {
    path: 'table',
    component: TableComponent,
    canActivate: [canActivateAuthRole],
    data: {role : 'USER'}
  },
  {
    path: 'profile',
    component: UserProfileComponent,
    canActivate: [canActivateAuthRole],
    data: {role : 'USER'}
  },
  { path: 'forbidden', component: ForbiddenComponent},
  { path: '**', component: NotFoundComponent}
];