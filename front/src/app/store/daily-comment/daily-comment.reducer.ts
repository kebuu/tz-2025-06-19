import { createReducer, on } from '@ngrx/store';
import { DailyComment } from '../../models/daily-comment.model';
import * as DailyCommentActions from './daily-comment.actions';

export interface DailyCommentState {
  currentComment: DailyComment | null;
  loading: boolean;
  error: string | null;
}

export const initialState: DailyCommentState = {
  currentComment: null,
  loading: false,
  error: null
};

export const dailyCommentReducer = createReducer(
  initialState,

  // Load
  on(DailyCommentActions.loadDailyComment, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(DailyCommentActions.loadDailyCommentSuccess, (state, { comment }) => ({
    ...state,
    currentComment: comment,
    loading: false
  })),

  on(DailyCommentActions.loadDailyCommentFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Create
  on(DailyCommentActions.createDailyComment, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(DailyCommentActions.createDailyCommentSuccess, (state, { comment }) => ({
    ...state,
    currentComment: comment,
    loading: false
  })),

  on(DailyCommentActions.createDailyCommentFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  })),

  // Update
  on(DailyCommentActions.updateDailyComment, state => ({
    ...state,
    loading: true,
    error: null
  })),

  on(DailyCommentActions.updateDailyCommentSuccess, (state, { comment }) => ({
    ...state,
    currentComment: comment,
    loading: false
  })),

  on(DailyCommentActions.updateDailyCommentFailure, (state, { error }) => ({
    ...state,
    error,
    loading: false
  }))
);
