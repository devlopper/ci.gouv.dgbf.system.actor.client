package ci.gouv.dgbf.system.actor.client.controller.entities;
import java.util.Date;

import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

import ci.gouv.dgbf.system.actor.server.representation.entities.ActorDto;

@Mapper
public abstract class ActorMapper extends AbstractMapperSourceDestinationImpl<Actor, ActorDto> {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected void __listenGetSourceAfter__(ActorDto destination, Actor source) {
		super.__listenGetSourceAfter__(destination, source);
		if(source.getActOfAppointmentSignatureDate() == null && destination.getActOfAppointmentSignatureDateAsTimestamp() != null)
			source.setActOfAppointmentSignatureDate(new Date(destination.getActOfAppointmentSignatureDateAsTimestamp()));
	}
}