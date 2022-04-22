import { login as loginImpl, LoginResult, logout as logoutImpl } from '@vaadin/fusion-frontend';
import { appStore } from './stores/app-store';

interface Authentication {
  timestamp: number;
}

let authentication: Authentication | undefined = undefined;

const AUTHENTICATION_KEY = 'authentication';
const THIRTY_DAYS_MS = 30 * 24 * 60 * 60 * 1000;

const storedAuthenticationJson = localStorage.getItem(AUTHENTICATION_KEY);
if (storedAuthenticationJson !== null) {
  const storedAuthentication = JSON.parse(storedAuthenticationJson) as Authentication;
  const hasRecentAuthenticationTimestamp = new Date().getTime() - storedAuthentication.timestamp < THIRTY_DAYS_MS;
  if (hasRecentAuthenticationTimestamp) {
    authentication = storedAuthentication;
  } else {
    setSessionExpired();
  }
}

export function setSessionExpired() {
  authentication = undefined;

  localStorage.removeItem(AUTHENTICATION_KEY);
}

export function isAuthenticated() {
  return !!authentication;
}

export async function login(username: string, password: string): Promise<LoginResult> {
  const result = await loginImpl(username, password);
  if (!result.error) {
    await appStore.fetchUserInfo();
    authentication = {
      timestamp: new Date().getTime(),
    };

    localStorage.setItem(AUTHENTICATION_KEY, JSON.stringify(authentication));
  }

  return result;
}

export async function logout() {
  setSessionExpired();
  await logoutImpl();
  appStore.clearUserInfo();
  location.href = '';
}
