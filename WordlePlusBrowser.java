/*
Leo Wang
06 / 18 / 26

Controls Wordle+ Infinite using Selenium.
Enters guesses automatically and reads the G/Y/B result.
*/

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WordlePlusBrowser {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private static final String URL = "https://mikhad.github.io/wordle/#infinite";

    public WordlePlusBrowser() {
        // Open Chrome.
        driver = new ChromeDriver();

        // JavaScript controller for the webpage.
        js = (JavascriptExecutor) driver;

        // Wait up to 15 seconds when needed.
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        // Open Wordle+ Infinite.
        driver.get(URL);

        // Wait until the Wordle+ keyboard exists.
        wait.until(driver -> driver.findElements(By.cssSelector(".keyboard")).size() > 0);

        sleep(1000);

        // Try to remove any popup overlays.
        closePopups();

        System.out.println("Wordle+ opened.");
    }

    private void closePopups() {

        for (int i = 0; i < 5; i++) {
            Object closed = js.executeScript(
                "const exits = document.querySelectorAll('.overlay.visible .exit');" +
                "if (exits.length > 0) {" +
                "   exits[0].click();" +
                "   return true;" +
                "}" +
                "return false;"
            );

            if (Boolean.FALSE.equals(closed)) {
                break;
            }

            sleep(300);
        }
    }

    public void enterGuess(String word) {
        System.out.println("Typing: " + word);

        closePopups();

        // Enter every letter.
        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i)).toUpperCase();
            boolean clicked = clickKeyboardKey(letter);

            if (!clicked) {
                throw new RuntimeException("Could not find Wordle+ key: " + letter);
            }

            sleep(120);
        }

        // Click ENTER.
        boolean enterClicked = clickKeyboardKey("ENTER");

        if (!enterClicked) {
            throw new RuntimeException("Could not find Wordle+ ENTER key.");
        }

        System.out.println("Guess submitted.");
    }

    private boolean clickKeyboardKey(String keyText) {
        /*
         * Wordle+'s keyboard is made from div elements.
         *
         * We find all direct keyboard keys,
         * compare their text,
         * and call JavaScript .click() on the matching key.
         *
         * This fires Wordle+'s actual Svelte click handler.
         */
        Object result = js.executeScript(
            "const keys = document.querySelectorAll('.keyboard .row > div');" +
            "for (const key of keys) {" +
            "   if (key.textContent.trim().toUpperCase() === arguments[0]) {" +
            "       key.click();" +
            "       return true;" +
            "   }" +
            "}" +
            "return false;",
            keyText
        );

        return Boolean.TRUE.equals(result);
    }

    public String readResult(int rowNumber) {
        /*
         * Wait until Wordle+ marks this row complete.
         *
         * Wordle+ adds the 'complete' class once
         * the guess has been successfully submitted.
         */
        WebElement row = wait.until(driver -> {
            List<WebElement> rows = driver.findElements(By.cssSelector(".board-row"));

            if (rows.size() <= rowNumber) {
                return null;
            }

            WebElement currentRow = rows.get(rowNumber);
            String rowClass = currentRow.getAttribute("class");

            if (rowClass != null && rowClass.contains("complete")) {
                return currentRow;
            }

            return null;
        });

        // Wait for all tile-flip animations to finish.
        sleep(1800);

        // Get the five tiles.
        List<WebElement> tiles = row.findElements(By.cssSelector(".tile"));
        String result = "";

        for (WebElement tile : tiles) {
            String tileClass = tile.getAttribute("class");
            System.out.println("Tile class: " + tileClass);

            /*
             * Wordle+ stores the state in each tile's class:
             *
             * 🟩 = correct
             * 🟨 = present
             * otherwise = absent
             */
            if (tileClass.contains("🟩")) {
                result += "G";
            } else if (tileClass.contains("🟨")) {
                result += "Y";
            } else {
                result += "B";
            }
        }

        return result;
    }

    public void newGame() {
        // Infinite mode generates a new word on refresh.
        driver.navigate().refresh();

        wait.until(driver -> driver.findElements(By.cssSelector(".keyboard")).size() > 0);

        sleep(1000);

        closePopups();
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void close() {
        driver.quit();
    }
}
