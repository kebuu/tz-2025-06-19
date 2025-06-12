import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SidebarComponent } from './sidebar.component';

describe('SidebarComponent', () => {
  let component: SidebarComponent;
  let fixture: ComponentFixture<SidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SidebarComponent, RouterTestingModule]
    }).compileComponents();

    fixture = TestBed.createComponent(SidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should apply correct classes when sidebar is open', () => {
    component.isOpen = true;
    fixture.detectChanges();
    
    const sidebar = fixture.nativeElement.querySelector('aside');
    expect(sidebar.classList).toContain('translate-x-0');
  });

  it('should apply correct classes when sidebar is closed', () => {
    component.isOpen = false;
    fixture.detectChanges();
    
    const sidebar = fixture.nativeElement.querySelector('aside');
    expect(sidebar.classList).toContain('-translate-x-full');
  });

  it('should emit closeSidebar when overlay is clicked', () => {
    component.isOpen = true;
    fixture.detectChanges();
    
    spyOn(component.closeSidebar, 'emit');
    
    const overlay = fixture.nativeElement.querySelector('.fixed.inset-0');
    if (overlay) {
      overlay.click();
      expect(component.closeSidebar.emit).toHaveBeenCalled();
    }
  });
});