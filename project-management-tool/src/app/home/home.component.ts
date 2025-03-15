import { Component } from '@angular/core';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: false,

  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  userInfo: any;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.userInfo = this.userService.getUserInfo();
  }

  logout(): void {
    console.log('logout');
    this.userService.logout();
    this.router.navigate(['/auth/choice']);

  }

}
