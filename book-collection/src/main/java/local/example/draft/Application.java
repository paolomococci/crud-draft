package local.example.draft;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Theme(value = "draft", variant = Lumo.DARK)
@NpmPackage(value = "line-awesome", version = "1.3.0")
@PWA(name = "Draft", shortName = "Draft", offlineResources = {"images/logo.png"})
public class Application 
        extends SpringBootServletInitializer 
        implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
