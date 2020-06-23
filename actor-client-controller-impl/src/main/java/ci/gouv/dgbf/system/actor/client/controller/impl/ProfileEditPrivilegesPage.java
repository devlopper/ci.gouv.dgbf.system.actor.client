package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.primefaces.model.DualListModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ProfileEditPrivilegesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Profile profile;
	private Actor actor;
	private Collection<ProfilePrivilege> profilePrivileges;
	private Collection<Privilege> availablePrivileges;
	private Collection<Privilege> selectedPrivileges;
	private DualListModel<Privilege> privilegesDualListModel;
	private AutoComplete actorAutoComplete;
	
	@Override
	protected void __listenPostConstruct__() {
		profile = WebController.getInstance().getRequestParameterEntity(Profile.class);
		if(profile == null) {
			actor = WebController.getInstance().getRequestParameterEntityAsParent(Actor.class);
			if(actor != null) {
				profile = CollectionHelper.getFirst(EntityReader.getInstance().readMany(Profile.class, new Arguments<Profile>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
								new QueryExecutorArguments.Dto().setQueryIdentifier(ProfileQuerier.QUERY_NAME_READ_BY_ACTORS_CODES)
								.addFilterField(ActorQuerier.PARAMETER_NAME_IDENTIFIER, List.of(actor.getCode()))))));
			}
			if(profile != null) {
				profilePrivileges = EntityReader.getInstance().readMany(ProfilePrivilege.class, new Arguments<ProfilePrivilege>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
								new QueryExecutorArguments.Dto().setQueryIdentifier(ProfilePrivilegeQuerier.QUERY_NAME_READ_BY_PROFILES_CODES)
								.addFilterField(ProfilePrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode())))));
				if(CollectionHelper.isNotEmpty(profilePrivileges))
					selectedPrivileges = profilePrivileges.stream().map(ProfilePrivilege::getPrivilege).collect(Collectors.toList());
				availablePrivileges = EntityReader.getInstance().readMany(Privilege.class, new Arguments<Privilege>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
								new QueryExecutorArguments.Dto().setQueryIdentifier(PrivilegeQuerier.QUERY_NAME_READ_BY_PROFILES_CODES_NOT_ASSOCIATED)
								.addFilterField(PrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode())))));
			}		
		}
		super.__listenPostConstruct__();		
		if(availablePrivileges == null)
			availablePrivileges = new ArrayList<>();
		if(selectedPrivileges == null)
			selectedPrivileges = new ArrayList<>();
		privilegesDualListModel = new DualListModel<Privilege>((List<Privilege>)availablePrivileges, (List<Privilege>)selectedPrivileges);
		
		actorAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<Actor>() {
			@Override
			public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
				
			}
		},AutoComplete.FIELD_PLACEHOLDER,"veuillez saisir le nom d'utilisateur");
		
		actorAutoComplete.enableAjaxItemSelect();
		actorAutoComplete.getAjaxes().get("itemSelect").setListener(new Ajax.Listener.AbstractImpl() {
			@Override
			protected void run(AbstractAction action) {
				Actor actor = (Actor) FieldHelper.read(action.get__argument__(), "source.value");
				if(actor != null)
					JsfController.getInstance().redirect("resourceEditInitialsView",Map.of(ParameterName.stringify(Actor.class),List.of(actor.getIdentifier())));
			}
		});
		actorAutoComplete.getAjaxes().get("itemSelect").setDisabled(Boolean.FALSE);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Compte utilisateur"),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_CONTROL,actorAutoComplete,Cell.FIELD_WIDTH,10)	
				,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Privilèges"),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"picklist",Cell.FIELD_WIDTH,10)				
			));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(actor != null)
			return "Privilèges de "+actor.getCode()+" : "+actor.getNames();
		if(profile != null)
			return "Privilèges - "+profile.getName();
		return "Sélectionner un compte utilisateur";
	}
}