package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.AccountRequestDto;

import java.util.Date;

import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class AccountRequestMapper extends AbstractMapperSourceDestinationImpl<AccountRequest, AccountRequestDto> {
	private static final long serialVersionUID = 1L;
    
	@Override
	protected void __listenGetSourceAfter__(AccountRequestDto accountRequestDto, AccountRequest accountRequest) {
		super.__listenGetSourceAfter__(accountRequestDto, accountRequest);
		if(accountRequest.getActOfAppointmentSignatureDate() == null && accountRequestDto.getActOfAppointmentSignatureDateAsTimestamp() != null)
			accountRequest.setActOfAppointmentSignatureDate(new Date(accountRequestDto.getActOfAppointmentSignatureDateAsTimestamp()));
	}
	
}