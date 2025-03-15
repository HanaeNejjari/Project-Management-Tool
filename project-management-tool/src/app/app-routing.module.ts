import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RegisterComponent} from './auth/register/register.component';
import {LoginComponent} from './auth/login/login.component';
import {ChoiceComponent} from './auth/choice/choice.component';
import {HomeComponent} from './home/home.component';
import {authGuard} from './auth/auth.guard';

const routes: Routes = [
  { path: '', redirectTo: 'auth/choice', pathMatch: 'full' },
  { path: 'auth/choice', component: ChoiceComponent },
  { path: 'auth/login', component: LoginComponent },
  { path: 'auth/register', component: RegisterComponent },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
