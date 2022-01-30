package cucumber.steps;

import cucumber.pages.AdminPage;
import cucumber.pages.DashboardPage;
import cucumber.pages.LoginPage;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import lombok.SneakyThrows;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

public class GeneralSteps {

    WebDriver driver = new ChromeDriver();

    LoginPage loginPage = new LoginPage(driver);
    AdminPage adminPage = new AdminPage(driver);
    DashboardPage dashboardPage = new DashboardPage(driver);

    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Александр\\Documents\\chromedriver\\chromedriver.exe");
    }

    @Дано("Страница по ссылке {string} открыта")
    public void openLink(String url) {
        driver.get(url);
        driver.get("chrome://settings/clearBrowsingDataConfirm");
        driver.get(url);
    }

    @SneakyThrows
    @И("Выполнено ожидание в течении {int} секунд")
    public void wait(int countOfSeconds) {
        driver.manage().timeouts().implicitlyWait(countOfSeconds, TimeUnit.SECONDS);
    }

    @И("В поле username введено значение \"([^\"]*)\"$")
    public void enterLogin(String username) {
        loginPage.inputLogin(username);
    }

    @И("В поле password введено значение \"([^\"]*)\"$")
    public void enterPassword(String pasw) {
        loginPage.inputPassword(pasw);
    }

    @И("Выполнено нажатие на кнопку sign in")
    public void clickSignIn() {
        loginPage.clickSignInButton();
    }

    @И("Страница админки загрузилась")
    public void adminPageLoading() {
        driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
        adminPage.getPlayersOnlineTab().isDisplayed();
    }

    @И("Страница с таблицей игроков открыта")
    public void dashboardPageOpen() {
        adminPage.openPlayersDashboard();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        dashboardPage.pageLoadingCheck();
    }

    @И("Выполнена сортировка по столбцу Registration date")
    public void sortByColumn() {
        dashboardPage.sortByRegistrationDateColumn();
        dashboardPage.checkSort();
    }

    @Тогда("Сортировка по Registration date выполнена корректно")
    public void sortChecking() {
        dashboardPage.checkSort();
        driver.close();
    }

}
