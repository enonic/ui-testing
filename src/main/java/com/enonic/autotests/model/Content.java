package com.enonic.autotests.model;

import java.util.List;

import com.enonic.autotests.model.userstores.AclEntry;

/**
 * Base class for all types of content.
 * 
 * 12.04.2013
 */
public class Content<T>
{
	private ContentHandler contentHandler;

	private String[] parentNames;
	
	private String displayName;

	private IContentInfo<T> contentTab;

	private ContentProperties propertiesTab;

	private Publishing publishingTab;
	
	private List<AclEntry> aclEntries;
	
	
	public List<AclEntry> getAclEntries()
	{
		return aclEntries;
	}

	public void setAclEntries(List<AclEntry> aclEntries)
	{
		this.aclEntries = aclEntries;
	}

	public IContentInfo<T> getContentTab()
	{
		return contentTab;
	}

	public void setContentTab(IContentInfo<T> contentTab)
	{
		this.contentTab = contentTab;
	}

	public ContentProperties getPropertiesTab()
	{
		return propertiesTab;
	}

	public void setPropertiesTab(ContentProperties propertiesTab)
	{
		this.propertiesTab = propertiesTab;
	}

	public Publishing getPublishingTab()
	{
		return publishingTab;
	}

	public void setPublishingTab(Publishing publishingTab)
	{
		this.publishingTab = publishingTab;
	}
	
	public static Content<FileContentInfo> buildFileContent()
	{
		Content<FileContentInfo> fileContent = new Content<>();
		return fileContent;
	}
	
	public String[] getParentNames()
	{
		return parentNames;
	}

	public void setParentNames(String[] parentsNames)
	{
		this.parentNames = parentsNames;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public ContentHandler getContentHandler()
	{
		return contentHandler;
	}

	public void setContentHandler(ContentHandler contentHandler)
	{
		this.contentHandler = contentHandler;
	}

	public String buildContentNameWithPath()
	{
		StringBuilder sb = new StringBuilder();
		if(parentNames != null)
		{
			sb.append("/");
			for (String s : parentNames)
			{
				sb.append(s).append("/");
			}
		}
		

		sb.append(displayName);
		return sb.toString();
	}
	 public  Content<T> cloneContent()
	 {
		 Content<T> clon = new Content<>();
		 clon.setContentHandler(this.getContentHandler());
		 clon.setContentTab(this.getContentTab());
		 clon.setDisplayName(this.getDisplayName());
		 clon.setParentNames(this.getParentNames());
		 
		return clon;
	 }

}
