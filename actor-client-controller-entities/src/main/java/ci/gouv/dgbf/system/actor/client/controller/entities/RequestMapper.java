package ci.gouv.dgbf.system.actor.client.controller.entities;
import java.util.Date;

import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

import ci.gouv.dgbf.system.actor.server.representation.entities.RequestDto;

@Mapper
public abstract class RequestMapper extends AbstractMapperSourceDestinationImpl<Request, RequestDto> {
	private static final long serialVersionUID = 1L;

	@Override
	protected void __listenGetSourceAfter__(RequestDto destination, Request source) {
		super.__listenGetSourceAfter__(destination, source);
		if(source.getActOfAppointmentSignatureDate() == null && destination.getActOfAppointmentSignatureDateAsTimestamp() != null)
			source.setActOfAppointmentSignatureDate(new Date(destination.getActOfAppointmentSignatureDateAsTimestamp()));
	}
}