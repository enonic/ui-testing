package com.enonic.autotests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Home' page. Version 4.7
 *
 */
public class HomePage extends Page {
	public static String TITLE = "Enonic CMS - Login";

	@FindBy(xpath = "//span[text()='Admin Console']")
	private WebElement admConsoleLink;

	/**
	 * @param session
	 */
	public HomePage(TestSession session) {
		super(session);

	}

	public void open() {
		// open page via the driver.get(BASE_URL)
		getSession().getDriver().get(getSession().getBaseUrl());
		TestUtils.getInstance().waitUntilTitleVisible(getSession(), TITLE);

	}



	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public void openAdminConsole(String username, String password) {
		//admConsoleLink.click();
		if (!getSession().isLoggedIn()) {
			getLogger().info("try to login with userName:" + username + " password: " + password);
			long start = System.currentTimeMillis();
			LoginPage loginPage = new LoginPage(getSession());
			loginPage.doLogin(username, password);

			getLogger().perfomance("user logged in " + username + "  password:" + password, start);
			getSession().setLoggedIn(true);
		}
		TestUtils.getInstance().waitUntilVisible(getSession(), By.name(AbstractAdminConsolePage.LEFT_FRAME_NAME),AppConstants.PAGELOAD_TIMEOUT);
		TestUtils.getInstance().waitUntilVisible(getSession(), By.name(AbstractAdminConsolePage.MAIN_FRAME_NAME), AppConstants.PAGELOAD_TIMEOUT);
		//TestUtils.getInstance().saveScreenshot(getSession());
	}

	/**
	 * @param siteName
	 * @return
	 */
	public boolean isSiteExistsOnHomePage(String siteName) {
		// "//a[text()='siteName']" <span class="name">gav</span>
		// List<WebElement> elements =
		// ((FindsByLinkText)getSession().getDriver()).findElementsByLinkText(siteName);
		return true;
	}
}
