---------------------------------------------------------------------------------- 
-- $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/webapp/WEB-INF/config/database/conversion/JForum_mysql_forum_id_data_type_change.sql $ 
-- $Id: JForum_mysql_forum_id_data_type_change.sql 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
----------------------------------------------------------------------------------- 
-- 
-- Copyright (c) 2008 Etudes, Inc. 
-- 
-- Licensed under the Apache License, Version 2.0 (the "License"); 
-- you may not use this file except in compliance with the License. 
-- You may obtain a copy of the License at 
-- 
-- http://www.apache.org/licenses/LICENSE-2.0 
-- 
-- Unless required by applicable law or agreed to in writing, software 
-- distributed under the License is distributed on an "AS IS" BASIS, 
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
-- See the License for the specific language governing permissions and 
-- limitations under the License. 
-- 
-- Portions completed before July 1, 2004 Copyright (c) 2003, 2004 Rafael Steil, All rights reserved, licensed under the BSD license. 
-- http://www.opensource.org/licenses/bsd-license.php 
-- 
-- Redistribution and use in source and binary forms, 
-- with or without modification, are permitted provided 
-- that the following conditions are met: 
-- 
-- 1) Redistributions of source code must retain the above 
-- copyright notice, this list of conditions and the 
-- following disclaimer. 
-- 2) Redistributions in binary form must reproduce the 
-- above copyright notice, this list of conditions and 
-- the following disclaimer in the documentation and/or 
-- other materials provided with the distribution. 
-- 3) Neither the name of "Rafael Steil" nor 
-- the names of its contributors may be used to endorse 
-- or promote products derived from this software without 
-- specific prior written permission. 
-- 
-- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
-- HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
-- EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
-- BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
-- MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
-- PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
-- THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
-- FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
-- EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
-- (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
-- SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
-- OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
-- CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
-- IN CONTRACT, STRICT LIABILITY, OR TORT 
-- (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
-- ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
-- ADVISED OF THE POSSIBILITY OF SUCH DAMAGE 
----------------------------------------------------------------------------------
--------------------------------------------------------------------------
-- This is for MySQL forum_id data type change from smallint to mediumint 
-- for the exiting users below 2.4.1 release
--------------------------------------------------------------------------
--Note : Before running this script back up the jforum_forums, jforum_posts, jforum_topics,
--       jforum_search_topics, jforum_forum_sakai_groups tables

--jforum_forums table
ALTER TABLE jforum_forums MODIFY COLUMN forum_id MEDIUMINT(8) UNSIGNED NOT NULL AUTO_INCREMENT;

--jforum_posts table
ALTER TABLE jforum_posts MODIFY COLUMN forum_id MEDIUMINT(8) UNSIGNED NOT NULL DEFAULT 0;

--jforum_topics table
ALTER TABLE jforum_topics MODIFY COLUMN forum_id MEDIUMINT(8) UNSIGNED NOT NULL DEFAULT 0;

--jforum_search_topics
ALTER TABLE jforum_search_topics MODIFY COLUMN forum_id MEDIUMINT(8) UNSIGNED NOT NULL DEFAULT 0;

--jforum_forum_sakai_groups table
ALTER TABLE jforum_forum_sakai_groups MODIFY COLUMN forum_id MEDIUMINT(8) UNSIGNED NOT NULL DEFAULT 0;