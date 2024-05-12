export class Page<T> {
  constructor(items: T[], total: number, totalElements: number, page: number, size: number, totalPages: number) {
    this.items = items;
    this.total = total;
    this.totalElements = totalElements;
    this.page = page;
    this.size = size;
    this.totalPages = totalPages;
  }

  items: T[];
  total: number;
  totalElements: number;
  page: number;
  size: number;
  totalPages: number;
}
