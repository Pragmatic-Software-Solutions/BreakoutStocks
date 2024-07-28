import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { StockDetailsService } from '../service/stock-details.service';
import { IStockDetails } from '../stock-details.model';
import { StockDetailsFormService } from './stock-details-form.service';

import { StockDetailsUpdateComponent } from './stock-details-update.component';

describe('StockDetails Management Update Component', () => {
  let comp: StockDetailsUpdateComponent;
  let fixture: ComponentFixture<StockDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stockDetailsFormService: StockDetailsFormService;
  let stockDetailsService: StockDetailsService;
  let stockService: StockService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StockDetailsUpdateComponent],
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
      .overrideTemplate(StockDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StockDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockDetailsFormService = TestBed.inject(StockDetailsFormService);
    stockDetailsService = TestBed.inject(StockDetailsService);
    stockService = TestBed.inject(StockService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call stock query and add missing value', () => {
      const stockDetails: IStockDetails = { id: 456 };
      const stock: IStock = { id: 3545 };
      stockDetails.stock = stock;

      const stockCollection: IStock[] = [{ id: 21417 }];
      jest.spyOn(stockService, 'query').mockReturnValue(of(new HttpResponse({ body: stockCollection })));
      const expectedCollection: IStock[] = [stock, ...stockCollection];
      jest.spyOn(stockService, 'addStockToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ stockDetails });
      comp.ngOnInit();

      expect(stockService.query).toHaveBeenCalled();
      expect(stockService.addStockToCollectionIfMissing).toHaveBeenCalledWith(stockCollection, stock);
      expect(comp.stocksCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const stockDetails: IStockDetails = { id: 456 };
      const stock: IStock = { id: 31061 };
      stockDetails.stock = stock;

      activatedRoute.data = of({ stockDetails });
      comp.ngOnInit();

      expect(comp.stocksCollection).toContain(stock);
      expect(comp.stockDetails).toEqual(stockDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockDetails>>();
      const stockDetails = { id: 123 };
      jest.spyOn(stockDetailsFormService, 'getStockDetails').mockReturnValue(stockDetails);
      jest.spyOn(stockDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockDetails }));
      saveSubject.complete();

      // THEN
      expect(stockDetailsFormService.getStockDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(stockDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockDetails>>();
      const stockDetails = { id: 123 };
      jest.spyOn(stockDetailsFormService, 'getStockDetails').mockReturnValue({ id: null });
      jest.spyOn(stockDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stockDetails }));
      saveSubject.complete();

      // THEN
      expect(stockDetailsFormService.getStockDetails).toHaveBeenCalled();
      expect(stockDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStockDetails>>();
      const stockDetails = { id: 123 };
      jest.spyOn(stockDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stockDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockDetailsService.update).toHaveBeenCalled();
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
