import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="max-w-7xl mx-auto">
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">Bienvenue sur RememberMe</h1>
        <p class="text-gray-600">Votre application de gestion des utilisateurs</p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
              </svg>
            </div>
            <h3 class="ml-4 text-lg font-semibold text-gray-900">Gestion des utilisateurs</h3>
          </div>
          <p class="text-gray-600 mb-4">Créez, modifiez et gérez vos utilisateurs facilement.</p>
          <a routerLink="/users" class="text-primary-600 hover:text-primary-700 font-medium">
            Voir les utilisateurs →
          </a>
        </div>
        
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <h3 class="ml-4 text-lg font-semibold text-gray-900">Sécurisé</h3>
          </div>
          <p class="text-gray-600 mb-4">Vos données sont protégées avec les dernières technologies.</p>
          <span class="text-green-600 font-medium">Sécurité garantie</span>
        </div>
        
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
              </svg>
            </div>
            <h3 class="ml-4 text-lg font-semibold text-gray-900">Performant</h3>
          </div>
          <p class="text-gray-600 mb-4">Interface rapide et réactive pour une meilleure expérience.</p>
          <span class="text-purple-600 font-medium">Optimisé</span>
        </div>
      </div>
      
      <div class="mt-12 bg-gradient-to-r from-primary-500 to-primary-600 rounded-lg p-8 text-white">
        <h2 class="text-2xl font-bold mb-4">Commencez dès maintenant</h2>
        <p class="text-primary-100 mb-6">Découvrez toutes les fonctionnalités de RememberMe et gérez vos utilisateurs efficacement.</p>
        <a routerLink="/users" 
           class="inline-flex items-center px-6 py-3 bg-white text-primary-600 font-semibold rounded-lg hover:bg-gray-50 transition-colors">
          Voir les utilisateurs
          <svg class="ml-2 w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 8l4 4m0 0l-4 4m4-4H3"></path>
          </svg>
        </a>
      </div>
    </div>
  `
})
export class HomeComponent {}