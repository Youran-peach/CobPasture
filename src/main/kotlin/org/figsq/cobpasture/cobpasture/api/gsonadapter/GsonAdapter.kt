package org.figsq.cobpasture.cobpasture.api.gsonadapter

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer

interface GsonAdapter<T> : JsonSerializer<T>, JsonDeserializer<T> {
}