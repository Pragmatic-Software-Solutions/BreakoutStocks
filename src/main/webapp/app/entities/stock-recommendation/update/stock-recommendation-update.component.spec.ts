import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { StockRecommendationService } from '../service/stock-recommendation.service';
import { IStockRecommendation } from '../stock-recommendation.model';
import { StockRecommendationFormService } from './stock-recommendation-form.service';

import { StockRecommendationUpdateComponent } from './stock-recommendation-update.component';

describe('StockRecommendation Management Update Component', () => {
  let comp: StockRecommendationUpdateComponent;
  let fixture: ComponentFixture<StockRecommendationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stockRecommendationFormService: StockRecommendationFormService;
  let stockRecommendationService: StockRecommendationService;
  let stockService: StockService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StockRecommendationUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(StockRecommendationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StockRecommendationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockRecommendationFormService = TestBed.inject(StockRecommendationFormService);
    stockRecommendationService = TestBed.inject(StockRecommendationService);
    stockService = TestBed.inject(StockService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Stock query and add missing value', () => {
      const stockRecommendation: IStockRecommendation = { id: 456 };
      const stock: IStock = { id: 20046 };
      stockRecommendation.stock = stock;

      const stockCollection: IStock[] = [{ id: 27190 }];
      jest.spyOn(stockService, 'query').mockReturnValue(of(new HttpResponse({ body: stockCollection })));
      const additionalStocks = [stock];
      const expectedCollection: IStock[] = [...additionalStocks, ...stockCollection];
      jest.spyOn(stockService, 'addStockToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockRecommendation });
      comp.ngOnInit();

      expect(stockService.query).toHaveBeenCalled();
      expect(stockService.addStockToCollectionIfMissing).toHaveBeenCalledWith(
        stockCollection,
        ...additionalStocks.map(expect.objectContaining),
      );
      expect(comp.stocksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const stockRecommendation: IStockRecommendation = { id: 456 };
      const stock: IStock = { id: 28168 };
      stockRecommendation.stock = stock;

      activatedRoute.data = of({ stockRecommendation });
      comp.ngOnInit();

      expect(comp.stocksSharedCollection).toContain(stock);
      expect(comp.stockRecommendation).toEqual(stockRecommendation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockRecommendation>>();
      const stockRecommendation = { id: 123 };
      jest.spyOn(stockRecommendationFormService, 'getStockRecommendation').mockReturnValue(stockRecommendation);
      jest.spyOn(stockRecommendationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockRecommendation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockRecommendation }));
      saveSubject.complete();

      // THEN
      expect(stockRecommendationFormService.getStockRecommendation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockRecommendationService.update).toHaveBeenCalledWith(expect.objectContaining(stockRecommendation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockRecommendation>>();
      const stockRecommendation = { id: 123 };
      jest.spyOn(stockRecommendationFormService, 'getStockRecommendation').mockReturnValue({ id: null });
      jest.spyOn(stockRecommendationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockRecommendation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockRecommendation }));
      saveSubject.complete();

      // THEN
      expect(stockRecommendationFormService.getStockRecommendation).toHaveBeenCalled();
      expect(stockRecommendationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockRecommendation>>();
      const stockRecommendation = { id: 123 };
      jest.spyOn(stockRecommendationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockRecommendation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockRecommendationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStock', () => {
      it('Should forward to stockService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stockService, 'compareStock');
        comp.compareStock(entity, entity2);
        expect(stockService.compareStock).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
