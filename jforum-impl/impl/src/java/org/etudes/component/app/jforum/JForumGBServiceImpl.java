/**********************************************************************************
 * $URL: https://source.sakaiproject.org/contrib/etudes/sakai-jforum/tags/2.9.11/jforum-impl/impl/src/java/org/etudes/component/app/jforum/JForumGBServiceImpl.java $ 
 * $Id: JForumGBServiceImpl.java 83559 2013-04-30 19:03:29Z murthy@etudes.org $ 
 *********************************************************************************** 
 * 
 * Copyright (c) 2008, 2009 Etudes, Inc. 
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.sakaiproject.db.api.SqlReader;
import org.sakaiproject.db.cover.SqlService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.etudes.api.app.jforum.JForumGBService;
import org.sakaiproject.service.gradebook.shared.AssessmentNotFoundException;
import org.sakaiproject.service.gradebook.shared.AssignmentHasIllegalPointsException;
import org.sakaiproject.service.gradebook.shared.ConflictingAssignmentNameException;
import org.sakaiproject.service.gradebook.shared.ConflictingExternalIdException;
import org.sakaiproject.service.gradebook.shared.GradebookNotFoundException;
import org.sakaiproject.service.gradebook.shared.GradebookService;
import org.sakaiproject.service.gradebook.shared.GradebookExternalAssessmentService;

public class JForumGBServiceImpl implements JForumGBService
{
	private static Log logger = LogFactory.getLog(JForumGBServiceImpl.class);
	private Map<String,Object[]> gradableObjectsMap = new HashMap<String,Object[]>();
	
	protected GradebookExternalAssessmentService gradebookService = null;
	
	public void init(){
		if (logger.isInfoEnabled()) logger.info("init....");
	}
	
	public void destroy(){
		if (logger.isInfoEnabled()) logger.info("destroy....");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isGradebookDefined(String context)
	{
		boolean hasGradebook = gradebookService.isGradebookDefined(context);
		return Boolean.valueOf(hasGradebook);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isExternalAssignmentDefined(String gradebookUid, String externalId)
	{
		boolean hasExternalAssignment = false;
		
		try
		{
			hasExternalAssignment = gradebookService.isExternalAssignmentDefined(gradebookUid, externalId);
		}
		catch (GradebookNotFoundException e)
		{
			if (logger.isWarnEnabled()) logger.warn("isExternalAssignmentDefined:" + e.toString());
		}

		return Boolean.valueOf(hasExternalAssignment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean isAssignmentDefined(String gradebookUid, String assignmentTitle)
	{
		boolean assignmentDefined = false;
		
		try
		{
			assignmentDefined = gradebookService.isAssignmentDefined(gradebookUid, assignmentTitle);
		}
		catch (GradebookNotFoundException e)
		{
			if (logger.isWarnEnabled()) logger.warn("isAssignmentDefined:" + e.toString());
		}
		return Boolean.valueOf(assignmentDefined);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean addExternalAssessment(String gradebookUid, String externalId, String externalUrl, String title, double points, Date dueDate, 
			String externalServiceDescription)
	{
		boolean addSuccess = false;
		String gradableObjectKey = gradebookUid + "_" + externalId;
		
		try
		{
			gradebookService.addExternalAssessment(gradebookUid, externalId, externalUrl, title, points, dueDate, 
					externalServiceDescription);
			
			addSuccess = true;
		}
		catch (GradebookNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("addExternalAssessment : "+ e.toString());
		}
		catch (ConflictingAssignmentNameException e)
		{
			if(logger.isWarnEnabled()) logger.warn("addExternalAssessment : "+ e.toString());
		}
		catch (ConflictingExternalIdException e)
		{
			if(logger.isWarnEnabled()) logger.warn("addExternalAssessment : "+ e.toString());
		}
		catch (AssignmentHasIllegalPointsException e)
		{
			if(logger.isWarnEnabled()) logger.warn("addExternalAssessment : "+ e.toString());
		}
		
		// insert category back into the gb_gradable_object_t record
        try {
            String categoryId = (String)gradableObjectsMap.get(gradableObjectKey)[0];     // index 0 is hardcoded to the categoryId
            String isExtraCredit = (String)gradableObjectsMap.get(gradableObjectKey)[1];  // index 1 is hardcoded be is_extra_credit
            if (categoryId != null || isExtraCredit != null) {
                String sql = "update gb_gradable_object_t " 
                        + "set category_id=" + categoryId + ",is_extra_credit=" + isExtraCredit + " "
                        + "where external_id='" + externalId + "' "
                        + "and gradebook_id = (select id from gb_gradebook_t where gradebook_uid='" + gradebookUid + "')";
                
                if (!SqlService.dbWrite(sql)) {
                    logger.error("Unknown error writing category_id to gb_gradeable_object_t for gradebookUID: " + gradebookUid + ", externalId: " + externalId);
                }
            }
        } catch (Exception e) {
            logger.error("ERROR writing category_id to gb_gradeable_object_t for gradebookUID: " + gradebookUid + ", externalId: " + externalId + "Exception: " + e);
        }
        gradableObjectsMap.remove(gradableObjectKey);
        logger.info("gradableObjectsMap size after setting gb_gradable_object_t category_id: " + gradableObjectsMap.size());

		return Boolean.valueOf(addSuccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean removeExternalAssessment(String gradebookUid, String externalId)
	{
		boolean removeSuccess = false;
		String gradableObjectKey = gradebookUid + "_" + externalId;
		
		try
		{
            String sql = "select b.category_id, b.is_extra_credit "
                    + "from gb_gradebook_t as a left join gb_gradable_object_t as b on a.id = b.gradebook_id " 
                    + "where a.gradebook_uid=? and b.external_id=?";

            String fields[] = {gradebookUid, externalId};
            List<String[]> l = SqlService.dbRead(sql, fields, new SqlReader() {
                public Object readSqlResultRecord(ResultSet result) {
                    try {
                        String row[] = {result.getString(1), result.getString(2)};  // index 0 is the categoryId, index 1 is is_extra_credit
                        return row;
                    } catch ( SQLException e ) {
                        throw new RuntimeException("Failed to find gradeable obejct for gradebookUid: " +  gradebookUid + ",external_id: " + externalId);
                    }
                }
            });
            if (l != null && !l.isEmpty()) {
                if (l.size() == 1) {
                    String categoryId = null;
                    String isExtraCredit = null;
                    for (Object o[] : l) {
                        //categoryId = (String)o[0];
                        //isExtraCredit = (String)o[1];
                        //System.out.println("Gradable Object CATEGORY_ID: " + categoryId + ", IS_EXTRA_CREDIT: " + isExtraCredit);
                        synchronized(gradableObjectsMap) {
                            gradableObjectsMap.put(gradableObjectKey, o);
                            logger.info("gradableObjectsMap size after adding a category inside of synchronized block: " + gradableObjectsMap.size());
                        }

                    }
                }
            }

			gradebookService.removeExternalAssessment(gradebookUid, externalId);
			removeSuccess = true;
		}
		catch (GradebookNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("removeExternalAssessment : "+ e.toString());
		}
		catch (AssessmentNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("removeExternalAssessment : "+ e.toString());
		}
		
		if (!removeSuccess) {
			gradableObjectsMap.remove(gradableObjectKey);
		}
		
		return Boolean.valueOf(removeSuccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean updateExternalAssessment(String gradebookUid, String externalId, String externalUrl,
			String title, double points, Date dueDate)
	{
		boolean updateAssessment = false;
		try
		{
			gradebookService.updateExternalAssessment(gradebookUid, externalId, externalUrl, title, points, dueDate);
			updateAssessment = true;
		}
		catch (GradebookNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessment : "+ e.toString());
		}
		catch (AssessmentNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessment : "+ e.toString());
		}
		catch (ConflictingAssignmentNameException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessment : "+ e.toString());
		}
		catch (AssignmentHasIllegalPointsException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessment : "+ e.toString());
		}
		return Boolean.valueOf(updateAssessment);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean updateExternalAssessmentScores(String gradebookUid, String externalId, Map studentUidsToScores)
	{
		boolean updateScoresSuccess = false;
		try
		{
			gradebookService.updateExternalAssessmentScores(gradebookUid, externalId, studentUidsToScores);
			updateScoresSuccess = true;
		}
		catch (GradebookNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessmentScores : "+ e.toString());
		}
		catch (AssessmentNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessmentScores : "+ e.toString());
		}
		
		return Boolean.valueOf(updateScoresSuccess);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Boolean updateExternalAssessmentScore(String gradebookUid, String externalId, String studentUid, Double points)
	{
		boolean updateScoreSuccess = false;
		try
		{
			gradebookService.updateExternalAssessmentScore(gradebookUid, externalId, studentUid, ((points != null) ? (points.toString()):null));
			updateScoreSuccess = true;
		}
		catch (GradebookNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessmentScore : "+ e.toString());
		}
		catch (AssessmentNotFoundException e)
		{
			if(logger.isWarnEnabled()) logger.warn("updateExternalAssessmentScore : "+ e.toString());
		}
		
		return Boolean.valueOf(updateScoreSuccess);
		
	}
	
	/**
	 * @return the gradebookService
	 */
	public GradebookExternalAssessmentService getGradebookService()
	{
		return gradebookService;
	}

	/**
	 * @param gradebookService the gradebookService to set
	 */
	public void setGradebookService(GradebookExternalAssessmentService gradebookService)
	{
		this.gradebookService = gradebookService;
	}

}
