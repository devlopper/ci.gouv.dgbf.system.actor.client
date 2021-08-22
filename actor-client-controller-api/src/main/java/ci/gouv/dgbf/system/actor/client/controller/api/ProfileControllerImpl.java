package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;

@ApplicationScoped
public class ProfileControllerImpl extends AbstractControllerEntityImpl<Profile> implements ProfileController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<Profile> readRequestable(String typeIdentifier) {
		Arguments<Profile> arguments = new Arguments<Profile>().queryIdentifierReadDynamicMany(Profile.class)
				.filterFieldsValues(ProfileQuerier.PARAMETER_NAME_REQUESTABLE,Boolean.TRUE);
		if(StringHelper.isNotBlank(typeIdentifier))
			arguments.filterFieldsValues(ProfileQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,typeIdentifier);
		return EntityReader.getInstance().readMany(Profile.class, arguments);
	}
	
	@Override
	public Profile prepareEdit(String identifier,String typeIdentifier) {
		Arguments<Profile> arguments = new Arguments<Profile>();
		if(StringHelper.isBlank(identifier))
			arguments.queryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_INSTANTIATE)
				.filterFieldsValues(ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.FIELD_TYPE_IDENTIFIER,typeIdentifier);
		else
			arguments.queryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE).flags(ProfileQuerier.FLAG_PREPARE_EDIT).filterByIdentifier(identifier);
		return EntityReader.getInstance().readOne(Profile.class,arguments);
	}
}