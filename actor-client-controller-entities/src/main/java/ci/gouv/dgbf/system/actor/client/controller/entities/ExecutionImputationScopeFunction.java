package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class ExecutionImputationScopeFunction implements Serializable {

	private ScopeFunction holder;
	private ScopeFunction assistant;
	
	private String holderIdentifier;
	private String assistantIdentifier;
	
	private Boolean holderOverridable;
	private Boolean assistantOverridable;
	
	public static final String FIELD_HOLDER = "holder";
	public static final String FIELD_ASSISTANT = "assistant";
	
	public static final String FIELD_HOLDER_OVERRIDABLE = "holderOverridable";
	public static final String FIELD_ASSISTANT_OVERRIDABLE = "assistantOverridable";
}