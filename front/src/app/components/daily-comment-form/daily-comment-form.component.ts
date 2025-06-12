import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { DailyComment } from '../../models/daily-comment.model';
import * as DailyCommentActions from '../../store/daily-comment/daily-comment.actions';

@Component({
  selector: 'app-daily-comment-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="commentForm" (ngSubmit)="onSubmit()" class="space-y-4">
      <div>
        <textarea
          formControlName="content"
          rows="4"
          class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-primary-500 focus:border-primary-500"
          placeholder="Écrivez votre commentaire du jour...">
        </textarea>

        <div *ngIf="commentForm.get('content')?.invalid && commentForm.get('content')?.touched"
             class="text-red-500 text-sm mt-1">
          Le commentaire ne peut pas être vide
        </div>
      </div>

      <div class="flex justify-end space-x-3">
        <button
          *ngIf="comment"
          type="button"
          (click)="onCancel()"
          class="px-4 py-2 text-gray-600 bg-gray-100 rounded hover:bg-gray-200 transition-colors">
          Annuler
        </button>

        <button
          type="submit"
          [disabled]="commentForm.invalid"
          class="px-4 py-2 bg-primary-500 text-white rounded hover:bg-primary-600 transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
          {{ comment ? 'Mettre à jour' : 'Enregistrer' }}
        </button>
      </div>
    </form>
  `
})
export class DailyCommentFormComponent implements OnInit {
  @Input() comment?: DailyComment;
  @Output() submitted = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  commentForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private store: Store
  ) {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(1)]]
    });
  }

  ngOnInit(): void {
    if (this.comment) {
      this.commentForm.patchValue({
        content: this.comment.content
      });
    }
  }

  onSubmit(): void {
    if (this.commentForm.valid) {
      const content = this.commentForm.get('content')?.value;

      if (this.comment) {
        this.store.dispatch(DailyCommentActions.updateDailyComment({
          id: this.comment.id,
          comment: { content }
        }));
      } else {
        const today = new Date().toISOString().split('T')[0];
        this.store.dispatch(DailyCommentActions.createDailyComment({
          comment: {
            content,
            date: today
          }
        }));
      }

      this.submitted.emit();
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
