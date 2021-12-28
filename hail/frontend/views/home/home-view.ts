import { html } from 'lit';
import { customElement } from 'lit/decorators.js';
import { View } from '../../views/view';

@customElement('home-view')
export class HomeView extends View {
  render() {
    return html`<div>
      <h2>Welcome to this new web application.</h2>
      <h4>The default common user is John Under, with username: johnunder and password: johnunder</h4>
      <h4>The default administrator user is Amy Boss, with username: amyboss and password: amyboss</h4>
    </div>`;
  }

  connectedCallback() {
    super.connectedCallback();
    this.classList.add(
      'flex',
      'flex-col',
      'h-full',
      'items-center',
      'justify-center',
      'p-l',
      'text-center',
      'box-border'
    );
  }
}
