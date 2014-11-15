package dataTypes;

import enums.EAssistantLevel;
import enums.EIndependenceStages;

public class IndependenceStages {
	@Override
	public String toString() {
		return String.format("stage - %s with %s help level", independenceStage, assistantLevel);
	}

	public IndependenceStages() {

	}

	public IndependenceStages(EIndependenceStages independenceStage, EAssistantLevel assistantLevel) {
		this.independenceStage = independenceStage;
		this.assistantLevel = assistantLevel;
	}

	private EIndependenceStages independenceStage;
	private EAssistantLevel assistantLevel;

	public EIndependenceStages getIndependenceStage() {
		return independenceStage;
	}

	public void setIndependenceStage(EIndependenceStages independenceStage) {
		this.independenceStage = independenceStage;
	}

	public EAssistantLevel getAssistantLevel() {
		return assistantLevel;
	}

	public void setAssistantLevel(EAssistantLevel assistantLevel) {
		this.assistantLevel = assistantLevel;
	}
}
