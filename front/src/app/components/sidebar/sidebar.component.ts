import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <aside class="fixed left-0 top-16 h-full bg-white shadow-lg border-r border-gray-200 transition-transform duration-300 ease-in-out z-40"
           [class.translate-x-0]="isOpen"
           [class.-translate-x-full]="!isOpen"
           [class.w-64]="isOpen">
      
      <!-- Overlay for mobile -->
      <div *ngIf="isOpen" 
           class="fixed inset-0 bg-black bg-opacity-50 lg:hidden z-30"
           (click)="closeSidebar.emit()">
      </div>
      
      <nav class="h-full flex flex-col">
        <div class="flex-1 px-4 py-6 space-y-2">
          <a routerLink="/" 
             routerLinkActive="bg-primary-50 text-primary-700 border-r-2 border-primary-500"
             [routerLinkActiveOptions]="{exact: true}"
             class="flex items-center px-4 py-3 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors group">
            <svg class="w-5 h-5 mr-3 text-gray-500 group-hover:text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6"></path>
            </svg>
            Accueil
          </a>
          
          <a routerLink="/users" 
             routerLinkActive="bg-primary-50 text-primary-700 border-r-2 border-primary-500"
             class="flex items-center px-4 py-3 text-gray-700 rounded-lg hover:bg-gray-100 transition-colors group">
            <svg class="w-5 h-5 mr-3 text-gray-500 group-hover:text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
            </svg>
            Utilisateurs
          </a>
        </div>
        
        <div class="p-4 border-t border-gray-200">
          <div class="text-xs text-gray-500 text-center">
            RememberMe v1.0.0
          </div>
        </div>
      </nav>
    </aside>
  `
})
export class SidebarComponent {
  @Input() isOpen = false;
  @Output() closeSidebar = new EventEmitter<void>();
}