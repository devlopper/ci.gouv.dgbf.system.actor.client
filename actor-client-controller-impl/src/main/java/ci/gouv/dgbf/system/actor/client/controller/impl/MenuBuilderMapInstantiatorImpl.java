package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.security.Principal;

import org.cyk.utility.__kernel__.icon.Icon;
import org.cyk.utility.client.controller.component.menu.MenuBuilder;
import org.cyk.utility.client.controller.component.menu.MenuItemBuilder;

import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class MenuBuilderMapInstantiatorImpl extends org.cyk.utility.client.controller.component.menu.AbstractMenuBuilderMapInstantiatorImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __instantiateSessionMenuBuilderItems__(Object key, MenuBuilder sessionMenuBuilder, Object request,Principal principal) {
		sessionMenuBuilder.addItems(
				__inject__(MenuItemBuilder.class).setCommandableName("Gestion des privilèges").setCommandableIcon(Icon.SUITCASE)
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Privilèges").setCommandableNavigationIdentifier("privilegeListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Type de privilèges").setCommandableNavigationIdentifier("privilegeTypeListView"))
				,/*__inject__(MenuItemBuilder.class).setCommandableName("Gestion des profiles").setCommandableIcon(Icon.FILE)
					.listOrTree(Profile.class,ProfileType.class,Function.class,FunctionType.class)
				*/__inject__(MenuItemBuilder.class).setCommandableName("Gestion des visibilités").setCommandableIcon(Icon.EYE)
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Domaines").setCommandableNavigationIdentifier("scopeListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Type de domaines").setCommandableNavigationIdentifier("scopeTypeListView"))
				,__inject__(MenuItemBuilder.class).setCommandableName("Gestion des utilisateurs").setCommandableIcon(Icon.USERS)/*.addChild(
						__inject__(MenuItemBuilder.class).setCommandableName("Création").setCommandableIcon(Icon.PLUS)
						.setCommandableNavigationIdentifier("userAccountCreateListUserView")
				)*/
				//.addEntitySelect(UserAccount.class, Constant.SYSTEM_ACTION_IDENTIFIER_ASSIGN_PRIVILEGES)
				//.addEntitySelect(UserAccount.class, Constant.SYSTEM_ACTION_IDENTIFIER_ASSIGN_FUNCTION_SCOPES)
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Comptes utilisateurs").setCommandableNavigationIdentifier("actorListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Assignations").setCommandableNavigationIdentifier("actorListPrivilegesView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Affectations").setCommandableNavigationIdentifier("actorListScopesView"))
				//.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Affectations").setCommandableNavigationIdentifier("userAccountAssignScopesListUserAccountView"))
				/*		
				//.addEntitiesList(UserAccountInterim.class,UserAccountInterimModel.class)
				,__inject__(MenuItemBuilder.class).setCommandableName("Retour au portail").setCommandableIcon(Icon.FLASH).addChild(
						__inject__(MenuItemBuilder.class).setCommandableName("Retour")
						.setCommandableNavigationValue("http://10.3.4.20:30300/sib/portail/")
						)
				*/
				);	
	}
}