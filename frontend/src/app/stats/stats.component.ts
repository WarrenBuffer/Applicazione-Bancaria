import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-stats',
  templateUrl: './stats.component.html',
  styleUrls: ['./stats.component.css']
})
export class StatsComponent implements OnInit {
  stats: any;
  loading = true;
  stateOptions: any[] = [
    { label: 'Generali', value: 'generali' },
    { label: 'Clienti', value: 'clienti' },
    { label: 'Prestiti', value: 'prestiti' },
    { label: 'Transazioni', value: 'transazioni' }
  ];
  value: string = 'generali';

  constructor(private apiService: ApiService) { }

  ngOnInit(): void {
    this.apiService.getStats().subscribe({
      next: v => {
        this.stats = v.message;
        this.loading = false;
      }
    });
  }

  downloadStatistics() {
    this.apiService.downloadStatistics().subscribe({
      next: (data: Blob) => {
        const url = window.URL.createObjectURL(data);
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'statistiche.pdf');
        document.body.appendChild(link);
        link.click();
        link.remove();
      }
    });
  }
}
