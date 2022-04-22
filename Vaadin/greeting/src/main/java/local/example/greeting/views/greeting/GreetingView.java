package local.example.greeting.views.greeting;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.RolesAllowed;

import local.example.greeting.views.MainLayout;

@PageTitle("Greeting")
@Route(value = "greeting", layout = MainLayout.class)
@RolesAllowed("user")
public class GreetingView
        extends HorizontalLayout {

    private final TextField nameTextField;

    public GreetingView() {
        nameTextField = new TextField("Please enter your name");
        Button helloButton = new Button("Hello");
        helloButton.addClickListener(e -> {
            Notification.show("Hello " + nameTextField.getValue() + "!");
            nameTextField.setValue("");
        });
        helloButton.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(
                Alignment.END,
                nameTextField,
                helloButton
        );

        add(nameTextField, helloButton);
    }
}
