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

package local.example.draft.ui.crud;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import local.example.draft.data.entity.EndUserEntity;
import local.example.draft.data.repo.EndUserRepo;
import local.example.draft.ui.service.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author paolo mococci
 */

@SpringComponent
@UIScope
public class EndUserCrud 
        extends VerticalLayout{
    
    private static final long serialVersionUID = 2278131861372999228L;
    private static final Logger USERCRUDLOG = LoggerFactory.getLogger(EndUserCrud.class);
    private EndUserEntity user;
    private final EndUserRepo endUserRepo;

    /* boolean flags */
    private boolean typingError = false;
    private boolean nameCheck = false;
    private boolean surnameCheck = false;

    /* fields to edit properties of user */
    public TextField name = new TextField("name: ");
    public TextField surname = new TextField("surname: ");
    public TextField username = new TextField();

    /* action buttons */
    public Button save = new Button("save");
    public Button restore = new Button("restore");
    public Button cancel = new Button("cancel");
    public Button delete = new Button("delete");
    public CssLayout actions = new CssLayout(
        save,
        restore,
        cancel,
        delete
    );

    /* binder to user entity */
    public Binder<EndUserEntity> binder = new Binder<>(EndUserEntity.class);
    
    @Autowired
    public EndUserCrud(EndUserRepo repo) {
        super();
        endUserRepo = repo;
        /* adding components */
        this.addComponents(name, surname, username, actions);
        /* configure components */
        name.setRequiredIndicatorVisible(true);
        name.setMaxLength(20);
        name.setDescription("max twenty charaters");
        name.setIcon(VaadinIcons.TEXT_LABEL);
        surname.setRequiredIndicatorVisible(true);
        surname.setMaxLength(20);
        surname.setDescription("max twenty characters");
        surname.setIcon(VaadinIcons.TEXT_LABEL);
        username.setVisible(false);
        /* binding using naming convention */
        binder.forField(name).bind(EndUserEntity::getName, EndUserEntity::setName);
        binder.forField(surname).bind(EndUserEntity::getSurname, EndUserEntity::setSurname);
        binder.forField(username).bind(EndUserEntity::getUsername, EndUserEntity::setUsername);
        /* configure and style components */
        this.setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        restore.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        cancel.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        delete.setStyleName(ValoTheme.BUTTON_DANGER);
        /* wire action buttons to save, delete and reset */
        save.addClickListener(listener -> {
            if (isNameCheck() && isSurnameCheck()) {
		try {
                    username.setValue(
                            name.getValue().concat(surname.getValue())
                                    .toLowerCase());
                    repo.save(user);
                    Notification.show("user's fields saved", 
                            Type.HUMANIZED_MESSAGE);
                    USERCRUDLOG.info("saved: " + user.toString());
                } catch (Exception e) {
                    Notification.show("impossible save user's fields", 
                            Type.ERROR_MESSAGE);
                    USERCRUDLOG.error("can't save: " + user.toString());
                }
                } else {
                    Notification.show("try another time", 
                            Type.WARNING_MESSAGE);
                    USERCRUDLOG.warn("can't save: " + user.toString());
                }
        });
        restore.addClickListener(listener -> {
            propertyHandler(user);
            setEnabled(false);
            USERCRUDLOG.info("restored: " + user.toString());
        });
        cancel.addClickListener(listener -> {
            name.setValue("");
            surname.setValue("");
            username.setValue("");
            propertyHandler(null);
        });
        delete.addClickListener(listener -> {
            try {
                repo.delete(user);
                USERCRUDLOG.info("deleted: " + user.toString());
            } catch (Exception e) {
                USERCRUDLOG.info("can't delete: " + user.toString());
                Notification.show("impossible delete user's fields", 
                        Type.ERROR_MESSAGE);
            }
        });
        name.addBlurListener(listener -> {
            if (isTypingError()) {
                setTypingError(false);
            } else if (name.isEmpty()) {
                Notification.show("name is required field", 
                        Type.HUMANIZED_MESSAGE);
            } else if (!name.getValue().matches("[a-zA-Z]{1,20}")) {
                setTypingError(true);
                name.setValue("");
                name.focus();
                Notification.show("typing least an illegal character in name field", 
                        Type.WARNING_MESSAGE);
            } else {
                setNameCheck(true);
            }
        });
        surname.addBlurListener(listener -> {
            if (isTypingError()) {
                setTypingError(false);
            } else if (surname.isEmpty()) {
                Notification.show("surname is required field", 
						Type.HUMANIZED_MESSAGE);
            } else if (!surname.getValue().matches("[a-zA-Z]{1,20}")) {
                setTypingError(true);
                surname.setValue("");
                surname.focus();
                Notification.show("typing least an illegal character in surname field", 
						Type.WARNING_MESSAGE);
            } else {
                setSurnameCheck(true);
            }
        });
        setEnabled(false);
    }
    
    public final void propertyHandler(EndUserEntity temp) {
        if (temp == null) {
            setEnabled(false);
            return;
        }
        final boolean persisted = temp.getId() != null;
        if (persisted) {
            //user = endUserRepo.findOne(temp.getId());
            /**
             * if you want to use Spring Boot 2.0.0.RELEASE in place of
             * Spring Boot 1.5.10.RELEASE, you will have to comment or 
             * delete the line of code that precedes this note and 
             * uncomment the line of code below
             */
            user = endUserRepo.getOne(temp.getId());
        } else {
            user = temp;
        }
        restore.setEnabled(persisted);
        /* bind user properties */
        binder.setBean(user);
        setEnabled(true);
        save.focus();
        /* select all text in name fields */
        name.selectAll();
    }

    public void triggerHandler(Trigger trigger) {
        /* notified when save or delete is clicked */
        save.addClickListener(listener -> {
            trigger.onChange();
        });
        delete.addClickListener(listener -> {
            trigger.onChange();
        });
    }

    public boolean isTypingError() {
        return typingError;
    }

    public void setTypingError(boolean typingError) {
        this.typingError = typingError;
    }

    public boolean isNameCheck() {
        return nameCheck;
    }

    public void setNameCheck(boolean nameCheck) {
        this.nameCheck = nameCheck;
    }

    public boolean isSurnameCheck() {
        return surnameCheck;
    }

    public void setSurnameCheck(boolean surnameCheck) {
        this.surnameCheck = surnameCheck;
    }
}
