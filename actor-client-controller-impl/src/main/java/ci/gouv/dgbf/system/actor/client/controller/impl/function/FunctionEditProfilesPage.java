package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectManyCheckbox;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileFunction;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class FunctionEditProfilesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Function function;
	private SelectManyCheckbox profilesSelectManyCheckbox;
	private Collection<ProfileFunction> profilesFunctions;
	private Collection<Profile> systemProfiles,initialSelectedProfiles;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Profiles systèmes de la fonction "+function.getName();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		function = WebController.getInstance().getRequestParameterEntity(Function.class);
		super.__listenPostConstruct__();		
		systemProfiles = EntityReader.getInstance().readMany(Profile.class, ProfileQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES
				,ProfileQuerier.PARAMETER_NAME_TYPES_CODES,List.of(ci.gouv.dgbf.system.actor.server.persistence.entities.ProfileType.CODE_SYSTEME));
		if(CollectionHelper.isNotEmpty(systemProfiles)) {
			profilesFunctions = EntityReader.getInstance().readMany(ProfileFunction.class, ProfileFunctionQuerier.QUERY_IDENTIFIER_READ_BY_FUNCTIONS_CODES
					,ProfileFunctionQuerier.PARAMETER_NAME_FUNCTIONS_CODES,List.of(function.getCode()));
			if(CollectionHelper.isNotEmpty(profilesFunctions))
				initialSelectedProfiles = profilesFunctions.stream().map(x -> x.getProfile()).collect(Collectors.toList());
		}
		
		profilesSelectManyCheckbox = SelectManyCheckbox.build(SelectManyCheckbox.FIELD_CHOICES,systemProfiles,SelectManyCheckbox.FIELD_VALUE,initialSelectedProfiles);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,profilesSelectManyCheckbox,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Arguments<ProfileFunction> arguments = new Arguments<ProfileFunction>();
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ProfileFunctionBusiness.SAVE));
								if(CollectionHelper.isNotEmpty(systemProfiles)) {
									Collection<Profile> selectedProfiles = CollectionHelper.cast(Profile.class, profilesSelectManyCheckbox.getValue());
									arguments.addCreatablesOrUpdatables(systemProfiles.stream().filter(systemProfile -> CollectionHelper
											.contains(selectedProfiles, systemProfile) && !CollectionHelper.contains(initialSelectedProfiles, systemProfile))
											.map(systemProfile -> new ProfileFunction().setProfile(systemProfile).setFunction(function))
										.collect(Collectors.toList()));
									if(CollectionHelper.isNotEmpty(profilesFunctions))
										arguments.addDeletables(profilesFunctions.stream().filter(profilesFunction -> !selectedProfiles.contains(profilesFunction.getProfile())).collect(Collectors.toList()));
								}
								if(CollectionHelper.isNotEmpty(arguments.getCreatables()) || CollectionHelper.isNotEmpty(arguments.getDeletables())) {
									EntitySaver.getInstance().save(ProfileFunction.class, arguments);	
									//JsfController.getInstance().redirect("profileListView");
								}
								return null;
							}
						},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"),Cell.FIELD_WIDTH,12)
				));
	}
}