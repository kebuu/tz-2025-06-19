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
          map(user => {
            console.log('Utilisateur créé avec succès:', user);
            return UserActions.createUserSuccess({ user });
          }),
          catchError(error => {
            console.error('Erreur lors de la création de l\'utilisateur:', error);
            return of(UserActions.createUserFailure({ 
              error: error.message || 'Erreur lors de la création de l\'utilisateur' 
            }));
          })
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