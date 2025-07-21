import { Component, inject, input, OnInit } from '@angular/core';
import { BookService } from '../../services/book/book.service';
import { Book } from '../../models/book.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { PrimeIcons } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { AutoCompleteCompleteEvent, AutoCompleteModule, AutoCompleteSelectEvent } from 'primeng/autocomplete';

@Component({
  selector: 'app-books',
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    AutoCompleteModule,
    ButtonModule
  ],
  templateUrl: './books.component.html',
  styleUrl: './books.component.css'
})
export class BooksComponent implements OnInit{

  page: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  books : Book[] = [];
  searchId: number | null = null;
  errorMessage: string = '';
  
  searchedBook?: Book | undefined;
  filteredBooks: Book[] = [];
  selectedBookTitle: string = '';

  private readonly bookService = inject(BookService);

  ngOnInit() {
    this.loadBooks(this.page, this.pageSize);
  }

  loadBooks(page: number, pageSize:number)
  {
    this.bookService.getListBooks(page, pageSize).subscribe(
      {
        next: (response) => {
          this.books = response.content;
          this.totalElements = response.totalElements;
          this.pageSize = response.size;
          this.page = response.number;
        },
        error: () => {
          this.errorMessage= 'Error on loading books';
        }
      }
    );
  }

  get tableBooks(): Book[] {
  return this.searchedBook ? [this.searchedBook] : this.books;
}

  onPageChange(event:any): void
  {
    this.pageSize = event.rows;
    this.page = event.first/event.rows;
      this.loadBooks(this.page, this.pageSize);
  }


  searchBookById()
  {
    if(!this.searchId && this.searchId !== 0) return;

    this.bookService.getBookMock(this.searchId).subscribe({
      next: (book) => {
        this.searchedBook = book;
        this.errorMessage = '';
      },
      error: () => {
        this.searchedBook = undefined;
        this.errorMessage = 'Book not found';
      }
    });
  }

    resetSearch(event:any) : void
    {
      this.searchedBook = undefined;
      this.searchId = null;
      this.errorMessage='';
      this.loadBooks(this.page, this.pageSize);
    }

    filterBooks(event: any): void {
    const query = event.query.toLowerCase();
    this.filteredBooks = this.books.filter(book =>
      book.title.toLowerCase().includes(query)
    );
    }

    onBookSelect(event: AutoCompleteSelectEvent): void {
      const selected = event.value as Book;
      this.searchedBook = selected;
      this.searchId = selected.id;
      this.selectedBookTitle = selected.title;
    }
}
