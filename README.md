# etudes-jforum

**Get the Source**

- Get the JForum 2.9.12-SNAPSHOT source and place it in the sakai source folder. 
- https://github.com/sakaicontrib/etudes-jforum.git

**Database Information**

- Etudes Jforum 2.9.12-SNAPSHOT works with MySQL 4.1+,  MySQL 5.0+ and Oracle databases(not yet supported).

**Dependency on etudes-util**

- Build etudes-util before building Etudes Jforum.
- etudes-util is included in etudes-dependencies
- Get etudes-dependencies from https://github.com/sakaicontrib/etudes-dependencies.git
- Place etudes-dependencies in the sakai source folder


**Sakai.properties Configurations**

- Add the jforum configurations to sakai.properties and customize.
- Refer to /etudes-jforum/readme/jforum_config_properties.txt for the properties or the Jforum properties.

**Build and Deploy Etudes JForum**

- add the etudes-dependencies module and etudes-jforum module to the sakai main pom.xml

<module>etudes-dependencies</module>
<module>etudes-jforum</module>

**Update Sakai Roles**

- Update Sakai Roles (under realms) to include JForum permissions

- Check appropriate JForum permissions under the roles in !site.template.course.
Check jforum.manage for teacher, instructor, faculty types of roles (maintain).
Check jforum.member for student types of custom roles that you have (access).
- If you have project sites and related roles in !site.template.project), appropriate permissions (jforum.manage or jforum.member) need to be checked as defined above.

CAUTION:
If you fail to check the Jforum.member and Jforum.manage permissions for your roles, Jforum may not work properly. 
If you add jforum to an installation with existing sites, users will not have the necessary permissions. You will need to user !Site.helper or other scripts to propagate the changes. 

**Add Jforum Icon to Sakai Left Menu**

- If you are using sakai’s default cascading style sheet create the icons folder under tomcat/webapps/library/skin/default/
and copy the image jforum-menu.png from jforum-tool/src/webapp/images/sakai-menu to

tomcat/webapps/library/skin/default/icons.

- Add the below line to tomcat/webapps/library/skin/default/portal.css

.icon-sakai-jforum-tool{
background-image: url(icons/jforum-menu.png);
}

**User Documentation**

- See relevant tutorials in our help pages:  http://etudes.org/help/instructors/
