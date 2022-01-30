package cucumber.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.FindBy;

public class LoginPage {

    public WebDriver driver;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(id = "UserLogin_username")
    private WebElement username;

    @FindBy(id = "UserLogin_password")
    private WebElement password;

    @FindBy(xpath = "//div[./input[@value = 'Sign in']]")
    private WebElement signIn;

    @FindBy(id = "UserLogin_verifyCode")
    private WebElement captcha;

    public void inputLogin(String login) {
        username.sendKeys(login);
    }

    public void inputPassword(String pass) {
        password.sendKeys(pass);
    }

    public void inputCaptcha() {
        captcha.sendKeys("rubede");
    }

    public void clickSignInButton() {
        signIn.click();
    }

    public void pageLoadingCheck(){
        username.isDisplayed();
        password.isDisplayed();
    }
}

