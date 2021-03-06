package com.enonic.autotests.general.acl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.enonic.autotests.BaseTest;
import com.enonic.autotests.model.Content;
import com.enonic.autotests.model.ContentCategory;
import com.enonic.autotests.model.ContentHandler;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.ContentWithEditorInfo;
import com.enonic.autotests.model.userstores.AclEntry;
import com.enonic.autotests.model.userstores.AclEntry.CategoryAvailableOperations;
import com.enonic.autotests.model.userstores.AclEntry.ContentAvailableOperations;
import com.enonic.autotests.model.userstores.AclEntry.PrincipalType;
import com.enonic.autotests.model.userstores.BuiltInGroups;
import com.enonic.autotests.model.userstores.PermissionOperation;
import com.enonic.autotests.model.userstores.User;
import com.enonic.autotests.pages.adminconsole.content.ContentsTableFrame;
import com.enonic.autotests.pages.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.AccountService;
import com.enonic.autotests.services.PageNavigator;
import com.enonic.autotests.utils.TestUtils;

public class ContributorACLCategoryBrowseTest extends BaseTest
{

	private AccountService accountService = new AccountService();

	private final String PASSWORD = "1q2w3e";
	private final String CONTRIBUTOR_USER_KEY = "contributor_key";
	private final String CONTRIBUTOR_CATEGORY_KEY = "contributor_cat_key";
	private final String CONTRIBUTOR_CONTENT_KEY = "contributor_content_key";
	private final String CONTENT_NAME = "contribcontent";
	
	private final String TINY_MCE_CFG = "test-data/contenttype/tiny-editor.xml";

	@Test(description = "add user")
	public void createUserTest()
	{
		logger.info("STARTED###: try to  add user.");
		User user = new User();
		user.setPassword(PASSWORD);
		String name = "contrib" + Math.abs(new Random().nextInt());
		user.setName(name);
		user.setEmail(name + "@mail.com");
		accountService.addUser(getTestSession(), user);
		getTestSession().put(CONTRIBUTOR_USER_KEY, user);
		logger.info("FINISHED ###:   user with name:"+user.getName() +" was created!");
	}
	

	@Test(description = "add user to Contrubutors group", dependsOnMethods = "createUserTest")
	public void addUserToContributorsTest()
	{
		logger.info("STARTED###:  add user to the Contrubutors group");
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		List<String> groups = new ArrayList<>();
		groups.add(BuiltInGroups.CONTRIBUTORS.getValue());
		contrubutor.setGroups(groups);

		accountService.editUser(getTestSession(), contrubutor.getName(), contrubutor);
		logger.info("FINISHED ###:   user with name:"+contrubutor.getName() +" was added  to the  Contrubutors group");
	}
	


	@Test(description = "add catrgory and content, admin browse not allowed", dependsOnMethods = "addUserToContributorsTest")
	public void addContentAndGrantAccessTest()
	{
		logger.info("STARTED###:  Setup category with content. Give user read access  but not admin-browse on category");
		// create content type with editor
		ContentType editorCtype = new ContentType();
		String contentTypeName = "Editor" + Math.abs(new Random().nextInt());
		editorCtype.setName(contentTypeName);
		editorCtype.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		editorCtype.setDescription("content type with html area tinyMCE");
		InputStream in = ContributorACLCategoryBrowseTest.class.getClassLoader().getResourceAsStream(TINY_MCE_CFG);
		String editorCFG = TestUtils.getInstance().readConfiguration(in);
		editorCtype.setConfiguration(editorCFG);
		ContentTypesFrame frame = contentTypeService.createContentType(getTestSession(), editorCtype);
		boolean isCreated = frame.verifyIsPresent(contentTypeName);
		if (!isCreated)
		{
			Assert.fail("error during creation of a content type!");
		}
        //2. create a repository
		ContentRepository repository = new ContentRepository();
		repository.setName("contrib" + Math.abs(new Random().nextInt()));
		repositoryService.createContentRepository(getTestSession(), repository);
		logger.info("repository was created. repository  name: " + repository.getName());
		
		
        //3. start to add category to the repository
		String[] parentNames = { repository.getName() };
		
		// 4. set ACL(Read permission, but not browse) for Category:
		List<AclEntry> catAclEntries = new ArrayList<>();
		AclEntry categoryAclEntry = new AclEntry();
		String principalName = ((User) getTestSession().get(CONTRIBUTOR_USER_KEY)).getName();
		categoryAclEntry.setPrincipalName(principalName);		
		categoryAclEntry.setType(PrincipalType.USER);
		logger.info("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz ");
		List<PermissionOperation> categoryPerm = new ArrayList<>();
		
		//READ ACCESS FOR USER , but not browse on category
		categoryPerm.add(PermissionOperation.with().name(CategoryAvailableOperations.READ.getUiValue()).allow(true).build());
		categoryAclEntry.setPermissions(categoryPerm);
		catAclEntries.add(categoryAclEntry);
		ContentCategory category = ContentCategory.with().contentTypeName(contentTypeName).name("cat").parentNames(parentNames).aclEntries(catAclEntries).build();
		
		
		//5. create category in the just added repository
		repositoryService.addCategory(getTestSession(), category);
		logger.info("category was added. category  name: " + category.getName());
		getTestSession().put(CONTRIBUTOR_CATEGORY_KEY, category);
		
       // 6. add  content to the category
		String[] pathToContent = new String[] { repository.getName(), category.getName() };

		Content<ContentWithEditorInfo> content = new Content<>();
		content.setDisplayName(CONTENT_NAME);
		content.setParentNames(pathToContent);
		ContentWithEditorInfo contentTab = new ContentWithEditorInfo();
		contentTab.setHtmlareaText("test text");
		content.setContentTab(contentTab);
		content.setContentHandler(ContentHandler.CUSTOM_CONTENT);
		
		List<AclEntry> contentAclEntries = new ArrayList<>();
		AclEntry contentAclEntry = new AclEntry();
		contentAclEntry.setPrincipalName(principalName);		
		contentAclEntry.setType(PrincipalType.USER);
		
		
		List<PermissionOperation> contentPermissions = new ArrayList<>();
		contentPermissions.add(PermissionOperation.with().name(ContentAvailableOperations.READ.getUiValue()).allow(true).build());
		contentPermissions.add(PermissionOperation.with().name(ContentAvailableOperations.UPDATE.getUiValue()).allow(true).build());
		contentAclEntry.setPermissions(contentPermissions);
		
		contentAclEntries.add(contentAclEntry);
		//content.setAclEntries(contentAclEntries);
		
		contentService.addContentWithEditor(getTestSession(), content);
		getTestSession().put(CONTRIBUTOR_CONTENT_KEY, content);
		logger.info("content was added to the categry with name: " + content.getDisplayName());
		
		logger.info("finished $$$: Setup category with content. Give user read access  but not admin-browse on category");

	} 

	
	@Test(description = "Verify that user with no admin-browse is not able to browse category", dependsOnMethods = "addContentAndGrantAccessTest")
	public void verifyIsPresentCategoryNoBrowseAccess()
	{
		logger.info("STARTED ###: Verify that user with no admin-browse is not able to browse category");
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(contrubutor);
		PageNavigator.navgateToAdminConsole(getTestSession());
		ContentCategory category = (ContentCategory) getTestSession().get(CONTRIBUTOR_CATEGORY_KEY);
		boolean result = repositoryService.isCategoryPresent(getTestSession(), category.getName(), category.getParentNames());

		Assert.assertFalse(result, "Error: user able to browse category ");
		logger.info("FINISHED $$$: Verify that user with no admin-browse is not able to browse category");

	}
	
	@Test(description = "change the category by adding 'admin-browse' for user", dependsOnMethods = "verifyIsPresentCategoryNoBrowseAccess")
	public void editCaregoryAddAdminBrowsePermissionTest()
	{
		logger.info("STARTED ### edit category and add 'Browse' permission for user");
		ContentCategory categoryToEdit = (ContentCategory) getTestSession().get(CONTRIBUTOR_CATEGORY_KEY);
		
		AclEntry entry = new AclEntry();
		String principalName = ((User) getTestSession().get(CONTRIBUTOR_USER_KEY)).getName();
		entry.setPrincipalName(principalName);
		
		entry.setType(PrincipalType.USER);
		List<PermissionOperation> categoryPerm = new ArrayList<>();
		categoryPerm.add(PermissionOperation.with().name(CategoryAvailableOperations.BROWSE.getUiValue()).allow(true).build());
		entry.setPermissions(categoryPerm);
		categoryToEdit.getAclEntries().add(entry);
		getTestSession().setUser(null);
		repositoryService.editCategory(getTestSession(), categoryToEdit);	
		logger.info("Setup category with content. Give user read access  but not admin-browse on category");
		logger.info("FINISHED $$$ edit category and add 'Browse' permission for user");

	}

	@Test(description = "Verify that user is allowed to browse category", dependsOnMethods = "editCaregoryAddAdminBrowsePermissionTest")
	public void verifyIsPresentCategoryBrowseAllowedForUser()
	{
		logger.info("Verify that user is allowed to browse category, when permission 'browse' added to category");
		User contrubutor = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(contrubutor);
		ContentCategory category = (ContentCategory) getTestSession().get(CONTRIBUTOR_CATEGORY_KEY);
		boolean result = repositoryService.isCategoryPresent(getTestSession(), category.getName(), category.getParentNames());

		ContentsTableFrame frame = repositoryService.findCategoryInContentAndOpen(getTestSession(), category);
		result = frame.isContentPresentInTable(CONTENT_NAME);
		TestUtils.getInstance().saveScreenshot(getTestSession());
		Assert.assertTrue(result, "content was not found!");
		logger.info("Finished $$$$ user is allowed to browse category, when permission 'browse' added to category");

	}
	
	@Test(description = "Verify that content is searchable, Verify that user is able to search for content in category", dependsOnMethods = "editCaregoryAddAdminBrowsePermissionTest")
	public void verifyContentIsSearchable()
	{
		logger.info("Verify that content is searchable, Verify that user is able to search for content in category ");
		User user = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(user);
		Content<ContentWithEditorInfo>  content = (Content<ContentWithEditorInfo>)getTestSession().get(CONTRIBUTOR_CONTENT_KEY);
		List<String> names = contentService.doSearchContentByName(getTestSession(),content.getDisplayName() );
		boolean result = names.isEmpty();

		
		Assert.assertFalse(result, String.format("content with name %s was not found!",content.getDisplayName()));
		logger.info("Finished $$$$ Verify: content is searchable, permission added: admin-browse ");

	}
	
	@Test(description="Contributors should not be able to edit the HTML-code.", dependsOnMethods ="verifyIsPresentCategoryBrowseAllowedForUser")
	public void verifyContributorTest()
	{
		logger.info("STARTED#### verify that 'Contributors' should be able to edit HTML fields");
		User expert = (User) getTestSession().get(CONTRIBUTOR_USER_KEY);
		getTestSession().setUser(expert);
		Content<ContentWithEditorInfo>  content = (Content<ContentWithEditorInfo>)getTestSession().get(CONTRIBUTOR_CONTENT_KEY);
		boolean result = accountService.isPresentEditHtmlButton(getTestSession(), content);
		Assert.assertFalse(result,"'edit html' button should not be present on the edit content page!");
		logger.info("Finished $$$$ verified that 'Contributors' should not be able to edit the HTML-code,");
	}

}
