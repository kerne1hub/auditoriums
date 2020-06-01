export class Subject {
  id: number;
  name: string;

  constructor() {
  }

  build(name: string) {
    this.name = name;
    return this;
  }
}
