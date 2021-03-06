package com.enonic.autotests.pages.adminconsole.content;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.enonic.autotests.AppConstants;
import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.IContentInfo;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

/**
 * Page Object for 'Add image content Wizard'
 * 
 */
public class AddImageContentWizard extends AbstractAddContentWizard<ImageContentInfo> 
{

	@FindBy(how = How.ID, using = "photographername")
	protected WebElement photographername;

	@FindBy(how = How.ID, using = "photographeremail")
	protected WebElement photographeremail;

	/**
	 * Constructor
	 * 
	 * @param session
	 */
	public AddImageContentWizard( TestSession session )
	{
		super(session);
	}

	@Override
	public void typeDataAndSave(Content<ImageContentInfo> newcontent)
	{
		IContentInfo<ImageContentInfo> contentTab = newcontent.getContentTab();
		String comment = contentTab.getInfo().getComment();
		//1. fill a comments field
		if (comment != null && !commentInput.getAttribute("value").equals(comment))
		{
			commentInput.sendKeys(contentTab.getInfo().getComment());
		}
		String description = contentTab.getInfo().getDescription();
		//2. fill a description field
		if (description != null && !descriptionTextarea.getAttribute("value").equals(description))
		{
			descriptionTextarea.sendKeys(contentTab.getInfo().getDescription());
		}
        //3. fill a photographername field.
		String phName = contentTab.getInfo().getPhotographerName();
		if (phName != null && !photographername.getAttribute("value").equals(phName))
		{
			photographername.sendKeys(newcontent.getContentTab().getInfo().getPhotographerName());
		}
		//4. fill a photographeremail field.
		String phEmail = contentTab.getInfo().getPhotographerEmail();
		if (phEmail!=null && !photographeremail.getAttribute("value").equals(phEmail))
		{
			photographeremail.sendKeys(newcontent.getContentTab().getInfo().getPhotographerEmail());
		}

		//5. specify a path to the file: 		
		String pathTofile = newcontent.getContentTab().getInfo().getPathToFile();
		
		URL dirURL = ContentConvertor.class.getClassLoader().getResource(pathTofile);
		File file = null;
		try
		{
			file = new File(dirURL.toURI());
		} catch (URISyntaxException e)
		{
			getLogger().error("Error during importing a content: Wrong file URL ", getSession());

		}
		WebElement fileInputType = findElement(By.id("origimagefilename"));
		if (!getSession().getIsRemote())
		{

			fileInputType.sendKeys(file.getAbsolutePath());
		}else{
			// if remote webdriver used, therefore LocalFileDetector should be used!
			LocalFileDetector detector = new LocalFileDetector();
			
			File localFile = detector.getLocalFile(file.getAbsolutePath());
			((RemoteWebElement) fileInputType).setFileDetector(detector);

			fileInputType.sendKeys(localFile.getAbsolutePath());
		}
		
		
		//6.  fill the display name:
		if (newcontent.getDisplayName()!=null && !nameInput.getAttribute("value").equals(newcontent.getDisplayName()))
		{
			TestUtils.getInstance().clearAndType(getSession(), nameInput, newcontent.getDisplayName());

		}
		saveButton.click();
		waituntilPageLoaded(AppConstants.PAGELOAD_TIMEOUT);
		closeButton.click();
	}
}
