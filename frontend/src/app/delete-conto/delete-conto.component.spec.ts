import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteContoComponent } from './delete-conto.component';

describe('DeleteContoComponent', () => {
  let component: DeleteContoComponent;
  let fixture: ComponentFixture<DeleteContoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeleteContoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteContoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
