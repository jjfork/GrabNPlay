import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrabFromDownloader {

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            GrabFromDownloaderApp app = new GrabFromDownloaderApp(playwright);
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class GrabFromDownloaderApp {

    private static final String PLAYLIST_URL = "https://soundcloud.com/user-33482280/sets/techno2/s-vK83KUmbYE9?ref=clipboard&p=a&c=1&si=7f04fde900d44815994696a03ffb1f5e&utm_source=clipboard&utm_medium=text&utm_campaign=social_sharing";
    private static final int NUMBER_OF_SONGS = 32;

    private final Playwright playwright;
    private final List<String> links;
    static List<String> permissions = Arrays.asList("clipboard-read" , "clipboard-write");

    public GrabFromDownloaderApp(Playwright playwright) {
        this.playwright = playwright;
        this.links = new ArrayList<>();
    }

    public void run() {
        try {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false).setSlowMo(0).setTimeout(9999999));
            BrowserContext context = browser.newContext();

            // Grant permissions
            context.grantPermissions(permissions);


            makeListOfSongs(context);

            String folderName = "techno2ToDie";//wez se tu daj tak zeby bralo nazwe z plejki
            for (int i = 0; i < NUMBER_OF_SONGS && i < links.size(); i++) {
                String songLink = links.get(i);
                downloadSong(context, songLink, folderName);
                System.out.println(NUMBER_OF_SONGS - i);
            }

            context.close();
            playwright.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadSong(BrowserContext context, String songLink, String folderName) throws IOException {
        try (Page page = context.newPage()) {
            page.navigate("https://www.grabfrom.com/");
            FrameLocator frame = page.frameLocator("iframe[name=\"conversionFrame\"]");
            frame.locator("#videoURL").fill(songLink);
            frame.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName(".mp3 (128 kbps)")).click();
            frame.getByRole(AriaRole.OPTION, new FrameLocator.GetByRoleOptions().setName(".mp3 (320 kbps)")).click();
            frame.locator("#submitFlashButtonContainer").click();

            Download download = page.waitForDownload(() -> {
                Page popupPage = page.waitForPopup(() -> frame.locator("#dloadButton").click());
            });

            Path desktopPath = Path.of(System.getProperty("user.home", "Desktop"));
            Path destinationFolder = desktopPath.resolve(folderName);

            if (!Files.exists(destinationFolder)) {
                Files.createDirectories(destinationFolder);
            }

            Path destinationFilePath = destinationFolder.resolve(download.suggestedFilename());
            download.saveAs(destinationFilePath);
        }
    }

    private void makeListOfSongs(BrowserContext context) {
        try (Page page = context.newPage()) {
            page.navigate(PLAYLIST_URL);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("I Accept")).click();
            for (int i = 1; i <= NUMBER_OF_SONGS; i++) {
                page.click(".trackList__item:nth-child(" + i + ") > .trackItem > .trackItem__additional > .trackItem__actions > .soundActions > .sc-button-group > .sc-button-copylink");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Skip to next")).click();
                links.add(validateCopyToClipboard(page));
                System.out.println(validateCopyToClipboard(page));
            }
        }
    }

    private String validateCopyToClipboard(Page page) {
        JSHandle clipboardHandle = page.evaluateHandle("() => navigator.clipboard.readText()");
        String clipboardValue = clipboardHandle.jsonValue().toString();
        clipboardHandle.dispose();
        return clipboardValue;
    }
}
