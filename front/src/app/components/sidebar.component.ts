import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <div 
      class="fixed top-0 left-0 z-40 h-screen pt-20 transition-transform bg-white border-r border-gray-200 w-64"
      [class.translate-x-0]="isOpen"
      [class.-translate-x-full]="!isOpen">
      <div class="h-full px-3 pb-4 overflow-y-auto">
        <ul class="space-y-2">
          <li>
            <a 
              routerLink="/users"
              routerLinkActive="bg-gray-100 text-primary-600"
              class="flex items-center p-2 text-gray-700 rounded-lg hover:bg-gray-100 group">
              <svg class="w-5 h-5 text-gray-600 group-hover:text-primary-600" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 18">
                <path d="M14 2a3.963 3.963 0 0 0-1.4.267 6.439 6.439 0 0 1-1.331 6.638A4 4 0 1 0 14 2Zm1 9h-1.264A6.957 6.957 0 0 1 15 15v2a2.97 2.97 0 0 1-.184 1H19a1 1 0 0 0 1-1v-1a5.006 5.006 0 0 0-5-5ZM6.5 9a4.5 4.5 0 1 0 0-9 4.5 4.5 0 0 0 0 9ZM8 10H5a5.006 5.006 0 0 0-5 5v2a1 1 0 0 0 1 1h11a1 1 0 0 0 1-1v-2a5.006 5.006 0 0 0-5-5Z"/>
              </svg>
              <span class="ml-3">Utilisateurs</span>
            </a>
          </li>
          <li>
            <a 
              routerLink="/daily-comments"
              routerLinkActive="bg-gray-100 text-primary-600"
              class="flex items-center p-2 text-gray-700 rounded-lg hover:bg-gray-100 group">
              <svg class="w-5 h-5 text-gray-600 group-hover:text-primary-600" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 20">
                <path d="M10 0C4.486 0 0 4.486 0 10s4.486 10 10 10 10-4.486 10-10S15.514 0 10 0Zm0 18a8 8 0 1 1 0-16 8 8 0 0 1 0 16Zm.999-5h-2v-5h2v5ZM9 5a1 1 0 1 1 2 0 1 1 0 0 1-2 0Z"/>
              </svg>
              <span class="ml-3">Commentaires</span>
            </a>
          </li>
        </ul>
      </div>
    </div>
  `
})
export class SidebarComponent {
  @Input() isOpen = false;
}
