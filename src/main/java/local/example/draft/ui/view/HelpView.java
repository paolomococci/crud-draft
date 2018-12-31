/**
 * 
 * Copyright 2018 paolo mococci
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package local.example.draft.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author paolo mococci
 */

@SpringComponent
@UIScope
@SpringView(name = HelpView.VIEW_NAME)
public class HelpView 
        extends VerticalLayout 
        implements View {
    
    private static final long serialVersionUID = -5708192920815457495L;
    public static final String TITLE_VIEW = "Help View";
    public static final String VIEW_NAME = "";
    private static final String SUB_TITLE = "Create Read Update and Delete " + 
            "data fields of system users";
    private static final String PRESENTATION = "This CRUD Web Application.";
    private static final String EXPLANITION =  
            "From which it's possible to administer the registration and " + 
            "cancellation of users in a hypotetical system.";
    public final Label label = new Label(TITLE_VIEW);
    public final Label subtitle = new Label();
    public final Label present = new Label();
    public final Label explain = new Label();
    
    @Autowired
    public HelpView() {
        super();
    }

    @PostConstruct
    public void init() {
        label.addStyleName(ValoTheme.LABEL_H2);
        this.addComponent(label);
        subtitle.addStyleName(ValoTheme.LABEL_H3);
        subtitle.setCaption(SUB_TITLE);
        this.addComponent(subtitle);
        present.addStyleName(ValoTheme.LABEL_HUGE);
        present.setCaption(PRESENTATION);
        this.addComponent(present);
        explain.addStyleName(ValoTheme.LABEL_HUGE);
        explain.setCaption(EXPLANITION);
        this.addComponent(explain);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        View.super.enter(event);
    }
}
