import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { UserService } from '../../services/user.service';
import * as UserActions from './user.actions';

@Injectable()
export class UserEffects {
  
  loadUsers$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.loadUsers),
      mergeMap(() =>
        this.userService.getUsers().pipe(
          map(users => UserActions.loadUsersSuccess({ users })),
          catchError(error => of(UserActions.loadUsersFailure({ 
            error: error.message || 'Erreur lors du chargement des utilisateurs' 
          })))
        )
      )
    )
  );
  
  loadUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.loadUser),
      mergeMap(action =>
        this.userService.getUserById(action.id).pipe(
          map(user => UserActions.loadUserSuccess({ user })),
          catchError(error => of(UserActions.loadUserFailure({ 
            error: error.message || 'Erreur lors du chargement de l\'utilisateur' 
          })))
        )
      )
    )
  );
  
  createUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.createUser),
      mergeMap(action =>
        this.userService.createUser(action.user).pipe(
          map(() => {
            // Since the backend returns void, we need to create a mock user response
            // In a real scenario, the backend should return the created user
            const mockUser = {
              id: { value: 'temp-id' },
              email: { value: action.user.email },
              pseudo: { value: action.user.pseudo }
            };
            return UserActions.createUserSuccess({ user: mockUser });
          }),
          catchError(error => of(UserActions.createUserFailure({ 
            error: error.message || 'Erreur lors de la création de l\'utilisateur' 
          })))
        )
      )
    )
  );
  
  updateUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.updateUser),
      mergeMap(action =>
        this.userService.updateUser(action.id, action.user).pipe(
          map(user => UserActions.updateUserSuccess({ user })),
          catchError(error => of(UserActions.updateUserFailure({ 
            error: error.message || 'Erreur lors de la mise à jour de l\'utilisateur' 
          })))
        )
      )
    )
  );
  
  deleteUser$ = createEffect(() =>
    this.actions$.pipe(
      ofType(UserActions.deleteUser),
      mergeMap(action =>
        this.userService.deleteUser(action.id).pipe(
          map(() => UserActions.deleteUserSuccess({ id: action.id })),
          catchError(error => of(UserActions.deleteUserFailure({ 
            error: error.message || 'Erreur lors de la suppression de l\'utilisateur' 
          })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private userService: UserService
  ) {}
}