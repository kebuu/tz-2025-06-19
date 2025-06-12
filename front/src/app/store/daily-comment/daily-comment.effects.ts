import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, mergeMap, catchError } from 'rxjs/operators';
import { DailyCommentService } from '../../services/daily-comment.service';
import * as DailyCommentActions from './daily-comment.actions';

@Injectable()
export class DailyCommentEffects {
  loadDailyComment$ = createEffect(() =>
    this.actions$.pipe(
      ofType(DailyCommentActions.loadDailyComment),
      mergeMap(action =>
        this.dailyCommentService.getDailyComment(action.date).pipe(
          map(comment => DailyCommentActions.loadDailyCommentSuccess({ comment })),
          catchError(error => of(DailyCommentActions.loadDailyCommentFailure({
            error: error.message || 'Erreur lors du chargement du commentaire'
          })))
        )
      )
    )
  );

  createDailyComment$ = createEffect(() =>
    this.actions$.pipe(
      ofType(DailyCommentActions.createDailyComment),
      mergeMap(action =>
        this.dailyCommentService.createDailyComment(action.comment).pipe(
          map(comment => DailyCommentActions.createDailyCommentSuccess({ comment })),
          catchError(error => of(DailyCommentActions.createDailyCommentFailure({
            error: error.message || 'Erreur lors de la création du commentaire'
          })))
        )
      )
    )
  );

  updateDailyComment$ = createEffect(() =>
    this.actions$.pipe(
      ofType(DailyCommentActions.updateDailyComment),
      mergeMap(action =>
        this.dailyCommentService.updateDailyComment(action.id, action.comment).pipe(
          map(comment => DailyCommentActions.updateDailyCommentSuccess({ comment })),
          catchError(error => of(DailyCommentActions.updateDailyCommentFailure({
            error: error.message || 'Erreur lors de la mise à jour du commentaire'
          })))
        )
      )
    )
  );

  constructor(
    private actions$: Actions,
    private dailyCommentService: DailyCommentService
  ) {}
}
