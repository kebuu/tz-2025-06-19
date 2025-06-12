import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DailyCommentComponent } from '../../components/daily-comment/daily-comment.component';

@Component({
  selector: 'app-daily-comment-page',
  standalone: true,
  imports: [CommonModule, DailyCommentComponent],
  template: `
    <div class="container mx-auto py-16">
      <h1 class="text-3xl font-bold mb-8 text-center">Mes commentaires journaliers</h1>
      <app-daily-comment></app-daily-comment>
    </div>
  `
})
export class DailyCommentPageComponent {}
