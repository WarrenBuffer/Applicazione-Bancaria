import { Component, OnInit } from '@angular/core';
import { Conto } from '../../conto/conto';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-delete-conto',
  templateUrl: './delete-conto.component.html',
  styleUrl: './delete-conto.component.css'
})
export class DeleteContoComponent implements OnInit{
  contiList!: Conto[];
  constructor(private apiService: ApiService){}
  ngOnInit(): void {
    this.listaConti();
  }

  listaConti() {
    this.apiService.contiList().subscribe({
      next: v => {
        this.contiList = v.message;
      }
    })
  }

  eliminaConto(codice: number) {
    this.apiService.deleteConto(codice).subscribe({
      next: v => {
        this.listaConti();
      }
    });
  }
  
}
