import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { KeycloakService } from './app/keycloak/keycloak.service';

export const canActivateProfile: CanActivateFn = () => {
  const keycloak = inject(KeycloakService);
  return keycloak.isLoggedIn();
};