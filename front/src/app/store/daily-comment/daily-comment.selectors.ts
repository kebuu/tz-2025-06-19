import { createFeatureSelector, createSelector } from '@ngrx/store';
import { DailyCommentState } from './daily-comment.reducer';

export const selectDailyCommentState = createFeatureSelector<DailyCommentState>('dailyComment');

export const selectCurrentComment = createSelector(
  selectDailyCommentState,
  (state) => state.currentComment
);

export const selectLoading = createSelector(
  selectDailyCommentState,
  (state) => state.loading
);

export const selectError = createSelector(
  selectDailyCommentState,
  (state) => state.error
);
