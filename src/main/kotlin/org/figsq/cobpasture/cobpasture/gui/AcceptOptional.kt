package org.figsq.cobpasture.cobpasture.gui

interface AcceptOptional<T : Optional<*>> {
    fun accept(e: T)
}