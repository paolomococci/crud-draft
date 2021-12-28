import { RouterLocation } from '@vaadin/router';
import { UserEndpoint } from 'Frontend/generated/endpoints';
import User from 'Frontend/generated/local/example/hail/data/entity/User';
import Role from 'Frontend/generated/local/example/hail/data/Role';
import { makeAutoObservable } from 'mobx';

export class AppStore {
  applicationName = 'hail';

  location = '';

  currentViewTitle = '';

  user: User | undefined = undefined;

  constructor() {
    makeAutoObservable(this);
  }

  setLocation(location: RouterLocation) {
    if (location.route && location.route.path != '(.*)') {
      this.location = location.route.path;
    } else if (location.pathname.startsWith(location.baseUrl)) {
      this.location = location.pathname.substr(location.baseUrl.length);
    } else {
      this.location = location.pathname;
    }
    this.currentViewTitle = (location?.route as any)?.title || '';
  }

  async fetchUserInfo() {
    this.user = await UserEndpoint.getAuthenticatedUser();
  }

  clearUserInfo() {
    this.user = undefined;
  }

  get loggedIn() {
    return !!this.user;
  }

  isUserInRole(role: Role) {
    return this.user?.roles?.includes(role);
  }
}
export const appStore = new AppStore();
