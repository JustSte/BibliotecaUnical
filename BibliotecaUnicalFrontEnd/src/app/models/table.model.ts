import { Chair } from "./chair.model";

export interface Table{
    id:number;
    name: string;
    chairs: Chair[];
}