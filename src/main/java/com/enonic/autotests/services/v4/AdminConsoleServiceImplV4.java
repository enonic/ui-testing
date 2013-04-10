package com.enonic.autotests.services.v4;

import com.enonic.autotests.TestSession;
import com.enonic.autotests.model.ContentRepository;
import com.enonic.autotests.model.ContentType;
import com.enonic.autotests.model.User;
import com.enonic.autotests.pages.v4.HomePage;
import com.enonic.autotests.pages.v4.adminconsole.AbstractAdminConsolePage;
import com.enonic.autotests.pages.v4.adminconsole.LeftMenuFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoriesFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.ContentRepositoryFrame;
import com.enonic.autotests.pages.v4.adminconsole.content.CreateContentWizardPage;
import com.enonic.autotests.pages.v4.adminconsole.contenttype.ContentTypesFrame;
import com.enonic.autotests.services.IAdminConsoleService;


public class AdminConsoleServiceImplV4 implements IAdminConsoleService {

	@Override
	public void createContentType(TestSession testSession, ContentType ctype) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentTypesFrame frame = menu.openContentTypesFrame(testSession);
		frame.createContentType(ctype);

	}
	@Override
	public void createContentRepository(TestSession testSession, ContentRepository cRepository) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentRepositoriesFrame frame = menu.openContentFrame(testSession);
		frame.createContentRepository(cRepository);
		

	}
	
	@Override
	public void addContentToRepository(TestSession testSession,String repName) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		String xpath = String.format(ContentRepositoryFrame.REPOSITORY_LINK_XPATH, repName);
		//LeftMenuFrame menu = new LeftMenuFrame(testSession);
		ContentRepositoryFrame frame = new ContentRepositoryFrame(testSession);
		frame.open(xpath);
		CreateContentWizardPage wizardPage = frame.openAddContentWizardPage(repName);
		//wizardPage.do
		
		
		

	}


	@Override
	public void editContentType(TestSession testSession, String name) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//TODO add implementation.

	}

	@Override
	public void deleteContentType(TestSession testSession, String name) {
		PageNavigatorV4.navgateToAdminConsole(testSession);
		//TODO add implementation.

	}

//	public void openAdminConsole(TestSession testSession, String userName, String password) {
//		HomePage home = new HomePage(testSession);
//		home.open();
//		home.openAdminConsole(userName, password);
//	}
//
//	/**
//	 * @param testSession
//	 */
//	private void navgateToAdminConsole(TestSession testSession) {
//		User user = testSession.getCurrentUser();
//		// if Admin-console page already loaded, return, otherwise navigate to
//		// the console
//		if (testSession.getDriver().getTitle().contains(AbstractAdminConsolePage.TITLE)) {
//			return;
//		}
//		if (user != null) {
//			openAdminConsole(testSession, user.getName(), user.getPassword());
//		} else {
//			openAdminConsole(testSession, "admin", "password");
//		}
//	}
}
