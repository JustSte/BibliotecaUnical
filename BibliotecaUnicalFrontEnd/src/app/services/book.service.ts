import { inject, Injectable, input } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { Page } from '../models/page.models';
import { Book } from '../models/book.model';


@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:8080/api/books';
  private http = inject(HttpClient);
  
private mockBooks: Book[] = [
 { id: 1, title: '1984', shelfId: 1 },
  { id: 2, title: 'To Kill a Mockingbird', shelfId: 1 },
  { id: 3, title: 'Brave New World', shelfId: 2 },
  { id: 4, title: 'The Great Gatsby', shelfId: 2 },
  { id: 5, title: 'Moby-Dick', shelfId: 3 },
  { id: 6, title: 'War and Peace', shelfId: 3 },
  { id: 7, title: 'The Catcher in the Rye', shelfId: 4 },
  { id: 8, title: 'Pride and Prejudice', shelfId: 4 },
  { id: 9, title: 'The Hobbit', shelfId: 5 },
  { id: 10, title: 'Crime and Punishment', shelfId: 5 }
  ];


  getlistBooks(page: number = 0, size: number = 2): Observable<Page<Book>> {
    let params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Book>>(this.apiUrl, {params});
  }
  
  getBook(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/${id}`);
  }

  getBooksPage(page: number, size: number): Observable<Page<Book>> {
    const start = page * size;
    const end = start + size;
    const content = this.mockBooks.slice(start, end);
    const totalElements = this.mockBooks.length;
    const totalPages = Math.ceil(totalElements / size);

    const pageData: Page<Book> = {
      content,
      totalPages,
      totalElements,
      number: page,
      size
    };

    return of(pageData);
  }

  getBookMock(id: number): Observable<Book> {
  const found = this.mockBooks.find(book => book.id === id);
  if (found) {
    return of(found);  // ritorna Observable con il libro trovato
  } else {
    return throwError(() => new Error('Book not found')); // errore se non trovato
  }
}
}

