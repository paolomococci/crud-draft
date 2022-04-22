import { MobxLitElement } from '@adobe/lit-mobx';
import { applyTheme } from 'Frontend/generated/theme';
import { autorun, IAutorunOptions, IReactionDisposer, IReactionOptions, IReactionPublic, reaction } from 'mobx';

export class MobxElement extends MobxLitElement {
  private disposers: IReactionDisposer[] = [];

  protected reaction<T, FireImmediately extends boolean = false>(
    expression: (r: IReactionPublic) => T,
    effect: (arg: T, prev: FireImmediately extends true ? T | undefined : T, r: IReactionPublic) => void,
    opts?: IReactionOptions<T, FireImmediately>
  ): void {
    this.disposers.push(reaction(expression, effect, opts));
  }

  protected autorun(view: (r: IReactionPublic) => any, opts?: IAutorunOptions): void {
    this.disposers.push(autorun(view, opts));
  }

  disconnectedCallback(): void {
    super.disconnectedCallback();
    this.disposers.forEach((disposer) => {
      disposer();
    });
    this.disposers = [];
  }
}

export class View extends MobxElement {
  createRenderRoot(): Element | ShadowRoot {
    return this;
  }
}

export class Layout extends MobxElement {
  connectedCallback(): void {
    super.connectedCallback();
    applyTheme(this.shadowRoot as ShadowRoot);
  }
}
