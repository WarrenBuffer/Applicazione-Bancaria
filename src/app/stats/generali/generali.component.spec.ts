import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GeneraliComponent } from './generali.component';

describe('GeneraliComponent', () => {
  let component: GeneraliComponent;
  let fixture: ComponentFixture<GeneraliComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GeneraliComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GeneraliComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
