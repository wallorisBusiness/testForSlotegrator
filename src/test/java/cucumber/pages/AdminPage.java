package cucumber.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdminPage {

    public WebDriver driver;

    public AdminPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//p[text()='Players online / total']")
    private WebElement playersOnlineTab;

    public WebElement getPlayersOnlineTab() {
        return playersOnlineTab;
    }

    public void openPlayersDashboard(){
        playersOnlineTab.click();
    }
}
