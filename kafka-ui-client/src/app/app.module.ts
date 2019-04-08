import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { TopicsService } from './api/topics.service';
import { ConsumerService } from './api/consumer.service';
import { ConsumerGroupService } from './services/consumerGroup/consumer-group.service';
import { NavbarComponent } from './components/layouts/navbar/navbar.component';
import { AppRoutingModule } from './app-routing.module';
import { TopicsComponent } from './components/topics/topics.component';
import { TopicContentComponent } from './components/topic-content/topic-content.component';
import { FilterPipe } from './shared/pipes/filter/filter.pipe';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Ng2Webstorage } from 'ngx-webstorage';
import { EventValueModelComponent } from './components/event-value-model/event-value-model.component';
import { PrettyJsonModule } from 'angular2-prettyjson';
@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        TopicsComponent,
        TopicContentComponent,
        FilterPipe,
        EventValueModelComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        HttpModule,
        FormsModule,
        PrettyJsonModule,
        NgxJsonViewerModule,
        NgbModule.forRoot(),
        Ng2Webstorage,
        AngularFontAwesomeModule
    ],
    providers: [TopicsService, ConsumerService, ConsumerGroupService],
    entryComponents: [EventValueModelComponent],
    bootstrap: [AppComponent]
})
export class AppModule {}
