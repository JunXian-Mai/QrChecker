package com.markensic.sdk.framework.reflect

import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun <T> Class<T>.rejectFinalModifier(fieldName: String): Field? {
  return try {
    val field = getDeclaredField(fieldName).also {
      it.isAccessible = true
      val accessFlagsField = it::class.java.getDeclaredField("accessFlags")
      accessFlagsField.isAccessible = true
      accessFlagsField.setInt(it, it.modifiers.and(Modifier.FINAL.inv()))
    }
    field
  } catch (e: NoSuchFieldException) {
    null
  }
}
