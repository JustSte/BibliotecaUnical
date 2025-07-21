import { AuthGuardData, createAuthGuard } from 'keycloak-angular';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';

const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  __: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const { authenticated, grantedRoles, keycloak } = authData;

  const requiredRole = route.data['role'];


  console.log('Authenticated:', authenticated);
  console.log('Granted Roles:', grantedRoles.realmRoles);
  console.log('Required Role:', requiredRole);
  if (!requiredRole) {
    return true;
  }

  const hasRequiredRole = (role: string): boolean => {
    if (Array.isArray(role)) {
      return role.some(r => grantedRoles.realmRoles.includes(r));
    }
    return grantedRoles.realmRoles.includes(role);
  };

  if (authenticated && hasRequiredRole(requiredRole)) {
    return true;
  }

  const router = inject(Router);
  return router.parseUrl('/forbidden');
};

export const canActivateAuthRole = createAuthGuard<CanActivateFn>(isAccessAllowed);