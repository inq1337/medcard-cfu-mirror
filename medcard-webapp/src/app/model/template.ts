export class Template {
  id: number;
  name: string;
  parameters: TemplateParameter[];
  deleted: boolean;

  constructor(id: number, name: string, parameters: TemplateParameter[], deleted: boolean) {
    this.id = id;
    this.name = name;
    this.parameters = parameters;
    this.deleted = deleted;
  }

}

export class TemplateParameter {

  constructor(name: string, unit: string, hasState: boolean) {
    this.name = name;
    this.unit = unit;
    this.hasState = hasState;
  }

  name: string;
  unit: string;
  hasState: boolean;
}
