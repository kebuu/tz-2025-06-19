import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent, RouterTestingModule]
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display welcome message', () => {
    const title = fixture.nativeElement.querySelector('h1');
    expect(title.textContent).toContain('Bienvenue sur RememberMe');
  });

  it('should display feature cards', () => {
    const cards = fixture.nativeElement.querySelectorAll('.bg-white.rounded-lg.shadow-md');
    expect(cards.length).toBe(3);
  });
});