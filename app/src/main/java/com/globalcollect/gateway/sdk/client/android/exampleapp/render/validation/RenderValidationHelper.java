package com.globalcollect.gateway.sdk.client.android.exampleapp.render.validation;

/**
 * Helper class for rendering validation messages
 * Copyright 2014 Global Collect Services B.V
 *
 */
public class RenderValidationHelper {
	
	
	// Default renderer for validation errors on all fields
	private RenderValidationMessageInterface validationMessageRenderer = new RenderValidationMessage();
	
	// The parent view to which all validationmessages are added
	private ViewGroup parentView;
	
	// List which contains all invalid fields which are rendered
	private ArrayList<ValidationErrorMessage> validationMessagesShown = new ArrayList<ValidationErrorMessage>();
	
	
	private Translator translator;
	
	
	/**
	 * Constructor 
	 * @param parentView, the ViewGroup to which all the rendered validationmessages are added
	 */
	public RenderValidationHelper(ViewGroup parentView, Context context) {

		if (parentView == null) {
			throw new InvalidParameterException("Error creating Validator, parentView may not be null");
		}
		
		this.parentView = parentView;
		translator = new Translator(context);
	}
	
	
	/**
	 * Registers a custom validationmessage renderer
	 * This renderer must implement the RenderValidationMessageInterface interface
	 * 
	 * @param renderer, the custom renderer which will handle the show and hide of validationmessages
	 */
	public void registerCustomRenderer(RenderValidationMessageInterface renderer) {
		
		if (renderer == null) {
			throw new InvalidParameterException("Error setting custom messageRenderer, renderer may not be null");
		}
		validationMessageRenderer = renderer;
	}
	
	
	/**
	 * Renders validationmessage for every invalid field in the invalidFields list
	 * @param validationResultList, for all these fields their validationmessage is shown
	 */
	public void renderValidationMessages(List<ValidationErrorMessage> validationResultList, PaymentRequest paymentRequest) {
		
		// Loop trough all the fields and see if the result is invalid
		for (ValidationErrorMessage validationResult : validationResultList) {
			
			// See if a validationmessage for this field is already showing, there could be more then one per field
			boolean messageShownForThisField = false;
			for (ValidationErrorMessage invalidFieldShown : validationMessagesShown) {
				if (invalidFieldShown.getPaymentProductFieldId().equals(validationResult.getPaymentProductFieldId())) {
					messageShownForThisField = true;
				}
			}
		
			// If no other invalidMessage is shown, show this one
			if (!messageShownForThisField) {
				
				// Get the correct view of the invalid inputfield 
				View view = parentView.findViewWithTag(validationResult.getPaymentProductFieldId());

				// Render the validationMessage
				String validationMessage = translator.getValidationMessage(validationResult.getErrorMessage());
				
				// Find the correct validationRule and format its message with variables attributes
					
				validationMessageRenderer.renderValidationMessage(validationMessage, (ViewGroup)view.getParent(), validationResult.getPaymentProductFieldId());
				validationMessagesShown.add(validationResult);
			}
		}
	}
	
	
	/**
	 * Hides all visible validationmessages
	 */
	public void hideValidationMessages() {
		
		for (ValidationErrorMessage invalidField : validationMessagesShown) {
			View view = parentView.findViewWithTag(invalidField.getPaymentProductFieldId());
			validationMessageRenderer.removeValidationMessage((ViewGroup)view.getParent(), invalidField.getPaymentProductFieldId());
		}
		validationMessagesShown.clear();
	}
	
}