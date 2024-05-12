export class TemplatesSimple {
  templates: TemplatesSimpleItem[]

  constructor(templates: TemplatesSimpleItem[]) {
    this.templates = templates;
  }
}

export class TemplatesSimpleItem {
  id: number
  name: string

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }
}
