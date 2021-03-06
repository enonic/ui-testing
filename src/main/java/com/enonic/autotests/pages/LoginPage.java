package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AuthenticationException;
import com.enonic.autotests.logger.Logger;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for Login page version 4.7
 * 
 */
public class LoginPage extends Page {

	private static Logger logger = Logger.getLogger();

	private String title = "Enonic CMS - Login";

	@FindBy(how = How.NAME, using = "username")
	private WebElement usernameInput;
	@FindBy(how = How.NAME, using = "password")
	private WebElement passwordInput;
	@FindBy(how = How.NAME, using = "login")
    private WebElement loginButton;



	/**
	 * @param session
	 */
	public LoginPage(TestSession session) {
		super(session);

		(new WebDriverWait(getSession().getDriver(), TestUtils.TIMEOUT_IMPLICIT)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().trim().contains(title);
			}
		});
	}

	public void doLogin(String username, String password)
    {
        TestUtils.getInstance().selectByText(getSession(), By.name("lang"), "English");
        TestUtils.getInstance().waitAndFind(By.xpath("//label[text()='Userstore']"), getDriver());
		usernameInput.sendKeys(username);
		passwordInput.sendKeys(password);
		loginButton.submit();

		if (TestUtils.getInstance().waitAndFind(By.className("cms-error"), getDriver())) {
			String erMess = getSession().getDriver().findElement(By.className("cms-error")).getText();
			logger.info("could not to login " + erMess);
			TestUtils.getInstance().saveScreenshot(getSession());
			throw new AuthenticationException("Wrong username or password");
		}

	}

}
