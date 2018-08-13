import { Injectable } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { EventValueModelComponent } from '../../components/event-value-model/event-value-model.component';

@Injectable()
export class ModelsService {

  constructor(private modalService: NgbModal) { }

  private closeResult: string;

  public openEventValueModel(inputParms) {
    const modalRef = this.modalService.open(EventValueModelComponent);
    modalRef.componentInstance.eventValueJson = inputParms;
    modalRef.result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }



  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

}