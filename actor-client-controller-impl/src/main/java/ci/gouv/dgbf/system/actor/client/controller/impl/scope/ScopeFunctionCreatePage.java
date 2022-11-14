package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.api.AdministrativeUnitController;
import ci.gouv.dgbf.system.actor.client.controller.api.BudgetSpecializationUnitController;
import ci.gouv.dgbf.system.actor.client.controller.api.ScopeFunctionController;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Locality;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.LocalityQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionCreatePage extends AbstractEntityEditPageContainerManagedImpl<ScopeFunction> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		SelectOneCombo subFunctionCodeSelectOneCombo = form.getInput(SelectOneCombo.class, ScopeFunction.FIELD_CODE_PREFIX);
		SelectOneCombo sectionSelectOneCombo = form.getInput(SelectOneCombo.class, ScopeFunction.FIELD_SECTION);
		SelectOneCombo administrativeUnitSelectOneCombo = form.getInput(SelectOneCombo.class, ScopeFunction.FIELD_ADMINISTRATIVE_UNIT);
		SelectOneCombo budgetSpecializationUnitSelectOneCombo = form.getInput(SelectOneCombo.class, ScopeFunction.FIELD_BUDGET_SPECIALIZATION_UNIT);
		SelectOneCombo localitySelectOneCombo = form.getInput(SelectOneCombo.class, ScopeFunction.FIELD_LOCALITY);
		InputText nameInputText = form.getInput(InputText.class, ScopeFunction.FIELD_NAME);
		
		subFunctionCodeSelectOneCombo.setListener(new SelectOneCombo.Listener.AbstractImpl<String>() {			
			@Override
			public void select(AbstractInputChoiceOne input, String choice) {
				super.select(input, choice);
				nameInputText.setValue(null);
				initializeCellsRenderedByIndexes();				
				if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_G1.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,4,5,10,11);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_G_EPN.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,4,5,10,11);
				
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O2.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,6,7,10,11);	
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O3.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,6,7,8,9,10,11);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O9.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,6,7,10,11);
				
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_C1.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,4,5,10,11);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_C2.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,4,5,10,11);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_C3.equals(choice))
					form.getLayout().setCellsRenderedByIndexes(Boolean.TRUE,2,3,4,5,10,11);
			}
		});
		initializeCellsRenderedByIndexes();
		subFunctionCodeSelectOneCombo.enableValueChangeListener(List.of(form.getLayout()));
				
		sectionSelectOneCombo.setListener(new SelectOneCombo.Listener.AbstractImpl<Section>() {
			@Override
			protected Collection<Section> __computeChoices__(AbstractInputChoice<Section> input,Class<?> entityClass) {
				List<Section> choices = (List<Section>) __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();						
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, Section section) {
				super.select(input, section);
				nameInputText.setValue(null);
				String codePrefix = (String) AbstractInput.getValue(subFunctionCodeSelectOneCombo);
				if(StringUtils.startsWith(codePrefix, "G") || StringUtils.startsWith(codePrefix, "C") || StringUtils.startsWith(codePrefix, "T")) {
					if(administrativeUnitSelectOneCombo != null) {				
						administrativeUnitSelectOneCombo.setChoicesInitialized(Boolean.FALSE);
						administrativeUnitSelectOneCombo.updateChoices();
						administrativeUnitSelectOneCombo.selectFirstChoice();
					}
				}else if(StringUtils.startsWith(codePrefix, "O")) {
					if(budgetSpecializationUnitSelectOneCombo != null) {				
						budgetSpecializationUnitSelectOneCombo.setChoicesInitialized(Boolean.FALSE);
						budgetSpecializationUnitSelectOneCombo.updateChoices();
						budgetSpecializationUnitSelectOneCombo.selectFirstChoice();
					}
				}
			}
		});
		sectionSelectOneCombo.enableValueChangeListener(List.of(administrativeUnitSelectOneCombo,budgetSpecializationUnitSelectOneCombo,nameInputText));
		
		administrativeUnitSelectOneCombo.setListener(new SelectOneCombo.Listener.AbstractImpl<AdministrativeUnit>() {
			@Override
			protected Collection<AdministrativeUnit> __computeChoices__(AbstractInputChoice<AdministrativeUnit> input,Class<?> entityClass) {
				Section section = (Section) AbstractInputChoice.getValue(sectionSelectOneCombo);
				if(section == null)
					return null;
				/*List<AdministrativeUnit> choices = (List<AdministrativeUnit>) __inject__(AdministrativeUnitController.class)
						.readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(section.getIdentifier());	
				*/
				List<AdministrativeUnit> choices;
				String codePrefix = (String) AbstractInput.getValue(subFunctionCodeSelectOneCombo);
				if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_G_EPN.equals(codePrefix)) {
					choices = (List<AdministrativeUnit>) __inject__(AdministrativeUnitController.class).readBySectionIdentifierByServiceGroupCodeStartsWith(section.getIdentifier(),"32");
				}else {
					choices = (List<AdministrativeUnit>) __inject__(AdministrativeUnitController.class).readBySectionIdentifier(section.getIdentifier());
				}
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, AdministrativeUnit administrativeUnit) {
				super.select(input, administrativeUnit);
				String codePrefix = (String) AbstractInput.getValue(subFunctionCodeSelectOneCombo);
				if(StringUtils.startsWith(codePrefix, "G")) {
					setCreditManagerName(nameInputText, administrativeUnitSelectOneCombo);
				}				
			}
		});
		administrativeUnitSelectOneCombo.enableValueChangeListener(List.of(nameInputText));
		
		budgetSpecializationUnitSelectOneCombo.setListener(new SelectOneCombo.Listener.AbstractImpl<BudgetSpecializationUnit>() {
			@Override
			protected Collection<BudgetSpecializationUnit> __computeChoices__(AbstractInputChoice<BudgetSpecializationUnit> input,Class<?> entityClass) {
				Section section = (Section) AbstractInputChoice.getValue(sectionSelectOneCombo);
				if(section == null)
					return null;
				List<BudgetSpecializationUnit> choices = (List<BudgetSpecializationUnit>) __inject__(BudgetSpecializationUnitController.class)
						.readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(section.getIdentifier());	
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, BudgetSpecializationUnit budgetSpecializationUnit) {
				super.select(input, budgetSpecializationUnit);
				setAuthorizingOfficerName(nameInputText,subFunctionCodeSelectOneCombo, budgetSpecializationUnitSelectOneCombo, localitySelectOneCombo);
			}
		});
		budgetSpecializationUnitSelectOneCombo.enableValueChangeListener(List.of(nameInputText));
		
		localitySelectOneCombo.setListener(new SelectOneCombo.Listener.AbstractImpl<Locality>() {
			@Override
			protected Collection<Locality> __computeChoices__(AbstractInputChoice<Locality> input,Class<?> entityClass) {
				Collection<Locality> choices = EntityReader.getInstance().readMany(Locality.class, new Arguments<Locality>()
						.queryIdentifier(LocalityQuerier.QUERY_IDENTIFIER_READ_DYNAMIC)); //(List<Locality>) __inject__(LocalityController.class).read();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, Locality locality) {
				super.select(input, locality);
				setAuthorizingOfficerName(nameInputText,subFunctionCodeSelectOneCombo, budgetSpecializationUnitSelectOneCombo, localitySelectOneCombo);
			}
		});
		localitySelectOneCombo.enableValueChangeListener(List.of(nameInputText));
		
	}
	
	private void initializeCellsRenderedByIndexes() {
		form.getLayout().setCellsRenderedByIndexes(Boolean.FALSE,2,3,4,5,6,7,8,9,10,11);
	}
	
	private void setAuthorizingOfficerName(InputText nameInputText,SelectOneCombo subFunctionCodeSelectOneCombo,SelectOneCombo budgetSpecializationUnitSelectOneCombo,SelectOneCombo localitySelectOneCombo) {
		String subFunctionCode = (String) AbstractInput.getValue(subFunctionCodeSelectOneCombo);
		BudgetSpecializationUnit budgetSpecializationUnit = (BudgetSpecializationUnit) AbstractInput.getValue(budgetSpecializationUnitSelectOneCombo);
		if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O2.equals(subFunctionCode)
				|| ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O9.equals(subFunctionCode)) {
			if(budgetSpecializationUnit == null)
				nameInputText.setValue(null);
			else {
				nameInputText.setValue(__inject__(ScopeFunctionController.class)
						.computeAuthorizingOfficerHolderNameByBudgetSpecializationUnitIdentifierByLocalityIdentifier(budgetSpecializationUnit.getIdentifier(), null));
			}
		}else {			
			Locality locality = (Locality) AbstractInput.getValue(localitySelectOneCombo);		
			if(budgetSpecializationUnit == null || locality == null)
				nameInputText.setValue(null);
			else {
				nameInputText.setValue(__inject__(ScopeFunctionController.class)
						.computeAuthorizingOfficerHolderNameByBudgetSpecializationUnitIdentifierByLocalityIdentifier(budgetSpecializationUnit.getIdentifier()
								, locality.getIdentifier()));
			}
		}		
	}
	
	private void setCreditManagerName(InputText nameInputText,SelectOneCombo administrativeUnitSelectOneCombo) {
		AdministrativeUnit administrativeUnit = (AdministrativeUnit) AbstractInput.getValue(administrativeUnitSelectOneCombo);
		if(administrativeUnit == null)
			nameInputText.setValue(null);
		else
			nameInputText.setValue(__inject__(ScopeFunctionController.class)
					.computeCreditManagerHolderNameByAdministrativeUnitIdentifier(administrativeUnit.getIdentifier()));
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm();
	}

	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map, Form.FIELD_ENTITY_CLASS, ScopeFunction.class);
		Action action = (Action) MapHelper.readByKey(map, Form.FIELD_ACTION);
		if(action == null)
			action = ValueHelper.defaultToIfNull(WebController.getInstance().getRequestParameterAction(), Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, action);
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());	
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(ScopeFunction.FIELD_CODE_PREFIX
				,ScopeFunction.FIELD_SECTION,ScopeFunction.FIELD_ADMINISTRATIVE_UNIT,ScopeFunction.FIELD_BUDGET_SPECIALIZATION_UNIT,ScopeFunction.FIELD_LOCALITY
				,ScopeFunction.FIELD_NAME));	
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		Form form = Form.build(map);
		return form;
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	/**/

	public static class FormListenerImpl extends Form.Listener.AbstractImpl implements Serializable {
		@Override
		public void act(Form form) {
			ScopeFunction scopeFunction = (ScopeFunction) form.getEntity();
			scopeFunction.setFunctionCode(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.getFunctionCodeFromCategoryCode(scopeFunction.getCodePrefix()));
			if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER.equals(scopeFunction.getFunctionCode()) && 
					scopeFunction.getAdministrativeUnit() != null)
				scopeFunction.setScopeIdentifier(scopeFunction.getAdministrativeUnit().getIdentifier());
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER.equals(scopeFunction.getFunctionCode()) && 
					scopeFunction.getBudgetSpecializationUnit() != null)
				scopeFunction.setScopeIdentifier(scopeFunction.getBudgetSpecializationUnit().getIdentifier());
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER.equals(scopeFunction.getFunctionCode()) && 
					scopeFunction.getAdministrativeUnit() != null)
				scopeFunction.setScopeIdentifier(scopeFunction.getAdministrativeUnit().getIdentifier());
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER.equals(scopeFunction.getFunctionCode()) && 
					scopeFunction.getAdministrativeUnit() != null)
				scopeFunction.setScopeIdentifier(scopeFunction.getAdministrativeUnit().getIdentifier());
			
			if(scopeFunction.getLocality() != null)
				scopeFunction.setLocalityIdentifier(scopeFunction.getLocality().getIdentifier());
			scopeFunction.set__auditWho__(SessionHelper.getUserName());
			
			Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(scopeFunction);
			arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ScopeFunctionBusiness.CREATE_BY_SCOPE_IDENTIFIER_BY_CATEGORY_CODE));
			EntitySaver.getInstance().save(ScopeFunction.class, arguments);
		}
		
		@Override
		public Boolean isSubmitButtonShowable(Form form) {
			return Boolean.TRUE;
		}
	}

	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
			if(ScopeFunction.FIELD_FUNCTION.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Catégorie de fonction");
				
			}else if(ScopeFunction.FIELD_CODE_PREFIX.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction");
				Collection<SelectItem> choices = new ArrayList<>();
				choices.add(new SelectItem(null, "-- Aucune sélection --"));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_G1
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_G1));
				
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O2
							, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_O2));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O3
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_O3));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_O9
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_O9));
				
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_C1
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_C1));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_C2
					, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_C2));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_C3
					, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_C3));
				
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T1
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T1));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T2
					, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T2));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T3
					, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T3));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T4
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T4));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T5
					, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T5));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T6
					, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T6));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T8
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T8));
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_T9
					, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_T9));
				
				choices.add(new SelectItem(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_CODE_G_EPN
						, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.CATEGORY_NAME_G_EPN));
				
				map.put(AbstractInputChoice.FIELD_CHOICES, choices);
			}else if(ScopeFunction.FIELD_SECTION.equals(fieldName)) {
				
			}else if(ScopeFunction.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
				
			}else if(ScopeFunction.FIELD_BUDGET_SPECIALIZATION_UNIT.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Programme/Dotation");
				
			}else if(ScopeFunction.FIELD_LOCALITY.equals(fieldName)) {
				
			}else if(ScopeFunction.FIELD_NAME.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Libellé");
			}
			return map;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			map.put(CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
	}
	
	public static final String OUTCOME = "scopeFunctionCreateView";
}