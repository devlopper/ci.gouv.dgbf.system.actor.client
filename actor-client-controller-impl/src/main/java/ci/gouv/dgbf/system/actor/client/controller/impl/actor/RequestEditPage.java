package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.ReadListener;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowableHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JavaServerFacesHelper;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputMask;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputLabel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.primefaces.PrimeFaces;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Civility;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentityGroup;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.identification.PublicRequestOpenPage;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CivilityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityGroupQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestEditPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	private ScopeFunctionSelectionController budgetaryScopeFunctionSelectionController;
		
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		//redirectIfTypeIsNull(form,RequestEditSelectTypePage.OUTCOME);
		//setAdministrativeUnitAutoCompleteReadItemLabelListener(form);
		postConstruct(form,budgetaryScopeFunctionSelectionController);
	}
	
	public static void postConstruct(Form form,ScopeFunctionSelectionController budgetaryScopeFunctionSelectionController) {
		if(form.getEntity() instanceof Request) {
			Request request = (Request) form.getEntity();
			if(request.getType() != null && ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.IDENTIFIER_DEMANDE_POSTES_BUDGETAIRES.equals(request.getType().getIdentifier())) {
				SelectOneCombo sectionSelectOne = form.getInput(SelectOneCombo.class, Request.FIELD_SECTION);
				SelectOneCombo administrativeUnitSelectOne = form.getInput(SelectOneCombo.class, Request.FIELD_ADMINISTRATIVE_UNIT);
				
				sectionSelectOne.setListener(new SelectOneCombo.Listener.AbstractImpl<Section>() {
					@Override
					public Collection<Section> computeChoices(AbstractInputChoice<Section> input) {
						Collection<Section> sections = EntityReader.getInstance().readMany(Section.class,SectionQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
						CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(sections);
						return sections;
					}
					
					@Override
					public void select(AbstractInputChoiceOne input, Section section) {
						super.select(input, section);
						if(administrativeUnitSelectOne != null) {
							administrativeUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
							administrativeUnitSelectOne.updateChoices();
							administrativeUnitSelectOne.selectFirstChoice();
						}
					}
				});
				sectionSelectOne.enableValueChangeListener(List.of(administrativeUnitSelectOne));
				
				administrativeUnitSelectOne.setListener(new SelectOneCombo.Listener.AbstractImpl<AdministrativeUnit>() {
					@Override
					public Collection<AdministrativeUnit> computeChoices(AbstractInputChoice<AdministrativeUnit> input) {
						if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
							return null;
						Collection<AdministrativeUnit> administrativeUnits = EntityReader.getInstance().readMany(AdministrativeUnit.class,AdministrativeUnitQuerier
								.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI,AdministrativeUnitQuerier.PARAMETER_NAME_SECTION_IDENTIFIER
								,((Section)sectionSelectOne.getValue()).getIdentifier());
						CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(administrativeUnits);
						return administrativeUnits;
					}
					
					@Override
					public void select(AbstractInputChoiceOne input, AdministrativeUnit administrativeUnit) {
						super.select(input, administrativeUnit);
						if(administrativeUnit != null) {
							if(budgetaryScopeFunctionSelectionController != null) {
								if(CollectionHelper.isEmpty(budgetaryScopeFunctionSelectionController.getSelected())) {
									Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>();
									arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments());
									arguments.getRepresentationArguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto());
									arguments.getRepresentationArguments().getQueryExecutorArguments()
										.setQueryIdentifier(ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_SCOPE_IDENTIFIER_BY_FUNCTION_CODE_FOR_UI);
									arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterFieldsValues(
											ScopeFunctionQuerier.PARAMETER_NAME_SCOPE_IDENTIFIER, administrativeUnit.getIdentifier()
											, ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_CODE
											, ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER);
									ArrayList<String> list = new ArrayList<>();
									list.addAll(List.of(ScopeFunction.FIELD_REQUESTED,ScopeFunction.FIELD_GRANTED));
									arguments.getRepresentationArguments().getQueryExecutorArguments().setProcessableTransientFieldsNames(list);
									Collection<ScopeFunction> scopeFunctions = EntityReader.getInstance().readMany(ScopeFunction.class,arguments);
									if(CollectionHelper.getSize(scopeFunctions) == 1) {
										ScopeFunction scopeFunction = CollectionHelper.getFirst(scopeFunctions);
										if(!Boolean.TRUE.equals(scopeFunction.getGranted())) {
											budgetaryScopeFunctionSelectionController.addScopeFunction(scopeFunction);
											PrimeFaces.current().ajax().update(":form:"+budgetaryScopeFunctionSelectionController.getScopeFunctionsListIdentifier());
										}
									}
								}
							}
						}						
					}
				});
				administrativeUnitSelectOne.enableValueChangeListener(List.of());
				
				if(Action.UPDATE.equals(form.getAction())) {
					sectionSelectOne.selectBySystemIdentifier(request.getSection().getIdentifier());
					administrativeUnitSelectOne.selectBySystemIdentifier(request.getAdministrativeUnit().getIdentifier());
				}
			}
		}		
	}
	
	public static void setAdministrativeUnitAutoCompleteReadItemLabelListener(Form form) {
		AutoComplete administrativeUnitAutoComplete = form.getInput(AutoComplete.class, Request.FIELD_ADMINISTRATIVE_UNIT);
		if(administrativeUnitAutoComplete != null) {
			administrativeUnitAutoComplete.setReadItemLabelListener(new ReadListener() {				
				@Override
				public Object read(Object object) {
					if(object instanceof AdministrativeUnit) {
						AdministrativeUnit administrativeUnit = (AdministrativeUnit)object;
						String label = administrativeUnit.toString();
						if(StringHelper.isNotBlank(administrativeUnit.getSectionCodeName()))
							label = label + " ("+administrativeUnit.getSectionCodeName()+")";
						return label;
					}
					return null;
				}
			});
		}
	}
	
	public static void redirectIfTypeIsNull(Form form,String outcome) {
		if(form.getEntity() == null || ((Request)form.getEntity()).getType() == null)
			Redirector.getInstance().redirect(outcome, null);
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		super.setActionFromRequestParameter();
		action = getAction(action);
	}
	
	public static Action getAction(Action action) {
		return ValueHelper.defaultToIfNull(action,Action.CREATE);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return getWindowTitleValue(form,super.__getWindowTitleValue__());
	}
	
	public static String getWindowTitleValue(Form form,String default_) {
		if(form.getEntity() == null || ((Request)form.getEntity()).getType() == null)
			return default_;
		if(Action.CREATE.equals(form.getAction()))
			return "Saisie de nouvelle demande : "+((Request)form.getEntity()).getType().getName();
		if(Action.UPDATE.equals(form.getAction()))
			return "Modification de demande : "+((Request)form.getEntity()).getType().getName();
		return default_;
	}
	
	@Override
	protected Form __buildForm__() {
		budgetaryScopeFunctionSelectionController = new ScopeFunctionSelectionController();
		return buildForm(Form.FIELD_ACTION,action,ScopeFunctionSelectionController.class,budgetaryScopeFunctionSelectionController);
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		Request request = (Request) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		if(request == null)
			request = getRequestFromParameter((Action) MapHelper.readByKey(arguments, Form.FIELD_ACTION),(String)MapHelper.readByKey(arguments, Actor.class));			
		ScopeFunctionSelectionController budgetaryScopeFunctionSelectionController = (ScopeFunctionSelectionController) MapHelper.readByKey(arguments, ScopeFunctionSelectionController.class);
		if(budgetaryScopeFunctionSelectionController != null)
			budgetaryScopeFunctionSelectionController.setSelected(request.getBudgetariesScopeFunctions());
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener(request,budgetaryScopeFunctionSelectionController));
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	public static Request getRequestFromParameter(Action action,String actorIdentifier) {
		if(Action.CREATE.equals(action)) {
			String requestTypeIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestType.class));
			if(StringHelper.isBlank(requestTypeIdentifier))
				requestTypeIdentifier = ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.IDENTIFIER_DEMANDE_POSTES_BUDGETAIRES;
				
			if(StringHelper.isBlank(requestTypeIdentifier))
				return null;			
			Request request = null;
			if(StringHelper.isBlank(actorIdentifier)) {
				request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_INSTANTIATE_ONE_BY_TYPE_IDENTIFIER
						, RequestQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,requestTypeIdentifier);
				request.setReadPageURL(JavaServerFacesHelper.buildUrlFromOutcome(PublicRequestOpenPage.OUTCOME));
				request.setActor(__inject__(ActorController.class).getLoggedIn());
			}else {
				request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_INSTANTIATE_ONE_BY_TYPE_IDENTIFIER_BY_ACTOR_IDENTIFIER
						, RequestQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,requestTypeIdentifier
						, RequestQuerier.PARAMETER_NAME_ACTOR_IDENTIFIER,actorIdentifier
						);
			}
			return request;
		}else {
			Request request = null;
			if(Action.UPDATE.equals(action)) {
				request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_EDIT
						, RequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue()));
			}else {
				request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI
						, RequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue()));				
			}
			if(request != null) {
				if(request.getActOfAppointmentSignatureDateAsTimestamp() != null && request.getActOfAppointmentSignatureDate() == null)
					request.setActOfAppointmentSignatureDate(new Date(request.getActOfAppointmentSignatureDateAsTimestamp()));
			}
			return request;
		}
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		protected Request request;
		protected ScopeFunctionSelectionController budgetaryScopeFunctionSelectionController;
		
		public FormListener(Request request,ScopeFunctionSelectionController budgetaryScopeFunctionSelectionController) {
			this.request = request;
			this.budgetaryScopeFunctionSelectionController = budgetaryScopeFunctionSelectionController;
		}
		
		@Override
		public void act(Form form) {
			ThrowableHelper.throwIllegalArgumentExceptionIfNull("demande", request);
			writeBudgetariesScopeFunctionsIdentifiers();
			
			String actionIdentifier = null;
			if(Action.CREATE.equals(form.getAction()))
				actionIdentifier = RequestBusiness.INITIALIZE;
			else if(Action.UPDATE.equals(form.getAction()))
				actionIdentifier = RequestBusiness.RECORD;
			else
				throw new RuntimeException("Action "+form.getAction()+" not yet handled");
			
			if(request.getActOfAppointmentSignatureDate() == null)
				request.setActOfAppointmentSignatureDateAsTimestamp(null);
			else
				request.setActOfAppointmentSignatureDateAsTimestamp(request.getActOfAppointmentSignatureDate().getTime());
			Arguments<Request> arguments = new Arguments<Request>().setRepresentationArguments(new org.cyk.utility.representation.Arguments()
					.setActionIdentifier(actionIdentifier)).addCreatablesOrUpdatables(request);
			EntitySaver.getInstance().save(Request.class,arguments);
			List<Object> v = arguments.get__response__().getHeaders().get(Request.FIELD_IDENTIFIER);
			if(CollectionHelper.isNotEmpty(v)) {
				String identifier = (String) v.get(0);
				request.setIdentifier(identifier);
			}
		}
		
		protected void writeBudgetariesScopeFunctionsIdentifiers() {
			if(budgetaryScopeFunctionSelectionController != null)
				request.setBudgetariesScopeFunctions(budgetaryScopeFunctionSelectionController.getSelected());
			if(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.CODE_DEMANDE_POSTES_BUDGETAIRES.equals(request.getType().getCode()) 
					&& CollectionHelper.isEmpty(request.getBudgetariesScopeFunctions()))
				throw new RuntimeException("Veuillez sélectionner au minimum une fonction budgétaire");			
			request.writeBudgetariesScopeFunctionsIdentifiers();
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		protected Request request;
		protected Map<String,IdentificationAttribute> fieldsNames;
		protected Integer identificationBlockStartIndex=0,requestBlockStartIndex,currentIndex;
		protected Boolean blocksShowable = Boolean.TRUE;
		
		public FormConfiguratorListener(Request request) {
			this.request = request;
			if(request != null && request.getType() != null && request.getType().getForm() != null) {
				fieldsNames = IdentificationForm.computeFieldsNames(request.getType().getForm(), Request.class);
			}
		}
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			if(request == null)
				return List.of(Request.FIELD_TYPE);
			if(fieldsNames == null)
				return null;
			List<String> collection = new ArrayList<>(fieldsNames.keySet());
			if(request.getType() != null 
					&& ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.IDENTIFIER_DEMANDE_POSTES_BUDGETAIRES.equals(request.getType().getIdentifier())) {
				Integer administrativeUnitIndex = collection.indexOf(Request.FIELD_ADMINISTRATIVE_UNIT);
				if(administrativeUnitIndex != null) {
					collection.add(administrativeUnitIndex,Request.FIELD_SECTION);
				}			
			}
			//collection.add(0, Request.FIELD_TYPE);
			return collection;
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(currentIndex == null)
				currentIndex = 0;
			else
				currentIndex++;
			IdentificationAttribute attribut = fieldsNames == null ? null : fieldsNames.get(fieldName);
			if(attribut != null) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, attribut.getName());
				map.put(AbstractInput.FIELD_REQUIRED, attribut.getRequired());
			}
			if(Request.FIELD_ACTOR.equals(fieldName)) {
				
			}else if(Request.FIELD_ACTOR_CODE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Nom d'utilisateur");
			}else if(Request.FIELD_ACTOR_NAMES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Nom et prénoms");
			}else if(Request.FIELD_TYPE_AS_STRING.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Type");
			}else if(Request.FIELD_COMMENT.equals(fieldName)) {
				
			}else if(Request.FIELD_TYPE.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_CHOICES
						,request == null ? EntityReader.getInstance().readMany(RequestType.class,RequestTypeQuerier.QUERY_IDENTIFIER_READ_ALL) : List.of(request.getType()));
			}else if(Request.FIELD_GROUP.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_CHOICES,EntityReader.getInstance().readMany(IdentityGroup.class,IdentityGroupQuerier.QUERY_IDENTIFIER_READ));
			}else if(Request.FIELD_CIVILITY.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_CHOICES,EntityReader.getInstance().readMany(Civility.class,CivilityQuerier.QUERY_IDENTIFIER_READ));
			}else if(Request.FIELD_SECTION.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_DESCRIPTION_OUTPUT_TEXT_VALUE, "La section où vous êtes en fonction");
				map.put(AbstractInputChoice.FIELD_CHOICE_CLASS,Section.class);
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				requestBlockStartIndex = currentIndex;
			}else if(Request.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_CHOICE_CLASS, AdministrativeUnit.class);
				//map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_DESCRIPTION_OUTPUT_TEXT_VALUE, "L'unité administrative où vous êtes en fonction");
			}else if(Request.FIELD_BUDGETARIES_FUNCTIONS.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, Function.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				//map.put(AutoComplete.FIELD_READ_QUERY_IDENTIFIER, FunctionQuerier.QUERY_IDENTIFIER_);
			}else if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS.equals(fieldName)) {
				//requestBlockStartIndex = currentIndex;
				map.put(AutoComplete.FIELD_ENTITY_CLASS, ScopeFunction.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE, Boolean.TRUE);
				//map.put(AutoComplete.FIELD_READ_QUERY_IDENTIFIER, ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER);
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_DESCRIPTION_OUTPUT_TEXT_VALUE, "La ou les fonctions budgétaires que vous avez");
			}else if(Request.FIELD_BUDGET_SPECIALIZATION_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, BudgetSpecializationUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(Request.FIELD_ADMINISTRATIVE_FUNCTION.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_DESCRIPTION_OUTPUT_TEXT_VALUE, "La fonction que vous exercez dans l'unité administrative spécifiée");
			}else if(Request.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_DESCRIPTION_OUTPUT_TEXT_VALUE, "L'adresse email avec laquelle vous allez vous connecter au système");
				map.put(AbstractInput.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
					@Override
					public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
						super.validate(context, component, value);
						if(value instanceof String) {
							String string = (String) value;
							if(!EmailValidator.getInstance().isValid(string))
								throwValidatorException("Valeur incorrecte. exemple : abc@mail.com");
						}
					}
				});
			}else if(Request.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
				map.put(InputMask.AbstractConfiguratorImpl.FIELD_DESCRIPTION_OUTPUT_TEXT_VALUE, "Format de dix(10) chiffres. exemple : 0102030405");
				map.put(InputMask.FIELD_MASK, StringUtils.repeat("99", 5));
			}else if(Request.FIELD_OFFICE_PHONE_NUMBER.equals(fieldName)) {
				map.put(InputMask.AbstractConfiguratorImpl.FIELD_DESCRIPTION_OUTPUT_TEXT_VALUE, "Format de dix(10) chiffres. exemple : 0102030405");
				map.put(InputMask.FIELD_MASK, StringUtils.repeat("99", 5));
			}
			return map;
		}
		
		@Override
		public Map<Object, Object> getInputLabelCellArguments(Form form, AbstractInput<?> input, OutputLabel label) {
			Map<Object, Object> arguments = super.getInputLabelCellArguments(form, input, label);
			arguments.put(Cell.FIELD_WIDTH, 3);
			if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS.equals(input.getField().getName())) {
				arguments.put(Cell.FIELD_CONTROL, OutputText.buildFromValue(label.getValue()));
			}
			return arguments;
		}
		
		@Override
		public Map<Object, Object> getInputCellArguments(Form form, AbstractInput<?> input) {
			Map<Object, Object> arguments = super.getInputCellArguments(form, input);
			arguments.put(Cell.FIELD_WIDTH, 9);
			if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS.equals(input.getField().getName())) {
				arguments.put(Cell.FIELD_CONTROL_TEMPLATE, "/private/request/edit/__budgetariesscopefunctions__.xhtml");
			}
			return arguments;
		}
		/*
		@Override
		protected Map<Integer, Cell> getLayoutArgumentsRowCellModel(Form form) {
			Map<Integer, Cell> map = super.getLayoutArgumentsRowCellModel(form);
			map.get(0).setWidth(3);
			map.get(1).setWidth(9);
			return map;
		}*/
		
		@Override
		public Map<Object, Object> getLayoutArguments(Form form, Collection<Map<Object, Object>> cellsArguments) {
			Map<Object, Object> arguments = super.getLayoutArguments(form, cellsArguments);
			if(request != null && request.getType() != null) {
				if(Boolean.TRUE.equals(blocksShowable) && ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.CODE_DEMANDE_POSTES_BUDGETAIRES.equals(request.getType().getCode())) {
					String styleClass = "ui-panel-titlebar ui-widget-header ui-helper-clearfix ui-corner-all";
					identificationBlockStartIndex = identificationBlockStartIndex * 2;
					CollectionHelper.addElementAt(cellsArguments, identificationBlockStartIndex
							, MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Identification"),Cell.FIELD_WIDTH,12
									,Cell.FIELD_STYLE_CLASS,styleClass));
					requestBlockStartIndex = requestBlockStartIndex * 2 + 1;
					CollectionHelper.addElementAt(cellsArguments, requestBlockStartIndex
							, MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Demande de fonction"),Cell.FIELD_WIDTH,12
									,Cell.FIELD_STYLE_CLASS,styleClass));
					/*
					//administrativeUnitStartIndex = administrativeUnitStartIndex * 2 + 1;
					CollectionHelper.addElementAt(cellsArguments, requestBlockStartIndex+1
							, MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Sélection de la section"),Cell.FIELD_WIDTH,3
									,Cell.FIELD_STYLE_CLASS,styleClass));
					CollectionHelper.addElementAt(cellsArguments, requestBlockStartIndex+2
							, MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Sélection de la section"),Cell.FIELD_WIDTH,12
									,Cell.FIELD_STYLE_CLASS,styleClass));
					*/
				}
			}			
			return arguments;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKeyDoNotOverride(map, CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
	}
	
	/**/
	
	public static final String OUTCOME = "requestEditView";	
}