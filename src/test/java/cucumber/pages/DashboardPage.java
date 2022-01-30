package cucumber.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPage {
    public WebDriver driver;

    public DashboardPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//table[@class='table table-hover table-striped table-bordered table-condensed']")
    private WebElement playersTable;

    @FindBy(xpath = "//a[text() ='Registration date']")
    private WebElement registrationDateColumnTitle;

    @FindBy(xpath = "//div[@class = 'grid-view grid-view-loading']")
    private WebElement preloader;

    public void pageLoadingCheck() {
        playersTable.isDisplayed();
    }

    public void sortByRegistrationDateColumn() {
        registrationDateColumnTitle.click();
    }

    public void checkSort() {
        List<WebElement> listOfRegistrationsDates = playersTable.findElements(By.xpath("//tr/td[10]"));
        List<String> listWithSiteSort = new ArrayList<>();
        for (int i = 2; i < listOfRegistrationsDates.size(); i++) {
            listWithSiteSort.add(listOfRegistrationsDates.get(i).getText());
        }
        List<String> listWithCorrectSort = listWithSiteSort.stream().sorted(Comparator.reverseOrder()).toList();
        for (int i = 0; i < listWithSiteSort.size(); i++) {
            Assertions.assertEquals(listWithSiteSort.get(i), listWithCorrectSort.get(i));
        }
    }

}
