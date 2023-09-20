import com.microsoft.playwright.*;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

import java.nio.file.Paths;

public class Zapisywacz {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false).setSlowMo(500));
            BrowserContext context = browser.newContext();
            context.tracing().start(new Tracing.StartOptions()
                    .setScreenshots(true)
                    .setSnapshots(true));
            Page page = context.newPage();
            login(page);
            regis(page);
//1 Modelowanie wolumetryczne obiektów przestrzennych [W06GIT-SI0025L]
            register(page, "#W06GIT-SI0025L");
            pick_group(page, 1);
            confirm(page);
            go_back(page);

//2 Modelowanie wolumetryczne obiektów przestrzennych [W06GIT-SI0025W]
            register(page, "#W06GIT-SI0025W");

//3 Podstawy geofizyki [W06GIT-SI0021L]
            register(page, "#W06GIT-SI0021L");
            pick_group(page,2);
            confirm(page);
            go_back(page);


//4 Podstawy geofizyki [W06GIT-SI0021C]
            register(page, "#W06GIT-SI0021C");

//5 Podstawy geofizyki [W06GIT-SI0021W]
            register(page, "#W06GIT-SI0021W");



//6 Podstawy przetwarzania geodanych [W06GIT-SI0024L]
            register(page, "#W06GIT-SI0024L");
            pick_group(page,1);
            confirm(page);
            go_back(page);


//7 Podstawy przetwarzania geodanych [W06GIT-SI0024W]
            register(page, "#W06GIT-SI0024W");

//8 Systemy geoinformacyjne [W06GIT-SI0026L]
            register(page, "#W06GIT-SI0026L");
            pick_group(page,2);
            confirm(page);
            go_back(page);


//9 Systemy geoinformacyjne [W06GIT-SI0026W]
            register(page, "#W06GIT-SI0026W");

//10 Wstęp do uczenia maszynowego [W06GIT-SI0022L]
            register(page, "#W06GIT-SI0022L");
            pick_group(page,1);
            confirm(page);
            go_back(page);

//11 Wstęp do uczenia maszynowego [W06GIT-SI0022W]
            register(page, "#W06GIT-SI0022W");

//12 Zastosowania GIS w naukach o Ziemi [W06GIT-SI0023L]
            register(page, "#W06GIT-SI0023L");
            pick_group(page,1);
            confirm(page);
            go_back(page);

//13 Zastosowania GIS w naukach o Ziemi [W06GIT-SI0023W]
            register(page, "#W06GIT-SI0023W");

            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));
        }
    }

    private static void login(Page page) {
        page.navigate("https:web.usos.pwr.edu.pl");
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("| zaloguj się")).click();
        page.getByPlaceholder("Nazwa użytkownika").click();
        String login = "hehe";
        page.getByPlaceholder("Nazwa użytkownika").fill(login);
        page.getByPlaceholder("Hasło").click();
        String haslo = "hehe@";
        page.getByPlaceholder("Hasło").fill(haslo);
    }

    private static void go_back(Page page) {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(". wróć do listy przedmiotów")).click();
    }

    private static void register(Page page, String selektor_grupa) {
        page.locator(selektor_grupa + " > td:nth-child(4) > span:nth-child(3) > a:nth-child(3)").click();
    }

    private static void pick_group(Page page, int numerGrupy) {
        page.locator("(//input[@type='radio'])["+ numerGrupy +"]").check();
    }

    private static void confirm(Page page) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Rejestruj")).nth(1).click();
    }

    private static void regis(Page page) {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Zaloguj")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("DLA STUDENTÓW")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Rejestracja")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Rejestracje na przedmioty .")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Kliknij tutaj, aby przejść do listy przedmiotów dostępnych w ramach tej rejestracji.")).click();
    }
}