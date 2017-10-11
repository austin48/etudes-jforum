/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/view/admin/SmiliesAction.java $ 
 * $Id: SmiliesAction.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008 Etudes, Inc. 
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
package org.etudes.jforum.view.admin;

import java.io.File;

import org.etudes.jforum.dao.DataAccessDriver;
import org.etudes.jforum.entities.Smilie;
import org.etudes.jforum.repository.SmiliesRepository;
import org.etudes.jforum.util.MD5;
import org.etudes.jforum.util.legacy.commons.fileupload.FileItem;
import org.etudes.jforum.util.preferences.ConfigKeys;
import org.etudes.jforum.util.preferences.SystemGlobals;
import org.etudes.jforum.util.preferences.TemplateKeys;
import org.etudes.jforum.view.forum.common.UploadUtils;


/**
 * @author Rafael Steil
 */
public class SmiliesAction extends AdminCommand 
{
	private String processUpload() throws Exception
	{
		String imgName = "";
		
		if (this.request.getObjectParameter("smilie_img") != null) {
			FileItem item = (FileItem)this.request.getObjectParameter("smilie_img");
			UploadUtils uploadUtils = new UploadUtils(item);

			imgName = MD5.crypt(item.getName());
			
			uploadUtils.saveUploadedFile(SystemGlobals.getApplicationPath() 
					+ "/"
					+ SystemGlobals.getValue(ConfigKeys.SMILIE_IMAGE_DIR) 
					+ "/"
					+ imgName + "." + uploadUtils.getExtension());
			
			imgName += "." + uploadUtils.getExtension();
		}
		
		return imgName;
	}
	
	public void insert()
	{
		this.setTemplateName(TemplateKeys.SMILIES_INSERT);
		this.context.put("action", "insertSave");
	}
		
	public void insertSave() throws Exception
	{
		Smilie s = new Smilie();
		s.setCode(this.request.getParameter("code"));
		
		String imgName = this.processUpload();
		s.setUrl(SystemGlobals.getValue(ConfigKeys.SMILIE_IMAGE_PATTERN).replaceAll("#IMAGE#", imgName));
		
		s.setDiskName(imgName);
		
		DataAccessDriver.getInstance().newSmilieDAO().addNew(s);
		
		SmiliesRepository.loadSmilies();
		this.list();
	}
	
	public void edit() throws Exception
	{
		int id = 1;
		
		if (this.request.getParameter("id") != null) {
			id = this.request.getIntParameter("id");
		}
		
		this.setTemplateName(TemplateKeys.SMILIES_EDIT);
		this.context.put("smilie", DataAccessDriver.getInstance().newSmilieDAO().selectById(id));
		this.context.put("action", "editSave");
	}
	
	public void editSave() throws Exception
	{
		Smilie s = DataAccessDriver.getInstance().newSmilieDAO().selectById(this.request.getIntParameter("id"));
		s.setCode(this.request.getParameter("code"));
		
		if (this.request.getObjectParameter("smilie_img") != null) {
			String imgName = this.processUpload();
			s.setUrl(SystemGlobals.getValue(ConfigKeys.SMILIE_IMAGE_PATTERN).replaceAll("#IMAGE#", imgName));
			s.setDiskName(imgName);
		}

		DataAccessDriver.getInstance().newSmilieDAO().update(s);
		
		SmiliesRepository.loadSmilies();
		this.list();
	}
	
	public void delete() throws Exception
	{
		if (this.request.getParameter("id") != null) {
			int id = this.request.getIntParameter("id");
			Smilie s = DataAccessDriver.getInstance().newSmilieDAO().selectById(id);
			
			DataAccessDriver.getInstance().newSmilieDAO().delete(id);
			
			File f = new File(SystemGlobals.getApplicationPath() +"/"+ SystemGlobals.getValue(ConfigKeys.SMILIE_IMAGE_DIR) +"/"+ s.getDiskName());
			if (f.exists()) {
				f.delete();
			}
		}
		
		SmiliesRepository.loadSmilies();
		this.list();
	}

	/** 
	 * @see org.etudes.jforum.Command#list()
	 */
	public void list() throws Exception 
	{
		this.context.put("smilies", SmiliesRepository.getSmilies());
		this.setTemplateName(TemplateKeys.SMILIES_LIST);
	}
}
