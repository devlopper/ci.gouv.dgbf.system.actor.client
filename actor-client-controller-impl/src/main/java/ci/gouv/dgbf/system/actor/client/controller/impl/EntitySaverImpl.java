package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfilePrivilegeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfilePrivilegeDto;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class EntitySaverImpl extends EntitySaver.AbstractImpl implements Serializable {

	@Override
	protected <T> void prepare(Class<T> controllerEntityClass,Arguments<T> arguments) {
		if(arguments.getRepresentationArguments() != null) {
			if(ProfilePrivilegeBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()) && arguments.getRepresentation() == null)
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
		}
		super.prepare(controllerEntityClass, arguments);
	}
	
	@Override
	protected <T> Response save(Object representation, Collection<?> creatables, Collection<?> updatables,Collection<?> deletables, org.cyk.utility.__kernel__.representation.Arguments arguments) {
		if(arguments != null && ProfilePrivilegeBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			Collection<ProfilePrivilegeDto> dtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables)
					dtos.add((ProfilePrivilegeDto) index);
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables)
					dtos.add((ProfilePrivilegeDto) index);
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables)
					dtos.add(((ProfilePrivilegeDto) index).set__deletable__(Boolean.TRUE));
			return ((ProfilePrivilegeRepresentation)representation).save(dtos);
		}
		return super.save(representation, creatables, updatables, deletables, arguments);
	}
}