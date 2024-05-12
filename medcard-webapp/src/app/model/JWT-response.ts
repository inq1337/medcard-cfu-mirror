export class JWTResponse {
  accessToken: string;

  constructor(token: string) {
    this.accessToken = token;
  }
}
