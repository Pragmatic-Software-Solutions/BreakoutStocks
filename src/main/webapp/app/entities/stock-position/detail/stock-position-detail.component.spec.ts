import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StockPositionDetailComponent } from './stock-position-detail.component';

describe('StockPosition Management Detail Component', () => {
  let comp: StockPositionDetailComponent;
  let fixture: ComponentFixture<StockPositionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockPositionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StockPositionDetailComponent,
              resolve: { stockPosition: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StockPositionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockPositionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stockPosition on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StockPositionDetailComponent);

      // THEN
      expect(instance.stockPosition()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
