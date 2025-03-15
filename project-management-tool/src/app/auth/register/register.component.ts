import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: false,

  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})

export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage: string = '';
  hide = true;

  constructor(private fb: FormBuilder, private userService: UserService, private router: Router) {
    this.registerForm = this.fb.group({
      userName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
    })
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      console.log("Formulaire invalid");
      return;
    }
    const { userName, email, password, confirmPassword } = this.registerForm.value;
    if (password !== confirmPassword) {
      this.registerForm.get('confirmPassword')?.setErrors({ passwordsNotMatching: true});
      console.log('Les mots de passe ne correspondent pas');
      return;
    }
    this.userService.isUserExist(email).subscribe(userExist => {
      if (userExist) {
        this.errorMessage = 'Un utilisateur avec cet email existe déjà !';
        return;
      }
        this.userService.register(userName, email, password).subscribe({
          next: (isRegistered: boolean) => {
            if (isRegistered) {
              console.log('Inscription réussie');
              this.router.navigate(['/home']);
            } else {
              this.errorMessage = 'Une erreur est survenue !';
            }

          },
          error: (err) => {
            console.log('Erreur lors de l\' inscription', err);
          }
        })
    });
  }

}
