import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideStore } from '@ngrx/store';
import { provideEffects } from '@ngrx/effects';
import { provideStoreDevtools } from '@ngrx/store-devtools';

import { routes } from './app.routes';
import { userReducer } from './store/user/user.reducer';
import { UserEffects } from './store/user/user.effects';
import { dailyCommentReducer } from './store/daily-comment/daily-comment.reducer';
import { DailyCommentEffects } from './store/daily-comment/daily-comment.effects';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    provideStore({ 
      user: userReducer,
      dailyComment: dailyCommentReducer 
    }),
    provideEffects([UserEffects, DailyCommentEffects]),
    provideStoreDevtools({
      maxAge: 25,
      logOnly: false,
      autoPause: true,
      trace: false,
      traceLimit: 75
    })
  ]
};