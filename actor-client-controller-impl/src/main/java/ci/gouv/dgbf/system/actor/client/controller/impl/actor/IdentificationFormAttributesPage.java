package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectBooleanButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationFormQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormAttributesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private SelectOneCombo formSelectOneCombo;
	private DataTable attributesDataTable;
	private SelectBooleanButton scopeTypeScopeFunctionDerivableSelectBooleanButton;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Champ(s) de formulaire ";
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();		
		buildFormSelectOneCombo();
		buildAttributesDataTable();
		buildLayout();
	}
	
	private void buildFormSelectOneCombo() {
		formSelectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,IdentificationAttribute.class
				,SelectOneCombo.FIELD_CHOICES,EntityReader.getInstance().readOne(IdentificationForm.class, IdentificationFormQuerier.QUERY_IDENTIFIER_READ_FOR_UI));
	}
	
	private void buildAttributesDataTable() {
		attributesDataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE);
	}

	private void buildLayout() {
	
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends IdentificationAttributeListPage.DataTableListenerImpl implements Serializable {
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends IdentificationAttributeListPage.LazyDataModelListenerImpl implements Serializable {
		
	}
}