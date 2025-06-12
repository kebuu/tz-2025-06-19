import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, SidebarComponent],
  template: `
    <div class="min-h-screen bg-gray-50">
      <app-header (toggleSidebar)="onToggleSidebar()"></app-header>
      
      <div class="flex">
        <app-sidebar 
          [isOpen]="sidebarOpen" 
          (closeSidebar)="onCloseSidebar()">
        </app-sidebar>
        
        <main class="flex-1 transition-all duration-300 ease-in-out"
              [class.ml-64]="sidebarOpen"
              [class.ml-0]="!sidebarOpen">
          <div class="p-6">
            <router-outlet></router-outlet>
          </div>
        </main>
      </div>
    </div>
  `
})
export class AppComponent {
  sidebarOpen = true;

  onToggleSidebar() {
    this.sidebarOpen = !this.sidebarOpen;
  }

  onCloseSidebar() {
    this.sidebarOpen = false;
  }
}