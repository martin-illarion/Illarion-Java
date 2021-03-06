package org.illarion.nifty.controls.illaButton.builder;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyIdCreator;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.loaderv2.types.ControlType;
import de.lessvoid.nifty.loaderv2.types.ElementType;
import de.lessvoid.nifty.screen.Screen;

public class CreateIllaButtonControl extends ControlAttributes {
	  private static final String NAME = "illaButton";

	  public CreateIllaButtonControl() {
	    setAutoId(NiftyIdCreator.generate());
	    setName(NAME);
	  }

	  public CreateIllaButtonControl(final String id) {
	    setId(id);
	    setName(NAME);
	  }

	  public Button create(
	      final Nifty nifty,
	      final Screen screen,
	      final Element parent) {
	    nifty.addControl(screen, parent, getStandardControl());
	    nifty.addControlsWithoutStartScreen();
	    return parent.findNiftyControl(attributes.get("id"), Button.class);
	  }

	  @Override
	  public ElementType createType() {
	    return new ControlType(attributes);
	  }
	
}
