package com.enonic.autotests.pages.adminconsole;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.pages.Page;
import com.enonic.autotests.pages.adminconsole.content.ContentRepositoryViewFrame;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.adminconsole.content.RepositoriesListFrame;
import com.enonic.autotests.pages.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.pages.adminconsole.site.SectionContentsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SiteInfoPage;
import com.enonic.autotests.pages.adminconsole.site.SiteMenuItemsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SitePortletsTablePage;
import com.enonic.autotests.pages.adminconsole.site.SiteTemplatesPage;
import com.enonic.autotests.pages.adminconsole.site.SitesTableFrame;
import com.enonic.autotests.pages.adminconsole.system.SystemFrame;
import com.enonic.autotests.pages.adminconsole.userstores.UsersTableFrame;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page object for Left Menu Frame.
 * 
 */
public class LeftMenuFrame extends Page
{
	/** locator for "Content" folder */
	public String CONTENT_FOLDER_LOCATOR_XPATH = "//a[@target='mainFrame' and descendant::img[contains(@src,'images/icon_folder.gif')]]//span[text()='Content']";

	/** '+' or '-' icon near the 'Content' folder */
	private String CONTENTFOLDER_EXPANDER_XPATH = "//a[@id='openBranch-categories-']/img[contains(@src,'%s')]";

	private String SITEFOLDER_EXPANDER_XPATH = "//a[descendant::span[@id='menuitemText' and contains(.,'Sites')]]/../../td/a/img[contains(@src,'%s')]";
	private String SITENAME_LINK_XPATH = "//a[descendant::span[contains(@id,'menuitemText') and contains(.,'%s')]]";
	private String SITE_EXPANDER_IMG_XPATH = SITENAME_LINK_XPATH + "/../../td/a/img";

	private String SITE_MENU_LINK_XPATH = SITENAME_LINK_XPATH + "/../../following-sibling::tr//span[@id='menuitemText' and contains(.,'Menu')]";
	private String SITE_PORTLETS_LINK_XPATH = SITENAME_LINK_XPATH + "/../../following-sibling::tr//span[@id='menuitemText' and contains(.,'Portlets')]";
	private String SITE_PAGE_TEMPLATES_LINK_XPATH = SITENAME_LINK_XPATH + "/../../following-sibling::tr//span[@id='menuitemText' and contains(.,'Page templates')]";
	private String SITE_MENU_EXPANDER_IMG_XPATH = SITE_MENU_LINK_XPATH + "/../../../td/a/img";

	/** locator for section menu item */
	private String SITE_MENU_ITEM_XPATH = SITENAME_LINK_XPATH + "/../../following-sibling::tr//span[contains(@id,'menuitemText') and contains(.,'%s')]";

	// 1) select <tr> which contains all categories 2) select the child, which contains the Category Name 3) select first element <td> - this is a '+/-'  link with icon
	private String CATEGORY_MENU_ITEM = "//tr[@id='id-categories']/child::td[2]//table[@class='menuItem' and descendant::tr[1]//span[text()='%s']]";
	private String CATEGORY_EXPANDER_IMG_XPATH = CATEGORY_MENU_ITEM + "//td[1]/a/img";
	private String CATEGORY_XPATH = CATEGORY_MENU_ITEM + "//td[2]/a";
	private String LAST_CHILD_CATEGORY_LINK = CATEGORY_MENU_ITEM + "//table[@class='menuItem' and descendant::img[contains(@src,'L.png')]]//a";

	public static String CONTENT_TYPES_MENU_ITEM_XPATH = "//a[text()='Content types']";
	public static String USERSTORES_MENU_ITEM_XPATH = "//a[text()='Userstores']";

	public static String SITES_MENU_ITEM_XPATH = "//a/span[@id='menuitemText' and contains(.,'Sites')]";
	public static String USERS_MENU_ITEM_XPATH = "//a[child::img[contains(@src,'icon_users.gif')] and text()='Users']";
	public static String SYSTEM_MENU_ITEM_XPATH = "//a[child::img[@src='images/icon_system.gif'] and text()='System']";
	public static String STATIC_RES_MENU_ITEM_XPATH = "//a[child::img[@src='images/icon_folder_resources.gif']]";

	/**
	 * @param session
	 */
	public LeftMenuFrame( TestSession session )
	{
		super(session);
	}

	/**
	 * Verify: is present menu-link in the Left Frame
	 * 
	 * @param xpath
	 * @return true if present, otherwise false.
	 */
	public boolean isMenuItemPresent(String xpath)
	{
		return TestUtils.getInstance().waitAndFind(By.xpath(xpath), getDriver());
	}

	/**
	 * Clicks by 'Sites' link and open frame with table of sites.
	 * 
	 * @param testSession
	 * @return
	 */
	public SitesTableFrame openSitesTableFrame(TestSession testSession)
	{
		SitesTableFrame sitesframe = new SitesTableFrame(testSession);
		PageNavigator.clickMenuItemAndSwitchToRightFrame(testSession, SITES_MENU_ITEM_XPATH);
		sitesframe.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return sitesframe;

	}

	public UsersTableFrame openUsersTableFrame(TestSession testSession)
	{
		UsersTableFrame usersframe = new UsersTableFrame(getSession());
		// 1 expand a userstores
		String userstoresExpanderXpath = "//img[@id = 'img-system-userstores']";
		TestUtils.getInstance().expandFolder(testSession, userstoresExpanderXpath);
		// 2 expand a default
		String defaultExpanderXpath = "//img[@id='img-domain1']";
		TestUtils.getInstance().expandFolder(testSession, defaultExpanderXpath);
		// 3 click by Users
		PageNavigator.clickMenuItemAndSwitchToRightFrame(testSession, USERS_MENU_ITEM_XPATH);
		return usersframe;
	}

	/**
	 * Clicks by 'Sites' link and open frame with table of sites.
	 * 
	 * @param testSession
	 * @return
	 */
	public SystemFrame openSystemPage(TestSession testSession)
	{
		SystemFrame systemFrame = new SystemFrame(testSession);
		PageNavigator.clickMenuItemAndSwitchToRightFrame(testSession, SYSTEM_MENU_ITEM_XPATH);
		systemFrame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return systemFrame;

	}

	/**
	 * @param testSession
	 * @return
	 */
	public ContentTypesFrame openContentTypesFrame(TestSession testSession)
	{
		ContentTypesFrame frame = new ContentTypesFrame(testSession);
		PageNavigator.clickMenuItemAndSwitchToRightFrame(testSession, CONTENT_TYPES_MENU_ITEM_XPATH);
		frame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return frame;

	}

	/**
	 * Clicks by "Content" link in the LeftFrame and opens a table with all repositories.
	 * 
	 * @return
	 */
	public RepositoriesListFrame openRepositoriesTableFrame()
	{
		PageNavigator.clickMenuItemAndSwitchToRightFrame(getSession(), CONTENT_FOLDER_LOCATOR_XPATH);
		RepositoriesListFrame frame = new RepositoriesListFrame(getSession());
		frame.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return frame;

	}

	/**
	 * Expands the "Content" folder and delete repository with all content inside.
	 * 
	 * @param repoName
	 */
	public void doDeleteRepository(String repoName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand the 'Content' folder.
		expandContentFolder();
		// 2.expand repository and find last category, click by last category
		String repoXpath = String.format(CATEGORY_EXPANDER_IMG_XPATH, repoName);
		List<WebElement> plusIconXpath = findElements(By.xpath(repoXpath));
		// if "+" image-icon is absent, click by repository and delete it.
		if (plusIconXpath.size() == 0)
		{
			// click by Repository-name and press "Remove content repository" button
			ContentRepositoryViewFrame view = openRepositoryViewFrame(repoName);
			view.doDeleteEmptyRepository();
			RepositoriesListFrame allRepositoriesView = new RepositoriesListFrame(getSession());
			allRepositoriesView.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		} else
		{
			// if "+" image-icon is present, expand the Repository:
			TestUtils.getInstance().expandFolder(getSession(), repoXpath);
			// and delete all categories from the Repository:
			do
			{
				// switch to right frame:
				doDeleteLastCategoryWithContent(repoName);
				plusIconXpath = findElements(By.xpath(repoXpath));
			} while (plusIconXpath.size() > 0);

			PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
			ContentRepositoryViewFrame repoView = new ContentRepositoryViewFrame(getSession());
			repoView.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
			repoView.doDeleteEmptyRepository();
			RepositoriesListFrame allRepositoriesView = new RepositoriesListFrame(getSession());
			allRepositoriesView.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);

		}

	}

	/**
	 * Finds a Category, that has icon: "images/L.png", empty all content and deletes this Category.
	 * 
	 * @param repoName
	 */
	private void doDeleteLastCategoryWithContent(String repoName)
	{
		// 1. find the last Category:
		String linkXpath = String.format(LAST_CHILD_CATEGORY_LINK, repoName);
		List<WebElement> elems = findElements(By.xpath(linkXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("Error during deleting a category. Category was not found! repositoryName" + repoName);
		}
		// click by this category
		elems.get(0).click();
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		ContentsTableFrame tableView = new ContentsTableFrame(getSession());
		tableView.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		// delete all.
		do{
			tableView.doDeleteAllContent();
		
		}while(!tableView.isEmpty());
		tableView.doDeleteEmptyCategory();
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
	}

	/**
	 * Expands the "Content" folder, click by RepositoryName, opens repository
	 *  <br> view and click by 'New-Category' button
	 * 
	 * @param session
	 * @param repoName
	 * @return
	 */
	public ContentRepositoryViewFrame openRepositoryViewFrame(String repoName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand a 'Content' folder
		expandContentFolder();

		String xpathString = String.format("//tr[@id='id-categories']/child::td[2]//table[@class='menuItem']//tr[1]//span[text()='%s']", repoName);
		// 2. Try to Find Repository by Name and click:
		TestUtils.getInstance().clickByLocator(By.xpath(xpathString), getDriver());

		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		ContentRepositoryViewFrame view = new ContentRepositoryViewFrame(getSession());
		view.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		String repoNameXpath = String.format("//h1/a[text()='%s']", repoName);
		List<WebElement> elems = findElements(By.xpath(repoNameXpath));
		if (elems.size() == 0)
		{
			throw new TestFrameworkException("The name of repository should be present! Name:" + repoName);
		}
		return view;

	}

	public int getCategoryKey(String catName, String... parents)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand a 'Content' folder
		expandContentFolder();
		// 2 . expands parents:
		for (String p : parents)
		{
			String catXpath = String.format(CATEGORY_EXPANDER_IMG_XPATH, p);
			getLogger().info("try to expand a folder: " + p);
			// expandFolder(catXpath);
			TestUtils.getInstance().expandFolder(getSession(), catXpath);
		}
		String hrefElemntXpath = String.format(CATEGORY_MENU_ITEM, catName) + "//a[contains(@title, 'Key')]";
		String title = findElement(By.xpath(hrefElemntXpath)).getAttribute("title");
		String keyString = title.substring(title.indexOf(":") + 1, title.indexOf(")"));
		return Integer.valueOf(keyString);

	}

	/**
	 * Gets Key value for repository.
	 * 
	 * @param repositoryName
	 * @return key for Repository
	 */
	public int getRepositoryKey(String repositoryName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand a 'Content' folder
		expandContentFolder();
		String hrefElemntXpath = String.format(CATEGORY_MENU_ITEM, repositoryName) + "//a[contains(@title, 'Key')]";
		String title = findElement(By.xpath(hrefElemntXpath)).getAttribute("title");
		String keyString = title.substring(title.indexOf(":") + 1, title.indexOf(")"));
		return Integer.valueOf(keyString);
	}

	/**
	 * Expands 'Sites' folder in Admin Console and click by site-name and opens Info page.
	 * 
	 * @param siteName site for opening.
	 * @return info-page for site, which contains buttons 'Delete', 'Edit', 'Open in ICE' .....
	 */
	public SiteInfoPage openSiteInfoPage(String siteName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand 'Sites' folder
		expandSitesFolder();
		// 2.click by site
		String siteXpath = String.format(SITENAME_LINK_XPATH, siteName);
		boolean isSitePresent = TestUtils.getInstance().waitAndFind(By.xpath(siteXpath), getDriver());
		if (!isSitePresent)
		{
			throw new TestFrameworkException("Site with name: " + siteName + " was not found!");
		}
		findElement(By.xpath(siteXpath)).click();
		// 3. switch to 'Right Frame'
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SiteInfoPage info = new SiteInfoPage(getSession());
		info.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return info;
	}

	/**
	 * @param siteName
	 * @return
	 */
	public SiteMenuItemsTablePage openSiteMenuItems(String siteName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand 'Sites' folder
		expandSitesFolder();
		// 2. expand site folder
		String siteExpanderXpath = String.format(SITE_EXPANDER_IMG_XPATH, siteName);
		// expandFolder(siteExpanderXpath);
		TestUtils.getInstance().expandFolder(getSession(), siteExpanderXpath);
		// 3. click by 'Menu' link, located under the Site.
		String menuXpath = String.format(SITE_MENU_LINK_XPATH, siteName);
		boolean isPresentMenuLink = TestUtils.getInstance().waitAndFind(By.xpath(menuXpath), getDriver());
		if (!isPresentMenuLink)
		{
			throw new TestFrameworkException("Menu link for Site " + siteName + " was not found!, probably wrong xpath! ");
		}
		// 4.click by Menu:
		findElement(By.xpath(menuXpath)).click();
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SiteMenuItemsTablePage siteMenuItems = new SiteMenuItemsTablePage(getSession());
		return siteMenuItems;
	}

	/**
	 * @param siteName
	 * @return
	 */
	public SitePortletsTablePage openSitePortletsTable(String siteName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand 'Sites' folder
		expandSitesFolder();
		// 2. expand site folder
		String siteExpanderXpath = String.format(SITE_EXPANDER_IMG_XPATH, siteName);
		// expandFolder(siteExpanderXpath);
		TestUtils.getInstance().expandFolder(getSession(), siteExpanderXpath);
		// 3. click by 'Portlets' link, located under the Site.
		String portletsXpath = String.format(SITE_PORTLETS_LINK_XPATH, siteName);
		boolean isPresent = TestUtils.getInstance().waitAndFind(By.xpath(portletsXpath), getDriver());
		if (!isPresent)
		{
			throw new TestFrameworkException("Portlet link for Site " + siteName + " was not found!, probably wrong xpath! ");
		}
		// 4.click by Menu:
		findElement(By.xpath(portletsXpath)).click();
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SitePortletsTablePage sitePortletsPage = new SitePortletsTablePage(getSession());
		return sitePortletsPage;
	}

	public SiteTemplatesPage openSitePageTemplates(String siteName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand 'Sites' folder
		expandSitesFolder();
		// 2. expand site folder
		String siteExpanderXpath = String.format(SITE_EXPANDER_IMG_XPATH, siteName);
		TestUtils.getInstance().expandFolder(getSession(), siteExpanderXpath);
		// 3. click by 'Page templates' link, located under the Site.
		String menuXpath = String.format(SITE_PAGE_TEMPLATES_LINK_XPATH, siteName);
		boolean isPresentMenuLink = TestUtils.getInstance().waitAndFind(By.xpath(menuXpath), getDriver());
		if (!isPresentMenuLink)
		{
			throw new TestFrameworkException("'Page templates' link for Site " + siteName + " was not found!, probably wrong xpath! ");
		}
		// 4.click by Menu:
		findElement(By.xpath(menuXpath)).click();
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SiteTemplatesPage siteTemplates = new SiteTemplatesPage(getSession());
		return siteTemplates;
	}

	/**
	 * @param siteName
	 * @param sectionName
	 * @return
	 */
	public SectionContentsTablePage openSiteSection(String siteName, String sectionName)
	{
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. expand 'Sites' folder
		expandSitesFolder();
		// 2. expand site folder
		String siteExpanderXpath = String.format(SITE_EXPANDER_IMG_XPATH, siteName);
		TestUtils.getInstance().expandFolder(getSession(), siteExpanderXpath);
		// 3. expand Menu in the site:
		String siteMenuExpanderXpath = String.format(SITE_MENU_EXPANDER_IMG_XPATH, siteName);
		TestUtils.getInstance().expandFolder(getSession(), siteMenuExpanderXpath);

		String siteMenuItem = String.format(SITE_MENU_ITEM_XPATH, siteName, sectionName);
		boolean isMenuItemPresent = TestUtils.getInstance().waitAndFind(By.xpath(siteMenuItem), getDriver());
		if (!isMenuItemPresent)
		{
			throw new TestFrameworkException("Menu Item does not exist or wrong xpath");
		}
		findElement(By.xpath(siteMenuItem)).click();

		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		SectionContentsTablePage contentTablePage = new SectionContentsTablePage(getSession());
		contentTablePage.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		contentTablePage.switchToViewContent();
		return contentTablePage;
	}

	/**
	 * Expands the "Content" folder in the 'Left Menu', expands
	 * Repository-folder, select a Category and opens a 'Category-View'
	 * 
	 * @param session
	 * @param names
	 * @return
	 */
	public ContentsTableFrame openCategoryViewFrame(String... names)
	{
		if (names.length == 0)
		{
			throw new TestFrameworkException("repository name or category name shhould be specified! ");
		}
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.LEFT_FRAME_NAME);
		// 1. check out if Expanded or Rolled up the 'Content' folder: try to  find '+' icon near the 'Content' link in the left-menu:
		expandContentFolder();

		// 2. find required category(repository) for add content: find  Repository and expand it.
		for (int i = 0; i < names.length - 1; i++)
		{
			String catXpath = String.format(CATEGORY_EXPANDER_IMG_XPATH, names[i]);
			getLogger().info("try to expand a folder: " + names[i]);
			// expandFolder(catXpath);
			TestUtils.getInstance().expandFolder(getSession(), catXpath);
		}

		// 3. click by category and open category-view, where 'New/add' button should present.
		String categoryName = names[names.length - 1];
		String categoryXpath = String.format(CATEGORY_XPATH, categoryName);
		boolean isCategoryPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getDriver());
		if (!isCategoryPresent)
		{
			throw new TestFrameworkException("category with name" + categoryName);
		}

		// 4.click by categoryName:
		findElement(By.xpath(categoryXpath)).click();

		// 5. switch to the main frame
		PageNavigator.switchToFrame(getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME);
		ContentsTableFrame view = new ContentsTableFrame(getSession());
		view.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		return view;
	}

	/**
	 * @param categoryToSearch
	 * @param parentNames
	 * @return
	 */
	public WebElement findCategoryInContentFolder(String categoryToSearch, String... parentNames)
	{
		if (parentNames.length == 0)
		{
			throw new TestFrameworkException("repository name or category name shhould be specified! ");
		}
		// 1. check out if Expanded or Rolled up the 'Content' folder: try to  find '+' icon near the 'Content' link in the left-menu:
		expandContentFolder();

		// 2. find required category(repository) for add content: find Repository and expand it.
		for (int i = 0; i < parentNames.length; i++)
		{
			String categoryXpath = String.format(CATEGORY_EXPANDER_IMG_XPATH, parentNames[i]);
			TestUtils.getInstance().expandFolder(getSession(), categoryXpath);
		}

		// 3. click by category and open category-view, where 'New/add' button should present.
		String categoryXpath = String.format(CATEGORY_XPATH, categoryToSearch);
		boolean isCategoryPresent = TestUtils.getInstance().waitAndFind(By.xpath(categoryXpath), getDriver());
		if (!isCategoryPresent)
		{
			return null;
		} else
		{
			return findElement(By.xpath(categoryXpath));
		}

	}

	/**
	 * Clicks by 'expand' icon and expands the 'Content' folder in the 'Left Menu' frame
	 */
	private void expandContentFolder()
	{
		String expanderPlusXpath = String.format(CONTENTFOLDER_EXPANDER_XPATH, AppConstants.PLUS_ICON_PNG);
		boolean isRolledUp = TestUtils.getInstance().waitAndFind(By.xpath(expanderPlusXpath), getDriver());
		if (isRolledUp)
		{
			// 1. Expand the 'Content' folder:
			TestUtils.getInstance().clickByLocator(By.xpath(expanderPlusXpath), getDriver());
		}
	}

	/**
	 * Clicks by 'expand' icon and expands the 'Sites' folder in the 'Left Menu' frame
	 */
	private void expandSitesFolder()
	{
		String expanderPlusXpath = String.format(SITEFOLDER_EXPANDER_XPATH, AppConstants.PLUS_ICON_PNG);
		boolean isRolledUp = TestUtils.getInstance().waitAndFind(By.xpath(expanderPlusXpath), getDriver());
		if (isRolledUp)
		{
			// 1. Expand the 'Content' folder:
			TestUtils.getInstance().clickByLocator(By.xpath(expanderPlusXpath), getDriver());
		}
	}

}
