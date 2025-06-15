import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { User, CreateUserRequest } from '../../models/user.model';
import { AppState } from '../../store/app.state';
import { loadUsers, createUser } from '../../store/user/user.actions';
import { selectAllUsers, selectUsersLoading, selectUsersError } from '../../store/user/user.selectors';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="max-w-7xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Gestion des utilisateurs</h1>
        <p class="text-gray-600">Consultez et gérez la liste des utilisateurs</p>
      </div>
      
      <div class="bg-white rounded-lg shadow-md">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h2 class="text-lg font-semibold text-gray-900">Liste des utilisateurs</h2>
            <button (click)="openAddUserModal()" class="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors">
              Ajouter un utilisateur
            </button>
          </div>
        </div>
        
        <div class="p-6">
          <div *ngIf="loading$ | async" class="flex justify-center py-8">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600"></div>
          </div>
          
          <div *ngIf="error$ | async as error" class="bg-red-50 border border-red-200 rounded-lg p-4 mb-4">
            <div class="flex">
              <svg class="w-5 h-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
              <div class="ml-3">
                <p class="text-red-800">{{ error }}</p>
              </div>
            </div>
          </div>
          
          <div *ngIf="!(loading$ | async) && !(error$ | async)">
            <div *ngIf="!(users$ | async)?.length" class="text-center py-8">
              <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
              </svg>
              <h3 class="mt-2 text-sm font-medium text-gray-900">Aucun utilisateur</h3>
              <p class="mt-1 text-sm text-gray-500">Commencez par ajouter un nouvel utilisateur.</p>
            </div>
            
            <div *ngIf="(users$ | async)?.length! > 0" class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Utilisateur
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Email
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Pseudo
                    </th>
                    <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr *ngFor="let user of users$ | async" class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="flex items-center">
                        <div class="w-10 h-10 bg-primary-500 rounded-full flex items-center justify-center">
                          <span class="text-white text-sm font-medium">{{ user.pseudo?.value?.charAt(0)?.toUpperCase() || '?' }}</span>
                        </div>
                        <div class="ml-4">
                          <div class="text-sm font-medium text-gray-900">{{ user.pseudo?.value || 'Sans nom' }}</div>
                          <div class="text-sm text-gray-500">ID: {{ user.id?.value || 'inconnu' }}</div>
                        </div>
                      </div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="text-sm text-gray-900">{{ user.email?.value || 'Pas d\'email' }}</div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="text-sm text-gray-900">{{ user.pseudo?.value || 'Sans pseudo' }}</div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <button class="text-primary-600 hover:text-primary-900 mr-4">Modifier</button>
                      <button class="text-red-600 hover:text-red-900">Supprimer</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal pour ajouter un utilisateur -->
    <div *ngIf="showModal" class="fixed inset-0 flex items-center justify-center z-50">
      <div class="absolute inset-0 bg-black opacity-50" (click)="closeModal()"></div>
      <div class="bg-white rounded-lg shadow-lg w-full max-w-md z-10 relative">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-semibold text-gray-900">Ajouter un nouvel utilisateur</h3>
            <button (click)="closeModal()" class="text-gray-400 hover:text-gray-500">
              <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <form [formGroup]="userForm" (ngSubmit)="submitForm()" class="p-6">
          <div class="mb-4">
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Adresse email</label>
            <input
              type="email"
              id="email"
              formControlName="email"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              placeholder="email@exemple.com"
            />
            <div *ngIf="userForm.get('email')?.invalid && userForm.get('email')?.touched" class="mt-1 text-sm text-red-600">
              Email invalide
            </div>
          </div>

          <div class="mb-4">
            <label for="pseudo" class="block text-sm font-medium text-gray-700 mb-1">Pseudo</label>
            <input
              type="text"
              id="pseudo"
              formControlName="pseudo"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-primary-500 focus:border-primary-500"
              placeholder="Votre pseudo"
            />
            <div *ngIf="userForm.get('pseudo')?.invalid && userForm.get('pseudo')?.touched" class="mt-1 text-sm text-red-600">
              Pseudo requis (minimum 3 caractères)
            </div>
          </div>

          <div *ngIf="error$ | async as error" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-md">
            <p class="text-sm text-red-600">{{ error }}</p>
          </div>

          <div class="flex justify-end space-x-3 mt-6">
            <button
              type="button"
              (click)="closeModal()"
              class="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 transition-colors"
            >
              Annuler
            </button>
            <button
              type="submit"
              class="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <div *ngIf="isSubmitting" class="flex items-center">
                <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                Traitement...
              </div>
              <span *ngIf="!isSubmitting">Sauvegarder</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  `
})
export class UsersComponent implements OnInit {
  users$: Observable<User[]>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  userForm: FormGroup;
  showModal = false;
  isSubmitting = false;

  constructor(
    private store: Store<AppState>,
    private fb: FormBuilder
  ) {
    this.users$ = this.store.select(selectAllUsers);
    this.loading$ = this.store.select(selectUsersLoading);
    this.error$ = this.store.select(selectUsersError);

    // S'abonner aux actions pour gérer la création d'utilisateur
    this.store.select(state => state.user).subscribe(userState => {
      if (!userState.loading && this.isSubmitting) {
        if (!userState.error) {
          // Création réussie
          this.isSubmitting = false;
          this.closeModal();
          this.store.dispatch(loadUsers());
        } else {
          // Erreur lors de la création
          this.isSubmitting = false;
          // Laisser le modal ouvert pour correction
        }
      }
    });

    this.userForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      pseudo: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  ngOnInit() {
    this.store.dispatch(loadUsers());

    // Débogage des utilisateurs
    this.users$.subscribe(users => {
      console.log('Utilisateurs dans le composant:', users);
    });
  }

  openAddUserModal() {
    this.showModal = true;
    this.userForm.reset();
  }

  closeModal() {
    this.showModal = false;
  }

  submitForm() {
    console.log(this.userForm.value);
    if (this.userForm.invalid) return;

    this.isSubmitting = true;
    const userData: CreateUserRequest = {
      email: this.userForm.value.email,
      pseudo: this.userForm.value.pseudo
    };

    this.store.dispatch(createUser({ user: userData }));
    // Le reste est géré par l'abonnement au store dans le constructeur
  }
}