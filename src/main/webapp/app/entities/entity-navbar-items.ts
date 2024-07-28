import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Stock',
    route: '/stock',
    translationKey: 'global.menu.entities.stock',
  },
  {
    name: 'StockDetails',
    route: '/stock-details',
    translationKey: 'global.menu.entities.stockDetails',
  },
  {
    name: 'StockRecommendation',
    route: '/stock-recommendation',
    translationKey: 'global.menu.entities.stockRecommendation',
  },
  {
    name: 'StockPosition',
    route: '/stock-position',
    translationKey: 'global.menu.entities.stockPosition',
  },
];
