import { ConsumerGroupService } from '../../services/consumerGroup/consumer-group.service';

export class ConsumerGroup {
    topicName: string;
    consumerGroupName: string;
    constructor(topicName: string, consumerGroupName: string) {
        this.topicName = topicName;
        this.consumerGroupName = consumerGroupName;
    }
}
