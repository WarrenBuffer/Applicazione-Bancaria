import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiestePrestitiComponent } from './richieste-prestiti.component';

describe('RichiestePrestitiComponent', () => {
  let component: RichiestePrestitiComponent;
  let fixture: ComponentFixture<RichiestePrestitiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RichiestePrestitiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RichiestePrestitiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
