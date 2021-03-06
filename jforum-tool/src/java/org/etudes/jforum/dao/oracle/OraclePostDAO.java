/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/dao/oracle/OraclePostDAO.java $ 
 * $Id: OraclePostDAO.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.jforum.dao.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.etudes.jforum.JForum;
import org.etudes.jforum.entities.Post;
import org.etudes.jforum.util.preferences.SystemGlobals;


/**
 * @author Dmitriy Kiriy
 */
public class OraclePostDAO extends org.etudes.jforum.dao.generic.GenericPostDAO
{
	/**
	 * @see org.etudes.jforum.dao.generic.GenericPostDAO#addNewPostText(org.etudes.jforum.entities.Post)
	 */
	protected void addNewPostText(Post post) throws Exception
	{
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("PostModel.addNewPostText"));
		p.setInt(1, post.getId());
		p.setString(2, post.getSubject());
		p.executeUpdate();
		p.close();
		
		OracleUtils.writeBlobUTF16BinaryStream(
				SystemGlobals.getSql("PostModel.addNewPostTextField"),
				post.getId(), post.getText()
			);
	}
	
	/**
	 * @see org.etudes.jforum.dao.generic.GenericPostDAO#updatePostsTextTable(org.etudes.jforum.entities.Post)
	 */
	protected void updatePostsTextTable(Post post) throws Exception
	{
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("PostModel.updatePostText"));
		p.setString(1, post.getSubject());
		p.setInt(2, post.getId());
		
		p.executeUpdate();
		p.close();
		
		OracleUtils.writeBlobUTF16BinaryStream(
			SystemGlobals.getSql("PostModel.addNewPostTextField"),
			post.getId(), post.getText()
		);
	}
	
	/**
	 * @see org.etudes.jforum.dao.generic.GenericPostDAO#getPostTextFromResultSet(java.sql.ResultSet)
	 */
	protected String getPostTextFromResultSet(ResultSet rs) throws Exception
	{
		return OracleUtils.readBlobUTF16BinaryStream(rs, "post_text");
	}

    /**
	 * @see org.etudes.jforum.dao.PostDAO#selectAllByTopicByLimit(int, int, int)
	 */
	public List selectAllByTopicByLimit(int topicId, int startFrom, int count) throws Exception
	{
		List l = new ArrayList();
		
		PreparedStatement p = JForum.getConnection().prepareStatement(SystemGlobals.getSql("PostModel.selectAllByTopicByLimit"));
		p.setInt(1, topicId);
		p.setInt(2, startFrom);
		p.setInt(3, startFrom + count);
		
		ResultSet rs = p.executeQuery();
						
		while (rs.next()) {
			l.add(super.makePost(rs));
		}
		
		rs.close();
		p.close();
				
		return l;
	}
}
