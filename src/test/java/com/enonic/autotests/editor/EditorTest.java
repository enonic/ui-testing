package com.enonic.autotests.editor;

import java.io.InputStream;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.ImageContentInfo;
import com.enonic.autotests.pages.adminconsole.content.AlignmentText;
import com.enonic.autotests.pages.adminconsole.content.SpecialCharacters;
import com.enonic.autotests.pages.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.TinyMCEService;
import com.enonic.autotests.testdata.contenttype.ContentConvertor;
import com.enonic.autotests.utils.TestUtils;

/**
 * Tests for content, that has TinyCME editor.
 */
public class EditorTest
    extends BaseTest
{
    private static final String IMAGE_TO_INSERT_KEY = "image_to insert";

	private static final String EDITOR_REPOSITORY_KEY = "editor_repository_key";

	private static final String EDITOR_CATEGORY_KEY = "editor_category_key";

    private static final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";

    private static final String IMAGE_CONTENTYPE_NAME = "Image";

	private TinyMCEService tinyMCEService = new TinyMCEService();

	@Test
	public void setup()
	{
		ContentType editorCtype = new ContentType();
		String contentTypeName = "Editor" + Math.abs(new Random().nextInt());
		editorCtype.setName(contentTypeName);
		editorCtype.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		editorCtype.setDescription("content type with html area tinyMCE");
		InputStream in = ContentConvertor.class.getClassLoader().getResourceAsStream(TINY_MCE_CFG);
		String editorCFG = TestUtils.getInstance().readConfiguration(in);
		editorCtype.setConfiguration(editorCFG);
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), editorCtype);
		boolean isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("error during creation of a content type!");
		}

		ContentRepository repository = new ContentRepository();
		repository.setName("editor" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		getTestSession().put(EDITOR_REPOSITORY_KEY, repository);

		ContentCategory category = new ContentCategory();
		category.setContentTypeName(contentTypeName);
		category.setName("editorcategory");
		String[] parentNames = { repository.getName() };
		category.setParentNames(parentNames);
		getTestSession().put(EDITOR_CATEGORY_KEY, category);
		repositoryService.addCategory(getTestSession(), category);
		 addImageContentType();
		 addImageCategory();

	}

	private void addImageContentType()
	{
		ContentType imagesType = new ContentType();
		imagesType.setName(IMAGE_CONTENTYPE_NAME);
		imagesType.setContentHandler(ContentHandler.IMAGES);
		imagesType.setDescription("content repository test");
		boolean isExist = contentTypeService.findContentType(getTestSession(), "Image");
		if (!isExist)
		{
			contentTypeService.createContentType(getTestSession(), imagesType);
			logger.info("New content type with 'Images' handler was created");
		}
	}

	private void addImageCategory()
	{
		ContentRepository repository = new ContentRepository();
		repository.setName("editorImageTest" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);

		ContentCategory imagecategory = new ContentCategory();
		imagecategory.setName("imageCategory");
		imagecategory.setContentTypeName(IMAGE_CONTENTYPE_NAME);
		String[] pathName = { repository.getName() };
		imagecategory.setParentNames(pathName);

		String pathToFile = "test-data/contentrepository/test.jpg";
		Content<ImageContentInfo> content = new Content<>();
		String[] pathToContent = new String[] { repository.getName(), imagecategory.getName() };
		content.setParentNames(pathToContent);
		ImageContentInfo contentTab = new ImageContentInfo();
		contentTab.setPathToFile(pathToFile);
		contentTab.setDescription("image for import test");
		content.setContentTab(contentTab);
		content.setDisplayName("image insert test");
		content.setContentHandler(ContentHandler.IMAGES);

		repositoryService.addCategory(getTestSession(), imagecategory);
		contentService.addImageContent(getTestSession(), content);
		getTestSession().put(IMAGE_TO_INSERT_KEY, content);
	}

	 @Test(description = "Format some text as bold and italic", dependsOnMethods = "setup")
	public void editingTextTest()
	{
		logger.info("### STARTED:");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyBoldItalic(getTestSession(), category);
		logger.info("$$$$ FINISHED: Format some text as bold and italic");
	}

    @Test(dataProvider = "parseAlignmentData",
          description = "Type text, mark them and click the different alignment buttons, center align, right align, full align and left align.",
          dependsOnMethods = "setup")
	public void textAlignmentTest(AlignmentText alignment)
	{
		logger.info("### STARTED: test text alignment:" + alignment.getValue());
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyTextAlignment(getTestSession(), category, alignment);
		logger.info("$$$$ FINISHED verify text with text alignment:" + alignment.getValue());
	}

	@DataProvider
	private Object[][] parseAlignmentData()
	{
		return new Object[][] {

		{ AlignmentText.LEFT }, { AlignmentText.RIGHT }, { AlignmentText.CENTER }, { AlignmentText.FULL } };
	}

    @Test(description = "Create an anchor some place in the text. Verify: an anchor symbol appears at that spot. ",
          dependsOnMethods = "setup")
	public void addAnchorTest()
	{
		logger.info("### STARTED: test add an anchor in the text");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyAddAnchorInText(getTestSession(), category);

		logger.info("$$$$ FINISHED verify text with text alignment:");
	}

	 @Test(description = "Insert Horizontal line ", dependsOnMethods = "setup")
	public void addHorizontalLineTest()
	{
		logger.info("### STARTED: Insert Horizontal line ");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyAddHorizontalLine(getTestSession(), category);
		logger.info("$$$$ FINISHED Insert Horizontal line ");
	}

	 @Test(description = "Creating a link in the text", dependsOnMethods = "setup")
	public void linkAndUnlinkTest()
	{
		logger.info("### STARTED: Creating a link in the text");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyLinkUnlink(getTestSession(), category);
		logger.info("$$$$ FINISHED verify text with text alignment:");

	}

	 @Test(description = "verify insert image", dependsOnMethods = "setup")
	public void addImageTest()
	{
		logger.info("### STARTED: verify insert image:");
		// 1. create category and upload image.

		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		// 2. add image to content:
		Content<ImageContentInfo> contentToInsert = (Content<ImageContentInfo>) getTestSession().get(IMAGE_TO_INSERT_KEY);
		tinyMCEService.verifyInsertImage(getTestSession(), category, contentToInsert);
		logger.info("$$$$ FINISHED verify insert image:");

	}

	 @Test(description = "verify insert Table ", dependsOnMethods = "setup")
	public void insertTableTest()
	{
		logger.info("### STARTED: verify insert Table :");

		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertTable(getTestSession(), category);
		logger.info("$$$$ FINISHED verify insert Table ");

	}

	@Test(description = "change text colour in Editor  ", dependsOnMethods = "setup")
	public void changeTextColorTest()
	{
		logger.info("### STARTED: change text colour in Editor ");

		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyChangeColorText(getTestSession(), category);
		logger.info("$$$$ FINISHED change text colour in Editor");

	}
	
	@Test(description = "change text Background colour in Editor  ", dependsOnMethods = "setup")
	public void changeTextBackgroundColorTest()
	{
		logger.info("### STARTED: change text Background colour in Editor  ");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyChangeBackgroundColorText(getTestSession(), category);
		logger.info("$$$$ FINISHED change text Background colour in Editor  ");

	}

	@Test(description = "Insert  special characters into the document.  ", dependsOnMethods = "setup")
	public void insertSpecialCharactersTest()
	{
		logger.info("### STARTED: Insert  special characters into the document.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertSpecialCharacters(getTestSession(), category, SpecialCharacters.EURO);
		logger.info("$$$$ FINISHED Insert special characters into the document.");
	}

	
	@Test(description = "Advanced styling :   Deletion ", dependsOnMethods = "setup")
	public void insertDeletionTest()
	{
		logger.info("### STARTED: Advanced styling : Deletion.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyDeletion(getTestSession(), category);
		logger.info("$$$$ FINISHED Advanced styling : Deletion.");
	}

	
	@Test(description = "Advanced styling :  Insertion ", dependsOnMethods = "setup")
	public void insertInsertionTest()
	{
		logger.info("### STARTED: Advanced styling : Insertion.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertion(getTestSession(), category);
		logger.info("$$$$ FINISHED Advanced styling : Insertion.");
	}

	
	@Test(description = "Advanced styling :  Block quote ", dependsOnMethods = "setup")
	public void insertBlockQuoteTest()
	{
		logger.info("### STARTED: Advanced styling : Block quote.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertBlockQuote(getTestSession(), category);
		logger.info("$$$$ FINISHED Advanced styling : Block quote.");
	}

	
	@Test(description = "Advanced styling :  Abbreviation ", dependsOnMethods = "setup")
	public void insertAbbreviationTest()
	{
		logger.info("### STARTED: Advanced styling : Abbreviation.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertAbbreviation(getTestSession(), category);
		logger.info("$$$$ FINISHED Advanced styling : Abbreviation.");
	}

	@Test(description = "Advanced styling :  Acronym ", dependsOnMethods = "setup")
	public void insertAcronymTest()
	{
		logger.info("### STARTED: Advanced styling : Acronym.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertAcronym(getTestSession(), category);
		logger.info("$$$$ FINISHED Advanced styling : Acronym.");
	}

	
	@Test(description = "Advanced styling :  Citation ", dependsOnMethods = "setup")
	public void insertCitationTest()
	{
		logger.info("### STARTED: Advanced styling : Citation.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertCitation(getTestSession(), category);
		logger.info("$$$$ FINISHED Advanced styling : Citation.");
	}

	
	@Test(description = "Advanced styling :  Superscript ", dependsOnMethods = "setup")
	public void insertSuperscriptTest()
	{
		logger.info("### STARTED: insert Superscript.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertSuperscript(getTestSession(), category);
		logger.info("$$$$ FINISHED insert  Superscript.");
	}

	@Test(description = "Advanced styling :  Subscript ", dependsOnMethods = "setup")
	public void insertSubscriptTest()
	{
		logger.info("### STARTED: insert Subscript.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertSubscript(getTestSession(), category);
		logger.info("$$$$ FINISHED insert : Subscript.");
	}

	
	@Test(description = "Insert bulleted list .", dependsOnMethods = "setup")
	public void insertBulletedListTest()
	{
		logger.info("### STARTED: Insert bulleted list .");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertBulletedList(getTestSession(), category);
		logger.info("$$$$ FINISHED  Insert bulleted list .");
	}

	
	@Test(description = "Insert numbered list .", dependsOnMethods = "setup")
	public void insertNumberedListTest()
	{
		logger.info("### STARTED: Insert numbered list .");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyInsertNumberedList(getTestSession(), category);
		logger.info("$$$$ FINISHED  Insert numbered list .");
	}
    
	
	@Test(description = "Increase and decrease Indents . Verify: text moved right, then left. ", dependsOnMethods = "setup")
    public void increaseDecreaseIndentTest()
	{
		logger.info("### STARTED: Increase and decrease Indents . Verify: text moved right, then left.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyIncreaseDecreaseIndents(getTestSession(), category);
		logger.info("$$$$ FINISHED  Increase and decrease Indents . Verify: text moved right, then left.");
	}

	@Test(description = "Undo / Redo . Test the undo button for  different steps", dependsOnMethods = "setup")
    public void undoRedoTest()
	{
		logger.info("### STARTED: Undo / Redo . Test the undo button for  different steps.");
		ContentCategory category = (ContentCategory) getTestSession().get(EDITOR_CATEGORY_KEY);
		tinyMCEService.verifyUndoRedo(getTestSession(), category);
		logger.info("$$$$ FINISHED  Undo / Redo . Test the undo button for  different steps.");
	}
	
	
}
