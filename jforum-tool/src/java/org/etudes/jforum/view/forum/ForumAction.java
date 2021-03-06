/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/view/forum/ForumAction.java $ 
 * $Id: ForumAction.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009, 2010, 2011 Etudes, Inc. 
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
 * 
 * Portions completed before July 1, 2004 Copyright (c) 2003, 2004 Rafael Steil, All rights reserved, licensed under the BSD license. 
 * http://www.opensource.org/licenses/bsd-license.php 
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met: 
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following disclaimer. 
 * 2) Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. 
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE 
 ***********************************************************************************/
package org.etudes.jforum.view.forum;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.Category;
import org.etudes.api.app.jforum.JForumAccessException;
import org.etudes.api.app.jforum.JForumCategoryService;
import org.etudes.api.app.jforum.JForumForumService;
import org.etudes.api.app.jforum.JForumPostService;
import org.etudes.jforum.Command;
import org.etudes.jforum.JForum;
import org.etudes.jforum.SessionFacade;
import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.dao.SearchData;
import org.etudes.jforum.dao.TopicMarkTimeDAO;
import org.etudes.jforum.entities.Forum;
import org.etudes.jforum.entities.Topic;
import org.etudes.jforum.entities.UserSession;
import org.etudes.jforum.repository.ForumRepository;
import org.etudes.jforum.util.I18n;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SakaiSystemGlobals;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.etudes.jforum.util.preferences.TemplateKeys;
import org.etudes.jforum.util.user.JForumUserUtil;
import org.etudes.jforum.view.admin.ModerationAction;
import org.etudes.jforum.view.forum.common.ForumCommon;
import org.etudes.jforum.view.forum.common.TopicsCommon;
import org.etudes.jforum.view.forum.common.ViewCommon;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.user.cover.UserDirectoryService;
/**
 * @author Rafael Steil
 * 08/30/05 - Rashmi - modified list() to show online users of a course
 * 09/14/05 - Rashmi - modified list() to show initial of firstname.LastName instead 
 * of usernames under who is online.
 * 09/14/05 - Rashmi - revised logic to automate logout and invalidate sessions. now 
 * check is based on userids as lowercase.
 * 10/04/05 - Rashmi - revised list() to show first name initial only when first name is not blank
 * 10/04/05 - Rashmi - revised if condition
 * 1/8/06 - Mallika - adding nowDate variable
 * 01/31/06 - Murthy - commented code in list() - as users of the site are not needed to be displayed
 * 06/22/06 - Murthy - adding nowDate variable inthe list()
 * 11/15/06 - Mallika - adding functionality for markTopicsAsRead
 * 12/01/06 - Murthy - redirect to message page after marking topics as read
 */
public class ForumAction extends Command 
{
	private static Log logger = LogFactory.getLog(ForumAction.class);
	
	/*public void list() throws Exception
	{
		addGradeTypesToContext();
		ForumDAO fm = DataAccessDriver.getInstance().newForumDAO();
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		
		this.setTemplateName(TemplateKeys.FORUMS_LIST);
		if (logger.isDebugEnabled()) logger.debug("EXECUTING FORUMACTION!! "+this.request.getSession().getAttribute("courseid"));
		
		List<Category> categories = ForumCommon.getAllCategoriesAndForums(true);
		
		this.context.put("allCategories", categories);
		
		boolean foundGrade = false;

		//check for grade forums
		for (Category c : categories) {

			if (foundGrade)
				break;
			
			if (c.isGradeCategory()) {
				this.context.put("gradingExis", true);
				foundGrade = true;
				break;
			}

			if (c.getForums().size() == 0)
				continue;

			for (Iterator<Forum> tmpIterator = c.getForums().iterator(); tmpIterator.hasNext(); ) {
				Forum f = tmpIterator.next();
				
				if (f.getGradeType() > 0) {
					this.context.put("gradingExis", true);
					foundGrade = true;
					break;
				}
			}
		}
		
		this.context.put("topicsPerPage",  new Integer(SakaiSystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE)));
		
		this.context.put("rssEnabled", SystemGlobals.getBoolValue(ConfigKeys.RSS_ENABLED));
		
		this.context.put("totalMessages", I18n.getMessage("ForumListing.totalMessagesInfo", 
						new Object[] {new Integer( ForumRepository.getTotalMessages() )}));
		
		this.context.put("totalUsers", I18n.getMessage("ForumListing.registeredUsers", 
				new Object[] { ForumRepository.totalUsers() }));
		this.context.put("lastUser", ForumRepository.lastRegisteredUser());
		
		SimpleDateFormat df = new SimpleDateFormat(SakaiSystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		GregorianCalendar gc = new GregorianCalendar();
		this.context.put("now", df.format(gc.getTime()));
		
		06/22/06 Murthy
		GregorianCalendar gc1 = new GregorianCalendar();
		this.context.put("nowDate", gc1.getTime());
		
		this.context.put("lastVisit", df.format(SessionFacade.getUserSession().getLastVisit()));
		this.context.put("fir", new ForumRepository());
		
		// Online Users
		this.context.put("totalOnlineUsers", new Integer(SessionFacade.size()));
		int aid = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);
	
		List userSessions = SessionFacade.getAllSessions();
		List onlineUsersList = SessionFacade.getLoggedSessions();
		
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		
		this.context.put("facilitator", isfacilitator);
		
		// Check for an optional language parameter
		UserSession currentUser = SessionFacade.getUserSession();
				
		if (currentUser.getUserId() == aid) {
			String lang = this.request.getParameter("lang");
			
			if (lang != null && I18n.languageExists(lang)) {
				currentUser.setLang(lang);
			}
		}

		// If there are only guest users, then just register
		// a single one. In any other situation, we do not
		// show the "guest" username
		if (onlineUsersList.size() == 0) {
			UserSession us = new UserSession();
			
			us.setUserId(aid);
			us.setUsername(I18n.getMessage("Guest"));
			
			onlineUsersList.add(us);
		}
		
		this.context.put("mostUsersEverOnline", I18n.getMessage("ForumListing.mostUsersEverOnline",
				new String[] { "0", "" }));
	}*/
	
	public void list() throws Exception
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("Entering list()...");
		}

		addGradeTypesToContext();

		this.setTemplateName(TemplateKeys.FORUMS_LIST);

		String courseId = ToolManager.getCurrentPlacement().getContext();

		JForumCategoryService jforumCategoryService = (JForumCategoryService) ComponentManager.get("org.etudes.api.app.jforum.JForumCategoryService");
		List<org.etudes.api.app.jforum.Category> categories = jforumCategoryService.getUserContextCategories(courseId, UserDirectoryService.getCurrentUser().getId());

		this.context.put("allCategories", categories);

		boolean foundGrade = false;

		// check for grades
		for (org.etudes.api.app.jforum.Category category : categories)
		{

			if (foundGrade)
				break;

			if (category.isGradable())
			{
				this.context.put("gradingExis", true);
				foundGrade = true;
				break;
			}

			if (category.getForums().isEmpty())
			{
				continue;
			}

			for (org.etudes.api.app.jforum.Forum forum : category.getForums())
			{
				if (forum.getGradeType() > 0)
				{
					this.context.put("gradingExis", true);
					foundGrade = true;
					break;
				}
			}
		}
		
		// forum description
		for (org.etudes.api.app.jforum.Category category : categories)
		{
			for (org.etudes.api.app.jforum.Forum forum : category.getForums())
			{
				if (forum.getDescription() != null && forum.getDescription().trim().length() > 0)
				{
					//f.setDescription(f.getDescription().replaceAll("\\r\\n|\\r|\\n", "<br/>"));
					forum.setDescription(forum.getDescription().replaceAll("\\r\\n|\\r", "<br/>"));
				}
				
			}
		}

		this.context.put("topicsPerPage", new Integer(SakaiSystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE)));
		this.context.put("now", new Date());
		this.context.put("nowDate", new Date());
		this.context.put("lastVisit", SessionFacade.getUserSession().getLastVisit());

		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		this.context.put("facilitator", isfacilitator);
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Exiting list()...");
		}
	}
	
	public void moderation() throws Exception
	{
		this.context.put("openModeration", true);
		this.show();
	}

	/*public void show() throws Exception
	{
		addGradeTypesToContext();
		
		int forumId = this.request.getIntParameter("forum_id");
		
		if (logger.isDebugEnabled()) logger.debug("Showing forum id = " + forumId);
		
		// The user can access this forum?
		Forum forum = ForumRepository.getForum(forumId);
		if (logger.isDebugEnabled()) logger.debug("Showing forum " + forum);

		if ((forum == null) || (forum.getId() == 0))
		{
			if (logger.isErrorEnabled())
			{
				logger.error("forum with id " + forumId + " not found");
			}
			this.context.put("message", I18n.getMessage("Forum.notFound"));
			this.setTemplateName(TemplateKeys.FORUMS_ERROR);
			return;
		}
		
		Category category = ForumRepository.getCategory(forum.getCategoryId());
		this.context.put("category", category);
		
		// Don't show the category if start date is a later date
		Date currentTime = Calendar.getInstance().getTime();
		if (!(JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser()))
		{
			// ignore forums with category invalid dates
			if ((category.getStartDate() != null) && (category.getEndDate() != null))
			{
				if (category.getStartDate().after(category.getEndDate()))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			
			// ignore forums with invalid dates
			if ((forum.getStartDate() != null) && (forum.getEndDate() != null))
			{
				if (forum.getStartDate().after(forum.getEndDate()))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			
			// for gradable category verify with coursemap access advisor
			JForumService jforumService = (JForumService)ComponentManager.get("org.etudes.api.app.jforum.JForumService");
			if (category.isGradeCategory() && jforumService != null)
			{ 
				UserSession currentUser = SessionFacade.getUserSession();
				Boolean checkAccess = jforumService.checkItemAccess(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_CATEGORY +"-"+ String.valueOf(category.getId()), currentUser.getSakaiUserId());
				
				if (!checkAccess)
				{
					category.setBlocked(true);
					category.setBlockedByTitle(jforumService.getItemAccessMessage(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_CATEGORY +"-"+ String.valueOf(category.getId()), currentUser.getSakaiUserId()));
					category.setBlockedByDetails(jforumService.getItemAccessDetails(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_CATEGORY +"-"+ String.valueOf(category.getId()), currentUser.getSakaiUserId()));
					
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized") +"</br>"+ I18n.getMessage("Prerequisite.Alert") +" "+ category.getBlockedByTitle());
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			
			if (forum.getGradeType() == Forum.GRADE_BY_FORUM && jforumService != null)
			{ 
				UserSession currentUser = SessionFacade.getUserSession();
				Boolean checkAccess = jforumService.checkItemAccess(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_FORUM +"-"+ String.valueOf(forum.getId()), currentUser.getSakaiUserId());
				
				if (!checkAccess)
				{
					forum.setBlocked(true);
					forum.setBlockedByTitle(jforumService.getItemAccessMessage(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_FORUM +"-"+ String.valueOf(forum.getId()), currentUser.getSakaiUserId()));
					forum.setBlockedByDetails(jforumService.getItemAccessDetails(ToolManager.getCurrentPlacement().getContext(), ConfigKeys.CM_ID_FORUM +"-"+ String.valueOf(forum.getId()), currentUser.getSakaiUserId()));
					
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized") +"</br>"+ I18n.getMessage("Prerequisite.Alert") +" "+ forum.getBlockedByTitle());
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			if (!ForumRepository.isForumAccessibleToUser(forum))
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
			boolean specialAccessUser = false, specialAccessUserAccess = false;
			
			// check forum special access
			List<SpecialAccess> forumSpecialAccessList = forum.getSpecialAccessList();
			
			List<SpecialAccess> specialAccessList = null;
			
			if ((forumSpecialAccessList != null) && (forumSpecialAccessList.size() > 0))
			{
				specialAccessList = forumSpecialAccessList;
			}			
			
			if (specialAccessList != null)
			{
				for (SpecialAccess specialAccess : specialAccessList)
				{
					UserSession currentUser = SessionFacade.getUserSession();
					if (specialAccess.getUserIds().contains(currentUser.getUserId()))
					{
						specialAccessUser = true;
						
						if (!specialAccess.isOverrideStartDate())
						{
							specialAccess.setStartDate(forum.getStartDate());
						}
						if (!specialAccess.isOverrideEndDate())
						{
							specialAccess.setEndDate(forum.getEndDate());
						}
						if (!specialAccess.isOverrideLockEndDate())
						{
							specialAccess.setLockOnEndDate(forum.isLockForum());
						}
						
						if ((specialAccess.getStartDate() != null) && (specialAccess.getStartDate().after(currentTime)))
						{
							this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
							this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
							return;
						}
						
						if (((specialAccess.getEndDate() != null) && (specialAccess.getEndDate().before(currentTime))) && specialAccess.isLockOnEndDate())
						{
							specialAccessUserAccess = false;
						}
						else
						{
							specialAccessUserAccess = true;
						}
						break;
					}
				}
			}
			
			this.context.put("specialAccessUser", specialAccessUser);
			this.context.put("specialAccessUserAccess", specialAccessUserAccess);
			
			if (!specialAccessUser)
			{
				if ((category.getStartDate() != null) && (category.getStartDate().after(currentTime)))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
				
				if ((forum.getStartDate() != null) && (forum.getStartDate().after(currentTime)))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
		}
		
		int start = ViewCommon.getStartPage();
		
		List tmpTopics = TopicsCommon.topicsByForum(forumId, start);
		
		this.setTemplateName(TemplateKeys.FORUMS_SHOW);
		
		// Moderation
		if (logger.isDebugEnabled()) logger.debug("isLogged = " + SessionFacade.isLogged());
		
		// forum grade
		if (forum.getGradeType() == Forum.GRADE_BY_FORUM)
		{
			GradeDAO gm = DataAccessDriver.getInstance().newGradeDAO();
			forum.setGrade(gm.selectByForumId(forum.getId()));
		}

		Map topicsToApprove = new HashMap();
		
		this.context.put("topicsToApprove", topicsToApprove);
		this.context.put("attachmentsEnabled", true);
		this.context.put("topics", TopicsCommon.prepareTopics(tmpTopics));
		this.context.put("allCategories", ForumCommon.getAllCategoriesAndForums(false));
		this.context.put("forum", forum);
		this.context.put("rssEnabled", SystemGlobals.getBoolValue(ConfigKeys.RSS_ENABLED));
		this.context.put("pageTitle", SystemGlobals.getValue(ConfigKeys.FORUM_NAME) + " - " + forum.getName());
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		this.context.put("canApproveMessages", isfacilitator);

		if (category.isArchived()) {
			this.context.put("archivedCategory", true);
		} else {
			this.context.put("archivedCategory", false);
		}
		
		//readonly and replyonly are for participants 
		this.context.put("replyOnly", (isfacilitator ? false : (forum.getType() == Forum.TYPE_REPLY_ONLY)));
		this.context.put("readonly", (isfacilitator ? false : (forum.getType() == Forum.TYPE_READ_ONLY)));
		this.context.put("facilitator", isfacilitator);
		
		// Pagination
		int topicsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		int postsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);
		int totalTopics = ForumRepository.getTotalTopics(forumId);
		
		ViewCommon.contextToPagination(start, totalTopics, topicsPerPage);
		this.context.put("postsPerPage", new Integer(postsPerPage));
		GregorianCalendar gc = new GregorianCalendar();
		this.context.put("nowDate", gc.getTime());
		this.context.put("openModeration", true);
		TopicsCommon.topicListingBase();
	}*/
	
	public void show() throws Exception
	{
		addGradeTypesToContext();
		
		this.setTemplateName(TemplateKeys.FORUMS_SHOW);

		int forumId = this.request.getIntParameter("forum_id");

		if (logger.isDebugEnabled())
		{
			logger.debug("Entering show()...");
			logger.debug("Showing forum id = " + forumId);
		}
		
		int start = ViewCommon.getStartPage();
		// pagination
		int topicsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		int postsPerPage = SakaiSystemGlobals.getIntValue(ConfigKeys.POST_PER_PAGE);
		
		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		List<org.etudes.api.app.jforum.Topic> topics = null;
		try
		{
			topics = jforumPostService.getTopics(forumId, start, topicsPerPage, UserDirectoryService.getCurrentUser().getId());
		}
		catch (JForumAccessException e)
		{
			this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
			this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
			return;
		}
		
		org.etudes.api.app.jforum.Category category = null;
		org.etudes.api.app.jforum.Forum forum = null;
		if (topics.isEmpty())
		{
			try
			{
				JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
				forum = jforumForumService.getForum(forumId, UserDirectoryService.getCurrentUser().getId());
				
				if (forum == null)
				{
					if (logger.isErrorEnabled())
					{
						logger.error("forum with id " + forumId + " not found");
					}
					this.context.put("message", I18n.getMessage("Forum.notFound"));
					this.setTemplateName(TemplateKeys.FORUMS_ERROR);
					return;
				}
				
				JForumCategoryService jforumCategoryService = (JForumCategoryService) ComponentManager.get("org.etudes.api.app.jforum.JForumCategoryService");
				category = jforumCategoryService.getCategory(forum.getCategoryId(), UserDirectoryService.getCurrentUser().getId());
			}
			catch (JForumAccessException e)
			{
				this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
				this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
				return;
			}
		}
		else
		{
			forum = topics.get(0).getForum();
			category = forum.getCategory();
		}
				
		boolean isfacilitator = JForumUserUtil.isJForumFacilitator(UserDirectoryService.getCurrentUser().getId()) || SecurityService.isSuperUser();
		boolean participant = false;
		
		if (!isfacilitator)
		{
			participant = JForumUserUtil.isJForumParticipant(UserDirectoryService.getCurrentUser().getId());
		}
		
		// check forum special access
		if (participant)
		{
			Date currentTime = new Date();
			boolean specialAccessUser = false, specialAccessUserAccess = false;
			
			List<org.etudes.api.app.jforum.SpecialAccess> specialAccessList = forum.getSpecialAccess();
			
			if (!specialAccessList.isEmpty() && specialAccessList.size() == 1)
			{
				specialAccessUser = true;
				
				org.etudes.api.app.jforum.SpecialAccess specialAccess = specialAccessList.get(0);
				
				if ((specialAccess.getAccessDates().getOpenDate() != null) && (specialAccess.getAccessDates().getOpenDate().after(currentTime)))
				{
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized"));
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
				
				if (specialAccess.getAccessDates().getAllowUntilDate() != null)
				{
					if (specialAccess.getAccessDates().getAllowUntilDate().after(currentTime))
					{
						specialAccessUserAccess = true;
					}
				}
				else if (((specialAccess.getAccessDates().getDueDate() != null) && (specialAccess.getAccessDates().getDueDate().after(currentTime))))
				{
					specialAccessUserAccess = true;
				}
			}
			
			this.context.put("specialAccessUser", specialAccessUser);
			this.context.put("specialAccessUserAccess", specialAccessUserAccess);		
		
			if (category.isGradable())
			{ 						
				if (category.getBlocked())
				{				
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized") +"</br>"+ I18n.getMessage("Prerequisite.Alert") +" "+ category.getBlockedByTitle());
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
			else if (forum.getGradeType() == org.etudes.api.app.jforum.Grade.GradeType.FORUM.getType())
			{
				if (forum.getBlocked())
				{				
					this.context.put("errorMessage", I18n.getMessage("User.NotAuthorized") +"</br>"+ I18n.getMessage("Prerequisite.Alert") +" "+ forum.getBlockedByTitle());
					this.setTemplateName(TemplateKeys.USER_NOT_AUTHORIZED);
					return;
				}
			}
		}
		
		this.context.put("attachmentsEnabled", true);
		this.context.put("topics", topics);
		
		if (forum.getDescription() != null && forum.getDescription().trim().length() > 0)
		{
			//f.setDescription(f.getDescription().replaceAll("\\r\\n|\\r|\\n", "<br/>"));
			forum.setDescription(forum.getDescription().replaceAll("\\r\\n|\\r", "<br/>"));
		}

		this.context.put("forum", forum);
		this.context.put("category", category);

		this.context.put("pageTitle", SystemGlobals.getValue(ConfigKeys.FORUM_NAME) + " - " + forum.getName());

		this.context.put("canApproveMessages", isfacilitator);

		//readonly and replyonly are for participants 
		//this.context.put("replyOnly", (isfacilitator ? false : (forum.getType() == org.etudes.api.app.jforum.Forum.ForumType.REPLY_ONLY.getType())));
		//this.context.put("readonly", (isfacilitator ? false : (forum.getType() == org.etudes.api.app.jforum.Forum.ForumType.READ_ONLY.getType())));
		this.context.put("facilitator", isfacilitator);
		
		JForumForumService jforumForumService = (JForumForumService) ComponentManager.get("org.etudes.api.app.jforum.JForumForumService");
		int totalTopics = jforumForumService.getTotalTopics(forumId);		
		
		ViewCommon.contextToPagination(start, totalTopics, topicsPerPage);
		this.context.put("postsPerPage", new Integer(postsPerPage));
		//GregorianCalendar gc = new GregorianCalendar();
		Date nowDate = new Date();
		this.context.put("nowDate", nowDate);
		
		JForumCategoryService jforumCategoryService = (JForumCategoryService) ComponentManager.get("org.etudes.api.app.jforum.JForumCategoryService");
		List<org.etudes.api.app.jforum.Category> categories = jforumCategoryService.getUserContextCategories(category.getContext(), UserDirectoryService.getCurrentUser().getId());
		
		// filter user categories and forums in the drop down
		if (!isfacilitator)
		{
			org.etudes.api.app.jforum.Category userCategory = null;
			for (Iterator<org.etudes.api.app.jforum.Category> catIter = categories.iterator(); catIter.hasNext(); ) 
			{
				userCategory = catIter.next();
				
				if (userCategory.getAccessDates() != null && userCategory.getAccessDates().getOpenDate() != null)
				{
					if (userCategory.getAccessDates().getOpenDate().before(nowDate))
					{
						catIter.remove();
						continue;
					}
				}
				
				org.etudes.api.app.jforum.Forum userForum = null;
				for (Iterator<org.etudes.api.app.jforum.Forum> forumIter = userCategory.getForums().iterator(); forumIter.hasNext(); ) 
				{
					userForum = forumIter.next();
					List<org.etudes.api.app.jforum.SpecialAccess> specialAccessList = userForum.getSpecialAccess();
					
					if (!specialAccessList.isEmpty() && specialAccessList.size() == 1)
					{
						org.etudes.api.app.jforum.SpecialAccess specialAccess = specialAccessList.get(0);
						
						if ((specialAccess.getAccessDates().getOpenDate() != null) && (specialAccess.getAccessDates().getOpenDate().after(nowDate)))
						{
							forumIter.remove();
						}
					}
					else
					{
						if ((userForum.getAccessDates().getOpenDate() != null) && (userForum.getAccessDates().getOpenDate().after(nowDate)))
						{
							forumIter.remove();
							continue;
						}
					}
				}
			}
		}
		this.context.put("allCategories", categories);
		
		
		this.context.put("openModeration", true);
		TopicsCommon.topicListingBase();
		
		if (logger.isDebugEnabled())
		{
			logger.debug("Exiting show()...");
		}
	}
	
	public void doModeration() throws Exception
	{
		String actionMode = JForum.getRequest().getParameter("actionMode");
		//if (JForum.getRequest().getParameter("markTopics") != null)
		if ((actionMode != null) && (actionMode.trim().equalsIgnoreCase("markTopics")))
		{
			/*if (JForum.getRequest().getParameter("markTopics").equals("true"))
			{
				markTopicsAsUnread();
				JForum.setRedirect(this.makeRedirect("moderationDone"));
				return;
			}*/
			markTopicsAsUnread();
			JForum.setRedirect(this.makeRedirect("moderationDone"));
			return;
		}

		int status = new ModerationHelper().doModeration(this.makeRedirect("moderationDone"));

		if (status == ModerationHelper.GRADED)
		{
			this.show();
			return;
		}

		
		//if (JForum.getRequest().getParameter("topicMove") != null)
		if ((actionMode != null) && (actionMode.trim().equalsIgnoreCase("topicMove")))
		{
			this.setTemplateName(TemplateKeys.MODERATION_MOVE_TOPICS);
		}

	}
	
	public void moveTopic() throws Exception
	{
		new ModerationHelper().moveTopicsSave(this.makeRedirect("show"));
	}
	
	public void moderationDone() throws Exception
	{
		this.setTemplateName(new ModerationHelper().moderationDone(this.makeRedirect("show")));
	}
	
	// Make an URL to some action
	private String makeRedirect(String action)
	{
		String path = this.request.getContextPath() +"/forums/"+ action +"/";
		String thisPage = this.request.getParameter("start");
		
		if (thisPage != null && !thisPage.equals("0")) {
			path += thisPage +"/";
		}
		
		String forumId = this.request.getParameter("forum_id");
		if (forumId == null) {
			forumId = this.request.getParameter("persistData"); 
		}

		path += forumId + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);
		
		return path;
	}
	
	//Method added by NG team
	public void markTopicsRead() throws Exception
	{
		TopicMarkTimeDAO tm = DataAccessDriver.getInstance().newTopicMarkTimeDAO();
		String[] topics = JForum.getRequest().getParameterValues("topic_id");
		UserSession us = SessionFacade.getUserSession();

		Date markTime;
		int topicId;
		if (topics != null && topics.length > 0) {
			for (int i = 0; i < topics.length; i++) {
			topicId = Integer.parseInt(topics[i]);
			
			markTime = null;
		    try
		    {
		      markTime = tm.selectMarkTime(topicId, us.getUserId());
		    }
		    catch (Exception e)
		    {
			  logger.error(this.getClass().getName() +
					".markTopicsRead() : " + e.getMessage(), e);
		    }
		    if (markTime == null)
		    {
		      tm.addMarkTime(topicId, us.getUserId(), new Date(System.currentTimeMillis()), true);
		    }
		    else
		    {
		      tm.updateMarkTime(topicId, us.getUserId(), new Date(System.currentTimeMillis()), true);
		    }
			}
		}	
	}
	
	public void markTopicsAsUnread() throws Exception
	{
		//TopicMarkTimeDAO tm = DataAccessDriver.getInstance().newTopicMarkTimeDAO();
		String[] topics = JForum.getRequest().getParameterValues("topic_id");
		//UserSession us = SessionFacade.getUserSession();

		JForumPostService jforumPostService = (JForumPostService)ComponentManager.get("org.etudes.api.app.jforum.JForumPostService");
		
		//Date markTime;
		int topicId;
		if (topics != null && topics.length > 0)
		{
			for (int i = 0; i < topics.length; i++)
			{
				topicId = Integer.parseInt(topics[i]);
				
				jforumPostService.markTopicRead(topicId, UserDirectoryService.getCurrentUser().getId(), null, false);

				/*markTime = null;
				try
				{
					markTime = tm.selectMarkTime(topicId, us.getUserId());
				}
				catch (Exception e)
				{
					logger.error(this.getClass().getName() + ".markTopicsAsUnread() : " + e.getMessage(), e);
				}
				if (markTime == null)
				{
					tm.addMarkTime(topicId, us.getUserId(), new Date(System.currentTimeMillis()), false);
				}
				else
				{
					tm.markTopicUnread(topicId, us.getUserId(), new Date(System.currentTimeMillis()));
				}*/
			}
		}
	}
	
	// Mark all topics as read
	public void readAll() throws Exception
	{
		SearchData sd = new SearchData();
		sd.setTime(SessionFacade.getUserSession().getLastVisit());
		
		String forumId = this.request.getParameter("forum_id");
		if (forumId != null) {
			sd.setForumId(Integer.parseInt(forumId));
		}
		
		List allTopics = DataAccessDriver.getInstance().newSearchDAO().search(sd);
		for (Iterator iter = allTopics.iterator(); iter.hasNext(); ) {
			Topic t = (Topic)iter.next();
			
			((HashMap)SessionFacade.getAttribute(ConfigKeys.TOPICS_TRACKING)).put(new Integer(t.getId()), 
					new Long(t.getLastPostTimeInMillis().getTime()));
		}
		
		if (forumId != null) {
			JForum.setRedirect(this.makeRedirect("show"));
		}
		else {
			JForum.setRedirect(this.request.getContextPath() + "/forums/list"
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}
	}
	
	// Messages since last visit
	public void newMessages() throws Exception
	{
		this.request.addParameter("post_time", Long.toString(SessionFacade.getUserSession().getLastVisit().getTime()));
		this.request.addParameter("clean", "true");
		this.request.addParameter("sort_by", "t." + SystemGlobals.getValue(ConfigKeys.TOPIC_TIME_FIELD));
		this.request.addParameter("sort_dir", "DESC");
		
		new SearchAction(this.request, this.response, this.context).search();
		
		this.setTemplateName(TemplateKeys.SEARCH_NEW_MESSAGES);
	}
	
	public void approveMessages() throws Exception
	{
		if (SessionFacade.getUserSession().isModerator(this.request.getIntParameter("forum_id"))) {
			new ModerationAction(this.context, this.request).doSave();
		}
		
		JForum.setRedirect(this.request.getContextPath()
			+ "/forums/show/" + this.request.getParameter("forum_id")
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}
	
	public void pingSession()
	{
		this.setTemplateName(TemplateKeys.FORUMS_PING);
	}
	
	/**
	 * add forum grade type to context
	 */
	private void addGradeTypesToContext()
	{
		this.context.put("gradeDisabled", Forum.GRADE_DISABLED);
		this.context.put("gradeForum", Forum.GRADE_BY_FORUM);
		this.context.put("gradeTopic", Forum.GRADE_BY_TOPIC);
		this.context.put("gradeCategory", Forum.GRADE_BY_CATEGORY);		
	}
}
