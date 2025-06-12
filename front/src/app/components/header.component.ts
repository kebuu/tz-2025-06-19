import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <header class="bg-white shadow-sm border-b border-gray-200 fixed w-full top-0 z-50">
      <div class="flex items-center justify-between px-6 py-4">
        <div class="flex items-center space-x-4">
          <button 
            (click)="toggleSidebar.emit()"
            class="p-2 rounded-md text-gray-600 hover:text-gray-900 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-primary-500 transition-colors">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
            </svg>
          </button>

          <h1 class="text-xl font-semibold text-gray-900">RememberMe</h1>

          <nav class="hidden md:flex space-x-4">
            <a routerLink="/users" class="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium">Utilisateurs</a>
            <a routerLink="/daily-comments" class="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium">Commentaires</a>
          </nav>
        </div>

        <div class="flex items-center space-x-4">
          <div class="relative">
            <button class="flex items-center space-x-2 text-gray-700 hover:text-gray-900 focus:outline-none focus:ring-2 focus:ring-primary-500 rounded-md p-2 transition-colors">
              <div class="w-8 h-8 bg-primary-500 rounded-full flex items-center justify-center">
                <span class="text-white text-sm font-medium">U</span>
              </div>
              <span class="hidden md:block text-sm font-medium">Utilisateur</span>
            </button>
          </div>
        </div>
      </div>
    </header>
  `
})
export class HeaderComponent {
  @Output() toggleSidebar = new EventEmitter<void>();
}
