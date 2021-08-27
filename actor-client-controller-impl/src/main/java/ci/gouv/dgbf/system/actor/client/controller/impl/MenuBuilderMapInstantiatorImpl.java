package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.security.Principal;

import org.cyk.utility.__kernel__.icon.Icon;
import org.cyk.utility.client.controller.component.menu.MenuBuilder;
import org.cyk.utility.client.controller.component.menu.MenuItemBuilder;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class MenuBuilderMapInstantiatorImpl extends org.cyk.utility.client.controller.component.menu.AbstractMenuBuilderMapInstantiatorImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String IDENTIFICATION = "identification";
	public static final String NONE = "none";
	
	@Override
	protected void __instantiateSessionMenuBuilderItems__(Object key, MenuBuilder sessionMenuBuilder, Object request,Principal principal) {		
		sessionMenuBuilder.addItems(
				__inject__(MenuItemBuilder.class).setCommandableName("Gestion des privilèges").setCommandableIcon(Icon.SUITCASE)
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Privilèges").setCommandableNavigationIdentifier("privilegeListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Type de privilèges").setCommandableNavigationIdentifier("privilegeTypeListView"))
				,__inject__(MenuItemBuilder.class).setCommandableName("Gestion des profiles").setCommandableIcon(Icon.FILE)
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Profiles").setCommandableNavigationIdentifier("profileListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Type de profile").setCommandableNavigationIdentifier("profileTypeListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Fonctions").setCommandableNavigationIdentifier("functionListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Type de fonctions").setCommandableNavigationIdentifier("functionTypeListView"))
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
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Comptes utilisateurs").setCommandableNavigationIdentifier("actorListView" /*"actorListView"*/))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Assignations").setCommandableNavigationIdentifier("assignationView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Affectations").setCommandableNavigationIdentifier("affectationView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Formulaires").setCommandableNavigationIdentifier("identificationFormListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Types de demandes").setCommandableNavigationIdentifier("requestTypeListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Demandes").setCommandableNavigationIdentifier("requestIndexView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Bordereaux").setCommandableNavigationIdentifier("requestDispatchSlipIndexView"))
				
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Demander").setCommandableNavigationIdentifier("actorRecordRequestsView"))
				
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Liste Demande profile").setCommandableNavigationIdentifier("actorProfileRequestListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Traiter Demande profile").setCommandableNavigationIdentifier("actorProfileRequestProcessManyView"))
				
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Liste Demande domaines").setCommandableNavigationIdentifier("actorScopeRequestListView"))
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Traiter Demande domaine").setCommandableNavigationIdentifier("actorScopeRequestProcessManyView"))
				
				.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Mon compte").setCommandableNavigationIdentifier("myAccountIndexView"))
							
				
				
				//.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Domaines d'administration").setCommandableNavigationIdentifier("clusterListView"))
				//.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Habilitations").setCommandableNavigationIdentifier("clusterPrivilegesListView"))
				//.addChild(__inject__(MenuItemBuilder.class).setCommandableName("Domaines d'administration").setCommandableNavigationIdentifier("administrationDomainsView"))
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