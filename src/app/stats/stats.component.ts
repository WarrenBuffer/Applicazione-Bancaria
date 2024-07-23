import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';
import { Cliente } from '../model/cliente';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrl: './stats.component.css'
})
export class StatsComponent implements OnInit{
  stats: any;
  loading = true;
  stateOptions: any[] = [{ label: 'Generali', value: 'generali' },{ label: 'Clienti', value: 'clienti' }, { label: 'Prestiti', value: 'prestiti' }, { label: 'Transazioni', value: 'transazioni' }];
  value: string = 'generali';

  constructor(private apiService: ApiService) {}
  ngOnInit(): void {
    this.apiService.getStats().subscribe({
      next: v => {
        this.stats = v.message;
        this.loading = false;
      }
    });
  }
}
