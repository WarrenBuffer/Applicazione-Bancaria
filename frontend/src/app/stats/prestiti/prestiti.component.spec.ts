import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrestitiComponent } from './prestiti.component';

describe('PrestitiComponent', () => {
  let component: PrestitiComponent;
  let fixture: ComponentFixture<PrestitiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PrestitiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrestitiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
