export class Group {
  id: number;
  name: string;

  build(name: string) {
    this.name = name;
    return this;
  }
}
