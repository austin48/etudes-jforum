/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-tool/src/java/org/etudes/jforum/util/FormSelectedData.java $ 
 * $Id: FormSelectedData.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
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
package org.etudes.jforum.util;

/**
 * @author Rafael Steil
 */
public class FormSelectedData 
{
	/**
	 * Nome do campo ( descricao textual )
	 * */
	private String name;
	
	/**
	 * ID relacionada com o campo
	 * */
	private String id;
	
	/**
	 * Para verificar se o campo deve ser marcado como selecionado
	 * */
	private boolean selected;
	
	/**
	 * @param name Nome do campo
	 * @param id ID relacionado com o campo
	 * @param selected <code>true</code> ou <code>false</code>, com base no status desejado
	 * */
	public FormSelectedData(String name, String id, boolean selected)
	{
		this.name = name;
		this.id = id;
		this.selected = selected;
	}
	
	/**
	 * Pega o nome do campo.
	 * 
	 * @return String contendo o nome do campo
	 * */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Pega o ID do campo.
	 * 
	 * @return String contendo o ID do campo
	 * */
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * Pega o status do campo.
	 * 
	 * @param selected <code>true</code> ou <code>false</code>, com base no status
	 * */
	public boolean getSelected()
	{
		return this.selected;
	}
}
