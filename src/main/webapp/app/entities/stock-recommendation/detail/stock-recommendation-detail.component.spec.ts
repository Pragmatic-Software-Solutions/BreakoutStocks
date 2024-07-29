import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StockRecommendationDetailComponent } from './stock-recommendation-detail.component';

describe('StockRecommendation Management Detail Component', () => {
  let comp: StockRecommendationDetailComponent;
  let fixture: ComponentFixture<StockRecommendationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockRecommendationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StockRecommendationDetailComponent,
              resolve: { stockRecommendation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StockRecommendationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockRecommendationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stockRecommendation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StockRecommendationDetailComponent);

      // THEN
      expect(instance.stockRecommendation()).toEqual(expect.objectContaining({ id: 123 }));
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
