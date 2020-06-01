import { Auditorium } from './auditorium';

export class Building {
  id: number;
  name: string;
  address: string;
  auditoriums: Auditorium[];

  constructor() {
  }

  build(name: string, address: string) {
    this.name = name;
    this.address = address;
    return this;
  }
}
