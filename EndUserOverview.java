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

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import local.example.draft.data.entity.EndUserEntity;
import local.example.draft.data.repo.EndUserRepo;
import local.example.draft.ui.crud.EndUserCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 *
 * @author paolo mococci
 */

@SpringComponent
@UIScope
@SpringView(name = EndUserOverview.VIEW_NAME)
public class EndUserOverview 
        extends VerticalLayout 
        implements View {
    
    private static final long serialVersionUID = -3643280202166333322L;
    public static final String TITLE_VIEW = "Users Overview";
    public static final String VIEW_NAME = "usersoverview";
    public static final String[] COLUMNS = {"id", "name", "surname", "username"};
    public final Label label = new Label(TITLE_VIEW);
    
    private final EndUserRepo endUserRepo;
    private final EndUserCrud endUserCrud;
    private final Grid<EndUserEntity> usersGrid;
    
    private final TextField filterById;
    private final TextField filterByName;
    private final TextField filterBySurname;
    private final TextField filterByUsername;

    private final Button entry;
    private final HorizontalLayout actions;
    private final VerticalLayout rightLayout;
    private final VerticalLayout leftLayout;
    private final HorizontalLayout mainLayout;
    
    @Autowired
    public EndUserOverview(EndUserRepo repo, EndUserCrud crud) {
        super();
        this.endUserRepo = repo;
        this.endUserCrud = crud;
        this.usersGrid = new Grid<>(EndUserEntity.class);
        this.filterById = new TextField();
        this.filterByName = new TextField();
        this.filterBySurname = new TextField();
        this.filterByUsername = new TextField();
        this.entry = new Button("adding new user", VaadinIcons.PLUS_CIRCLE);
        this.actions = new HorizontalLayout(
                filterById,
                filterByName,
                filterBySurname,
                filterByUsername
        );
        this.rightLayout = new VerticalLayout(actions, usersGrid);
        this.leftLayout = new VerticalLayout(entry, endUserCrud);
        this.mainLayout = new HorizontalLayout(leftLayout, rightLayout);
    }

    @PostConstruct
    public void init() {
        /* add components */
        label.addStyleName(ValoTheme.LABEL_H2);
        this.addComponents(label, mainLayout);
        /* set size of components */
        leftLayout.setSizeUndefined();
        rightLayout.setSizeUndefined();
        mainLayout.setSizeFull();
        usersGrid.setSizeFull();
        /* call handler utility function */
        iota();
    }

    @Override
    public void enter(ViewChangeEvent event) {
        View.super.enter(event);
    }

    private void iota() {
        /* set style */
        entry.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        /* set users grid column */
        usersGrid.setColumns(COLUMNS);
        /* set property of filter text elements */
        filterById.setPlaceholder("filter by id");
        filterById.setValueChangeMode(ValueChangeMode.LAZY);
        filterById.setMaxLength(10);
        filterByName.setPlaceholder("filter by name");
        filterByName.setDescription("case sensitive");
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByName.setMaxLength(20);
        filterBySurname.setPlaceholder("filter by surname");
        filterBySurname.setDescription("case sensitive");
        filterBySurname.setValueChangeMode(ValueChangeMode.LAZY);
        filterBySurname.setMaxLength(20);
        filterByUsername.setPlaceholder("filter by username");
        filterByUsername.setDescription("case sensitive");
        filterByUsername.setValueChangeMode(ValueChangeMode.LAZY);
        filterByUsername.setMaxLength(20);
        /* listing with filtered content */
        filterById.addValueChangeListener(listener -> {
            listById(listener.getValue());
        });
        filterByName.addValueChangeListener(listener -> {
            listByName(listener.getValue());
        });
        filterBySurname.addValueChangeListener(listener -> {
            listBySurname(listener.getValue());
        });
        filterByUsername.addValueChangeListener(listener -> {
            listByUsername(listener.getValue());
        });
        /** 
         * connect selected user on users grid 
         * or disable editor of fields if none is selected 
        */
        usersGrid.asSingleSelect().addValueChangeListener(listener -> {
            endUserCrud.propertyHandler(listener.getValue());
        });
        /* instatiate and be able to typing fields of new comer user */
        entry.addClickListener(listener -> {
            endUserCrud.propertyHandler(new EndUserEntity());
        });
        /** 
         * listen changes make with re-typing fields
         * and refresh data from database 
        */
        endUserCrud.triggerHandler(() -> {
            endUserCrud.setEnabled(false);
            listById(filterById.getValue());
            listByName(filterByName.getValue());
            listBySurname(filterBySurname.getValue());
            listByUsername(filterByUsername.getValue());
        });
        /* initialize listing user grid */
        listById(null);
        listByName(null);
        listBySurname(null);
        listByUsername(null);
    }

    public void listById(String str) {
        if (StringUtils.isEmpty(str)) {
            usersGrid.setItems(endUserRepo.findAll());
        } else if (!str.matches("^[1-9]{1,20}")) {
            Notification.show("typing least an illegal character", 
                    Type.WARNING_MESSAGE);
            filterById.setValue("");
            filterById.focus();
        } else if (!str.matches("[0-9]{1,20}")) {
            Notification.show("", 
                    Type.WARNING_MESSAGE);
            filterById.setValue("typing least an illegal character");
            filterById.focus();
        } else {
            Long items = endUserRepo.count();
            Long id = Long.parseLong(str);
            if (items <= 0) {
                Notification.show("repository is void", 
                        Type.WARNING_MESSAGE);
                filterById.setValue("");
                filterById.focus();
            } else if (!(id >= 1 && id <= items)) {
                Notification.show("out of range", 
                        Type.WARNING_MESSAGE);
                filterById.setValue("");
                filterById.focus();
            } else {
                usersGrid.setItems(endUserRepo.findById(Long.parseLong(str)));
            }
        }
    }

    public void listByName(String str) {
        if (StringUtils.isEmpty(str)) {
            usersGrid.setItems(endUserRepo.findAll());
        } else if (!str.matches("[a-zA-Z]{1,20}")) {
            Notification.show("typing least an illegal character", 
                    Type.WARNING_MESSAGE);
            filterByName.setValue("");
            filterByName.focus();
        } else {
            usersGrid.setItems(endUserRepo.likeByName(str));
        }
    }

    public void listBySurname(String str) {
        if (StringUtils.isEmpty(str)) {
            usersGrid.setItems(endUserRepo.findAll());
        } else if (!str.matches("[a-zA-Z]{1,20}")) {
            Notification.show("typing least an illegal character", 
                    Type.WARNING_MESSAGE);
            filterBySurname.setValue("");
            filterBySurname.focus();
        } else {
            usersGrid.setItems(endUserRepo.likeBySurname(str));
        }
    }

    public void listByUsername(String str) {
        if (StringUtils.isEmpty(str)) {
            usersGrid.setItems(endUserRepo.findAll());
        } else if (!str.matches("[a-zA-Z]{1,20}")) {
            Notification.show("typing least an illegal character", 
                    Type.WARNING_MESSAGE);
            filterByUsername.setValue("");
            filterByUsername.focus();
        } else {
            usersGrid.setItems(endUserRepo.likeByUsername(str));
        }
    }
}
