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
        JFrame frame = new JFrame("Simple GUI Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);

        // Create a JPanel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Create labels and text fields
        JLabel urlLabel = new JLabel("URL:");
        JTextField urlTextField = new JTextField();

        JLabel intLabel = new JLabel("Integer:");
        JTextField intTextField = new JTextField();

        // Create a button
        JButton submitButton = new JButton("Submit");

        // Add components to the panel
        panel.add(urlLabel);
        panel.add(urlTextField);
        panel.add(intLabel);
        panel.add(intTextField);
        panel.add(submitButton);

        // Add an action listener to the button
        submitButton.addActionListener(e -> {
            String urlString = urlTextField.getText();
            int intValue = Integer.parseInt(intTextField.getText());

            // Call your function here with urlString and intValue
            // Replace the following line with your actual function call
            System.out.println("URL: " + urlString);
            System.out.println("Integer: " + intValue);
            try (Playwright playwright = Playwright.create()) {
                GrabFromDownloaderApp app = new GrabFromDownloaderApp(playwright);
                app.run(urlString, intValue);
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
    private static int NUMBER_OF_SONGS = 0;

    private final Playwright playwright;
    private final List<String> links;
    static List<String> permissions = Arrays.asList("clipboard-read", "clipboard-write");

    public GrabFromDownloaderApp(Playwright playwright) {
        this.playwright = playwright;
        this.links = new ArrayList<>();
    }

    public void run(String urlString, int intValue) {
        try {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false).setSlowMo(0).setTimeout(9999999));
            BrowserContext context = browser.newContext();

            // Grant permissions
            context.grantPermissions(permissions);

            NUMBER_OF_SONGS = intValue;
            makeListOfSongs(context, urlString);

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
