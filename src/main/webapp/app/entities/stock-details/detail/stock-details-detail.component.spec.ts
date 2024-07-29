import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { StockDetailsDetailComponent } from './stock-details-detail.component';

describe('StockDetails Management Detail Component', () => {
  let comp: StockDetailsDetailComponent;
  let fixture: ComponentFixture<StockDetailsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockDetailsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StockDetailsDetailComponent,
              resolve: { stockDetails: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(StockDetailsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load stockDetails on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StockDetailsDetailComponent);

      // THEN
      expect(instance.stockDetails()).toEqual(expect.objectContaining({ id: 123 }));
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
