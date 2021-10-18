package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableAuditedImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.__kernel__.string.StringHelper;

import ci.gouv.dgbf.system.actor.server.business.api.RequestDispatchSlipBusiness;
import ci.gouv.dgbf.system.actor.server.representation.entities.RequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class RequestDispatchSlip extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableAuditedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOne @InputChoiceOneCombo
	private Section section;
	private String sectionIdentifier;
	private String sectionAsString;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOne @InputChoiceOneCombo
	private Function function;
	private String functionIdentifier;
	private String functionAsString;
	
	private String creationDateAsString,sendingDateAsString,processingDateAsString,comment;
	
	private Collection<Request> requests;
	private Integer numberOfRequests,numberOfRequestsProcessed,numberOfRequestsAccepted,numberOfRequestsRejected,numberOfRequestsNotProcessed;
	private Boolean isNumberOfRequestsEqualNumberOfRequestsProcessed;
	private Collection<String> requestsIdentifiers;
	private Collection<String> acceptedRequestsIdentifiers;
	private Collection<RequestDto.Acceptation> requestsAcceptations;
	private Collection<RequestDto.Rejection> requestsRejections;
	
	private Boolean sent;
	private Boolean processed;
	
	public void writeIdentifiers(String actionIdentifier) {
		if(section == null)
			sectionIdentifier = null;
		else
			sectionIdentifier = section.getIdentifier();		
		if(StringHelper.isBlank(sectionIdentifier))
			throw new RuntimeException("Veuillez sélectionner une section");
		
		if(function == null)
			functionIdentifier = null;
		else
			functionIdentifier = function.getIdentifier();		
		if(StringHelper.isBlank(functionIdentifier))
			throw new RuntimeException("Veuillez sélectionner une catégorie de fonction budgétaire");
		
		if(CollectionHelper.isEmpty(requests))
			requestsIdentifiers = null;
		else {
			if(RequestDispatchSlipBusiness.RECORD.equals(actionIdentifier)) {
				requestsIdentifiers = requests.stream().map(x -> x.getIdentifier()).collect(Collectors.toList());
				if(CollectionHelper.isEmpty(requestsIdentifiers))
					throw new RuntimeException("Veuillez sélectionner au moins une demande");
			}else if(RequestDispatchSlipBusiness.PROCESS.equals(actionIdentifier)) {
				for(Request request : requests)
					if(StringHelper.isBlank(request.getTreatment()))
						throw new RuntimeException("Veuillez accepter, rejeter ou ignorer la demande N° "+request.getCode());
					else if(TREATMENT_REJECT.equals(request.getTreatment()) && StringHelper.isBlank(request.getTreatmentComment()))
						throw new RuntimeException("Veuillez spécifier le motif de rejet de la demande N° "+request.getCode());
				requestsAcceptations = requests.stream()
						.filter(x -> TREATMENT_ACCEPT.equals(x.getTreatment()))
						.map(x -> new RequestDto.Acceptation().setIdentifier(x.getIdentifier()).setComment(x.getTreatmentComment()))
						.collect(Collectors.toList());
				
				requestsRejections = requests.stream()
						.filter(x -> TREATMENT_REJECT.equals(x.getTreatment()))
						.map(x -> new RequestDto.Rejection().setIdentifier(x.getIdentifier()).setReason(x.getTreatmentComment()))
						.collect(Collectors.toList());
				
				if(CollectionHelper.isEmpty(requestsAcceptations) && CollectionHelper.isEmpty(requestsRejections))
					throw new RuntimeException("Veuillez sélectionner les demandes à accepter, rejeter ou ignorer");
			}
		}
		requests = null;
	}
	
	public void writeProcessedIdentifiers() {
		acceptedRequestsIdentifiers = null;
		requestsRejections = null;
		if(CollectionHelper.isEmpty(requests))
			return;
		for(Request request : requests) {
			if(Boolean.TRUE.equals(request.getAccepted())) {
				if(acceptedRequestsIdentifiers == null)
					acceptedRequestsIdentifiers = new ArrayList<>();
				acceptedRequestsIdentifiers.add(request.getIdentifier());
			}else {
				if(requestsRejections == null)
					requestsRejections = new ArrayList<>();
				requestsRejections.add(new RequestDto.Rejection().setIdentifier(request.getIdentifier()).setReason(request.getRejectionReason()));
			}
		}
	}
	
	/**/
	
	public static final String FIELD_SECTION = "section";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	public static final String FIELD_FUNCTION = "function";
	public static final String FIELD_FUNCTION_AS_STRING = "functionAsString";
	public static final String FIELD_REQUESTS = "requests";
	public static final String FIELD_COMMENT = "comment";
	public static final String FIELD_CREATION_DATE_AS_STRING = "creationDateAsString";
	public static final String FIELD_SENDING_DATE_AS_STRING = "sendingDateAsString";
	public static final String FIELD_PROCESSING_DATE_AS_STRING = "processingDateAsString";
	public static final String FIELD_NUMBER_OF_REQUESTS = "numberOfRequests";
	public static final String FIELD_NUMBER_OF_REQUESTS_PROCESSED = "numberOfRequestsProcessed";
	public static final String FIELD_NUMBER_OF_REQUESTS_ACCEPTED = "numberOfRequestsAccepted";
	public static final String FIELD_NUMBER_OF_REQUESTS_REJECTED = "numberOfRequestsRejected";
	public static final String FIELD_NUMBER_OF_REQUESTS_NOT_PROCESSED = "numberOfRequestsNotProcessed";
	public static final String FIELD_IS_NUMBER_OF_REQUESTS_EQUAL_NUMBER_OF_REQUESTS_PROCESSED = "isNumberOfRequestsEqualNumberOfRequestsProcessed";
	public static final String FIELD_SENT = "sent";
	public static final String FIELD_PROCESSED = "processed";
	public static final String FIELD_SEARCH = "search";
	
	public static final String TREATMENT_ACCEPT = "0";
	public static final String TREATMENT_REJECT = "1";
	public static final String TREATMENT_IGNORE = "2";
}