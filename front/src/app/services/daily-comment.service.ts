import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DailyComment, CreateDailyCommentRequest, UpdateDailyCommentRequest } from '../models/daily-comment.model';

@Injectable({
  providedIn: 'root'
})
export class DailyCommentService {
  private readonly apiUrl = 'http://localhost:8080/api/daily-comments';

  constructor(private http: HttpClient) {}

  getDailyComment(date: string): Observable<DailyComment> {
    return this.http.get<DailyComment>(`${this.apiUrl}/date/${date}`);
  }

  createDailyComment(comment: CreateDailyCommentRequest): Observable<DailyComment> {
    return this.http.post<DailyComment>(this.apiUrl, comment);
  }

  updateDailyComment(id: string, comment: UpdateDailyCommentRequest): Observable<DailyComment> {
    return this.http.put<DailyComment>(`${this.apiUrl}/${id}`, comment);
  }

  getUserComments(userId: string): Observable<DailyComment[]> {
    return this.http.get<DailyComment[]>(`${this.apiUrl}/user/${userId}`);
  }
}
