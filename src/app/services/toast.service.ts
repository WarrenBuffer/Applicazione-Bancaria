import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  constructor( private messageService: MessageService) { }

  showError(message: string) {
    this.messageService.add({key: 'br', severity:'error', summary: 'Error', detail: message});
  }

  showSuccess(message: string) {
    this.messageService.add({key: 'br', severity:'success', summary: 'Success', detail: message});
  }
}
