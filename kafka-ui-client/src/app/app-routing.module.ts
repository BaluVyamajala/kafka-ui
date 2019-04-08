import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TopicsComponent } from './components/topics/topics.component';
import { NavbarComponent } from './components/layouts/navbar/navbar.component';
import { TopicContentComponent } from './components/topic-content/topic-content.component';
const routes: Routes = [
    { path: 'topics', component: TopicsComponent },
    { path: 'topicContent/:topic', component: TopicContentComponent },
    { path: '', component: NavbarComponent, outlet: 'navbar' },
    { path: '', redirectTo: '/topics', pathMatch: 'full' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
