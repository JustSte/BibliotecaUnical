export interface Chair{
    id:number;
    occupied:boolean;
    occupiedUntil?: Date;
    reserved:boolean;
    reservationId?: number;
}