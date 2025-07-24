import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Chair } from '../../models/chair.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChairService {
  private apiUrl = 'http://localhost:8080/api/chairs';
  private http = inject(HttpClient);

  getChairById(id:number):Observable<Chair>{
    return this.http.get<Chair>(`${this.apiUrl}/${id}`);
  }
  constructor() { }
}
