/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-api/src/java/org/etudes/api/app/jforum/dao/PrivateMessageDao.java $ 
 * $Id: PrivateMessageDao.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.api.app.jforum.dao;

import java.util.List;

import org.etudes.api.app.jforum.PrivateMessage;


public interface PrivateMessageDao
{
	/**
	 * Deletes the private message
	 * 
	 * @param privateMessages	Deletes the private messages
	 */
	public void delete(PrivateMessage privateMessage);
	
	/**
	 * Create new private message
	 * 
	 * @param pm	Private message
	 * 
	 * @return newly created private message id's of "from user sent box" and "to user inbox"
	 */
	
	public List<Integer> saveMessage(PrivateMessage pm);
	
	
	/**
	 * Gets the private message
	 * 
	 * @param privateMessageId	Private message id
	 * 
	 * @return	Gets the private message or null if there is no private message with given id
	 */
	public PrivateMessage selectById(int privateMessageId);
	
	/**
	 * Updates follow up flag
	 * 
	 * @param messageId		Message id
	 * 
	 * @param flag	true - to add follow up flag
	 * 				flase - to clear follow up flag
	 */
	public void updateFlagToFollowup(int messageId, boolean flag);
	
	/**
	 * Updates message type
	 * 
	 * @param messageId		Message id
	 * 
	 * @param messageType	Message type
	 */
	public void updateMessageType(int messageId, int messageType);
	
	/**
	 * Updates replied status
	 * 
	 * @param pm	Private message
	 */
	public void updateRepliedStatus(PrivateMessage pm);
	
	public int selectUnreadCount(String context, int userId);
}