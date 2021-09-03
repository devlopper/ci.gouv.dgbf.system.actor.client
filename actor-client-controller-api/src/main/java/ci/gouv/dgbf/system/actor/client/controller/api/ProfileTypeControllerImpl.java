package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;

@ApplicationScoped
public class ProfileTypeControllerImpl extends AbstractControllerEntityImpl<ProfileType> implements ProfileTypeController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<ProfileType> read() {
		return EntityReader.getInstance().readMany(ProfileType.class, new Arguments<ProfileType>().queryIdentifierReadDynamicMany(ProfileType.class));
	}

	@Override
	public ProfileType getByIdentifier(String identifier) {
		if(StringHelper.isBlank(identifier))
			return null;
		return EntityReader.getInstance().readOne(ProfileType.class, new Arguments<ProfileType>().queryIdentifierReadDynamicOne(ProfileType.class)
				.filterByIdentifier(identifier));
	}

}