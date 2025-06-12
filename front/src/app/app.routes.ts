import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'users', loadComponent: () => import('./pages/users/users.component').then(m => m.UsersComponent) },
  { path: 'daily-comments', loadComponent: () => import('./pages/daily-comment-page/daily-comment-page.component').then(m => m.DailyCommentPageComponent) },
  { path: '**', redirectTo: '' }
];