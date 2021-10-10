package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.RequestStatusController;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestStatusListPage extends AbstractEntityListPageContainerManagedImpl<RequestStatus> implements Serializable {

	public static SelectOneCombo buildSelectOne(RequestStatus requestStatus,Object container,String processedSelectOneFieldName) {
		SelectOneCombo selectOne = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,requestStatus,SelectOneCombo.FIELD_CHOICE_CLASS,RequestStatus.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<RequestStatus>() {
			@Override
			public Collection<RequestStatus> computeChoices(AbstractInputChoice<RequestStatus> input) {
				Collection<RequestStatus> choices = null;
				if(container == null || StringHelper.isBlank(processedSelectOneFieldName))
					choices = __inject__(RequestStatusController.class).read();
				else {					
					AbstractInput<?> processedInput = (AbstractInput<?>)FieldHelper.read(container, processedSelectOneFieldName);
					if(processedInput != null)
						choices = __inject__(RequestStatusController.class).read((Boolean)processedInput.getValue());
				}
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, RequestStatus requestStatus) {
				super.select(input, requestStatus);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.LABEL);
		selectOne.updateChoices();
		selectOne.selectByValueSystemIdentifier();
		return selectOne;
	}
}