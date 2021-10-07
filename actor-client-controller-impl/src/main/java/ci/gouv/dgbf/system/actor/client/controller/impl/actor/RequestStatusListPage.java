package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
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

	public static SelectOneCombo buildSelectOne(RequestStatus requestStatus) {
		SelectOneCombo selectOne = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,requestStatus,SelectOneCombo.FIELD_CHOICE_CLASS,RequestStatus.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<RequestStatus>() {
			@Override
			public Collection<RequestStatus> computeChoices(AbstractInputChoice<RequestStatus> input) {
				Collection<RequestStatus> choices = __inject__(RequestStatusController.class).read();
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