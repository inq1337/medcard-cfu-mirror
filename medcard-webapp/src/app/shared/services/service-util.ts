import DevExpress from 'devextreme';
import {OptionChangedEvent} from "devextreme/ui/data_grid";
import {OrderingDescriptor, SortDescriptor} from "devextreme/data";
import LoadOptions = DevExpress.data.LoadOptions;

export function onGridOptionsChanged(e: OptionChangedEvent) {
  if (e.fullName.includes("pageSize")) {
    if (e.value > e.previousValue)
      e.component.refresh();
  }
}

export function convertToQuery(loadOptions: LoadOptions) {
  let params = '?';

  let page = 0;
  if (loadOptions.skip && loadOptions.take && loadOptions.skip > 0) {
    page = Math.floor(loadOptions.skip / loadOptions.take);
  }

  params += 'page=' + page;
  params += '&size=' + (loadOptions.take || 10);

  if (loadOptions.sort && loadOptions.sort instanceof Array && loadOptions.sort[0]) {
    let sortElement: SortDescriptor<any> = loadOptions.sort[0];
    params += '&sort=' + (sortElement as OrderingDescriptor<any>).selector; // TODO: resolve
    if ((sortElement as OrderingDescriptor<any>).desc) { // TODO: resolve
      params += ',desc';
    }
  }

  if (loadOptions.filter && loadOptions.filter instanceof Array && loadOptions.filter[0]) {
    params += '&filter=' + loadOptions.filter;
  }

  return params;
}

export function enumToArray(enumToConvert: any): Array<string> {
  const keys = Object.keys(enumToConvert);
  return keys.slice(keys.length / 2);
}

