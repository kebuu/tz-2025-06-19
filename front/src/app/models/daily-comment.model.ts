export interface DailyComment {
  id: string;
  userId: string;
  content: string;
  date: string;  // Format YYYY-MM-DD
  lastModified?: string;
}

export interface CreateDailyCommentRequest {
  content: string;
  date: string;
}

export interface UpdateDailyCommentRequest {
  content: string;
}
