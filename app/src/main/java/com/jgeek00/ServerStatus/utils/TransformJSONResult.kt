package com.jgeek00.ServerStatus.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun transformStatusJSON(input: JsonElement): JsonElement {
    val output = JsonObject()

    if (input.isJsonObject) {
        val jsonObject = input.asJsonObject

        val cpu = jsonObject.getAsJsonObject("cpu")
        if (cpu != null) {
            val coresData = JsonArray()

            //val temperatures = cpu.getAsJsonObject("temperatures")
            val frequencies = cpu.getAsJsonObject("frequencies")

            frequencies?.entrySet()?.forEach { (index, values) ->
                val coreData = JsonObject()
                /*val coreIndex = index.replace("cpu", "")
                val coreTemperature = temperatures?.getAsJsonArray("Core $coreIndex")
                if (coreTemperature != null) {
                    coreData.add("temperatures", coreTemperature)
                }*/
                coreData.add("frequencies", JsonObject().apply {
                    values.asJsonObject.entrySet().forEach { (k, v) ->
                        if (k.toString() != "base") {
                        addProperty(k, v.asInt) } //else { addProperty(k, 0)}
                    }
                })
                coresData.add(coreData)
            }

            cpu.remove("frequencies")
            //cpu.remove("temperatures")

            output.add("cpu", JsonObject().apply {
                cpu.entrySet().forEach { (key, value) ->
                    add(key, value)
                }
                add("cpuCores", coresData)
            })
        }

        val memory = jsonObject.getAsJsonObject("memory")
        output.add("memory", JsonObject().apply {
            memory.entrySet().forEach { (key, value) ->
                add(key, value)
            }
        })

        val storage = jsonObject.getAsJsonObject("storage")
        if (storage != null && storage.isJsonObject) {
            val convertedStorage = JsonArray()

            storage.entrySet().forEach { (key, value) ->
                val storageItem = JsonObject()
                storageItem.addProperty("name", key)
                value.asJsonObject.entrySet().forEach { (innerKey, innerValue) ->
                    storageItem.add(innerKey, innerValue)
                }
                convertedStorage.add(storageItem)
            }

            output.add("storage", convertedStorage)
        }

        val network = jsonObject.getAsJsonObject("network")
        output.add("network", JsonObject().apply {
            network.entrySet().forEach { (key, value) ->
                add(key, value)
            }
        })

        val host = jsonObject.getAsJsonObject("host")
        output.add("host", JsonObject().apply {
            host.entrySet().forEach { (key, value) ->
                add(key, value)
            }
        })
    }

    return output
}