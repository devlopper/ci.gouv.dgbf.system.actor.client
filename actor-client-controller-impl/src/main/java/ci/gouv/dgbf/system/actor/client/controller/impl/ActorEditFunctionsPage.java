package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectManyCheckbox;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileFunction;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorEditFunctionsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Actor actor;
	private Profile profile;
	private SelectManyCheckbox functionsSelectManyCheckbox;
	private Collection<ProfileFunction> profilesFunctions;
	private Collection<Function> functions,initialSelectedFunctions;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Fonction(s) de "+actor.getCode()+" - "+actor.getNames();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntity(Actor.class);
		if(actor != null) {
			profile = CollectionHelper.getFirst(EntityReader.getInstance().readMany(Profile.class, ProfileQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES, ProfileQuerier.PARAMETER_NAME_ACTORS_CODES
					,List.of(actor.getCode())));
		}
		super.__listenPostConstruct__();		
		functions = EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ);
		if(CollectionHelper.isNotEmpty(functions)) {
			profilesFunctions = EntityReader.getInstance().readMany(ProfileFunction.class, ProfileFunctionQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES
					,ProfileFunctionQuerier.PARAMETER_NAME_PROFILES_CODES,List.of(profile.getCode()));
			if(CollectionHelper.isNotEmpty(profilesFunctions))
				initialSelectedFunctions = profilesFunctions.stream().map(x -> x.getFunction()).collect(Collectors.toList());
		}
		
		functionsSelectManyCheckbox = SelectManyCheckbox.build(SelectManyCheckbox.FIELD_CHOICES,functions,SelectManyCheckbox.FIELD_VALUE,initialSelectedFunctions);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,functionsSelectManyCheckbox,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Arguments<ProfileFunction> arguments = new Arguments<ProfileFunction>();
								arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ProfileFunctionBusiness.SAVE));
								if(CollectionHelper.isNotEmpty(functions)) {
									Collection<Function> selectedFunctions = CollectionHelper.cast(Function.class, functionsSelectManyCheckbox.getValue());
									arguments.addCreatablesOrUpdatables(functions.stream().filter(function -> CollectionHelper
											.contains(selectedFunctions, function) && !CollectionHelper.contains(initialSelectedFunctions, function))
											.map(function -> new ProfileFunction().setProfile(profile).setFunction(function).setActorIdentifier(actor.getIdentifier()))
										.collect(Collectors.toList()));
									if(CollectionHelper.isNotEmpty(profilesFunctions))
										arguments.addDeletables(profilesFunctions.stream()
												.filter(profilesFunction -> !selectedFunctions.contains(profilesFunction.getFunction()))
												.collect(Collectors.toList()));
									if(CollectionHelper.isNotEmpty(arguments.getDeletables()))
										arguments.getDeletables().forEach(x -> x.setActorIdentifier(actor.getIdentifier()));
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