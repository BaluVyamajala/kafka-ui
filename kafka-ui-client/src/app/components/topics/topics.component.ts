import { Component, OnInit } from '@angular/core';
import { TopicsService } from '../../api/topics.service';
import { Topic } from '../../model/topic';
import { FilterPipe } from '../../shared/pipes/filter/filter.pipe';

@Component({
    selector: 'app-topics',
    templateUrl: './topics.component.html',
    styleUrls: ['./topics.component.css']
})
export class TopicsComponent implements OnInit {
    constructor(private topicsApi: TopicsService) {}

    topics: Array<Topic>;
    searchTopic: string;
    loadingTopics: boolean;

    ngOnInit() {
        this.loadingTopics = true;
        this.topicsApi.getAll().subscribe(
            result => {
                this.topics = result;
                this.loadingTopics = false;
            },
            error => {
                this.loadingTopics = false;
            }
        );
    }
}
