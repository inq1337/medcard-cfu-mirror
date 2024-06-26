export class Analysis {
  constructor(id: number, name: string, templateName: string, templateId: number, analysisDate: Date, cardUserId: number, parameters: AnalysisParameter[], commentary: string, deleted: boolean, images: string[]) {
    this.id = id;
    this.name = name;
    this.templateName = templateName;
    this.templateId = templateId;
    this.analysisDate = analysisDate;
    this.cardUserId = cardUserId;
    this.parameters = parameters;
    this.commentary = commentary;
    this.deleted = deleted;
    this.images = images;
  }

  id: number
  name: string
  templateName: string
  templateId: number
  analysisDate: Date
  cardUserId: number
  parameters: AnalysisParameter[]
  commentary: string
  deleted: boolean
  images: string[]
}

export class AnalysisParameter {
  constructor(id: number, name: string, value: string, unit: string, state: ParameterState) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.unit = unit;
    this.state = state;
  }

  id: number
  name: string
  value: string
  unit: string
  state: ParameterState
}

export enum ParameterState {
  elevated,
  low,
  not_specified

}

export const AnalysisParameterStateMapping = [
  {
    id: 'elevated',
    value: 'Повышен'
  },
  {
    id: 'low',
    value: 'Понижен'
  },
  {
    id: 'not_specified',
    value: 'Не указано'
  }
];
