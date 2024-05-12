export class ShareRequest {
  constructor(types: number[], analysisDatesSince: string | null) {
    this.ids = types;
    this.analysisDatesSince = analysisDatesSince;
  }

  ids: number[]
  analysisDatesSince: string | null
}
