import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { DailyComment } from '../../models/daily-comment.model';
import * as DailyCommentActions from '../../store/daily-comment/daily-comment.actions';
import * as DailyCommentSelectors from '../../store/daily-comment/daily-comment.selectors';
import { DailyCommentFormComponent } from '../daily-comment-form/daily-comment-form.component';

@Component({
  selector: 'app-daily-comment',
  standalone: true,
  imports: [CommonModule, DailyCommentFormComponent],
  template: `
    <div class="p-4">
      <div class="max-w-2xl mx-auto bg-white rounded-lg shadow-md p-6">
        <h2 class="text-2xl font-bold mb-4">Commentaire du jour</h2>

        <ng-container *ngIf="loading$ | async">
          <div class="flex justify-center">
            <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-500"></div>
          </div>
        </ng-container>

        <ng-container *ngIf="error$ | async as error">
          <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {{ error }}
          </div>
        </ng-container>

        <ng-container *ngIf="currentComment$ | async as comment">
          <div *ngIf="!isEditing" class="space-y-4">
            <p class="text-gray-700">{{ comment.content }}</p>
            <div class="flex justify-between items-center text-sm text-gray-500">
              <span>Dernière modification: {{ comment.lastModified | date:'medium' }}</span>
              <button 
                (click)="toggleEdit()"
                class="px-4 py-2 bg-primary-500 text-white rounded hover:bg-primary-600 transition-colors">
                Modifier
              </button>
            </div>
          </div>

          <app-daily-comment-form
            *ngIf="isEditing"
            [comment]="comment"
            (cancel)="toggleEdit()"
            (submitted)="toggleEdit()">
          </app-daily-comment-form>
        </ng-container>

        <div *ngIf="!(currentComment$ | async) && !(loading$ | async)">
          <app-daily-comment-form
            (submitted)="toggleEdit()">
          </app-daily-comment-form>
        </div>
      </div>
    </div>
  `
})
export class DailyCommentComponent implements OnInit {
  currentComment$: Observable<DailyComment | null>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;
  isEditing = false;

  constructor(private store: Store) {
    this.currentComment$ = this.store.select(DailyCommentSelectors.selectCurrentComment);
    this.loading$ = this.store.select(DailyCommentSelectors.selectLoading);
    this.error$ = this.store.select(DailyCommentSelectors.selectError);
  }

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];
    this.loadDailyComment(today);
  }

  loadDailyComment(date: string): void {
    this.store.dispatch(DailyCommentActions.loadDailyComment({ date }));
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
  }
}
