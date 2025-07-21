export interface User {
  id: string;
  name: string;
  email?: string;
  username?: string;
  lockerReserved: number;
  seatReserved: number;
}