import { Injectable } from '@angular/core';
import { Location, LocationStrategy, PathLocationStrategy } from '@angular/common';

@Injectable()
export class UtilService {
    private static baseUrl: string;
    private location: Location;

    constructor() {}

    public static getBaseUrl() {
        UtilService.baseUrl = location.origin;
        return UtilService.baseUrl;
    }
}
