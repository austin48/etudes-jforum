/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/AttachmentExtensionImpl.java $ 
 * $Id: AttachmentExtensionImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011, 2012 Etudes, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 ***********************************************************************************/
package org.etudes.component.app.jforum;

import org.etudes.api.app.jforum.AttachmentExtension;

public class AttachmentExtensionImpl implements AttachmentExtension
{
	protected boolean allow;
	
	protected String comment;
	
	protected String extension;
	
	protected int extensionGroupId;
	
	protected int id;
	
	protected boolean physicalDownloadMode = true;
	
	protected boolean unknown;
	
	protected String uploadIcon;
	
	/**
	 * {@inheritDoc}
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getExtension()
	{
		return extension;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getExtensionGroupId()
	{
		return extensionGroupId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getUploadIcon()
	{
		return uploadIcon;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isAllow()
	{
		return allow;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isPhysicalDownloadMode()
	{
		return physicalDownloadMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUnknown()
	{
		return unknown;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAllow(boolean allow)
	{
		this.allow = allow;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setExtension(String extension)
	{
		this.extension = extension;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setExtensionGroupId(int extensionGroupId)
	{
		this.extensionGroupId = extensionGroupId;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPhysicalDownloadMode(boolean physicalDownloadMode)
	{
		this.physicalDownloadMode = physicalDownloadMode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUnknown(boolean unknown)
	{
		this.unknown = unknown;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUploadIcon(String uploadIcon)
	{
		this.uploadIcon = uploadIcon;
	}
}
