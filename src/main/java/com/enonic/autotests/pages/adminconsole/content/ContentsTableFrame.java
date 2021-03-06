package com.enonic.autotests.pages.adminconsole.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.exceptions.AddContentException;
import com.enonic.autotests.exceptions.TestFrameworkException;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.site.SectionMenuItem;
import com.enonic.autotests.pages.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Category View'
 * 
 * this Frame appears when category selected from the left menu. Frame contains
 * a table with content.
 * 
 */
public class ContentsTableFrame extends AbstractContentTableView
{
	private final String MOVE_CONTENT_POPUP_WINDOW_TITLE = "Choose destination category";
	private static final String CHECKALL_XPATH = "//img[@id='batch_operation_image']";
	private String POPUP_WINDOW_DESTINATION_CATEGORY_XPATH = "//a[contains(@title,'%s')]";
	private final String DELETE_CATEGORY_BUTTON = "//a[@class='button_link']/button[text()='Delete']";

	private String POPUP_WINDOW_DESTINATION_REPO_EXPANDER_XPATH = "//tr[contains(@id,'tr-category') and descendant::td[contains(.,'%s')]]/td[1]/a";

	@FindBy(how = How.ID, using = "batch_operation_image")
	protected WebElement checkAllcheckbox;

	@FindBy(how = How.ID, using = "movecategorybtn")
	protected WebElement moveButton;
	
	@FindBy(xpath= "//button[text()='Import']")
	protected WebElement importButton;
	
	@FindBy(xpath= "//button[text()='Edit']")
	protected WebElement editButton;
	
	
	/** this button appears during add content to section */
	private final String ADD_CONTENT_TO_SECTION_XPATH = "//button[text()='Add']";
	@FindBy(how = How.XPATH, using = ADD_CONTENT_TO_SECTION_XPATH)
	protected WebElement addButton;

	/** this button appears during Inserting content to tinyMCE Editor */
	@FindBy(how = How.XPATH, using = "//button[text()='Choose']")
	protected WebElement chooseButton;

	
	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public ContentsTableFrame( TestSession session )
	{
		super(session);

	}
	
	public void doEditCategory(ContentCategory categoryToEdit)
	{
		editButton.click();
		CreateCategoryWizard wizard = new CreateCategoryWizard(getSession());
		wizard.doEditCategory(categoryToEdit, false);
		
		
	}
	public List<ContentStatus> getContentStatus(String contentName)
	{
	   List<ContentStatus> statuses = new ArrayList<>();
       String statusImageXpath  = String.format("//tr[contains(@class,'tablerowpainter')]//td[3][contains(@class,'browsetablecell')]/div[contains(@style,'font-weight: bold') and text()='%s']/../../td[6]//img",contentName);
       List<WebElement> elems = findElements(By.xpath(statusImageXpath));
       for(WebElement el: elems)
       {
    	   statuses.add(getStatus(el));
       }
      return statuses;
	}

	private ContentStatus getStatus(WebElement element)
	{
		String srcAttribute = element.getAttribute("src");
		if (srcAttribute.contains("unapprove.gif"))
		{
			return ContentStatus.UNAPPROVED;
		}
		if (srcAttribute.contains("approved.gif"))
       {
    	   return ContentStatus.APPROVED;
       }
		if (srcAttribute.contains("draft.gif"))
       {
    	   return ContentStatus.DRAFT;
       }
		if (srcAttribute.contains("archived.gif"))
       {
    	   return ContentStatus.ARCHIVED;
       }
		if (srcAttribute.contains("published.gif"))
       {
    	   return ContentStatus.PUBLISHED;
       }
		if (srcAttribute.contains("pending.gif"))
       {
    	   return ContentStatus.PENDING;
       }
		getLogger().error("STATUS IS :" + srcAttribute, getSession());
       
       throw new TestFrameworkException("unknown status");
	}
	/**
	 * Gets names of all contents from the table.
	 * 
	 * @return
	 */
	public List<String> getContentNames()
	{
		List<String> names = new ArrayList<>();
		String namesXpath = "//tr[contains(@class,'tablerowpainter')]//td[3][contains(@class,'browsetablecell')]/div[contains(@style,'font-weight: bold')]";
		List<WebElement> contentElements = findElements(By.xpath(namesXpath));
		for(WebElement elem:contentElements)
		{
			names.add(elem.getText());
		}
		return names;
	}
	/**
	 * Clicks by a 'Content Name' and opens a content-info page. 
	 * @param contentName
	 */
	public void clickByNameAndOpenInfo(String contentName)
	{ 
		String nameLinkXpath = String.format("//tr[contains(@class,'tablerowpainter')]//td[3][contains(@class,'browsetablecell')]/div[contains(@style,'font-weight: bold') and text()='%s']", contentName);
		WebElement contentNameElement = findElement(By.xpath(nameLinkXpath));
		contentNameElement.click();
		
	}
	/**
	 * Clicks by 'Import' button
	 */
	public void startImportContent()
	{
		if(importButton == null)
		{
			throw new TestFrameworkException("import button was not found!");
		}
		importButton.click();
	}
	/**
	 * Clicks by 'approve and publish' and publish content to section.
	 * 
	 * @param contentDisplayName
	 */
	public void doPublishToSection(String contentDisplayName, SectionMenuItem section)
	{
		String publishIcon = String.format(PUBLISH_CONTENT_LINK, contentDisplayName);
		// 1. verify 'approve and publish'-icon is present.
		if (!TestUtils.getInstance().waitAndFind(By.xpath(publishIcon), getDriver()))
		{
			throw new TestFrameworkException("publish link was not found or wrong xpath");
		}
		// 2. click by 'approve and publish' icon
		WebElement elemPublish = findElement(By.xpath(publishIcon));
		elemPublish.click();
		ContentPublishingWizardPage1 page1 = new ContentPublishingWizardPage1(getSession());
		page1.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		ContentPublishingWizardPage2 page2 = page1.doSelectSiteAndNext(section.getSiteName());
		page2.doSelectSectionAndNext(section.getDisplayName(),section.isOrdered());
		if(section.isOrdered())
		{
			ContentPublishingWizardPageOrderPosition orderPosition = new ContentPublishingWizardPageOrderPosition(getSession());
			orderPosition.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
			orderPosition.doNext();
		}
	
		ContentPublishingWizardPage3 page3 = new ContentPublishingWizardPage3(getSession());
		page3.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
	    page3.doFinish();
	    waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
			
		
	}
	/**
	 * @param contentDisplayName
	 * @param section
	 */
	public void doPublishToSectionAndMoveToEnd(String contentDisplayName, SectionMenuItem section)
	{
		if(!section.isOrdered())
		{
			throw new IllegalArgumentException();
		}
		String publishIcon = String.format(PUBLISH_CONTENT_LINK, contentDisplayName);
		// 1. verify 'approve and publish'-icon is present.
		if (!TestUtils.getInstance().waitAndFind(By.xpath(publishIcon), getDriver()))
		{
			throw new TestFrameworkException("publish link was not found or wrong xpath");
		}
		// 2. click by 'approve and publish' icon
		WebElement elemPublish = findElement(By.xpath(publishIcon));
		elemPublish.click();
		ContentPublishingWizardPage1 page1 = new ContentPublishingWizardPage1(getSession());
		page1.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		ContentPublishingWizardPage2 page2 = page1.doSelectSiteAndNext(section.getSiteName());
		page2.doSelectSectionAndNext(section.getDisplayName(),section.isOrdered());
		if(section.isOrdered())
		{
			ContentPublishingWizardPageOrderPosition orderPosition = new ContentPublishingWizardPageOrderPosition(getSession());
			orderPosition.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
			orderPosition.moveToEnd(contentDisplayName);
			orderPosition.doNext();
		}
	
		ContentPublishingWizardPage3 page3 = new ContentPublishingWizardPage3(getSession());
		page3.waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
	    page3.doFinish();
	    waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
			
		
	}
	
	/**
	 * @param contentName
	 */
	public void doAddContentToSection(String contentName)
	{
		//1. select checkbox for content
		String checkboxXpath = String.format(SELECT_CONTENT_CHECKBOX, contentName);
		boolean result = TestUtils.getInstance().waitAndFind(By.xpath(checkboxXpath), getDriver());
		if(!result)
		{
			throw new AddContentException("Content with name:"+ contentName + "was not found!");
		} 
		findElement(By.xpath(checkboxXpath)).click();
		//2. press the 'Add' button
		addButton.click();
	}

	public void doChooseContent(String contentName)
	{
		//1. select checkbox for content
		String checkboxXpath = String.format(SELECT_CONTENT_CHECKBOX, contentName);
		boolean result = TestUtils.getInstance().waitAndFind(By.xpath(checkboxXpath), getDriver());
		if(!result)
		{
			throw new AddContentException("Content with name:"+ contentName + "was not found!");
		} 
		findElement(By.xpath(checkboxXpath)).click();
		//2. press the 'choose' button
		chooseButton.click();
	}

	@Override
	public void waituntilPageLoaded(long timeout)
	{
		findElements(By.xpath(SELECT_TOP_XPATH));
		new WebDriverWait(getDriver(), timeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@class='browsetable']")));

	}

	/**
	 * Move content to the destination folder.
	 * @param contentName
	 * @param repoName
	 * @param destinationFolderName
	 */
	public void doMoveContent(String contentName, String repoName, String destinationFolderName)
	{
		String moveLinkXpath = String.format(MOVE_CONTENT_LINK, contentName);
		if (!TestUtils.getInstance().waitAndFind(By.xpath(moveLinkXpath), getSession().getDriver()))
		{
			throw new TestFrameworkException("move link was not found or wrong xpath");
		}
		getSession().getDriver().findElement(By.xpath(moveLinkXpath)).click();
		// wait
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e1)
		{
			
		}
		Set<String> allWindows = getSession().getDriver().getWindowHandles();

		if (!allWindows.isEmpty())
		{
			String whandle = getSession().getDriver().getWindowHandle();
			for (String windowId : allWindows)
			{

				try
				{
					if (getSession().getDriver().switchTo().window(windowId).getTitle().equals(MOVE_CONTENT_POPUP_WINDOW_TITLE))
					{
						clickByDestinationFolder(repoName, destinationFolderName);
						// popup window closed, need to switch to the main  window
						getSession().getDriver().switchTo().window(whandle);
						PageNavigator.switchToFrame( getSession(), AbstractAdminConsolePage.MAIN_FRAME_NAME );
						break;
					}
				} catch (NoSuchWindowException e)
				{
					throw new TestFrameworkException("NoSuchWindowException- wrong ID" + e.getLocalizedMessage());
				}
			}
		}

	}

	private void clickByDestinationFolder(String repoName, String destinationFolderName)
	{
		System.out.println("START clickByDestinationFolder");
		// 1. expand the Repository Folder.
		String repoExpanderPath = String.format(POPUP_WINDOW_DESTINATION_REPO_EXPANDER_XPATH, repoName);
		if (!TestUtils.getInstance().waitAndFind(By.xpath(repoExpanderPath), getSession().getDriver()))
		{
			throw new TestFrameworkException("destination Repository does not exists or wrong xpath");
		}
		getSession().getDriver().findElement(By.xpath(repoExpanderPath)).click();

		// 2. click by destination: Category Folder.
		String destinationFolderXpath = String.format(POPUP_WINDOW_DESTINATION_CATEGORY_XPATH, destinationFolderName);
		getLogger().info("content will be moved to category: " + destinationFolderName);
		if (!TestUtils.getInstance().waitAndFind(By.xpath(destinationFolderXpath), getSession().getDriver()))
		{
			throw new TestFrameworkException("destination folder does not exists or wrong xpath");
		}

		try
		{
			getSession().getDriver().findElement(By.xpath(destinationFolderXpath)).click();
		} catch (Exception e)
		{
			System.out.println("move content popup window was closed");
		}

		System.out.println("#####  clickByDestinationFolder finished");

	}

	/**
	 * Deletes content from the table of contents. Clicks by 'Delete' link and
	 * confirm deletion.
	 * 
	 * @param contentDisplayName
	 */
	public void doDeleteContent(String contentDisplayName)
	{
		String deleteContenIcon = String.format(DELETE_CONTENT_LINK, contentDisplayName);
		// 1. verify delete-icon is present.
		if (!TestUtils.getInstance().waitAndFind(By.xpath(deleteContenIcon), getSession().getDriver()))
		{
			throw new TestFrameworkException("delete link was not found or wrong xpath");
		}
		// 2. click by 'Delete' icon
		WebElement elem = getSession().getDriver().findElement(By.xpath(deleteContenIcon));
		elem.click();
		// 3.confirm the deletion
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 2l);
		if (isAlertPresent)
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deleting of the content , alert message:" + textOnAlert);
			// Click OK button, by calling accept() method of Alert Class:
			alert.accept();
		}
		waituntilPageLoaded(2l);
	}

	/**
	 * Clicks by 'select all' checkbox and chooses "Delete" action from the
	 * drop-down list.
	 */
	public void doDeleteAllContent()
	{
		checkAllcheckbox.click();
		boolean isClickable = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(SELECT_TOP_XPATH), AppConstants.IMPLICITLY_WAIT);
		if (!isClickable)
		{
			getLogger().info("The category is empty");
			return;
		}
		TestUtils.getInstance().selectByText(getSession(), By.xpath(SELECT_TOP_XPATH), "Delete");
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 2l);
		if (isAlertPresent)
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deletion of contents, alert message:" + textOnAlert);
			// Click OK button, by calling accept() method of Alert Class:
			alert.accept();
		}

	}

	/**
	 * Click by 'Delete' button on the  toolbar, confirm and delete an empty category.
	 * <br>Button 'Delete' is not visible if category is not empty.
	 */
	public void doDeleteEmptyCategory()
	{
		boolean isDeleteButtonPresent = TestUtils.getInstance().waitUntilClickableNoException(getSession(), By.xpath(DELETE_CATEGORY_BUTTON), 2l);
		if (!isDeleteButtonPresent)
		{
			throw new TestFrameworkException("Delete category button is not clickable! Wrong xpath or category is not empty!");
		}
		getSession().getDriver().findElement(By.xpath(DELETE_CATEGORY_BUTTON)).click();
		boolean isAlertPresent = TestUtils.getInstance().alertIsPresent(getSession(), 2l);
		if (isAlertPresent)
		{
			Alert alert = getSession().getDriver().switchTo().alert();
			// Get the Text displayed on Alert:
			String textOnAlert = alert.getText();
			getLogger().info("Deletion of category, alert message:" + textOnAlert);
			alert.accept();
		}

	}

}
