import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TopicsService } from '../../api/topics.service';
import { ConsumerService } from '../../api/consumer.service';
import * as models from '../../model/models';
import { PartitionOffset } from '../../model/partitionOffset';
import { DecimalPipe } from '@angular/common';
import { ConsumerGroupService } from '../../services/consumerGroup/consumer-group.service';
import { Observable } from 'rxjs/Rx';
import { ModelsService } from '../../services/models/models.service';
@Component({
    selector: 'app-topic-content',
    templateUrl: './topic-content.component.html',
    providers: [ModelsService],
    styleUrls: ['./topic-content.component.css']
})
export class TopicContentComponent implements OnInit, OnDestroy {
    constructor(
        private route: ActivatedRoute,
        private topicsApi: TopicsService,
        private consumerApi: ConsumerService,
        private modalService: ModelsService,
        private consumerGroupService: ConsumerGroupService
    ) {}

    topicName: string;
    consumerGroupName: string;
    topic: models.Topic;
    begginingOffsets: Array<models.PartitionOffset> = new Array();
    endingOffsets: Array<models.PartitionOffset> = new Array();
    fetchFromOffsets: Array<models.PartitionOffset> = new Array();
    loadingBeginningoffsets: boolean;
    loadingEndingoffsets: boolean;
    instanceInitializationInProgress = true;
    fetchInProgress = false;
    partitionEmptyOffsetArray: Array<models.PartitionOffset> = new Array();

    searchKey: string;
    positionArray: Array<string> = ['OFFSET', 'BEGINNING'];
    position = 'BEGINNING';
    noOfRecordsPerPartition = 2;
    seekPartitionOffset = 10;
    partitionContentArray: Array<models.PartitionContent>;

    ngOnInit() {
        this.route.params.subscribe(params => {
            this.topicName = params['topic'];
            this.consumerGroupName = this.consumerGroupService.getConsumerGroup(this.topicName);
            this.initializeTopicData();
        });
    }

    ngOnDestroy() {
        console.log('On Destroy executed');
    }

    private keepConsumerAlive() {
        console.log('keepConsumerAlive');
        Observable.interval(1 * 60 * 1000).subscribe(x => {
            this.consumerApi.keepAlive(this.topicName, this.consumerGroupName).subscribe(
                result => {},
                error => {
                    this.initializeTopicData();
                }
            );
        });
    }

    private initializeTopicData() {
        this.topicsApi.get(this.topicName).subscribe(result => {
            this.topic = result;
            this.getTopicOffsets();
            this.initalizeCosumerGroup();
        });
    }

    public setPartitionOffsets() {
        if (this.position.match('BEGINNING')) {
            this.fetchFromOffsets = new Array();
            this.begginingOffsets.forEach(element => {
                this.fetchFromOffsets.push(new PartitionOffset(element.partition, element.offset));
            });
        }

        if (this.position.match('LATEST')) {
            this.fetchFromOffsets = new Array();
            this.endingOffsets.forEach(element => {
                var toOffset = element.offset - this.noOfRecordsPerPartition;
                this.fetchFromOffsets.push(new PartitionOffset(element.partition, toOffset));
            });
        }

        if (this.position.match('OFFSET')) {
            this.fetchFromOffsets = new Array();
            this.endingOffsets.forEach(element => {
                var toOffset = element.offset - this.seekPartitionOffset;
                if (toOffset < 0) toOffset = 0;
                this.fetchFromOffsets.push(new PartitionOffset(element.partition, toOffset));
            });
        }
    }
    private getTopicOffsets() {
        this.loadingBeginningoffsets = true;
        this.topicsApi.getOffsets('Beginning', this.topic).subscribe(
            result => {
                this.begginingOffsets = result;
                this.loadingBeginningoffsets = false;
                this.setPartitionOffsets();
            },
            error => {
                this.loadingBeginningoffsets = false;
            }
        );
        this.loadingEndingoffsets = true;
        this.topicsApi.getOffsets('Ending', this.topic).subscribe(
            result => {
                this.endingOffsets = result;
                this.loadingEndingoffsets = false;
            },
            error => {
                this.loadingEndingoffsets = false;
            }
        );
    }
    private initalizeCosumerGroup() {
        this.topic.partitions.forEach(element => {
            this.partitionEmptyOffsetArray.push(new PartitionOffset(element));
            this.fetchFromOffsets.push(new PartitionOffset(element));
        });

        this.consumerApi
            .initializeConsumerInstance(this.topicName, this.consumerGroupName, this.partitionEmptyOffsetArray)
            .subscribe(
                result => {
                    console.log('Initialization of consumer Instance Sucesss');
                    this.instanceInitializationInProgress = false;
                    this.keepConsumerAlive();
                },
                error => {
                    this.instanceInitializationInProgress = true;
                }
            );
    }
    public fetch() {
        this.fetchInProgress = true;
        this.partitionContentArray = new Array();
        this.consumerApi
            .consumeFromTopic(
                this.topicName,
                this.consumerGroupName,
                this.position,
                this.fetchFromOffsets,
                this.noOfRecordsPerPartition,
                this.searchKey
            )
            .subscribe(
                result => {
                    this.partitionContentArray = result;
                    this.fetchInProgress = false;
                },
                error => {
                    console.log('error:' + JSON.stringify(error));
                    this.fetchInProgress = false;
                }
            );
    }

    openEventValueModel(inputParms) {
        this.modalService.openEventValueModel(inputParms);
    }
}
