import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { StockPositionService } from '../service/stock-position.service';
import { IStockPosition } from '../stock-position.model';
import { StockPositionFormService } from './stock-position-form.service';

import { StockPositionUpdateComponent } from './stock-position-update.component';

describe('StockPosition Management Update Component', () => {
  let comp: StockPositionUpdateComponent;
  let fixture: ComponentFixture<StockPositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stockPositionFormService: StockPositionFormService;
  let stockPositionService: StockPositionService;
  let stockService: StockService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StockPositionUpdateComponent],
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
      .overrideTemplate(StockPositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StockPositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockPositionFormService = TestBed.inject(StockPositionFormService);
    stockPositionService = TestBed.inject(StockPositionService);
    stockService = TestBed.inject(StockService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Stock query and add missing value', () => {
      const stockPosition: IStockPosition = { id: 456 };
      const stock: IStock = { id: 27731 };
      stockPosition.stock = stock;

      const stockCollection: IStock[] = [{ id: 14537 }];
      jest.spyOn(stockService, 'query').mockReturnValue(of(new HttpResponse({ body: stockCollection })));
      const additionalStocks = [stock];
      const expectedCollection: IStock[] = [...additionalStocks, ...stockCollection];
      jest.spyOn(stockService, 'addStockToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockPosition });
      comp.ngOnInit();

      expect(stockService.query).toHaveBeenCalled();
      expect(stockService.addStockToCollectionIfMissing).toHaveBeenCalledWith(
        stockCollection,
        ...additionalStocks.map(expect.objectContaining),
      );
      expect(comp.stocksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const stockPosition: IStockPosition = { id: 456 };
      const stock: IStock = { id: 24594 };
      stockPosition.stock = stock;

      activatedRoute.data = of({ stockPosition });
      comp.ngOnInit();

      expect(comp.stocksSharedCollection).toContain(stock);
      expect(comp.stockPosition).toEqual(stockPosition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockPosition>>();
      const stockPosition = { id: 123 };
      jest.spyOn(stockPositionFormService, 'getStockPosition').mockReturnValue(stockPosition);
      jest.spyOn(stockPositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockPosition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockPosition }));
      saveSubject.complete();

      // THEN
      expect(stockPositionFormService.getStockPosition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockPositionService.update).toHaveBeenCalledWith(expect.objectContaining(stockPosition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockPosition>>();
      const stockPosition = { id: 123 };
      jest.spyOn(stockPositionFormService, 'getStockPosition').mockReturnValue({ id: null });
      jest.spyOn(stockPositionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockPosition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockPosition }));
      saveSubject.complete();

      // THEN
      expect(stockPositionFormService.getStockPosition).toHaveBeenCalled();
      expect(stockPositionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockPosition>>();
      const stockPosition = { id: 123 };
      jest.spyOn(stockPositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockPosition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockPositionService.update).toHaveBeenCalled();
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
