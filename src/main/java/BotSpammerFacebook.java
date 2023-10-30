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
        guestList.add("https://www.facebook.com/messages/t/100003803361820");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");
        guestList.add("https://www.facebook.com/messages/t/100009082526949");









//        guestList.add("https://www.facebook.com/messages/t/100042345515190");
//        guestList.add("https://www.facebook.com/messages/t/100027892662571");
//        guestList.add("https://www.facebook.com/messages/t/100010730058212");
//        guestList.add("https://www.facebook.com/messages/t/100008372508217");
//        guestList.add("https://www.facebook.com/messages/t/100007465501992");
//        guestList.add("https://www.facebook.com/messages/t/100007213782076");
//        guestList.add("https://www.facebook.com/messages/t/100006778832958");
//        guestList.add("https://www.facebook.com/messages/t/100006145034217");
//        guestList.add("https://www.facebook.com/messages/t/100004626850937");
//        guestList.add("https://www.facebook.com/messages/t/100004599094020");
//        guestList.add("https://www.facebook.com/messages/t/100004476454410");
//        guestList.add("https://www.facebook.com/messages/t/100004346044285");
//        guestList.add("https://www.facebook.com/messages/t/100004077882068");
//        guestList.add("https://www.facebook.com/messages/t/100003891297551");
//        guestList.add("https://www.facebook.com/messages/t/100003803361820");
//        guestList.add("https://www.facebook.com/messages/t/100003048761906");
//        guestList.add("https://www.facebook.com/messages/t/100002623426756");
//        guestList.add("https://www.facebook.com/messages/t/100002591067321");
//        guestList.add("https://www.facebook.com/messages/t/100002531965158");
//        guestList.add("https://www.facebook.com/messages/t/100002439036337");
//        guestList.add("https://www.facebook.com/messages/t/100001969219285");
//        guestList.add("https://www.facebook.com/messages/t/100001841523011");
//        guestList.add("https://www.facebook.com/messages/t/100001577606222");
//        guestList.add("https://www.facebook.com/messages/t/100001466931399");
//        guestList.add("https://www.facebook.com/messages/t/100000596517692");
//        guestList.add("https://www.facebook.com/messages/t/100003017408105");
//        guestList.add("https://www.facebook.com/messages/t/100002605760383");
//        guestList.add("https://www.facebook.com/messages/t/100015326016740");
//        guestList.add("https://www.facebook.com/messages/t/100009311576361");
//        guestList.add("https://www.facebook.com/messages/t/100009281598076");
//        guestList.add("https://www.facebook.com/messages/t/100008958394898");
//        guestList.add("https://www.facebook.com/messages/t/100007924441718");
//        guestList.add("https://www.facebook.com/messages/t/100007519895358");
//        guestList.add("https://www.facebook.com/messages/t/100007424948801");
//        guestList.add("https://www.facebook.com/messages/t/100005217102884");
//        guestList.add("https://www.facebook.com/messages/t/100004771939110");
//        guestList.add("https://www.facebook.com/messages/t/100004524939012");
//        guestList.add("https://www.facebook.com/messages/t/100003968053224");
//        guestList.add("https://www.facebook.com/messages/t/100003595186985");
//        guestList.add("https://www.facebook.com/messages/t/100003064305310");
//        guestList.add("https://www.facebook.com/messages/t/100002610707100");
//        guestList.add("https://www.facebook.com/messages/t/100002169131137");
//        guestList.add("https://www.facebook.com/messages/t/100000127758632");
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