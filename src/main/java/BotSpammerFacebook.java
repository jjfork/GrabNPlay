import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class BotSpammerFacebook {

    public static String message = "testsystemudospamu";
    public static String message1 = "22:20"; // First message
    public static String message2 = "22:16";


    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduleMessage(scheduler, LocalTime.of(22, 20), message1);
//        scheduleMessage(scheduler, LocalTime.of(22,16), message2);
    }

    private static void scheduleMessage(ScheduledExecutorService scheduler, LocalTime targetTime, String message) {
        Duration initialDelay = Duration.between(LocalTime.now(), targetTime);

        scheduler.scheduleAtFixedRate(() -> {
            Playwright playwright = Playwright.create();
            Page page = initPW(playwright);
            login(page);
            page.waitForTimeout(5000);
            sendScheduledMessage(page, message);

            closePW(page);
        }, initialDelay.toMinutes(), 24 * 60, TimeUnit.MINUTES);
    }

    private static void sendScheduledMessage(Page page, String message) {
        List <String> guestList = createGuestList();
        spamSpamSpam(page, guestList, message);
    }

    private static void spamSpamSpam(Page page, List<String> guestList, String message) {

        guestList.stream()
                .forEach(link -> {
                    page.navigate(link);
                    page.getByRole(AriaRole.PARAGRAPH).click();
                    page.getByLabel("Message", new Page.GetByLabelOptions().setExact(true)).fill(message);
                    page.getByLabel("Press enter to send").click();
                    page.waitForTimeout(500);
                });

    }

    private static List<String> createGuestList() {
        List <String> guestList = new ArrayList<>();
        
        return guestList;
    }

    private static void login(Page page) {
        page.navigate("https://www.facebook.com/");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Zezwól na wszystkie pliki cookie")).click();
        page.getByTestId("royal_email").fill("");
        page.getByTestId("royal_email").press("Tab");
        page.getByTestId("royal_pass").fill("");
        page.getByTestId("royal_pass").press("Enter");
    }

    private static Page initPW(Playwright playwright) {
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false).setSlowMo(10));
        BrowserContext context = browser.newContext();
        Page page = context.newPage();
        return page;
    }

    private static void closePW(Page page) {
        BrowserContext context = page.context();
        Browser browser = context.browser();

        // Close the page, context, and browser
        page.close();
        context.close();
        browser.close();
    }
}
