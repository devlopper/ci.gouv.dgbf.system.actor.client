package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestDispatchSlipQuerier;

@ApplicationScoped
public class RequestDispatchSlipControllerImpl extends AbstractControllerEntityImpl<RequestDispatchSlip> implements RequestDispatchSlipController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<RequestDispatchSlip> read(String sectionIdentifier, String functionIdentifier) {
		Arguments<RequestDispatchSlip> arguments = new Arguments<RequestDispatchSlip>().queryIdentifierReadDynamicMany(RequestDispatchSlip.class);
		if(StringHelper.isNotBlank(sectionIdentifier))
			arguments.filterFieldsValues(RequestDispatchSlipQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,sectionIdentifier);
		if(StringHelper.isNotBlank(functionIdentifier))
			arguments.filterFieldsValues(RequestDispatchSlipQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER,functionIdentifier);
		return EntityReader.getInstance().readMany(RequestDispatchSlip.class, arguments);
	}	
}