package coffee.amo.interactionkey.client

import coffee.amo.interactionkey.api.Interactable
import java.util.*

class InteractionStack {
    companion object{
        private var stack = mutableListOf<Interactable>()
        var selected = 0;
        fun  getStack(): MutableList<Interactable> {
            return stack
        }
        fun clearStack() {
            stack = mutableListOf()
            selected = 0
        }
        fun put(priority: Int, value: Interactable) {
            stack = (stack.filter { it.priority < priority } + value).toMutableList()
        }

        fun getLast(): Interactable {
            return stack.last()
        }

        fun remove(priority: Int) {
            stack = stack.filter { it.priority != priority }.toMutableList()
        }

        fun clear() {
            stack = mutableListOf()
        }

        operator fun get(priority: Int): Interactable {
            return stack[priority]
        }

        fun getSelected(): Interactable {
            if(selected >= stack.size) selected = 0
            return stack[selected]
        }

        fun selectNext() {
            selected = (selected + 1) % stack.size
        }

        fun selectPrevious() {
            selected = (selected - 1 + stack.size) % stack.size
        }

        fun getSelectedPriority(): Int {
            return selected
        }

        fun getSelectedIndex(): Int {
            return stack.indexOf(getSelected())
        }

        fun getSelectedIndexFromLast(): Int {
            return stack.size - getSelectedIndex() - 1
        }

        fun getSelectedIndexFromFirst(): Int {
            return getSelectedIndex()
        }
    }

}