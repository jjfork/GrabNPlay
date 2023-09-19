import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrabFromDownloader {

    public static void main(String[] args) {
        JFrame frame = new JFrame("GrabNPlay");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel urlLabel = new JLabel("LINK< JESLI PLEJKA JEST PRYWATNA TO LINK MUSI BYC Z 'COPY' I PLEJKA NIE MOZE BYC PRYWATNA:");
        JTextField urlTextField = new JTextField();

        JLabel folderLabel = new JLabel("DAJ SE NAZWE FOLDERA");
        JTextField folderTextLabel = new JTextField();

        JLabel intLabel = new JLabel("ILE NUT JEST NA PLEJCE:");
        JTextField intTextField = new JTextField();

        JButton submitButton = new JButton("POBIERAJ");
        final JProgressBar progressBar = new JProgressBar(0, GrabFromDownloaderApp.getNumberOfSongs());

        panel.add(urlLabel);
        panel.add(urlTextField);
        panel.add(folderLabel);
        panel.add(folderTextLabel);
        panel.add(intLabel);
        panel.add(intTextField);
        panel.add(submitButton);
        panel.add(progressBar);

        submitButton.addActionListener(e -> {
            String urlString = urlTextField.getText();
            String folderString = folderTextLabel.getText();
            int intValue = Integer.parseInt(intTextField.getText());

            System.out.println("URL: " + urlString);
            System.out.println("Integer: " + intValue);
            try (Playwright playwright = Playwright.create()) {
                GrabFromDownloaderApp app = new GrabFromDownloaderApp(playwright);
                app.run(urlString, intValue, progressBar, folderString);
            } catch(Exception ee) {
                ee.printStackTrace();
            }
        });

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame visible
        frame.setVisible(true);

    }
}


class GrabFromDownloaderApp {

    private static String PLAYLIST_URL = "";
    private static String FOLDER_NAME = "";

    public static int getNumberOfSongs() {
        return NUMBER_OF_SONGS;
    }

    private static int NUMBER_OF_SONGS = 0;

    private final Playwright playwright;
    private final List<String> links;
    static List<String> permissions = Arrays.asList("clipboard-read", "clipboard-write");

    public GrabFromDownloaderApp(Playwright playwright) {
        this.playwright = playwright;
        this.links = new ArrayList<>();
    }

    public void run(String urlString, int intValue, JProgressBar progressBar, String folderString) {
        try {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false).setSlowMo(0).setTimeout(9999999));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setLocale("en-US")
                    .setTimezoneId("America/New_York"));

            // Grant permissions
            context.grantPermissions(permissions);

            NUMBER_OF_SONGS = intValue;
            makeListOfSongs(context, urlString);

            FOLDER_NAME = folderString;//wez se tu daj tak zeby bralo nazwe z plejki
            for (int i = 0; i < NUMBER_OF_SONGS && i < links.size(); i++) {
                String songLink = links.get(i);
                downloadSong(context, songLink, folderString, progressBar);
                System.out.println(NUMBER_OF_SONGS - i);
            }

            context.close();
            playwright.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadSong(BrowserContext context, String songLink, String folderName, JProgressBar progressBar) throws IOException {
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
            progressBar.setValue(progressBar.getValue() + 1);
        }
    }

    private void makeListOfSongs(BrowserContext context, String urlString) {
        try (Page page = context.newPage()) {
            PLAYLIST_URL = urlString;
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
