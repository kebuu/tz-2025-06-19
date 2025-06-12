import { createAction, props } from '@ngrx/store';
import { DailyComment, CreateDailyCommentRequest, UpdateDailyCommentRequest } from '../../models/daily-comment.model';

export const loadDailyComment = createAction(
  '[Daily Comment] Load Daily Comment',
  props<{ date: string }>()
);

export const loadDailyCommentSuccess = createAction(
  '[Daily Comment] Load Daily Comment Success',
  props<{ comment: DailyComment }>()
);

export const loadDailyCommentFailure = createAction(
  '[Daily Comment] Load Daily Comment Failure',
  props<{ error: string }>()
);

export const createDailyComment = createAction(
  '[Daily Comment] Create Daily Comment',
  props<{ comment: CreateDailyCommentRequest }>()
);

export const createDailyCommentSuccess = createAction(
  '[Daily Comment] Create Daily Comment Success',
  props<{ comment: DailyComment }>()
);

export const createDailyCommentFailure = createAction(
  '[Daily Comment] Create Daily Comment Failure',
  props<{ error: string }>()
);

export const updateDailyComment = createAction(
  '[Daily Comment] Update Daily Comment',
  props<{ id: string; comment: UpdateDailyCommentRequest }>()
);

export const updateDailyCommentSuccess = createAction(
  '[Daily Comment] Update Daily Comment Success',
  props<{ comment: DailyComment }>()
);

export const updateDailyCommentFailure = createAction(
  '[Daily Comment] Update Daily Comment Failure',
  props<{ error: string }>()
);
