import { Route } from '@vaadin/router';
import Role from './generated/local/example/hail/data/Role';
import { appStore } from './stores/app-store';
import './views/home/home-view';
import './views/main-layout';

export type ViewRoute = Route & {
  title?: string;
  icon?: string;
  requiresLogin?: boolean;
  rolesAllowed?: Role[];
  children?: ViewRoute[];
};

export const hasAccess = (route: Route) => {
  const viewRoute = route as ViewRoute;
  if (viewRoute.requiresLogin && !appStore.loggedIn) {
    return false;
  }

  if (viewRoute.rolesAllowed) {
    return viewRoute.rolesAllowed.some((role) => appStore.isUserInRole(role));
  }
  return true;
};

export const views: ViewRoute[] = [
  {
    path: '',
    component: 'home-view',
    icon: '',
    title: '',
  },
  {
    path: 'home',
    component: 'home-view',
    icon: 'la la-home',
    title: 'Home',
  },
  {
    path: 'greeting',
    component: 'greeting-view',
    rolesAllowed: [Role.USER],
    icon: 'la la-handshake',
    title: 'Greeting',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      await import('./views/greeting/greeting-view');
      return;
    },
  },
  {
    path: 'guests',
    component: 'guests-view',
    rolesAllowed: [Role.ADMIN],
    icon: 'la la-users-cog',
    title: 'Guests',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      await import('./views/guests/guests-view');
      return;
    },
  },
];
export const routes: ViewRoute[] = [
  {
    path: 'login',
    component: 'login-view',
    rolesAllowed: [Role.USER],
    icon: '',
    title: 'Login',
    action: async (_context, _command) => {
      await import('./views/login/login-view');
      return;
    },
  },

  {
    path: '',
    component: 'main-layout',
    children: [...views],
  },
];
