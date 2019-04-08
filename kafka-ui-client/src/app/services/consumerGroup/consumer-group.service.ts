import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { ConsumerGroup } from '../../model/helper/consumerGroup';
import { CONSUMER_GROUP_LOCAL_STORAGE_KEY } from '../../variables';
import { UUID } from 'angular2-uuid';
@Injectable({
    providedIn: 'root'
})
@Injectable()
export class ConsumerGroupService {
    consumerGroupArray: Array<ConsumerGroup> = new Array();
    constructor(private localSt: LocalStorageService) {
        const localStrogeOut = this.localSt.retrieve(CONSUMER_GROUP_LOCAL_STORAGE_KEY);
        if (localStrogeOut) {
            this.consumerGroupArray = localStrogeOut;
        }
    }

    public getConsumerGroup(topicName: string) {
        for (let element of this.consumerGroupArray) {
            if (element.topicName.match(topicName)) {
                if (!element.consumerGroupName.startsWith('KafkaUI_')) {
                    console.log('no it doesnt start' + element.consumerGroupName);
                    this.consumerGroupArray = new Array();
                    this.localSt.store(CONSUMER_GROUP_LOCAL_STORAGE_KEY, this.consumerGroupArray);
                } else {
                    return element.consumerGroupName;
                }
            }
        }
        // Consumer Group not found in local storage - create one and store.
        const consumerGroupName = 'KafkaUI_' + UUID.UUID();
        this.consumerGroupArray.push(new ConsumerGroup(topicName, consumerGroupName));
        this.saveToLocalStorage();
        return consumerGroupName;
    }

    private saveToLocalStorage() {
        this.localSt.store(CONSUMER_GROUP_LOCAL_STORAGE_KEY, this.consumerGroupArray);
    }
}
