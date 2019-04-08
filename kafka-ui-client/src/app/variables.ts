import { InjectionToken } from '@angular/core';

export const BASE_PATH = new InjectionToken<string>('basePath');
export const COLLECTION_FORMATS = {
    csv: ',',
    tsv: '   ',
    ssv: ' ',
    pipes: '|'
};

export const CONSUMER_GROUP_LOCAL_STORAGE_KEY: string = 'consumerGroup';
