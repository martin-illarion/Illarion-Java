package org.illarion.nifty.controls.chatbox;

import java.util.List;

import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;

/**
 * The representation of a ListBoxView from the world of a ListBox.
 * @author void
 *
 * @param <T> The Item this class is a view for.
 */
public interface ListBoxView<T> {

  /**
   * Display the given descriptions.
   * @param captions
   */
  void display(List<T> captions, int focusElementIndex, List<Integer> selectionElements);

  /**
   * Updates the view with the total count of elements currently in the ListBox.
   * This can be used to update the scrollbar.
   * @param newCount the new count to display
   */
  void updateTotalCount(int newCount);

  /**
   * Update the ListBox view with the given width. This is used to update the horizontal
   * scrollbar to a new maximum value.
   * @param newWidth new maximum width of all items in the ListBox
   */
  void updateTotalWidth(int newWidth);

  /**
   * Scroll the view to the given position.
   * @param newPosition the new index to scroll to
   */
  void scrollTo(int newPosition);

  /**
   * Publish this event.
   * @param event the event to publish
   */
  void publish(ListBoxSelectionChangedEvent<T> event);

  /**
   * Return the width of the given item. This is used to keep track of the maximum width of
   * all items in the ListBox to update the horizontal Scrollbar correctly.
   * @param item the item to get the width for
   * @return the width of the item
   */
  int getWidth(T item);
}
