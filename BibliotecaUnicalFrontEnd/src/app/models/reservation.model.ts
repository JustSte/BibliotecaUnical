export interface Reservation {

    id:number;

    resourceType:String;
    resourceId:number;

    userId:String;
    userMail:String;

    status:String;

    startTime:Date;
    nextConfirmationTime:Date;
}