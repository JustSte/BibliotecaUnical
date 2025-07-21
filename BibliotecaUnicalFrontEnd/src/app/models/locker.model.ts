export interface Locker{
    id:number;
    occupied:boolean;
    occupiedUntil?: Date;
    reserved:boolean;
    reservationId?: number;
    side:string;
}