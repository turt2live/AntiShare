package com.turt2live.antishare.conversations.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

import com.turt2live.antishare.conversations.ConfigurationConversation;

public class MainMenu extends FixedSetPrompt {

	// TODO: Make this work
	public MainMenu(){
		super("test", "test2");
	}

	@Override
	protected boolean isInputValid(ConversationContext context, String input){
		if(ConfigurationConversation.isValid(super.fixedSet, input)){
			return true;
		}
		return false;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input){
		context.setSessionData("test", input);
		return new MessagePrompt(){
			@Override
			public String getPromptText(ConversationContext context){
				return "You are done! [" + context.getSessionData("test") + "]";
			}

			@Override
			protected Prompt getNextPrompt(ConversationContext context){
				return Prompt.END_OF_CONVERSATION;
			}
		};
	}

	@Override
	protected String getFailedValidationText(ConversationContext context, String invalidInput){
		return "Please enter one of the following: " + super.formatFixedSet();
	}

	@Override
	public String getPromptText(ConversationContext context){
		return "Please choose an option from above: ";
	}

}
