package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectBooleanButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormEditAttributsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private IdentificationForm form;
	private DataTable attributssDataTable;
	private SelectBooleanButton scopeTypeScopeFunctionDerivableSelectBooleanButton;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Champ(s) du formulaire "+form.getName();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		form = WebController.getInstance().getRequestParameterEntity(IdentificationForm.class);
		super.__listenPostConstruct__();		
		
	}
	
	
}